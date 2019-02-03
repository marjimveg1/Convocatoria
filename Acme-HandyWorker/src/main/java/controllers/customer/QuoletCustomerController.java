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
import services.QuoletService;
import controllers.AbstractController;
import domain.Customer;
import domain.FixUpTask;
import domain.Quolet;

@Controller
@RequestMapping("/quolet/customer")
public class QuoletCustomerController extends AbstractController {

	@Autowired
	private QuoletService		quoletService;

	@Autowired
	private CustomerService		customerService;

	@Autowired
	private FixUpTaskService	fixUpTaskService;


	// Constructors -----------------------------------------------------------
	public QuoletCustomerController() {

	}

	//Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Customer principal;
		Collection<Quolet> quolets;

		principal = this.customerService.findByPrincipal();

		quolets = this.quoletService.findQuoletsByCustomerid(principal.getId());

		result = new ModelAndView("quolet/list");
		result.addObject("quolets", quolets);

		return result;
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int fixUpTaskId) {
		ModelAndView result;
		Quolet quolet;

		quolet = this.quoletService.create();
		result = this.createEditModelAndView(quolet);
		result.addObject("fixUpTaskId", fixUpTaskId);

		return result;
	}

	// Edition ----------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int quoletId) {
		ModelAndView result;
		Quolet quolet;
		FixUpTask fixUpTask;

		quolet = this.quoletService.findOne(quoletId);
		fixUpTask = this.quoletService.findFixUpTaskByQuoletid(quoletId);
		result = this.createEditModelAndView(quolet);
		result.addObject("fixUpTaskId", fixUpTask.getId());

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Quolet quolet, final BindingResult binding, final HttpServletRequest request) {
		ModelAndView result;
		Quolet saved;
		final FixUpTask fixUpTask;
		Integer fixUpTaskId;
		String paramFixUpTaskId;

		paramFixUpTaskId = request.getParameter("fixUpTaskId");
		fixUpTaskId = paramFixUpTaskId == null ? null : Integer.parseInt(paramFixUpTaskId);

		if (binding.hasErrors())
			result = this.createEditModelAndView(quolet, fixUpTaskId);
		else
			try {
				if (quolet.getId() == 0) {

					fixUpTask = this.fixUpTaskService.findOne(fixUpTaskId);
					saved = this.quoletService.save(quolet, fixUpTask);
				} else
					saved = this.quoletService.save(quolet);
				result = new ModelAndView("redirect:/quolet/handyWorker,customer/display.do?quoletId=" + saved.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(quolet, fixUpTaskId, "quolet.commit.error");
			}
		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Quolet quolet, final BindingResult binding) {
		ModelAndView result;

		try {
			this.quoletService.delete(quolet);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(quolet, "quolet.delete.error");
		}

		return result;
	}

	// Other business methods -------------------------------------------------

	@RequestMapping(value = "/makeFinal", method = RequestMethod.GET)
	public ModelAndView makeFinal(@RequestParam final int quoletId, final RedirectAttributes redir) {
		ModelAndView result;
		Quolet quolet;

		quolet = this.quoletService.findOne(quoletId);

		try {
			this.quoletService.makeFinal(quolet);
		} catch (final Throwable oops) {
			redir.addFlashAttribute("messageCode", "quolet.make.final.error");
		}

		result = new ModelAndView("redirect:/quolet/handyWorker,customer/display.do?quoletId=" + quoletId);

		return result;
	}

	// Ancillary methods ------------------------------------------------------
	protected ModelAndView createEditModelAndView(final Quolet quolet) {
		ModelAndView result;

		result = this.createEditModelAndView(quolet, null, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Quolet quolet, final Integer fixUpTaskId) {
		ModelAndView result;

		result = this.createEditModelAndView(quolet, fixUpTaskId, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Quolet quolet, final String messageCode) {
		ModelAndView result;

		result = this.createEditModelAndView(quolet, null, messageCode);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Quolet quolet, final Integer fixUpTaskId, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("quolet/edit");
		result.addObject("quolet", quolet);
		result.addObject("fixUpTaskId", fixUpTaskId);

		result.addObject("messageCode", messageCode);

		return result;
	}
}
