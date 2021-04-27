package kr.co.okheeokey.song.domain;

import kr.co.okheeokey.song.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {

}
