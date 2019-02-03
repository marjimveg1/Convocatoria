
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.BoxRepository;
import domain.Actor;
import domain.Box;
import domain.Message;

@Service
@Transactional
public class BoxService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private BoxRepository	boxRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService	actorService;


	// Constructors -----------------------------------------------------------

	public BoxService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Box create() {
		Box result;
		final Actor actor;

		actor = this.actorService.findPrincipal();
		Assert.notNull(actor);

		result = new Box();
		result.setMessages(new ArrayList<Message>());
		result.setActor(actor);

		return result;
	}

	public Collection<Box> findAll() {
		Collection<Box> result;

		result = this.boxRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Box findOne(final int boxId) {
		Assert.isTrue(boxId != 0);

		Box result;

		result = this.boxRepository.findOne(boxId);
		Assert.notNull(result);

		return result;
	}

	public Box save(final Box box) {
		Assert.notNull(box);
		Assert.isTrue(!(box.getIsSystemBox()));
		this.checkByPrincipal(box);
		this.checkName(box);

		Box result;

		result = this.boxRepository.save(box);

		return result;
	}

	public void delete(final Box box) {
		Assert.notNull(box);
		Assert.isTrue(box.getId() != 0);
		Assert.isTrue(!(box.getIsSystemBox()));
		this.checkByPrincipal(box);

		this.boxRepository.delete(box);
	}

	// Other business methods -------------------------------------------------
	protected void checkByPrincipal(final Box box) {
		final Actor actor;

		actor = this.actorService.findPrincipal();

		Assert.notNull(actor);
		Assert.isTrue(box.getActor().equals(actor));
	}

	private void checkName(final Box box) {
		Assert.isTrue((!(box.getName().equals("in box"))) && (!(box.getName().equals("out box"))) && (!(box.getName().equals("trash box"))) && (!(box.getName().equals("spam box"))));
	}

	public void createDefaultBox(final Actor actor) {
		Box inbox, outbox, trashbox, spambox;

		inbox = new Box();
		outbox = new Box();
		trashbox = new Box();
		spambox = new Box();

		inbox.setMessages(Collections.<Message> emptySet());
		outbox.setMessages(Collections.<Message> emptySet());
		trashbox.setMessages(Collections.<Message> emptySet());
		spambox.setMessages(Collections.<Message> emptySet());

		inbox.setActor(actor);
		outbox.setActor(actor);
		trashbox.setActor(actor);
		spambox.setActor(actor);

		inbox.setName("in box");
		outbox.setName("out box");
		trashbox.setName("trash box");
		spambox.setName("spam box");

		inbox.setIsSystemBox(true);
		outbox.setIsSystemBox(true);
		trashbox.setIsSystemBox(true);
		spambox.setIsSystemBox(true);

		inbox = this.boxRepository.save(inbox);
		outbox = this.boxRepository.save(outbox);
		trashbox = this.boxRepository.save(trashbox);
		spambox = this.boxRepository.save(spambox);
	}

	public Box searchBox(final Actor actor, final String nameBox) {
		Box searchBox;

		searchBox = this.boxRepository.searchBox(actor.getId(), nameBox);

		Assert.notNull(searchBox);

		return searchBox;
	}

	public Page<Box> findAllBoxByActor(final Actor actor, final Pageable pageable) {
		Page<Box> res;

		res = this.boxRepository.findAllBoxByActor(actor.getId(), pageable);

		return res;
	}

	public Collection<Box> findBoxesByActor(final int actorId) {
		Collection<Box> result;

		result = this.boxRepository.findBoxesByActor(actorId);

		return result;
	}

	public Collection<Box> boxWithMessage(final Message message) {
		Collection<Box> res = null;

		res = this.boxRepository.boxWithMessage(message.getId());

		return res;
	}

	public Box searchBoxByMessageAndActor(final int messageId, final int actorId) {
		Box result;

		result = this.boxRepository.searchBoxByMessageAndActor(messageId, actorId);

		return result;
	}

}
