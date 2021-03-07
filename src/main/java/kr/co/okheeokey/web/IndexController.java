package kr.co.okheeokey.web;

import kr.co.okheeokey.service.SongService;
import kr.co.okheeokey.web.dto.SongAddDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final SongService songService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/songlist")
    public String songList(Model model) {
        songService.addSong(SongAddDto.builder()
                .songName("Candy Jelly Love")
                .albumId(20141117L)
                .fileId(11001L)
                .build());
        songService.addSong(SongAddDto.builder()
                .songName("Hi~")
                .albumId(20150302L)
                .fileId(12002L)
                .build());

        model.addAttribute("songList", songService.getSongList());
        return "question";
    }

}
