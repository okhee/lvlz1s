package kr.co.okheeokey.web;

import kr.co.okheeokey.domain.song.Song;
import kr.co.okheeokey.service.SongService;
import kr.co.okheeokey.web.dto.SongSubmitDto;
import kr.co.okheeokey.web.dto.UserRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final SongService songService;

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        String loginUserName = (String) session.getAttribute("loginUserName");
        model.addAttribute("loginUserName", loginUserName);
        return "index";
    }

    @GetMapping("/songlist")
    public String songList(Model model) {
        model.addAttribute("songList", songService.getSongList());
        return "songList";
    }

    @GetMapping("/register")
    public String registerPage(HttpSession session, Model model) {
        String userHashSalt = "vSf2taAc";

        String loginUserName = (String) session.getAttribute("loginUserName");

        model.addAttribute("user", loginUserName);
//        model.addAttribute("userHashSalt", userHashSalt);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(UserRegisterDto userRegisterDto, HttpSession session, Model model) {
        String loginUserName = userRegisterDto.getLoginUserName();
        session.setAttribute("loginUserName", loginUserName);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, Model model) {
        session.removeAttribute("loginUserName");
        return "redirect:/";
    }

    @GetMapping("/quiz/{id}")
    public String songQuiz(@PathVariable("id") Long id, Model model) {

        Optional<Song> song = songService.getSongById(id);
        Optional<String> songHash = songService.getSongHashById(id);

        if (song.isEmpty() || songHash.isEmpty()) {
            model.addAttribute("songId", id);
            return "song_not_found";
        }

        model.addAttribute("song", song.get());
        model.addAttribute("nextSongId", id + 1);
        model.addAttribute("songHash", songHash.get());
        model.addAttribute("songList", songService.getSongList());
        return "quiz";
    }

    @PostMapping("/submit")
    public String submit(SongSubmitDto songSubmitDto, Model model) {
        Boolean isAnswer = songService.checkAnswer(songSubmitDto);

        model.addAttribute("correctMessage", isAnswer ? "Correct!" : "Wrong Answer!");
        model.addAttribute("songId", songSubmitDto.getSongId());
        model.addAttribute("nextSongId", songSubmitDto.getNextSongId());

        return "quiz_result";
    }
}
