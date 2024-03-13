package at.jwe.planetracer.repository;

import at.jwe.planetracer.data.entity.SurveyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<SurveyEntity, Long> {
    List<SurveyEntity> findAllBySurveyId(Long surveyId);
}
