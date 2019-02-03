
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.UserAccount;
import utilities.AbstractTest;
import domain.Customer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class CustomerServiceTest extends AbstractTest {

	// Services under testing ---------------------------
	@Autowired
	private CustomerService	customerService;


	// Supporting services ------------------------------

	// Test ---------------------------------------------

	@Test
	public void testCreate() {
		Customer customer;

		customer = this.customerService.create();

		Assert.notNull(customer);
		Assert.notNull(customer.getUserAccount());
		Assert.notNull(customer.getFixUpTasks());
		Assert.isNull(customer.getEmail());
		Assert.isNull(customer.getAddress());
		Assert.isNull(customer.getMiddleName());
		Assert.isNull(customer.getName());
		Assert.isNull(customer.getPhoneNumber());
		Assert.isNull(customer.getPhotoLink());
		Assert.isNull(customer.getSurname());
		Assert.isTrue(customer.getIsSuspicious() == false);
	}

	/* Test negativo: customer = null */
	@Test(expected = IllegalArgumentException.class)
	public void negativeTestSave_uno() {
		Customer customer, saved;

		customer = null;

		saved = this.customerService.save(customer);

		Assert.isNull(saved);
	}

	/*
	 * Test negativo: admin1 trata de editar los datos personales
	 * de otro actor.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negativeTestSave_dos() {
		super.authenticate("admin1");

		int id;
		Customer customer, saved;

		id = super.getEntityId("customer1");

		customer = this.customerService.findOne(id);
		customer.setName("Paca");
		customer.setSurname("Pacheco Reynolds");

		saved = this.customerService.save(customer);

		Assert.isNull(saved);

		super.unauthenticate();
	}

	/* Formato de correo: "identifier@domain" */
	@Test
	public void positiveTestSave_uno() {
		Customer customer, saved, showed;
		UserAccount userAccount;

		customer = this.customerService.create();
		customer.setAddress("C/ Columbia");
		customer.setEmail("georgeg@alum.us.es");
		customer.setMiddleName("Ronald");
		customer.setName("George");
		customer.setPhoneNumber("+34 611203040");
		customer.setSurname("Martin");

		userAccount = customer.getUserAccount();
		userAccount.setUsername("customer7");
		userAccount.setPassword("customer7");

		customer.setUserAccount(userAccount);

		saved = this.customerService.save(customer);

		showed = this.customerService.findOne(saved.getId());

		Assert.notNull(showed);

	}

	/* Formato de correo: "identifier@domain" */
	@Test
	public void positiveTestSave_dos() {
		Customer customer, saved, showed;
		UserAccount userAccount;

		customer = this.customerService.create();
		customer.setAddress("C/ Columbia");
		customer.setEmail("Georgia <georgeg@alum.us.es>");
		customer.setMiddleName("Ronald");
		customer.setName("George");
		customer.setPhoneNumber("+34 611203040");
		customer.setSurname("Martin");

		userAccount = customer.getUserAccount();
		userAccount.setUsername("customer7");
		userAccount.setPassword("customer7");

		customer.setUserAccount(userAccount);

		saved = this.customerService.save(customer);

		showed = this.customerService.findOne(saved.getId());

		Assert.notNull(showed);
	}

	@Test
	public void positiveTestSave_tres() {
		super.authenticate("customer1");

		Customer customer, saved, showed;
		int id;

		final String nombre = "Lola";
		final String apellidos = "Reina Alvarez";
		id = super.getEntityId("customer1");

		customer = this.customerService.findOne(id);
		customer.setName(nombre);
		customer.setSurname(apellidos);

		saved = this.customerService.save(customer);

		showed = this.customerService.findOne(saved.getId());

		Assert.notNull(showed);
		Assert.isTrue(saved.getId() == id);
		Assert.isTrue(showed.getName().equals(nombre));
		Assert.isTrue(showed.getSurname().equals(apellidos));

		super.unauthenticate();
	}

	@Test
	public void testFindEndorsableCustomers() {
		Collection<Customer> customers;
		final int handyWorkerId = super.getEntityId("handyworker3");

		customers = this.customerService.findEndorsableCustomers(handyWorkerId);

		Assert.notNull(customers);
		Assert.isTrue(customers.size() == 2);
	}

}
