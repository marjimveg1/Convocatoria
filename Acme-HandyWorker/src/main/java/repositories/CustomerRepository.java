
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	@Query("select f.customer from FixUpTask f group by f.customer having count(f) >= 1.1*((select count(t) from FixUpTask t)/(select count(c) from Customer c)) order by f.applications.size")
	Collection<Customer> customerMoreThanAverage();

	@Query("select distinct a.fixUpTask.customer from HandyWorker h join h.applications a where a.status='ACCEPTED' and h.id=?1")
	Collection<Customer> findEndorsableCustomers(int handyWorkerId);

	@Query("select c from Customer c where c.userAccount.id=?1")
	Customer findByUserAccount(int userAccountId);

}
