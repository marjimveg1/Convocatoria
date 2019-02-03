
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Phase;

@Repository
public interface PhaseRepository extends JpaRepository<Phase, Integer> {

	@Query(value = "select ph from Phase ph where ph.id in (select phh.id from FixUpTask f join f.phases phh where f.id = ?1)", countQuery = "select count(ph) from FixUpTask f join f.phases ph where f.id = ?1")
	Page<Phase> findByFixUpTaskId(int fixUpTaskId, Pageable pageable);

}
