
package controllers.handyWorker;

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

import services.FixUpTaskService;
import services.PhaseService;
import controllers.AbstractController;
import domain.FixUpTask;
import domain.Phase;

@Controller
@RequestMapping("phase/handyWorker")
public class PhaseHandyWorkerController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private PhaseService		phaseService;

	@Autowired
	private FixUpTaskService	fixUpTaskService;


	// Constructors -----------------------------------------------------------

	public PhaseHandyWorkerController() {
		super();
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int fixUpTaskId) {
		ModelAndView result;
		Phase phase;

		phase = this.phaseService.create();
		result = this.createEditModelAndView(phase, fixUpTaskId);

		return result;
	}

	// Edition ----------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int phaseId) {
		ModelAndView result;
		Phase phase;

		phase = this.phaseService.findOne(phaseId);
		result = this.createEditModelAndView(phase);

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int phaseId, final RedirectAttributes redir) {
		ModelAndView result;
		Phase phase;
		int fixUpTaskId;

		phase = this.phaseService.findOne(phaseId);
		fixUpTaskId = this.fixUpTaskService.findIdByPhaseId(phase.getId());

		try {
			this.phaseService.delete(phase);
		} catch (final Throwable oops) {
			redir.addFlashAttribute("messageCode", "phase.delete.error");
		}

		result = new ModelAndView("redirect:/phase/customer,handyWorker,referee/list.do?fixUpTaskId=" + fixUpTaskId);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Phase phase, final BindingResult binding, final HttpServletRequest request) {
		ModelAndView result;
		final FixUpTask fixUpTask;
		Phase saved;
		Integer fixUpTaskId;
		String paramFixUpTaskId;

		paramFixUpTaskId = request.getParameter("fixUpTaskId");
		fixUpTaskId = paramFixUpTaskId.isEmpty() ? null : Integer.parseInt(paramFixUpTaskId);

		if (binding.hasErrors())
			result = this.createEditModelAndView(phase, fixUpTaskId);
		else
			try {
				if (phase.getId() == 0) {
					fixUpTask = this.fixUpTaskService.findOne(fixUpTaskId);
					saved = this.phaseService.save(fixUpTask, phase);
				} else
					saved = this.phaseService.save(phase);

				result = new ModelAndView("redirect:/phase/customer,handyWorker,referee/display.do?phaseId=" + saved.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(phase, fixUpTaskId, "phase.save.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Phase phase, final BindingResult binding) {
		ModelAndView result;
		int fixUpTaskId;

		try {
			fixUpTaskId = this.fixUpTaskService.findIdByPhaseId(phase.getId());
			this.phaseService.delete(phase);
			result = new ModelAndView("redirect:/phase/customer,handyWorker,referee/list.do?fixUpTaskId=" + fixUpTaskId);
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(phase, "phase.delete.error");
		}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Phase phase) {
		ModelAndView result;

		result = this.createEditModelAndView(phase, null, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Phase phase, final Integer fixUpTaskId) {
		ModelAndView result;

		result = this.createEditModelAndView(phase, fixUpTaskId, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Phase phase, final String messageCode) {
		ModelAndView result;

		result = this.createEditModelAndView(phase, null, messageCode);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Phase phase, final Integer fixUpTaskId, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("phase/edit");
		result.addObject("phase", phase);
		result.addObject("fixUpTaskId", fixUpTaskId);

		result.addObject("messageCode", messageCode);

		return result;
	}
}
