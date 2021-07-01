package kr.co.okheeokey.audiofile.service;

import kr.co.okheeokey.audiofile.domain.AudioFile;
import kr.co.okheeokey.audiofile.domain.AudioFileContentStore;
import kr.co.okheeokey.audiofile.domain.AudioFileRepository;
import kr.co.okheeokey.audiofile.vo.AudioFileSetValues;
import kr.co.okheeokey.question.domain.Question;
import kr.co.okheeokey.question.domain.QuestionRepository;
import kr.co.okheeokey.question.exception.AudioFileAlreadyExistsException;
import kr.co.okheeokey.question.vo.AudioFileValues;
import kr.co.okheeokey.util.CryptoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AudioFileService {
    private final QuestionRepository questionRepository;
    private final AudioFileRepository audioFileRepository;
    private final AudioFileContentStore audioFileContentStore;

    private final CryptoUtils cryptoUtils;

    @Transactional
    public String setAudioFile(AudioFileSetValues values)
            throws NoSuchElementException, AudioFileAlreadyExistsException, IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Question question = questionRepository.findById(values.getQuestionId())
                .orElseThrow(NoSuchElementException::new);
        question.diffEmptyCheck(values.getDifficulty());

        String mimeType = verifyMultipartFile(values.getFile());

        AudioFile audioFile = AudioFile.builder()
                .difficulty(values.getDifficulty())
                .mimeType(mimeType)
                .build();
        audioFile.setQuestion(question);
        audioFileRepository.save(audioFile);
        audioFileContentStore.setContent(audioFile, values.getFile().getInputStream());

        return cryptoUtils.encryptUuid(audioFile.getUuid());
    }

    public AudioFileValues getAudioFile(String encryptUuid) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        UUID uuid = cryptoUtils.decryptUuid(encryptUuid);
        AudioFile audioFile = audioFileRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Invalid uuid value"));

        return AudioFileValues.builder()
                .audioStream(audioFileContentStore.getContent(audioFile))
                .contentLength(audioFile.getContentLength())
                .mimeType(audioFile.getMimeType())
                .build();
    }

    private String verifyMultipartFile(MultipartFile file) throws IOException {
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
