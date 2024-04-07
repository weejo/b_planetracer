package at.jwe.snyder.repository;

import at.jwe.snyder.data.entity.SolutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolutionRepository extends JpaRepository<SolutionEntity, Integer> {
}
