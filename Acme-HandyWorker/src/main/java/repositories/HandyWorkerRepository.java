
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.HandyWorker;
import domain.Phase;

@Repository
public interface HandyWorkerRepository extends JpaRepository<HandyWorker, Integer> {

	@Query("select h from HandyWorker h where h.userAccount.id = ?1")
	HandyWorker findByUserAccount(int id);

	@Query("select a.handyWorker.id from FixUpTask f join f.applications a where ?1 member of f.phases and a.status = 'ACCEPTED'")
	int findPhaseCreatorId(Phase phase);

	//Req 12.5.10 
	@Query("select h from HandyWorker h where h.applications.size/ (select avg(h1.applications.size) from HandyWorker h1)>=1.1 order by h.applications.size")
	Collection<HandyWorker> atLeast10Application();

}
