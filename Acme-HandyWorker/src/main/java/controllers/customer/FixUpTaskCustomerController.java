
package controllers.customer;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import services.FixUpTaskService;
import services.WarrantyService;
import utilities.internal.PaginatedListAdapter;
import controllers.AbstractController;
import domain.Category;
import domain.FixUpTask;
import domain.Warranty;

@Controller
@RequestMapping("fixUpTask/customer")
public class FixUpTaskCustomerController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private FixUpTaskService	fixUpTaskService;

	@Autowired
	private CategoryService		categoryService;

	@Autowired
	private WarrantyService		warrantyService;


	// Constructors -----------------------------------------------------------

	public FixUpTaskCustomerController() {
		super();
	}

	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final Integer customerId, @RequestParam(defaultValue = "1", required = false) final int page, @RequestParam(required = false) final String sort, @RequestParam(required = false) final String dir) {
		ModelAndView result;
		Page<FixUpTask> fixUpTasks;
		Pageable pageable;
		PaginatedListAdapter fixUpTasksAdapted;

		pageable = this.newFixedPageable(page, dir, sort);
		fixUpTasks = customerId == null ? this.fixUpTaskService.findByCustomerPrincipal(pageable) : this.fixUpTaskService.findByCustomerId(customerId, pageable);
		fixUpTasksAdapted = new PaginatedListAdapter(fixUpTasks, sort);

		result = new ModelAndView("fixUpTask/list");
		result.addObject("requestURI", "fixUpTask/customer/list.do");
		result.addObject("fixUpTasks", fixUpTasksAdapted);

		return result;
	}

	// Create -----------------------------------------------------------------		

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(final Locale locale) {
		ModelAndView result;
		FixUpTask fixUpTask;

		fixUpTask = this.fixUpTaskService.create();

		result = this.createEditModelAndView(fixUpTask, locale);

		return result;
	}

	// Edition ----------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int fixUpTaskId, final Locale locale) {
		ModelAndView result;
		FixUpTask fixUpTask;

		fixUpTask = this.fixUpTaskService.findOne(fixUpTaskId);

		result = this.createEditModelAndView(fixUpTask, locale);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final FixUpTask fixUpTask, final BindingResult binding, final Locale locale) {
		ModelAndView result;
		FixUpTask saved;

		if (binding.hasErrors())
			result = this.createEditModelAndView(fixUpTask, locale);
		else
			try {
				saved = this.fixUpTaskService.save(fixUpTask);
				result = new ModelAndView("redirect:/fixUpTask/customer,handyWorker,referee/display.do?fixUpTaskId=" + saved.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(fixUpTask, locale, "fixUpTask.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final FixUpTask fixUpTask, final BindingResult binding, final Locale locale) {
		ModelAndView result;

		try {
			this.fixUpTaskService.delete(fixUpTask);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(fixUpTask, locale, "fixUpTask.commit.error");
		}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final FixUpTask fixUpTask, final Locale locale) {
		ModelAndView result;

		result = this.createEditModelAndView(fixUpTask, locale, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final FixUpTask fixUpTask, final Locale locale, final String messageCode) {
		ModelAndView result;
		Collection<Warranty> warranties;
		SortedMap<Integer, List<String>> categoryMap;
		Collection<Category> categories;

		warranties = this.warrantyService.findFinalWarranties();
		categories = this.categoryService.findAllExceptDefault();
		categoryMap = this.categoryService.categoriesByLanguage(categories, locale.getLanguage());

		result = new ModelAndView("fixUpTask/edit");
		result.addObject("fixUpTask", fixUpTask);
		result.addObject("warranties", warranties);
		result.addObject("categories", categoryMap);

		result.addObject("messageCode", messageCode);

		return result;
	}
}
