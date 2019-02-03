
package controllers.handyWorker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.FixUpTaskService;
import utilities.internal.PaginatedListAdapter;
import controllers.AbstractController;
import domain.FixUpTask;

@Controller
@RequestMapping("fixUpTask/handyWorker")
public class FixUpTaskHandyWorkerController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private FixUpTaskService	fixUpTaskService;


	// Constructors -----------------------------------------------------------

	public FixUpTaskHandyWorkerController() {
		super();
	}

	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ModelAndView listAll(@RequestParam(defaultValue = "1", required = false) final int page, @RequestParam(required = false) final String sort, @RequestParam(required = false) final String dir) {
		ModelAndView result;
		Page<FixUpTask> fixUpTasks;
		Pageable pageable;
		PaginatedListAdapter fixUpTasksAdapted;

		pageable = this.newFixedPageable(page, dir, sort);
		fixUpTasks = this.fixUpTaskService.findAll(pageable);
		fixUpTasksAdapted = new PaginatedListAdapter(fixUpTasks, sort);

		result = new ModelAndView("fixUpTask/list");
		result.addObject("requestURI", "fixUpTask/handyWorker/listAll.do");
		result.addObject("fixUpTasks", fixUpTasksAdapted);

		return result;
	}

	@RequestMapping(value = "/listInvolved", method = RequestMethod.GET)
	public ModelAndView listInvolved(@RequestParam(defaultValue = "1", required = false) final int page, @RequestParam(required = false) final String sort, @RequestParam(required = false) final String dir) {
		ModelAndView result;
		Page<FixUpTask> fixUpTasks;
		Pageable pageable;
		PaginatedListAdapter fixUpTasksAdapted;

		pageable = this.newFixedPageable(page, dir, sort);
		fixUpTasks = this.fixUpTaskService.findWorkableByHandyWorkerPrincipal(pageable);
		fixUpTasksAdapted = new PaginatedListAdapter(fixUpTasks, sort);

		result = new ModelAndView("fixUpTask/list");
		result.addObject("requestURI", "fixUpTask/customer/listInvolved.do");
		result.addObject("fixUpTasks", fixUpTasksAdapted);

		return result;
	}
}
