
package controllers.authenticated;

import java.util.Collection;

import javax.validation.Valid;

import org.displaytag.pagination.PaginatedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.BoxService;
import utilities.internal.PaginatedListAdapter;
import controllers.AbstractController;
import domain.Actor;
import domain.Box;
import domain.Message;

@Controller
@RequestMapping(value = "/box/administrator,customer,handyWorker,referee,sponsor")
public class BoxMultiUserController extends AbstractController {

	// Services

	@Autowired
	private BoxService		boxService;

	@Autowired
	private ActorService	actorService;


	// Constructors

	public BoxMultiUserController() {
		super();
	}

	// List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(defaultValue = "1", required = false) final int page, @RequestParam(required = false) final String sort, @RequestParam(required = false) final String dir) {
		ModelAndView result;
		Page<Box> boxes;
		Pageable pageable;
		PaginatedList boxesAdapted;
		Actor actor;

		actor = this.actorService.findPrincipal();

		pageable = this.newFixedPageable(page, dir, sort);
		boxes = this.boxService.findAllBoxByActor(actor, pageable);
		boxesAdapted = new PaginatedListAdapter(boxes, sort);

		result = new ModelAndView("box/list");
		result.addObject("boxes", boxesAdapted);
		result.addObject("requestURI", "box/administrator,customer,handyWorker,referee,sponsor/list.do");

		return result;
	}

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int boxId) {
		ModelAndView result;
		Box box;
		Collection<Message> messages;

		box = this.boxService.findOne(boxId);
		messages = box.getMessages();

		result = new ModelAndView("box/display");
		result.addObject("messages", messages);
		result.addObject("box", box);

		return result;

	}

	// Create

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Box box;

		box = this.boxService.create();

		result = this.createEditModelAndView(box);

		return result;
	}

	// Edit

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int boxId) {
		ModelAndView result;
		Box box;

		box = this.boxService.findOne(boxId);
		Assert.notNull(box);
		result = this.createEditModelAndView(box);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Box box, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(box);
		else
			try {
				this.boxService.save(box);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(box, "box.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Box box, final BindingResult binding) {
		ModelAndView result;

		try {
			this.boxService.delete(box);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(box, "box.commit.error");
		}

		return result;
	}

	// Arcillary methods --------------------------

	protected ModelAndView createEditModelAndView(final Box box) {
		ModelAndView result;

		result = this.createEditModelAndView(box, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Box box, final String messageCode) {
		ModelAndView result;
		Actor sender;

		sender = this.actorService.findPrincipal();

		result = new ModelAndView("box/edit");
		result.addObject("box", box);
		result.addObject("messageCode", messageCode);
		result.addObject("actor", sender);

		return result;

	}

}
