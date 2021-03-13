package kr.co.okheeokey.service;

import kr.co.okheeokey.domain.song.Song;
import kr.co.okheeokey.domain.song.SongRepository;
import kr.co.okheeokey.web.dto.SongAddDto;
import kr.co.okheeokey.web.dto.SongSubmitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SongService {
    private final SongRepository songRepository;

    public Song addSong(SongAddDto songAddDto) {
        return songRepository.save(songAddDto.toEntity());
    }

    public Song getSong(Long id) {
        return songRepository.findById(id).orElseThrow();
    }

    public Boolean checkAnswer(SongSubmitDto songSubmitDto) {
        Song song = songRepository.findByUuid(songSubmitDto.getUuid());
        return song.songNameMatch(songSubmitDto.getSongChoice());
    }

    public List<Song> getSongList() {
        return songRepository.findAll();
    }
}
