
package controllers.administrator;

import javax.validation.Valid;

import org.displaytag.pagination.PaginatedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import services.WarrantyService;
import utilities.internal.PaginatedListAdapter;
import controllers.AbstractController;
import domain.Warranty;

@Controller
@RequestMapping("warranty/administrator")
public class WarrantyAdministratorController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private WarrantyService	warrantyService;


	// Constructors -----------------------------------------------------------

	public WarrantyAdministratorController() {
		super();
	}

	// Display ----------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int warrantyId) {
		ModelAndView result;
		Warranty warranty;

		warranty = this.warrantyService.findOne(warrantyId);

		result = new ModelAndView("warranty/display");
		result.addObject("warranty", warranty);

		return result;
	}

	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(defaultValue = "1", required = false) final int page, @RequestParam(required = false) final String sort, @RequestParam(required = false) final String dir) {
		ModelAndView result;
		Page<Warranty> warranties;
		Pageable pageable;
		PaginatedList warrantiesAdapted;

		pageable = this.newFixedPageable(page, dir, sort);
		warranties = this.warrantyService.findAll(pageable);
		warrantiesAdapted = new PaginatedListAdapter(warranties, sort);

		result = new ModelAndView("warranty/list");
		result.addObject("warranties", warrantiesAdapted);

		return result;
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Warranty warranty;

		warranty = this.warrantyService.create();
		result = this.createEditModelAndView(warranty);

		return result;
	}

	// Edition ----------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int warrantyId) {
		ModelAndView result;
		Warranty warranty;

		warranty = this.warrantyService.findOne(warrantyId);
		result = this.createEditModelAndView(warranty);

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int warrantyId, final RedirectAttributes redir) {
		ModelAndView result;
		Warranty warranty;

		warranty = this.warrantyService.findOne(warrantyId);

		try {
			this.warrantyService.delete(warranty);
		} catch (final Throwable oops) {
			redir.addFlashAttribute("messageCode", "warranty.delete.error");
		}

		result = new ModelAndView("redirect:list.do");

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Warranty warranty, final BindingResult binding) {
		ModelAndView result;
		Warranty saved;

		if (binding.hasErrors())
			result = this.createEditModelAndView(warranty);
		else
			try {
				saved = this.warrantyService.save(warranty);
				result = new ModelAndView("redirect:/warranty/administrator/display.do?warrantyId=" + saved.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(warranty, "warranty.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Warranty warranty, final BindingResult binding) {
		ModelAndView result;

		try {
			this.warrantyService.delete(warranty);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(warranty, "warranty.commit.error");
		}

		return result;
	}

	// Other business methods -------------------------------------------------

	@RequestMapping(value = "/makeFinal", method = RequestMethod.GET)
	public ModelAndView makeFinal(@RequestParam final int warrantyId, final RedirectAttributes redir) {
		ModelAndView result;
		Warranty warranty;

		warranty = this.warrantyService.findOne(warrantyId);

		try {
			this.warrantyService.makeFinal(warranty);
		} catch (final Throwable oops) {
			redir.addFlashAttribute("messageCode", "warranty.make.final.error");
		}

		result = new ModelAndView("redirect:/warranty/administrator/display.do?warrantyId=" + warrantyId);

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Warranty warranty) {
		ModelAndView result;

		result = this.createEditModelAndView(warranty, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Warranty warranty, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("warranty/edit");
		result.addObject("warranty", warranty);

		result.addObject("messageCode", messageCode);

		return result;
	}
}
