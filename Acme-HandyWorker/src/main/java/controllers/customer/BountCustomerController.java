/*
 * 
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.customer;

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

import services.CustomerService;
import services.FixUpTaskService;
import services.BountService;
import controllers.AbstractController;
import domain.Customer;
import domain.FixUpTask;
import domain.Bount;

@Controller
@RequestMapping("/bount/customer")
public class BountCustomerController extends AbstractController {

	@Autowired
	private BountService		bountService;

	@Autowired
	private CustomerService		customerService;

	@Autowired
	private FixUpTaskService	fixUpTaskService;


	// Constructors -----------------------------------------------------------
	public BountCustomerController() {

	}

	//Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Customer principal;
		Collection<Bount> bounts;

		principal = this.customerService.findByPrincipal();

		bounts = this.bountService.findBountsByCustomerid(principal.getId());

		result = new ModelAndView("bount/list");
		result.addObject("bounts", bounts);

		return result;
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int fixUpTaskId) {
		ModelAndView result;
		Bount bount;

		bount = this.bountService.create();
		result = this.createEditModelAndView(bount);
		result.addObject("fixUpTaskId", fixUpTaskId);

		return result;
	}

	// Edition ----------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int bountId) {
		ModelAndView result;
		Bount bount;
		FixUpTask fixUpTask;

		bount = this.bountService.findOne(bountId);
		fixUpTask = this.bountService.findFixUpTaskByBountid(bountId);
		result = this.createEditModelAndView(bount);
		result.addObject("fixUpTaskId", fixUpTask.getId());

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Bount bount, final BindingResult binding, final HttpServletRequest request) {
		ModelAndView result;
		Bount saved;
		final FixUpTask fixUpTask;
		Integer fixUpTaskId;
		String paramFixUpTaskId;

		paramFixUpTaskId = request.getParameter("fixUpTaskId");
		fixUpTaskId = paramFixUpTaskId == null ? null : Integer.parseInt(paramFixUpTaskId);

		if (binding.hasErrors())
			result = this.createEditModelAndView(bount, fixUpTaskId);
		else
			try {
				if (bount.getId() == 0) {

					fixUpTask = this.fixUpTaskService.findOne(fixUpTaskId);
					saved = this.bountService.save(bount, fixUpTask);
				} else
					saved = this.bountService.save(bount);
				result = new ModelAndView("redirect:/bount/handyWorker,customer/display.do?bountId=" + saved.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(bount, fixUpTaskId, "bount.commit.error");
			}
		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Bount bount, final BindingResult binding) {
		ModelAndView result;

		try {
			this.bountService.delete(bount);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(bount, "bount.delete.error");
		}

		return result;
	}

	// Other business methods -------------------------------------------------

	@RequestMapping(value = "/makeFinal", method = RequestMethod.GET)
	public ModelAndView makeFinal(@RequestParam final int bountId, final RedirectAttributes redir) {
		ModelAndView result;
		Bount bount;

		bount = this.bountService.findOne(bountId);

		try {
			this.bountService.makeFinal(bount);
		} catch (final Throwable oops) {
			redir.addFlashAttribute("messageCode", "bount.make.final.error");
		}

		result = new ModelAndView("redirect:/bount/handyWorker,customer/display.do?bountId=" + bountId);

		return result;
	}

	// Ancillary methods ------------------------------------------------------
	protected ModelAndView createEditModelAndView(final Bount bount) {
		ModelAndView result;

		result = this.createEditModelAndView(bount, null, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Bount bount, final Integer fixUpTaskId) {
		ModelAndView result;

		result = this.createEditModelAndView(bount, fixUpTaskId, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Bount bount, final String messageCode) {
		ModelAndView result;

		result = this.createEditModelAndView(bount, null, messageCode);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Bount bount, final Integer fixUpTaskId, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("bount/edit");
		result.addObject("bount", bount);
		result.addObject("fixUpTaskId", fixUpTaskId);

		result.addObject("messageCode", messageCode);

		return result;
	}
}
