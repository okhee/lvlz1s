package kr.co.okheeokey.service;

import kr.co.okheeokey.domain.song.SongFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class QuizService {
    private final SongFileRepository songFileRepository;



}
