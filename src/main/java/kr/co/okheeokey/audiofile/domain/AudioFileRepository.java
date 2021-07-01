package kr.co.okheeokey.audiofile.domain;

import kr.co.okheeokey.question.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AudioFileRepository extends JpaRepository<AudioFile, Long> {
    Optional<AudioFile> findByUuid(UUID uuid);
    Optional<AudioFile> findByQuestionAndDifficulty(Question question, Long difficulty);
}
