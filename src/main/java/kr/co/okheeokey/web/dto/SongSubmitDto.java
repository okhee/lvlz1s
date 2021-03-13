package kr.co.okheeokey.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SongSubmitDto {
    private String uuid;
    private String songChoice;

    @Builder
    public SongSubmitDto(String uuid, String songChoice) {
        this.uuid = uuid;
        this.songChoice = songChoice;
    }
}
