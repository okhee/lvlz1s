package kr.co.okheeokey.songfile.service;

import kr.co.okheeokey.songfile.domain.*;
import kr.co.okheeokey.songfile.exception.AudioFileAlreadyExistsException;
import kr.co.okheeokey.songfile.exception.NoAudioFileExistsException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class SongFileService{
    private final SongFileRepository songFileRepository;
    private final AudioFileRepository audioFileRepository;
    private final AudioFileContentStore audioFileContentStore;

    @Transactional
    public AudioFile setAudioFile(Long songFileId, MultipartFile file, Long difficulty)
            throws NoSuchElementException, AudioFileAlreadyExistsException, IOException {
        SongFile songFile = songFileRepository.findById(songFileId).orElseThrow(NoSuchElementException::new);
        songFile.diffEmptyCheck(difficulty);

        verifyMultipartFile(file);

        AudioFile audioFile = new AudioFile(difficulty);
        audioFile.setSongFile(songFile);
        audioFileRepository.save(audioFile);
        audioFileContentStore.setContent(audioFile, file.getInputStream());

        return audioFile;
    }

    public byte[] getAudioFile(Long songFileId, Long difficulty)
            throws NoSuchElementException, NoAudioFileExistsException, IOException {
        SongFile songFile = songFileRepository.findById(songFileId).orElseThrow(NoSuchElementException::new);
        songFile.diffExistCheck(difficulty);

        AudioFile audioFile = songFile.getAudio(difficulty);

        return IOUtils.toByteArray(audioFileContentStore.getContent(audioFile));
    }

    private void verifyMultipartFile(MultipartFile file) throws IOException{
        byte[] mp3MagicNumber = new byte[]{0x49, 0x44, 0x33};
        byte[] flacMagicNumber = new byte[]{0x66, 0x4C, 0x61, 0x43};
        byte[] buffer = new byte[4];

        if (file.getInputStream().read(buffer) < 0)
            throw new IOException("Uploaded file is not mp3 nor flac");

        if (Arrays.equals(Arrays.copyOfRange(buffer, 0,
                mp3MagicNumber.length), mp3MagicNumber))
            return;

        if (Arrays.equals(Arrays.copyOfRange(buffer, 0,
                flacMagicNumber.length), flacMagicNumber))
            return;

        throw new IOException("Uploaded file is not mp3 nor flac");
    }
}
