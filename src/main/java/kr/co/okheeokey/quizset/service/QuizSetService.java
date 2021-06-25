package kr.co.okheeokey.quizset.service;

import kr.co.okheeokey.question.domain.Question;
import kr.co.okheeokey.question.domain.QuestionDifficulty;
import kr.co.okheeokey.question.domain.QuestionRepository;
import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.domain.QuizSetRepository;
import kr.co.okheeokey.quizset.dto.QuizSetAddDto;
import kr.co.okheeokey.song.domain.Album;
import kr.co.okheeokey.song.domain.AlbumRepository;
import kr.co.okheeokey.song.domain.Song;
import kr.co.okheeokey.song.domain.SongRepository;
import kr.co.okheeokey.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class QuizSetService {
    private final QuizSetRepository quizSetRepository;
    private final AlbumRepository albumRepository;
    private final SongRepository songRepository;
    private final UserRepository userRepository;

    public List<QuizSet> getAllQuizSet() {
        return quizSetRepository.findAll();
    }

    public QuizSet getQuizSet(Long quizSetId) throws NoSuchElementException {
        return quizSetRepository.findById(quizSetId)
                .orElseThrow(() -> new NoSuchElementException("No quiz set exists with id { " + quizSetId + " }"));
    }

    public QuizSet createNewQuizSet(QuizSetAddDto dto) throws IllegalArgumentException{
        EnumSet<QuestionDifficulty> difficultyMask = getDifficultyMask(dto.getEasy(), dto.getMedium(), dto.getHard());

        Set<Question> questionPool = new HashSet<>();
        Set<Song> songs = new HashSet<>(songRepository.findAllById(dto.getSongIdList()));
        List<Album> albums = albumRepository.findAllById(dto.getAlbumIdList());

        if(!albums.isEmpty())
            albums.forEach(a -> songs.addAll(a.getSongList()));

        songs.stream()
            .flatMap(s -> s.getQuestion().stream())
            .filter(q -> difficultyMask.contains(q.getDifficulty()))
            .forEach(questionPool::add);

        if(questionPool.isEmpty())
            throw new IllegalArgumentException("No album or song is selected");

        return quizSetRepository.save(
            QuizSet.builder()
                    .owner(userRepository.findById(dto.getUserId())
                            .orElseThrow(() -> new NoSuchElementException("No user exists with id { " + dto.getUserId() + " }")))
                    .title(dto.getTitle())
                    .description(dto.getDescription())
                    .readyMade(false)
                    .questionPool(new ArrayList<>(questionPool))
                    .build()
        );
    }

    private EnumSet<QuestionDifficulty> getDifficultyMask(Boolean easy, Boolean medium, Boolean hard) {
        EnumSet<QuestionDifficulty> difficultyMask = EnumSet.noneOf(QuestionDifficulty.class);
        if(easy)
            difficultyMask.add(QuestionDifficulty.EASY);
        if(medium)
            difficultyMask.add(QuestionDifficulty.MEDIUM);
        if(hard)
            difficultyMask.add(QuestionDifficulty.HARD);
        return difficultyMask;
    }
}
