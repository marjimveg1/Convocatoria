
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Actor;
import domain.Customer;
import domain.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ActorServiceTest extends AbstractTest {

	// Service under test ---------------------------------
	@Autowired
	private ActorService	actorService;

	@Autowired
	private MessageService	messageService;

	@Autowired
	private CustomerService	customerService;


	// Tests ----------------------------------------------

	@Test
	public void testFindAll() {
		Collection<Actor> actors;
		actors = this.actorService.findAll();
		Assert.notEmpty(actors);
		Assert.notNull(actors);
		//System.out.println(actors);
	}

	@Test
	public void testFindOne() {
		Actor actor;
		actor = this.actorService.findOne(super.getEntityId("customer2"));
		Assert.notNull(actor);
	}

	@Test
	public void testIsBanner() {
		super.authenticate("system");
		Actor actor;
		actor = this.actorService.findOne(super.getEntityId("customer2"));
		Assert.isTrue(!(actor.getUserAccount().getIsBanned()));
		this.actorService.changeBan(actor);
		Assert.isTrue(actor.getUserAccount().getIsBanned());
		super.unauthenticate();
	}

	@Test
	public void testNotBanner() {
		super.authenticate("system");
		Actor actor;
		actor = this.actorService.findOne(super.getEntityId("customer2"));
		Assert.isTrue(!(actor.getUserAccount().getIsBanned()));
		this.actorService.changeBan(actor);
		Assert.isTrue(actor.getUserAccount().getIsBanned());
		this.actorService.changeBan(actor);
		Assert.isTrue(!(actor.getUserAccount().getIsBanned()));
		super.unauthenticate();
	}

	@Test
	public void testIsSuspicious() {
		super.authenticate("customer1");
		Actor sender;
		Actor recipient;
		sender = this.actorService.findPrincipal();
		recipient = this.actorService.findOne(super.getEntityId("customer2"));
		Assert.isTrue(!(sender.getIsSuspicious()));
		Message message;
		message = this.messageService.create();
		message.setBody("Hola éste es el cuerpo del mensaje, viagra");
		message.getRecipients().add(recipient);
		message.setSender(sender);
		message.setSubject("buenas tardes");
		message.setPriority("NEUTRAL");
		message.setTags("");

		message = this.messageService.save(message);
		Assert.isTrue(sender.getIsSuspicious());
		super.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsSuspiciousError() {
		super.authenticate("customer1");
		Actor sender;
		Actor recipient;
		sender = this.actorService.findPrincipal();
		recipient = this.actorService.findOne(super.getEntityId("customer2"));
		Assert.isTrue(!(sender.getIsSuspicious()));
		Message message;
		message = this.messageService.create();
		message.setBody("Hola éste es el cuerpo del mensaje");
		message.getRecipients().add(recipient);
		message.setSender(sender);
		message.setSubject("buenas tardes");
		message.setPriority("NEUTRAL");

		message = this.messageService.save(message);
		Assert.isTrue(sender.getIsSuspicious());
		super.unauthenticate();
	}

	@Test
	public void testIsCustomer() {
		final int id = super.getEntityId("customer1");

		final Actor actor = this.actorService.findOne(id);
		Customer customer = null;

		if (actor instanceof Customer)
			customer = this.customerService.findOne(id);

		System.out.println(customer.getFullname());
		Assert.notNull(customer.getFullname());
	}

	@Test
	public void testSaveActor() {
		Actor actor, saved;
		String middleName, newMiddleName;

		super.authenticate("admin1");

		actor = this.actorService.findOne(super.getEntityId("administrator1"));
		newMiddleName = "manuel";

		middleName = actor.getMiddleName();

		actor.setMiddleName(newMiddleName);

		saved = this.actorService.save(actor);

		Assert.isTrue(!saved.getMiddleName().equals(middleName));

		Assert.notNull(this.actorService.findOne(saved.getId()));

		super.authenticate(null);

	}

}
