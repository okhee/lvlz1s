package kr.co.okheeokey.song;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SongHashRepository extends JpaRepository<SongHash, Long> {
    List<SongHash> findAllBySongId(Long songId);
    Optional<SongHash> findBySongIdAndSongHash(Long songId, String hashCode);
}
