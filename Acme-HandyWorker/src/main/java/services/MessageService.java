
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.MessageRepository;
import domain.Actor;
import domain.Application;
import domain.Box;
import domain.Customer;
import domain.HandyWorker;
import domain.Message;

@Service
@Transactional
public class MessageService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private MessageRepository	messageRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private BoxService			boxService;

	@Autowired
	private UtilityService		utilityService;


	// Constructors -----------------------------------------------------------

	public MessageService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Message create() {
		Message result;

		final Actor sender = this.actorService.findPrincipal();
		Assert.notNull(sender);

		result = new Message();
		result.setSendMoment(this.utilityService.current_moment());
		result.setSender(sender);
		result.setRecipients(new ArrayList<Actor>());

		return result;
	}

	public Message findOne(final int messageId) {
		Assert.isTrue(messageId != 0);

		Message result;

		result = this.messageRepository.findOne(messageId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Message> findAll() {
		Collection<Message> result;

		result = this.messageRepository.findAll();

		return result;
	}

	public Message save(final Message message) {
		this.utilityService.checkTags(message.getTags());
		Assert.notNull(message);
		Assert.isTrue(message.getId() == 0);
		Assert.notNull(message.getSender());
		Assert.notEmpty(message.getRecipients());
		this.checkByPrincipal(message);

		final Message result;
		final Actor sender;
		final Collection<Actor> recipients;
		final Box outBoxSender;

		sender = this.actorService.findPrincipal();
		recipients = message.getRecipients();
		outBoxSender = this.boxService.searchBox(sender, "out box");

		result = this.messageRepository.save(message);

		if (this.utilityService.checkIsSpamMarkAsSuspicious(message.getBody(), sender) || this.utilityService.checkIsSpamMarkAsSuspicious(message.getSubject(), sender))
			for (final Actor r : recipients) {
				final Box spamBoxRecipients = this.boxService.searchBox(r, "spam box");
				spamBoxRecipients.getMessages().add(result);
			}
		else
			for (final Actor r : recipients) {
				final Box inBoxRecipients = this.boxService.searchBox(r, "in box");
				inBoxRecipients.getMessages().add(result);
			}

		outBoxSender.getMessages().add(result);

		return result;
	}

	public void delete(final Message message) {
		Assert.notNull(message);
		Assert.notNull(message.getId() != 0);

		final Box trashBoxActor;
		final Collection<Message> messagesTrashBox;
		final Actor principal;

		principal = this.actorService.findPrincipal();
		Assert.isTrue((message.getSender().equals(principal)) || (message.getRecipients().contains(principal)));

		trashBoxActor = this.boxService.searchBox(principal, "trash box");
		messagesTrashBox = trashBoxActor.getMessages();

		if (messagesTrashBox.contains(message)) {
			messagesTrashBox.remove(message);
			this.deleteMessageBD(message, principal);
		} else {
			this.deleteMessageAllBoxActor(principal, message);
			trashBoxActor.getMessages().add(message);
		}

	}

	// Other business methods -------------------------------------------------
	protected void checkByPrincipal(final Message message) {
		Actor principal;

		principal = this.actorService.findPrincipal();

		Assert.isTrue(message.getSender().equals(principal));
	}

	private void deleteMessageAllBoxActor(final Actor actor, final Message message) {
		final Collection<Box> findAllBoxByActor = this.boxService.findBoxesByActor(actor.getId());

		for (final Box b : findAllBoxByActor)
			if (b.getMessages().contains(message))
				b.getMessages().remove(message);
	}

	private void deleteMessageBD(final Message message, final Actor actor) {
		if (this.boxService.boxWithMessage(message).isEmpty())
			this.messageRepository.delete(message);
	}

	public void moveMessageFromBoxToBox(final Box boxInicio, final Box boxFin, final Message message) {
		final Actor actor = this.actorService.findPrincipal();

		Assert.isTrue((boxInicio.getActor().equals(actor)) && (boxFin.getActor().equals(actor)));
		Assert.isTrue(boxInicio.getMessages().contains(message));
		Assert.isTrue(!(boxFin.getMessages().contains(message)));
		Assert.notNull(message);
		Assert.notNull(boxInicio);
		Assert.notNull(boxFin);

		boxInicio.getMessages().remove(message);
		boxFin.getMessages().add(message);
	}

	public void messageToStatus(final Application application, final String status) {
		final Actor system;
		final Message message, message_saved;
		HandyWorker handyWorkerApplication;
		Customer customerApplication;
		Box inBoxHandyWorker, inBoxCustomer, outBoxSystemActor;
		String statusEn, statusEs;
		Date sendMoment;
		final List<Actor> recipients;

		system = this.actorService.findSystem();
		handyWorkerApplication = application.getHandyWorker();
		customerApplication = application.getFixUpTask().getCustomer();

		outBoxSystemActor = this.boxService.searchBox(system, "out box");
		inBoxHandyWorker = this.boxService.searchBox(handyWorkerApplication, "in box");
		inBoxCustomer = this.boxService.searchBox(customerApplication, "in box");

		message = new Message();

		statusEn = null;
		statusEs = null;

		if (status.equals("REJECTED")) {
			statusEn = "reject";
			statusEs = "rechazado";
		}

		if (status.equals("ACCEPTED")) {
			statusEn = "acepted";
			statusEs = "aceptado";
		}

		message.setSender(system);
		message.setSubject("Status changed / Estado cambiado");
		message.setBody("The status for application  assigned to fix-up task whose ticker is " + application.getFixUpTask().getTicker() + " is change to " + statusEn + " status.\nEl estado de la solicitud asignada a la tarea de reparación cuyo ticker es "
			+ application.getFixUpTask().getTicker() + " ha cambiado a estado" + statusEs + ".");
		message.setPriority("HIGH");

		recipients = new ArrayList<Actor>();
		recipients.add(handyWorkerApplication);
		recipients.add(customerApplication);

		message.setRecipients(recipients);

		sendMoment = this.utilityService.current_moment();
		message.setSendMoment(sendMoment);

		message_saved = this.messageRepository.save(message);
		Assert.notNull(message_saved);

		inBoxHandyWorker.getMessages().add(message_saved);
		inBoxCustomer.getMessages().add(message_saved);
		outBoxSystemActor.getMessages().add(message_saved);
	}

	public Message broadcastMessage(final Message message) {
		Assert.notNull(message);
		Assert.isTrue(message.getId() == 0);
		Assert.notNull(message.getSender());
		Assert.notEmpty(message.getRecipients());
		this.checkByPrincipal(message);

		final Message result;
		final Actor sender;
		final Collection<Actor> recipients;
		final Box outBoxSender;

		sender = this.actorService.findPrincipal();
		recipients = this.actorService.findAll();
		outBoxSender = this.boxService.searchBox(sender, "out box");

		result = this.messageRepository.save(message);

		if (this.utilityService.checkIsSpamMarkAsSuspicious(message.getBody(), sender) || this.utilityService.checkIsSpamMarkAsSuspicious(message.getSubject(), sender))
			for (final Actor r : recipients) {
				final Box spamBoxRecipients = this.boxService.searchBox(r, "spam box");
				spamBoxRecipients.getMessages().add(result);
			}
		else
			for (final Actor r : recipients) {
				final Box inBoxRecipients = this.boxService.searchBox(r, "in box");
				inBoxRecipients.getMessages().add(result);
			}

		outBoxSender.getMessages().add(result);

		return result;

	}
}
