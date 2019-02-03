
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.FixUpTask;

@Repository
public interface FixUpTaskRepository extends JpaRepository<FixUpTask, Integer> {

	@Query("select f from FixUpTask f where f.customer.id = ?1")
	Page<FixUpTask> findByCustomerId(int customerId, Pageable pageable);

	@Query("select a.fixUpTask from HandyWorker hw join hw.applications a where hw.id = ?1 and a.status = 'ACCEPTED'")
	Page<FixUpTask> findWorkableByHandyWorkerPrincipal(int handyWorkerId, Pageable pageable);

	@Query("select f from FixUpTask f join f.phases ph where ph.id = ?1")
	FixUpTask findByPhaseId(int phaseId);

	@Query("select f.id from FixUpTask f join f.phases ph where ph.id = ?1")
	Integer findIdByPhaseId(int phaseId);

	@Query("select a.fixUpTask from HandyWorker hw join hw.applications a where hw.id = ?2 and a.status = 'ACCEPTED' and a.fixUpTask.id = ?1")
	FixUpTask findWorkableFixUpTask(int fixUpTaskId, int handyWorkerId);

	@Query("select avg(c.fixUpTasks.size), min(c.fixUpTasks.size), max(c.fixUpTasks.size), sqrt(sum(c.fixUpTasks.size * c.fixUpTasks.size)/ count(c.fixUpTasks.size) -avg(c.fixUpTasks.size)*avg(c.fixUpTasks.size)) from Customer c")
	Double[] findDataNumberFixUpTaskPerUser();

	@Query("select avg(f.maxPrice), min(f.maxPrice),max(f.maxPrice), sqrt(sum(f.maxPrice*f.maxPrice)/count(f.maxPrice)- avg(f.maxPrice)*avg(f.maxPrice)) from FixUpTask f")
	Double[] findDataMaximumPrice();

	@Query("select f.ticker from FixUpTask f where f.ticker = ?1")
	String existTicker(String ticker);

	@Query("select f from FixUpTask f where f.category.id=?1")
	Collection<FixUpTask> findFixUpTaskByCategory(int categoryId);
}
