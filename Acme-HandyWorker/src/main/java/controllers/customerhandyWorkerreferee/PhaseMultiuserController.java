
package controllers.customerhandyWorkerreferee;

import org.displaytag.pagination.PaginatedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.FixUpTaskService;
import services.PhaseService;
import services.UtilityService;
import utilities.internal.PaginatedListAdapter;
import controllers.AbstractController;
import domain.FixUpTask;
import domain.Phase;

@Controller
@RequestMapping("phase/customer,handyWorker,referee")
public class PhaseMultiuserController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private PhaseService		phaseService;

	@Autowired
	private FixUpTaskService	fixUpTaskService;

	@Autowired
	private UtilityService		utilityService;


	// Constructors -----------------------------------------------------------

	public PhaseMultiuserController() {
		super();
	}

	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final Integer fixUpTaskId, @RequestParam(defaultValue = "1", required = false) final int page, @RequestParam(required = false) final String sort, @RequestParam(required = false) final String dir) {
		ModelAndView result;
		Page<Phase> phases;
		FixUpTask fixUpTask;
		Pageable pageable;
		PaginatedList phasesAdapted;
		boolean phaseEditionPerm;

		pageable = this.newFixedPageable(page, dir, sort);
		phases = this.phaseService.findByFixUpTaskId(fixUpTaskId, pageable);
		phasesAdapted = new PaginatedListAdapter(phases, sort);
		fixUpTask = this.fixUpTaskService.findOne(fixUpTaskId);
		phaseEditionPerm = this.editionPerm(fixUpTask);

		result = new ModelAndView("phase/list");
		result.addObject("phases", phasesAdapted);
		result.addObject("fixUpTaskId", fixUpTaskId);
		result.addObject("phaseEditionPerm", phaseEditionPerm);

		return result;
	}

	// Display ----------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int phaseId) {
		ModelAndView result;
		Phase phase;
		FixUpTask fixUpTask;
		boolean phaseEditionPerm;

		phase = this.phaseService.findOne(phaseId);
		fixUpTask = this.fixUpTaskService.findByPhaseId(phaseId);
		phaseEditionPerm = this.editionPerm(fixUpTask);

		result = new ModelAndView("phase/display");
		result.addObject("phase", phase);
		result.addObject("phaseEditionPerm", phaseEditionPerm);

		return result;
	}

	// Other business methods -------------------------------------------------

	@RequestMapping(value = "/back", method = RequestMethod.GET)
	public ModelAndView back(@RequestParam final int phaseId) {
		ModelAndView result;
		Integer fixUpTaskId;

		fixUpTaskId = this.fixUpTaskService.findIdByPhaseId(phaseId);
		result = new ModelAndView("redirect:/phase/customer,handyWorker,referee/list.do?fixUpTaskId=" + fixUpTaskId);

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	private boolean editionPerm(final FixUpTask fixUpTask) {
		boolean result;

		result = this.phaseService.checkHandyWorkerAccess(fixUpTask) && fixUpTask.getEndDate().after(this.utilityService.current_moment());

		return result;
	}

}
