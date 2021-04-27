package kr.co.okheeokey.quiz;

import kr.co.okheeokey.song.SongFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class QuizService {
    private final SongFileRepository songFileRepository;



}
