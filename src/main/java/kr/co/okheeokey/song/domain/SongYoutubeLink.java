package kr.co.okheeokey.song.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "song_youtube_link")
public class SongYoutubeLink {
    private static final Long timeSyncOffset = 3L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;

    private SongType songType;

    private String youtubeLink;

    private Long mvSyncOffset;

    public String getYoutubeAddress(Long timeLocation) {
        long location = timeLocation + mvSyncOffset - timeSyncOffset;
        location = Math.max(location, 0L);
        return "https://www.youtube.com/embed/" + youtubeLink + "?start=" + location;
    }
}
