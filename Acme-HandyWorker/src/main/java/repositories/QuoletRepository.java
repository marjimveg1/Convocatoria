
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Customer;
import domain.FixUpTask;
import domain.Quolet;

@Repository
public interface QuoletRepository extends JpaRepository<Quolet, Integer> {

	//QUERY A (1) - The average and standard deviation of the number of published QUOLET per CUSTOMER
	@Query("select avg(f.maxPrice), sqrt(sum(f.maxPrice*f.maxPrice)/count(f.maxPrice)- avg(f.maxPrice)*avg(f.maxPrice)) from FixUpTask f")
	Double[] findDataQuoletPerCustomer();

	//QUERY A (2) - The average and standard deviation of the number of published QUOLET per FUT
	@Query("select avg(f.maxPrice), min(f.maxPrice),max(f.maxPrice), sqrt(sum(f.maxPrice*f.maxPrice)/count(f.maxPrice)- avg(f.maxPrice)*avg(f.maxPrice)) from FixUpTask f")
	Double[] findDataQuoletPerFixUpTask();

	//QUERY B (1) - The ratio of published QUOLET vs total number of QUOLET
	@Query("select (sum(case when q.finalMode=true then 1.0 else 0 end)/count(*)) from Quolet q")
	Double findRatioPublishedQuoletvsAllQuolet();

	//QUERY B (2) - The ratio of published QUOLET vs total number of FUT
	@Query(" select (sum(case when q.finalMode=true then 1.0 else 0 end)/(select count(f) from FixUpTask f)) from Quolet q")
	Double findRatioPublishedQuoletvsAllFixUpTask();

	//QUERY C (1) - The ratio of unpublished QUOLET vs total number of QUOLET
	@Query("select (sum(case when q.finalMode=false then 1.0 else 0 end)/count(*)) from Quolet q")
	Double findRatioUnpublishedQuoletvsAllQuoles();

	//QUERY C (2) - The ratio of unpublished QUOLET vs total number of FUT
	@Query(" select (sum(case when q.finalMode=false then 1.0 else 0 end)/(select count(f) from FixUpTask f)) from Quolet q")
	Double findRatioUnpublishedQuoletvsAllFixUpTask();

	//QUERY A (1) - The average and standard deviation of the number of published QUOLET per CUSTOMER
	@Query("select count(q)*1.0/(select count(f) from FixUpTask f) from Quolet q where q.finalMode=true")
	Double findAvgQuoletPerFixUpTask();

	@Query("select count(f) from FixUpTask f")
	Long findTam();

	@Query("select count(q)*count(q) from FixUpTask f join f.quolets q where q.finalMode=true group by f")
	List<Long> findElementSum();

	@Query("select q.ticker from Quolet q where q.ticker = ?1")
	String existTicker(String ticker);

	//Me da el fut al que pertenece el quolet del parametro
	@Query("select f from FixUpTask f join f.quolets q where q.id= ?1")
	FixUpTask findFixUpTaskByQuoletid(int id);

	//Me da el customer al que pertenece el quolet del parametro
	@Query("select f.customer from FixUpTask f join f.quolets q where q.id= ?1")
	Customer findCustomerByQuoletid(int id);

	//Me da los quolets de un customer pasado por parametro
	@Query("select f.quolets from FixUpTask f join f.customer c where c.id= ?1")
	Collection<Quolet> findQuoletsByCustomerid(int id);

	//Me da los quolets publicadas de la chapuza pasado por parametro
	@Query("select q from FixUpTask f join f.quolets q where q.finalMode=true and f.id=?1")
	Collection<Quolet> findQuoletsPublishedByTask(int id);
}
