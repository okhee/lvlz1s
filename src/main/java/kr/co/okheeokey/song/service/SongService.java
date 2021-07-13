package kr.co.okheeokey.song.service;

import kr.co.okheeokey.song.domain.Song;
import kr.co.okheeokey.song.domain.SongRepository;
import kr.co.okheeokey.web.dto.SongAddDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SongService {
    private final SongRepository songRepository;

    public Song addSong(SongAddDto songAddDto) {
        return songRepository.save(songAddDto.toEntity());
    }

    public Optional<Song> getSongById(Long id){
        return songRepository.findById(id);
    }

    public List<Song> getSongList() {
        return songRepository.findAll();
    }
}
