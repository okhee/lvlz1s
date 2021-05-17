package kr.co.okheeokey.songfile.service;

import kr.co.okheeokey.songfile.domain.*;
import kr.co.okheeokey.songfile.exception.NoAudioFileExistsException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class SongFileService{
    private final SongFileRepository songFileRepository;
    private final AudioFileRepository audioFileRepository;
    private final AudioFileContentStore audioFileContentStore;

    @Transactional
    public void saveSongFile(Long songFileId, MultipartFile file, Long difficulty) throws IOException, NoSuchElementException, SQLException {
        SongFile songFile = songFileRepository.findById(songFileId).orElseThrow(NoSuchElementException::new);

        AudioFile audioFile = new AudioFile(difficulty);
        audioFile.setSongFile(songFile);
        audioFileRepository.save(audioFile);
        audioFileContentStore.setContent(audioFile, file.getInputStream());
    }

    public byte[] getAudioFile(Long songFileId, Long difficulty) throws NoAudioFileExistsException {
        SongFile songFile = songFileRepository.findById(songFileId).orElseThrow(NoSuchElementException::new);
        AudioFile audioFile = songFile.getAudio(difficulty);

        try {
            return IOUtils.toByteArray(audioFileContentStore.getContent(audioFile));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
