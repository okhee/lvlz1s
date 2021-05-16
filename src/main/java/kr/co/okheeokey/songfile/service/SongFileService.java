package kr.co.okheeokey.songfile.service;

import kr.co.okheeokey.songfile.domain.SongFile;
import kr.co.okheeokey.songfile.domain.SongFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class SongFileService {
    private final SongFileRepository songFileRepository;

    @Transactional
    public void saveSongFile(Long songFileId, MultipartFile file) throws IOException, NoSuchElementException{
        SongFile songFile = songFileRepository.findById(songFileId).orElseThrow(NoSuchElementException::new);
        songFile.setAudio(file.getBytes());
    }

}
