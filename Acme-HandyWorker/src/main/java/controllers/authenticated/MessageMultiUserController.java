
package controllers.authenticated;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import services.ActorService;
import services.BoxService;
import services.MessageService;
import services.UtilityService;
import controllers.AbstractController;
import converters.StringToBoxConverter;
import converters.StringToMessageConverter;
import domain.Actor;
import domain.Box;
import domain.Message;

@Controller
@RequestMapping(value = "/message/administrator,customer,handyWorker,referee,sponsor")
public class MessageMultiUserController extends AbstractController {

	// Services

	@Autowired
	private MessageService				messageService;

	@Autowired
	private BoxService					boxService;

	@Autowired
	private ActorService				actorService;

	@Autowired
	private UtilityService				utilityService;

	// Converters

	@Autowired
	private StringToMessageConverter	stringToMessageConverter;

	@Autowired
	private StringToBoxConverter		stringToBoxConverter;


	// Contructor

	public MessageMultiUserController() {
		super();
	}

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int messageId) {
		ModelAndView result;
		Message messageToDisplay;
		Box box;
		Actor actor;
		Collection<String> tags;

		actor = this.actorService.findPrincipal();
		messageToDisplay = this.messageService.findOne(messageId);
		tags = this.utilityService.getSplittedString(messageToDisplay.getTags());
		box = this.boxService.searchBoxByMessageAndActor(messageId, actor.getId());

		result = new ModelAndView("message/display");
		result.addObject("messageToDisplay", messageToDisplay);
		result.addObject("boxId", box.getId());
		result.addObject("tags", tags);

		return result;
	}

	// Send

	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Message message;

		message = this.messageService.create();

		result = this.createEditModelAndView(message);

		return result;

	}

	@RequestMapping(value = "/send", method = RequestMethod.POST, params = "send")
	public ModelAndView send(@Valid final Message messageToSend, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(messageToSend);
		else
			try {
				this.messageService.save(messageToSend);
				result = new ModelAndView("redirect:/box/administrator,customer,handyWorker,referee,sponsor/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(messageToSend, "message.commit.error");
			}

		return result;

	}

	// Delete

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int messageId) {
		ModelAndView result;
		Message messageToDelete;

		messageToDelete = this.messageService.findOne(messageId);

		try {
			this.messageService.delete(messageToDelete);
			result = new ModelAndView("redirect:/box/administrator,customer,handyWorker,referee,sponsor/list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(messageToDelete, "message.commit.error");
		}

		return result;

	}

	// Move

	@RequestMapping(value = "/move", method = RequestMethod.GET)
	public ModelAndView move(@RequestParam final int messageId) {
		ModelAndView result;
		Message messageToMove;
		Collection<Box> targetBoxes;
		Box sourceBox;
		Actor actor;

		actor = this.actorService.findPrincipal();
		sourceBox = this.boxService.searchBoxByMessageAndActor(messageId, actor.getId());
		messageToMove = this.messageService.findOne(messageId);
		targetBoxes = this.boxService.findBoxesByActor(actor.getId());
		targetBoxes.remove(sourceBox);

		result = new ModelAndView("message/move");
		result.addObject("messageToMove", messageToMove);
		result.addObject("targetBoxes", targetBoxes);
		result.addObject("sourceBox", sourceBox);

		return result;

	}

	@RequestMapping(value = "/move", method = RequestMethod.POST, params = "move")
	public ModelAndView moveMessage(final HttpServletRequest request, final RedirectAttributes redir) {
		ModelAndView result;
		Message messageToMove;
		Box sourceBox, targetBox;
		String messageToMoveId, sourceBoxId, targetBoxId;

		messageToMoveId = request.getParameter("messageToMoveId");
		sourceBoxId = request.getParameter("sourceBoxId");
		targetBoxId = request.getParameter("targetBoxId");

		messageToMove = this.stringToMessageConverter.convert(messageToMoveId);
		sourceBox = this.stringToBoxConverter.convert(sourceBoxId);
		targetBox = this.stringToBoxConverter.convert(targetBoxId);

		try {
			this.messageService.moveMessageFromBoxToBox(sourceBox, targetBox, messageToMove);
			result = new ModelAndView("redirect:/box/administrator,customer,handyWorker,referee,sponsor/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("message/move");
			redir.addFlashAttribute("messageCode", "message.commit.error");
		}

		return result;

	}

	// Arcillary methods --------------------------

	protected ModelAndView createEditModelAndView(final Message messageToSend) {
		ModelAndView result;

		result = this.createEditModelAndView(messageToSend, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Message message, final String messageCode) {
		ModelAndView result;
		Collection<Actor> actors;
		Actor principal;

		principal = this.actorService.findPrincipal();
		actors = this.actorService.findAll();
		actors.remove(principal);

		result = new ModelAndView("message/send");
		result.addObject("message", message);
		result.addObject("actors", actors);
		result.addObject("messageCode", messageCode);

		return result;

	}

}
