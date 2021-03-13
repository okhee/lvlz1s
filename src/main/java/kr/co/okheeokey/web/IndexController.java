package kr.co.okheeokey.web;

import kr.co.okheeokey.domain.song.Song;
import kr.co.okheeokey.service.SongService;
import kr.co.okheeokey.web.dto.SongAddDto;
import kr.co.okheeokey.web.dto.SongSubmitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        return "songList";
    }

    @GetMapping("/quiz/{id}")
    public String songQuiz(@PathVariable("id") Long id, Model model) {
        Song song = songService.getSong(id);
        model.addAttribute("song", song);
        model.addAttribute("songList", songService.getSongList());
        return "quiz";
    }

    @PostMapping("/submit")
    @ResponseBody
    public String submit(SongSubmitDto songSubmitDto) {
        Boolean isAnswer = songService.checkAnswer(songSubmitDto);
        StringBuilder answerMessage = new StringBuilder();
        if (isAnswer) {
            answerMessage.append("Correct!!\n");
        } else {
            answerMessage.append("Wrong Answer!!\n");
        }
        answerMessage.append("UUID: ");
        answerMessage.append(songSubmitDto.getUuid());
        answerMessage.append(" songName: ");
        answerMessage.append(songSubmitDto.getSongChoice());
        return answerMessage.toString();
    }
}
