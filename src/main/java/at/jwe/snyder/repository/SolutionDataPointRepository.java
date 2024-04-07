package at.jwe.snyder.repository;

import at.jwe.snyder.data.entity.SolutionDataPointEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolutionDataPointRepository extends CrudRepository<SolutionDataPointEntity, Integer> {
}
