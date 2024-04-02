package at.jwe.snyder.repository;

import at.jwe.snyder.data.entity.ResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<ResultEntity, Long> {
    List<ResultEntity> findAllByLevelId(Long levelId);

    Optional<ResultEntity> findByLevelId(Long levelId);
}