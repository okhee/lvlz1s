package kr.co.okheeokey.audiofile.service;

import com.sun.media.sound.InvalidFormatException;
import kr.co.okheeokey.audiofile.domain.AudioFile;
import kr.co.okheeokey.audiofile.domain.AudioFileContentStore;
import kr.co.okheeokey.audiofile.domain.AudioFileRepository;
import kr.co.okheeokey.audiofile.exception.AudioFileAlreadyExistsException;
import kr.co.okheeokey.audiofile.exception.NoAudioFileExistsException;
import kr.co.okheeokey.audiofile.vo.AudioFileSetValues;
import kr.co.okheeokey.question.domain.Question;
import kr.co.okheeokey.question.domain.QuestionRepository;
import kr.co.okheeokey.question.vo.AudioFileValues;
import kr.co.okheeokey.util.CryptoUtils;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AudioFileService {
    private final QuestionRepository questionRepository;
    private final AudioFileRepository audioFileRepository;
    private final AudioFileContentStore audioFileContentStore;

    @Transactional
    public String setAudioFile(MultipartFile file)
            throws NoSuchElementException, AudioFileAlreadyExistsException, NoAudioFileExistsException, IOException, IllegalArgumentException {
        AudioFileSetValues values = parseFile(file);
        return setAudioFile(values);
    }

    @Transactional
    public String setAudioFile(AudioFileSetValues values)
            throws NoSuchElementException, AudioFileAlreadyExistsException, NoAudioFileExistsException, IOException {
        Question question = questionRepository.findById(values.getQuestionId())
                .orElseThrow(() -> new NoSuchElementException("No question exists with id { " + values.getQuestionId() + " }"));

        String mimeType = verifyMultipartFile(values.getFile());

        AudioFile audioFile;
        Optional<AudioFile> previousAudioFile = audioFileRepository.findByQuestionAndDifficulty(question, values.getDifficulty());
        if(values.getOverwrite() && previousAudioFile.isPresent()){
            audioFile = previousAudioFile.get();

            audioFile.setMimeType(mimeType);
            audioFileContentStore.unsetContent(audioFile);
        }
        else {
            question.diffEmptyCheck(values.getDifficulty());
            audioFile = AudioFile.builder()
                        .difficulty(values.getDifficulty())
                        .mimeType(mimeType)
                        .build();

            audioFile.setQuestion(question);
            audioFileRepository.save(audioFile);
        }
        audioFileContentStore.setContent(audioFile, values.getFile().getInputStream());

        return CryptoUtils.encryptUuid(audioFile.getUuid());
    }

    public AudioFileValues getAudioFile(String encryptUuid) throws IllegalArgumentException {
        UUID uuid = CryptoUtils.decryptUuid(encryptUuid);
        AudioFile audioFile = audioFileRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Invalid uuid value"));

        return AudioFileValues.builder()
                .audioStream(audioFileContentStore.getContent(audioFile))
                .contentLength(audioFile.getContentLength())
                .mimeType(audioFile.getMimeType())
                .build();
    }

    private AudioFileSetValues parseFile(MultipartFile file) throws IllegalArgumentException {
        String fileName = file.getOriginalFilename();
        if (fileName == null)
            throw new IllegalArgumentException("Empty file name is not allowed!");

        String[] split = fileName.split("[\\s_-]");
        if (split.length != 2)
            throw new IllegalArgumentException("Invalid file name: " + fileName);

        String questionId = split[0].replaceAll("\\D", "");
        String difficulty = split[1];
        if (difficulty.contains("."))
            difficulty = difficulty.substring(0, difficulty.lastIndexOf('.'));
        difficulty = difficulty.replaceAll("\\D", "");

        if (questionId.isEmpty() || difficulty.isEmpty())
            throw new IllegalArgumentException("Invalid file name: " + fileName);

        return new AudioFileSetValues(file, Long.valueOf(questionId), Long.valueOf(difficulty), true);
    }

    private String verifyMultipartFile(MultipartFile file) throws IOException {
        String mediaType = new Tika().detect(file.getInputStream());

        if (!mediaType.contains("audio/"))
            throw new InvalidFormatException("The uploaded file is not an audio file");

        return mediaType;
    }

}
