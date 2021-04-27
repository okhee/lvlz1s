package kr.co.okheeokey.song.domain;

import kr.co.okheeokey.song.domain.SongFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongFileRepository extends JpaRepository<SongFile, Long> {
}
