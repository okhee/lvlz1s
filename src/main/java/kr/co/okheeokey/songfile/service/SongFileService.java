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
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class SongFileService{
    private final SongFileRepository songFileRepository;
    private final AudioFileRepository audioFileRepository;
    private final AudioFileContentStore audioFileContentStore;

    @Transactional
    public AudioFile saveAudioFile(Long songFileId, MultipartFile file, Long difficulty)
            throws NoSuchElementException, AudioFileAlreadyExistsException, IOException {
        SongFile songFile = songFileRepository.findById(songFileId).orElseThrow(NoSuchElementException::new);
        songFile.diffEmptyCheck(difficulty);

        AudioFile audioFile = new AudioFile(difficulty);
        audioFile.setSongFile(songFile);
        audioFileRepository.save(audioFile);
        audioFileContentStore.setContent(audioFile, file.getInputStream());

        return audioFile;
    }

    public byte[] getAudioFile(Long songFileId, Long difficulty) throws NoAudioFileExistsException, IOException {
        SongFile songFile = songFileRepository.findById(songFileId).orElseThrow(NoSuchElementException::new);
        songFile.diffExistCheck(difficulty);

        AudioFile audioFile = songFile.getAudio(difficulty);

        return IOUtils.toByteArray(audioFileContentStore.getContent(audioFile));
    }
}
