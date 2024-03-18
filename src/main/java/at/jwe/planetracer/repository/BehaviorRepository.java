package at.jwe.planetracer.repository;

import at.jwe.planetracer.data.entity.BehaviorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BehaviorRepository extends JpaRepository<BehaviorEntity, Long> {
}
