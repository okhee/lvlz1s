package kr.co.okheeokey.question.service;

import kr.co.okheeokey.audiofile.domain.AudioFile;
import kr.co.okheeokey.audiofile.domain.AudioFileContentStore;
import kr.co.okheeokey.audiofile.domain.AudioFileRepository;
import kr.co.okheeokey.question.domain.Question;
import kr.co.okheeokey.question.domain.QuestionRepository;
import kr.co.okheeokey.question.exception.AudioFileAlreadyExistsException;
import kr.co.okheeokey.question.exception.NoAudioFileExistsException;
import kr.co.okheeokey.question.vo.AudioFileValues;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final AudioFileRepository audioFileRepository;
    private final AudioFileContentStore audioFileContentStore;

    @Transactional
    public AudioFile setAudioFile(Long questionId, MultipartFile file, Long difficulty)
            throws NoSuchElementException, AudioFileAlreadyExistsException, IOException {
        Question question = questionRepository.findById(questionId).orElseThrow(NoSuchElementException::new);
        question.diffEmptyCheck(difficulty);

        String mimeType = verifyMultipartFile(file);

        AudioFile audioFile = AudioFile.builder()
                .difficulty(difficulty)
                .mimeType(mimeType)
                .build();
        audioFile.setQuestion(question);
        audioFileRepository.save(audioFile);
        audioFileContentStore.setContent(audioFile, file.getInputStream());

        return audioFile;
    }

    public AudioFileValues getAudioFile(Long questionId, Long difficulty)
            throws NoSuchElementException, NoAudioFileExistsException {
        Question question = questionRepository.findById(questionId).orElseThrow(NoSuchElementException::new);
        question.diffExistCheck(difficulty);

        AudioFile audioFile = question.getAudio(difficulty);

        return AudioFileValues.builder()
                .audioStream(audioFileContentStore.getContent(audioFile))
                .contentLength(audioFile.getContentLength())
                .mimeType(audioFile.getMimeType())
                .build();
    }

    private String verifyMultipartFile(MultipartFile file) throws IOException{
        byte[] mp3MagicNumber = new byte[]{0x49, 0x44, 0x33};
        byte[] flacMagicNumber = new byte[]{0x66, 0x4C, 0x61, 0x43};
        byte[] buffer = new byte[4];

        if (file.getInputStream().read(buffer) < 0)
            throw new IOException("Uploaded file is not mp3 nor flac");

        if (Arrays.equals(mp3MagicNumber, Arrays.copyOfRange(buffer, 0, mp3MagicNumber.length)))
            return "audio/mpeg";

        if (Arrays.equals(flacMagicNumber, Arrays.copyOfRange(buffer, 0, flacMagicNumber.length)))
            return "audio/flac";

        throw new IOException("Uploaded file is not mp3 nor flac");
    }
}
