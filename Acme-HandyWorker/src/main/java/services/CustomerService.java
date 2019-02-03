
package services;

import java.util.Collection;
import java.util.Collections;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CustomerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Customer;
import domain.FixUpTask;

@Service
@Transactional
public class CustomerService {

	// Managed repository ----------------------------
	@Autowired
	private CustomerRepository	customerRepository;

	// Supporting services -------------------------

	@Autowired
	private ActorService		actorService;


	// Constructors ----------------------------------
	public CustomerService() {
		super();
	}

	// Simple CRUD methods ---------------------------
	public Customer findOne(final int customerId) {
		Customer result;

		result = this.customerRepository.findOne(customerId);
		Assert.notNull(result);

		return result;
	}

	public Customer create() {
		Customer result;

		result = new Customer();
		result.setFixUpTasks(Collections.<FixUpTask> emptySet());
		result.setUserAccount(this.actorService.createUserAccount(Authority.CUSTOMER));

		return result;
	}

	public Customer save(final Customer customer) {
		Customer result;

		result = (Customer) this.actorService.save(customer);

		return result;
	}

	// Other business methods ------------------------
	public Collection<Customer> customerMoreThanAverage() {
		Collection<Customer> results;

		results = this.customerRepository.customerMoreThanAverage();

		return results;
	}

	public Collection<Customer> findEndorsableCustomers(final int handyWorkerId) {
		Collection<Customer> results;

		results = this.customerRepository.findEndorsableCustomers(handyWorkerId);

		return results;
	}

	public Customer findByPrincipal() {
		Customer result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();

		result = this.findByUserAccount(userAccount.getId());

		return result;
	}

	public Customer findByUserAccount(final int userAccountId) {
		Customer result;

		result = this.customerRepository.findByUserAccount(userAccountId);

		return result;
	}

	protected void addFixUpTask(final Customer customer, final FixUpTask f) {
		Collection<FixUpTask> fixUpTasks;

		fixUpTasks = customer.getFixUpTasks();
		fixUpTasks.add(f);

		customer.setFixUpTasks(fixUpTasks);
	}

	protected void removeFixUpTask(final Customer customer, final FixUpTask f) {
		Collection<FixUpTask> fixUpTasks;

		fixUpTasks = customer.getFixUpTasks();
		fixUpTasks.remove(f);

		customer.setFixUpTasks(fixUpTasks);
	}

}
