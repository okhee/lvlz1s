package kr.co.okheeokey.song;

import kr.co.okheeokey.song.Song;
import kr.co.okheeokey.song.SongHash;
import kr.co.okheeokey.song.SongHashRepository;
import kr.co.okheeokey.song.SongRepository;
import kr.co.okheeokey.web.dto.SongAddDto;
import kr.co.okheeokey.web.dto.SongSubmitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class SongService {
    private final SongRepository songRepository;
    private final SongHashRepository songHashRepository;

    public Song addSong(SongAddDto songAddDto) {
        return songRepository.save(songAddDto.toEntity());
    }

    public Optional<Song> getSongById(Long id){
        return songRepository.findById(id);
    }

    public Optional<String> getSongHashById(Long id) throws IllegalArgumentException{
        List<SongHash> songHashList = songHashRepository.findAllBySongId(id);

        if (songHashList.isEmpty()){
            return Optional.empty();
        }
        Random r = new Random();
        String songHash = songHashList.get(r.nextInt(songHashList.size())).getSongHash();
        return Optional.of(songHash);
    }

    public Boolean checkAnswer(SongSubmitDto songSubmitDto) {
        Optional<SongHash> songHash = songHashRepository.findBySongIdAndSongHash(songSubmitDto.getSongId(),
                                                        songSubmitDto.getSongHash());
        return songHash.isPresent();
    }

    public List<Song> getSongList() {
        return songRepository.findAll();
    }
}
