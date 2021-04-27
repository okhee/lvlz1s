package kr.co.okheeokey.song.domain;

import kr.co.okheeokey.song.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {

}
