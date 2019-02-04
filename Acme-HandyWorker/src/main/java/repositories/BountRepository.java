
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Bount;
import domain.Customer;
import domain.FixUpTask;

@Repository
public interface BountRepository extends JpaRepository<Bount, Integer> {

	//QUERY B - The ratio of published BOUNT vs total number of BOUNTS
	@Query("select (sum(case when b.finalMode=true then 1.0 else 0 end)/count(*)) from Bount b")
	Double findRatioPublishedBountvsAllBount();

	//QUERY C  - The ratio of unpublished BOUNT vs total number of BOUNTS
	@Query("select (sum(case when b.finalMode=false then 1.0 else 0 end)/count(*)) from Bount b")
	Double findRatioUnpublishedBountvsAllQuoles();

	//QUERY A - The average and standard deviation of the number of published BOUNT per FUT
	@Query("select count(b)*1.0/(select count(f) from FixUpTask f) from Bount b where b.finalMode=true")
	Double findAvgBountPerFixUpTask();

	@Query("select count(f) from FixUpTask f")
	Long findTam();

	@Query("select count(b)*count(b) from FixUpTask f join f.bounts b where b.finalMode=true group by f")
	List<Long> findElementSum();

	@Query("select b.ticker from Bount b where b.ticker = ?1")
	String existTicker(String ticker);

	//Me da el fut al que pertenece el bount del parametro
	@Query("select f from FixUpTask f join f.bounts b where b.id= ?1")
	FixUpTask findFixUpTaskByBountid(int id);

	//Me da el customer al que pertenece el bount del parametro
	@Query("select f.customer from FixUpTask f join f.bounts b where b.id= ?1")
	Customer findCustomerByBountid(int id);

	//Me da los bounts de un customer pasado por parametro
	@Query("select f.bounts from FixUpTask f join f.customer c where c.id= ?1")
	Collection<Bount> findBountsByCustomerid(int id);

	//Me da los bounts publicadas de la chapuza pasado por parametro
	@Query("select b from FixUpTask f join f.bounts b where b.finalMode=true and f.id=?1")
	Collection<Bount> findBountsPublishedByTask(int id);
}
