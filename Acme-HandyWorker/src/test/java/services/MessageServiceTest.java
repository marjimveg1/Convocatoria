
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
import domain.Actor;
import domain.Application;
import domain.Box;
import domain.HandyWorker;
import domain.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class MessageServiceTest extends AbstractTest {

	// Service under test ---------------------------------
	@Autowired
	private MessageService		messageService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private BoxService			boxService;

	@Autowired
	private ApplicationService	applicationService;


	// Tests ----------------------------------------------

	@Test
	public void testCreate() {
		super.authenticate("sponsor1");
		final Message message;
		message = this.messageService.create();
		Assert.notNull(message);
		super.unauthenticate();
	}

	@Test
	public void testFindOne() {
		Message message;
		message = this.messageService.findOne(super.getEntityId("message1"));
		Assert.notNull(message);
	}

	@Test
	public void testFindAll() {
		Collection<Message> messages;
		messages = this.messageService.findAll();
		Assert.notEmpty(messages);
		Assert.notNull(messages);
	}

	@Test
	public void testSave() {
		super.authenticate("customer1");
		Message message;

		final Actor recipient = this.actorService.findOne(super.getEntityId("customer2"));
		final Actor sender = this.actorService.findPrincipal();

		message = this.messageService.create();
		message.setBody("Hola éste es el cuerpo del mensaje, viagra");
		message.getRecipients().add(recipient);
		message.setSender(sender);
		message.setSubject("buenas tardes");
		message.setPriority("NEUTRAL");
		message.setTags("");

		message = this.messageService.save(message);

		super.unauthenticate();
	}

	@Test
	public void testDelete() {
		super.authenticate("sponsor1");
		final Message message;
		message = this.messageService.findOne(super.getEntityId("message6"));
		Assert.notNull(message);
		this.messageService.delete(message);
		super.unauthenticate();
	}

	@Test
	public void moveMessageFromBoxToBoxTest() {
		super.authenticate("handyworker1");
		final Message message;
		Box box1;
		Box box2;
		message = this.messageService.findOne(super.getEntityId("message2"));
		box1 = this.boxService.findOne(super.getEntityId("box9"));
		box2 = this.boxService.findOne(super.getEntityId("box10"));
		this.messageService.moveMessageFromBoxToBox(box1, box2, message);
		super.unauthenticate();
	}

	@Test
	public void messageToStatusTest() {
		super.authenticate("customer1");
		Application application;
		Box inBoxHandyWorker1;
		Box inBoxHandyWorker2;

		final int size1;
		final int size2;

		application = this.applicationService.findOne(this.getEntityId("application2"));
		final HandyWorker hw = application.getHandyWorker();
		inBoxHandyWorker1 = this.boxService.searchBox(hw, "in box");
		size1 = inBoxHandyWorker1.getMessages().size();

		this.messageService.messageToStatus(application, "rejected");

		inBoxHandyWorker2 = this.boxService.searchBox(hw, "in box");

		size2 = inBoxHandyWorker2.getMessages().size();

		Assert.isTrue(size1 + 1 == size2);
		super.unauthenticate();
	}

	@Test
	public void broadcastMessageTest() {
		super.authenticate("admin1");
		Message message;
		Collection<Actor> recipients;
		Box outBoxSender;

		final Actor sender = this.actorService.findPrincipal();
		recipients = this.actorService.findAll();

		message = this.messageService.create();
		message.setBody("Hola éste es el cuerpo del mensaje");
		message.getRecipients().addAll(recipients);
		message.setSender(sender);
		message.setSubject("buenas tardes");
		message.setPriority("NEUTRAL");
		message.setTags("");

		message = this.messageService.save(message);

		for (final Actor r : recipients) {
			Box inbox;

			inbox = this.boxService.searchBox(r, "in box");
			Assert.isTrue(this.boxService.boxWithMessage(message).contains(inbox));
		}

		outBoxSender = this.boxService.searchBox(sender, "out box");
		Assert.isTrue(this.boxService.boxWithMessage(message).contains(outBoxSender));

		super.unauthenticate();

	}
}
