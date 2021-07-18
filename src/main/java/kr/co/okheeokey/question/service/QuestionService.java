package kr.co.okheeokey.question.service;

import kr.co.okheeokey.audiofile.domain.AudioFile;
import kr.co.okheeokey.audiofile.domain.AudioFileRepository;
import kr.co.okheeokey.question.domain.Question;
import kr.co.okheeokey.question.domain.QuestionDifficulty;
import kr.co.okheeokey.question.domain.QuestionRepository;
import kr.co.okheeokey.question.vo.QuestionInfoValues;
import kr.co.okheeokey.question.vo.QuestionResultValues;
import kr.co.okheeokey.song.domain.Song;
import kr.co.okheeokey.song.domain.SongRepository;
import kr.co.okheeokey.song.vo.AlbumInfoValues;
import kr.co.okheeokey.song.vo.SongInfoValues;
import kr.co.okheeokey.util.CryptoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final AudioFileRepository audioFileRepository;
    private final SongRepository songRepository;

    public QuestionInfoValues getRandomQuestion(QuestionDifficulty difficulty) throws NoSuchElementException {
        List<Question> questionList = questionRepository.findAllByDifficulty(difficulty);

        if (questionList.isEmpty())
            throw new NoSuchElementException("No question exists with difficulty { " + difficulty.name() + " }");

        Collections.shuffle(questionList);
        Question question = questionList.get(0);

        if (question.getAudioList().isEmpty())
            throw new NoSuchElementException("No audio file corresponding to question { " + question.getId() + " } exists");
        if (!question.getAudioList().containsKey(0L))
            throw new NoSuchElementException("No default audio file corresponding to question { " + question.getId() + " } exists");

        return QuestionInfoValues.builder()
                .encryptUuid(CryptoUtils.encryptUuid(question.getAudioList().get(0L).getUuid()))
                .hintAvailable(question.getAudioList().containsKey(1L))
                .nextHintCost(0L)
                .build();
    }

    public QuestionInfoValues getQuestionHint(String encryptUuid) throws NoSuchElementException {
        UUID uuid = CryptoUtils.decryptUuid(encryptUuid);
        AudioFile audioFile = audioFileRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("No audio file exists with encryptUuid { " + encryptUuid + " }"));

        Map<Long, AudioFile> audioList = audioFile.getQuestion().getAudioList();
        Long nextDifficulty = audioFile.getDifficulty() + 1L;
        if (!audioList.containsKey(nextDifficulty))
            throw new NoSuchElementException("No additional audio file hint exists with encryptUuid { " + encryptUuid + " }");

        return QuestionInfoValues.builder()
                .encryptUuid(CryptoUtils.encryptUuid(audioList.get(nextDifficulty).getUuid()))
                .hintAvailable(audioList.containsKey(nextDifficulty + 1L))
                .nextHintCost(0L)
                .build();
    }

    public QuestionResultValues submitResponse(String encryptUuid, Long responseSongId) throws NoSuchElementException {
        UUID uuid = CryptoUtils.decryptUuid(encryptUuid);
        AudioFile audioFile = audioFileRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("No audio file exists with encryptUuid { " + encryptUuid + " }"));
        Question question = audioFile.getQuestion();

        Song song = songRepository.findById(responseSongId)
                .orElseThrow(() -> new NoSuchElementException("No song with id { " + responseSongId + " } exists "));

        return QuestionResultValues.builder()
                .songInfoValues(SongInfoValues.builder()
                        .songName(song.getSongName())
                        .albumValues(new AlbumInfoValues(song.getAlbum()))
                        .build())
                .correct(question.getSong().equals(song))
                .youtubeAddress(question.getSongYoutubeLink().getYoutubeAddress(question.getAnswerLocationInSecond()))
                .build();

    }
}
