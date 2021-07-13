package kr.co.okheeokey.quizset.service;

import kr.co.okheeokey.question.domain.Question;
import kr.co.okheeokey.question.domain.QuestionDifficulty;
import kr.co.okheeokey.quizset.domain.QuizSet;
import kr.co.okheeokey.quizset.domain.QuizSetRepository;
import kr.co.okheeokey.quizset.vo.QuizSetCreateValues;
import kr.co.okheeokey.quizset.vo.QuizSetInfoValues;
import kr.co.okheeokey.song.domain.Album;
import kr.co.okheeokey.song.domain.AlbumRepository;
import kr.co.okheeokey.song.domain.Song;
import kr.co.okheeokey.song.domain.SongRepository;
import kr.co.okheeokey.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class QuizSetService {
    private final QuizSetRepository quizSetRepository;
    private final AlbumRepository albumRepository;
    private final SongRepository songRepository;

    public List<QuizSetInfoValues> getAllQuizSet(User user) {
        List<QuizSet> availableQuizSets = quizSetRepository.findAllByReadyMadeIsTrue();
        if(user != null)
            availableQuizSets.addAll(quizSetRepository.findAllByOwner(user));

        return availableQuizSets.stream().map(QuizSetInfoValues::new).collect(Collectors.toList());
    }

    public QuizSetInfoValues getQuizSet(User user, Long quizSetId) throws NoSuchElementException, IllegalAccessException {
        QuizSet quizSet = quizSetRepository.findById(quizSetId)
                .orElseThrow(() -> new NoSuchElementException("No quiz set exists with id { " + quizSetId + " }"));

        return Optional.of(quizSet)
                .filter(qs -> qs.getOwner().equals(user) || qs.getReadyMade())
                .map(QuizSetInfoValues::new)
                .orElseThrow(() -> new IllegalAccessException("Unauthorized access to quiz set with id { " + quizSet.getId() + " }"));
    }

    public QuizSet createNewQuizSet(User user, QuizSetCreateValues values) throws IllegalArgumentException {
        EnumSet<QuestionDifficulty> difficultyMask = getDifficultyMask(values.getEasy(), values.getMedium(), values.getHard());

        Set<Question> questionPool = new HashSet<>();
        Set<Song> songs = new HashSet<>(songRepository.findAllById(values.getSongIdList()));
        List<Album> albums = albumRepository.findAllById(values.getAlbumIdList());

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
                    .owner(user)
                    .title(values.getTitle())
                    .description(values.getDescription())
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
