
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {

	@Query("select a from Actor a where a.userAccount.id=?1")
	Actor findActorByUseraccount(int userAccountId);

	@Query("select a from Actor a where a.isSuspicious=true group by a.userAccount")
	Page<Actor> findAllSuspicious(Pageable pageable);

	@Query("select a from Actor a where a.name='System'")
	Actor findSystem();

	@Query("select a from Actor a where a.email=?1")
	Actor findActorByEmail(String email);

}
