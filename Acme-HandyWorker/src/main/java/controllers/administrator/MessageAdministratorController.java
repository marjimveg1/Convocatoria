
package controllers.administrator;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.MessageService;
import controllers.AbstractController;
import domain.Actor;
import domain.Message;

@Controller
@RequestMapping(value = "/message/administrator")
public class MessageAdministratorController extends AbstractController {

	@Autowired
	private MessageService	messageService;

	@Autowired
	private ActorService	actorService;


	// Constructors -----------------------------------------------------------
	public MessageAdministratorController() {
		super();
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/broadcast", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Message messageToBroadcast;
		Collection<Actor> recipients;
		Actor sender;

		sender = this.actorService.findPrincipal();
		recipients = this.actorService.findAll();
		recipients.remove(sender);
		messageToBroadcast = this.messageService.create();
		messageToBroadcast.setRecipients(recipients);

		result = this.broadcastModelAndView(messageToBroadcast);

		return result;
	}

	// Edition ----------------------------------------------------------------

	@RequestMapping(value = "/broadcast", method = RequestMethod.POST, params = "send")
	public ModelAndView broadcast(@Valid final Message messageToBroadcast, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.broadcastModelAndView(messageToBroadcast);
		else
			try {
				this.messageService.broadcastMessage(messageToBroadcast);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.broadcastModelAndView(messageToBroadcast, "message.commit.error");
			}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView broadcastModelAndView(final Message messageToBroadcast) {
		ModelAndView result;

		result = this.broadcastModelAndView(messageToBroadcast, null);

		return result;
	}

	protected ModelAndView broadcastModelAndView(final Message message, final String messageCode) {
		ModelAndView result;
		Collection<Actor> recipients;

		recipients = this.actorService.findAll();

		result = new ModelAndView("message/broadcast");
		result.addObject("message", message);
		result.addObject("recipients", recipients);

		result.addObject("messageCode", messageCode);

		return result;
	}

}
