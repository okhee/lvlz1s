package kr.co.okheeokey.song.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "song_youtube_link")
public class SongYoutubeLink {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;

    private SongType songType;

    private String youtubeLink;

    private Long timeSyncOffset;

    public String getYoutubeAddress(Long timeLocation) {
        return "https://www.youtube.com/embed/" + youtubeLink + "?start=" + (timeLocation + timeSyncOffset);
    }
}
