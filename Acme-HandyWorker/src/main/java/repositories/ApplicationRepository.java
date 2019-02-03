
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<domain.Application, Integer> {

	//Req 12.5.2 
	@Query("select avg(f.applications.size), min(f.applications.size), max(f.applications.size), sqrt(sum(f.applications.size * f.applications.size)/count(f.applications.size) - avg(f.applications.size)*avg(f.applications.size)) from FixUpTask f")
	Double[] findDataOfApplicationPerFixUpTask();

	//Req 12.5.4 
	@Query("select avg(a.offeredPrice),min(a.offeredPrice),max(a.offeredPrice),sqrt(sum(a.offeredPrice * a.offeredPrice)/count(a.offeredPrice)-avg(a.offeredPrice)*avg(a.offeredPrice)) from Application a")
	Double[] findDataOfApplicationPrice();

	//Req 12.5.5 
	@Query("select (sum(case when a.status='PENDING' then 1.0 else 0 end)/count(*)) from Application a")
	Double findRatioPendingApplications();

	//Req 12.5.6
	@Query("select (sum(case when a.status='ACCEPTED' then 1.0 else 0 end)/count(*)) from Application a")
	Double findRatioAcceptedApplications();

	//Req 12.5.7 
	@Query("select (sum(case when a.status='REJECTED' then 1.0 else 0 end)/count(*)) from Application a")
	Double findRatioRejectedApplications();

	//Req 12.5.8
	@Query("select (sum(case when ((a.status='PENDING') and (a.registerMoment<CURRENT_TIMESTAMP))then 1.0 else 0 end)/count(*))from Application a")
	Double findRatioPendingApplicationsNotChangeStatus();

	@Query("select a from Application a where a.fixUpTask.id = ?1 and a.status = 'ACCEPTED'")
	Application findAcceptedApplication(int fixUpTaskId);

	@Query("select a from Application a where a.handyWorker.id = ?1")
	Page<Application> findApplicationByHandyWorker(int handyWorkerid, Pageable pageable);

	@Query("select a from Application a where a.fixUpTask.id = ?1")
	Page<Application> findApplicationByFixUpTask(final int fixUpTaskId, final Pageable pageable);

	@Query("select a from Application a where a.handyWorker.id =?1 and a.fixUpTask.id=?2")
	Collection<Application> findApplicationByHWFixUpTask(int idHW, int idFixUpTask);

}
