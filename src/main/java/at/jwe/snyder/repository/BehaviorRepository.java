package at.jwe.snyder.repository;

import at.jwe.snyder.data.entity.BehaviorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BehaviorRepository extends JpaRepository<BehaviorEntity, Long> {
}
