package at.jwe.snyder.repository;

import at.jwe.snyder.data.entity.DataPointEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataPointRepository extends CrudRepository<DataPointEntity, Integer> {
}
