package kr.co.okheeokey.song.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SongYoutubeLinkRepository extends JpaRepository<SongYoutubeLink, Long> {
    Optional<SongYoutubeLink> findBySongAndSongType(Song song, SongType songType);
}
