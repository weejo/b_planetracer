package at.jwe.snyder.repository;

import at.jwe.snyder.data.entity.HighscoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HighscoreRepository extends JpaRepository<HighscoreEntity, Integer> {

    List<HighscoreEntity> findAllByLevelId(Integer levelId);
}
