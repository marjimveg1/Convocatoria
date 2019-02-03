
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Application;
import domain.CreditCard;
import domain.FixUpTask;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ApplicationServiceTest extends AbstractTest {

	//Service under test ----------------------------------
	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private FixUpTaskService	fixUpTaskService;


	// Test ------------------------------------------------
	@Test
	public void testdata() {
		Double[] data;

		data = this.applicationService.findDataOfApplicationPrice();
		Assert.notNull(data);
		System.out.println(data.length);
		System.out.println(data[0]);
		System.out.println(data[1]);
		System.out.println(data[2]);
		System.out.println(data[3]);
	}

	@Test
	public void testCreate() {
		super.authenticate("handyWorker3");
		final Application application;
		FixUpTask fixUpTask;
		fixUpTask = this.fixUpTaskService.findOne(super.getEntityId("fixUpTask4"));
		application = this.applicationService.create(fixUpTask);
		Assert.isTrue(application.getStatus().equals("PENDING"));
		Assert.notNull(application);
		super.unauthenticate();
	}

	/*
	 * Can not edit application which status is accepted
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSaveNegative() {
		super.authenticate("handyworker2");
		final Application application;
		final Application applicationSaved;

		application = this.applicationService.findOne(super.getEntityId("application3"));
		application.setStatus("ACCEPTED");

		applicationSaved = this.applicationService.save(application);

		Assert.isNull(applicationSaved);

		super.unauthenticate();
	}
	/*
	 * Can not edit application which status is rejected
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSaveNegative1() {
		super.authenticate("handyworker2");
		final Application application;
		final Application applicationSaved;

		application = this.applicationService.findOne(super.getEntityId("application3"));
		application.setStatus("REJECTED");

		applicationSaved = this.applicationService.save(application);

		Assert.isNull(applicationSaved);

		super.unauthenticate();
	}
	/*
	 * Can not change application which handyWorker has not curriculum
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSaveNegative3() {
		super.authenticate("handyworker2");
		final Application application;
		final Application applicationSaved;

		application = this.applicationService.findOne(super.getEntityId("application3"));
		application.setStatus("REJECTED");
		applicationSaved = this.applicationService.save(application);

		Assert.isNull(applicationSaved);

		super.unauthenticate();
	}
	@Test
	public void testFindAll() {
		Collection<Application> applications;
		applications = this.applicationService.findAll();
		Assert.notEmpty(applications);
		Assert.notNull(applications);

	}

	@Test
	public void testFindOne() {
		Application application;

		application = this.applicationService.findOne(super.getEntityId("application2"));
		Assert.notNull(application);

	}

	@Test
	public void testfindDataOfApplicationPrice() {
		Double[] result;

		result = this.applicationService.findDataOfApplicationPrice();
		Assert.notNull(result);
	}
	@Test
	public void testfindRatioPendingApplications() {
		Double result;

		result = this.applicationService.findRatioPendingApplications();

		Assert.notNull(result);
	}
	@Test
	public void testfindRatioAcceptedApplications() {
		Double result;

		result = this.applicationService.findRatioAcceptedApplications();

		Assert.notNull(result);
	}
	@Test
	public void testfindRatioRejectedApplications() {
		Double result;

		result = this.applicationService.findRatioRejectedApplications();

		Assert.notNull(result);
	}
	@Test
	public void testfindRatioPendingApplicationsNotChangeStatus() {
		Double result;

		result = this.applicationService.findRatioPendingApplicationsNotChangeStatus();

		Assert.notNull(result);
	}

	@Test
	public void testCreckCreditCard() {
		super.authenticate("handyWorker1");
		final Application application;
		CreditCard creditCard;

		creditCard = new CreditCard();
		creditCard.setBrandName("maria");
		creditCard.setCvvCode(123);
		creditCard.setExpirationMonth("02");
		creditCard.setHolderName("maria");
		creditCard.setExpirationYear("23");
		creditCard.setNumber("6702386065238009");

		application = this.applicationService.findOne(super.getEntityId("application3"));
		this.applicationService.addCreditCard(application, creditCard);
		Assert.isTrue(application.getCreditCard().equals(creditCard));
		super.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreckCreditCard1() {
		super.authenticate("handyWorker1");
		final Application application;
		CreditCard creditCard;

		creditCard = new CreditCard();
		creditCard.setBrandName("maria");
		creditCard.setCvvCode(023);
		creditCard.setExpirationMonth("0");
		creditCard.setHolderName("maria jimenez");
		creditCard.setExpirationYear("23");
		creditCard.setNumber("679");

		application = this.applicationService.findOne(super.getEntityId("application3"));
		this.applicationService.addCreditCard(application, creditCard);
		Assert.isTrue(application.getCreditCard().equals(creditCard));
		super.unauthenticate();
	}
	/*
	 * Credit card which number is not correct
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNegativeCreckCreditCard() {
		super.authenticate("handyWorker1");
		final Application application;
		CreditCard creditCard;

		creditCard = new CreditCard();
		creditCard.setBrandName("maria");
		creditCard.setCvvCode(123);
		creditCard.setExpirationMonth("08");
		creditCard.setHolderName("maria");
		creditCard.setExpirationYear("22");
		creditCard.setNumber("670209");

		application = this.applicationService.findOne(super.getEntityId("application3"));
		this.applicationService.addCreditCard(application, creditCard);
		Assert.isTrue(application.getCreditCard().equals(creditCard));
		super.unauthenticate();
	}
}
