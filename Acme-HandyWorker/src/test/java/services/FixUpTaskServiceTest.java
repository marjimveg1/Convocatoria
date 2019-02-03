
package services;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Category;
import domain.Customer;
import domain.FixUpTask;
import domain.Warranty;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class FixUpTaskServiceTest extends AbstractTest {

	// Service under test -----------------------------------------------------

	@Autowired
	private FixUpTaskService	fixUpTaskService;

	// Supporting services ----------------------------------------------------

	@Autowired
	private CategoryService		categoryService;

	@Autowired
	private WarrantyService		warrantyService;

	@Autowired
	private CustomerService		customerService;


	// Test -------------------------------------------------------------------

	@Test
	public void testdata() {
		Double[] data;

		data = this.fixUpTaskService.findDataNumberFixUpTaskPerUser();
		Assert.notNull(data);
		System.out.println(data.length);
		//System.out.println(data[0]);
	}

	@Test
	public void testSaveDeleteFixUpTask() {
		FixUpTask fixUpTask, saved;
		Warranty warranty;
		Category category;
		Customer customer;

		category = this.categoryService.findOne(super.getEntityId("category1"));
		warranty = this.warrantyService.findOne(super.getEntityId("warranty1"));

		super.authenticate("customer1");

		customer = this.customerService.findByPrincipal();

		fixUpTask = this.fixUpTaskService.create();
		fixUpTask.setAddress("Direccion de test");
		fixUpTask.setCategory(category);
		fixUpTask.setCustomer(customer);
		fixUpTask.setDescription("Descripción de test");
		fixUpTask.setEndDate(LocalDate.now().plusYears(1).toDate());
		fixUpTask.setMaxPrice(5000.0);
		fixUpTask.setPublicationMoment(LocalDate.now().toDate());
		fixUpTask.setStartDate(LocalDate.now().plusMonths(1).toDate());
		fixUpTask.setWarranty(warranty);

		saved = this.fixUpTaskService.save(fixUpTask);

		customer = this.customerService.findByPrincipal();
		Assert.isTrue(customer.getFixUpTasks().contains(saved));

		this.fixUpTaskService.delete(saved);
		customer = this.customerService.findByPrincipal();
		Assert.isTrue(!customer.getFixUpTasks().contains(fixUpTask));

		super.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUpdateNotOwnedFixUpTask() {
		FixUpTask fixUpTask, saved;
		Warranty warranty;
		Category category;
		Customer customer;

		category = this.categoryService.findOne(super.getEntityId("category1"));
		warranty = this.warrantyService.findOne(super.getEntityId("warranty1"));

		super.authenticate("customer1");
		customer = this.customerService.findByPrincipal();
		fixUpTask = this.fixUpTaskService.create();
		fixUpTask.setAddress("Direccion de test");
		fixUpTask.setCategory(category);
		fixUpTask.setCustomer(customer);
		fixUpTask.setDescription("Descripción de test");
		fixUpTask.setEndDate(LocalDate.now().plusYears(1).toDate());
		fixUpTask.setMaxPrice(5000.0);
		fixUpTask.setPublicationMoment(LocalDate.now().toDate());
		fixUpTask.setStartDate(LocalDate.now().plusMonths(1).toDate());
		fixUpTask.setWarranty(warranty);
		saved = this.fixUpTaskService.save(fixUpTask);
		super.unauthenticate();

		super.authenticate("customer2");
		fixUpTask = this.fixUpTaskService.findOne(saved.getId());
		fixUpTask.setAddress("Actualizando dirección en los tests");
		this.fixUpTaskService.save(fixUpTask);
		super.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUpdateFixUpTaskWithApplications() {
		FixUpTask fixUpTask;

		fixUpTask = this.fixUpTaskService.findOne(super.getEntityId("fixUpTask7"));

		super.authenticate("customer6");

		fixUpTask.setAddress("Actualizando dirección en los tests");
		this.fixUpTaskService.save(fixUpTask);

		super.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDeleteNotOwnedFixUpTask() {
		FixUpTask fixUpTask, saved;
		Warranty warranty;
		Category category;
		Customer customer;

		category = this.categoryService.findOne(super.getEntityId("category1"));
		warranty = this.warrantyService.findOne(super.getEntityId("warranty1"));

		super.authenticate("customer1");
		customer = this.customerService.findByPrincipal();
		fixUpTask = this.fixUpTaskService.create();
		fixUpTask.setAddress("Direccion de test");
		fixUpTask.setCategory(category);
		fixUpTask.setCustomer(customer);
		fixUpTask.setDescription("Descripción de test");
		fixUpTask.setEndDate(LocalDate.now().plusYears(1).toDate());
		fixUpTask.setMaxPrice(5000.0);
		fixUpTask.setPublicationMoment(LocalDate.now().toDate());
		fixUpTask.setStartDate(LocalDate.now().plusMonths(1).toDate());
		fixUpTask.setWarranty(warranty);
		saved = this.fixUpTaskService.save(fixUpTask);
		super.unauthenticate();

		super.authenticate("customer2");
		fixUpTask = this.fixUpTaskService.findOne(saved.getId());
		this.fixUpTaskService.delete(fixUpTask);
		super.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDeleteFixUpTaskWithApplications() {
		FixUpTask fixUpTask;

		fixUpTask = this.fixUpTaskService.findOne(super.getEntityId("fixUpTask7"));

		super.authenticate("customer6");

		this.fixUpTaskService.delete(fixUpTask);

		super.unauthenticate();
	}

}
