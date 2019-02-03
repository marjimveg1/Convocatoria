/*
 * 
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.handyWorkercustomer;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ApplicationService;
import services.CustomisationService;
import controllers.AbstractController;
import domain.Application;
import domain.Customisation;

@Controller
@RequestMapping("/application/handyWorker,customer")
public class ApplicationHandyWorkerCustomerController extends AbstractController {

	@Autowired
	private ApplicationService		applicationService;

	@Autowired
	private CustomisationService	customisationService;


	// Constructors -----------------------------------------------------------
	public ApplicationHandyWorkerCustomerController() {

	}

	// Application save -----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Application application, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(application);
		else
			try {
				this.applicationService.save(application);
				if (LoginService.getPrincipal().getAuthorities().toString().equals("[HANDYWORKER]"))
					result = new ModelAndView("redirect:../../application/handyWorker/list.do");
				else
					//if actor is customer
					result = new ModelAndView("redirect:../../application/customer,handyWorker,referee/list.do?fixUpTaskId=" + application.getFixUpTask().getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(application, "application.commit.error");
			}

		return result;
	}

	// Arcillary methods-----------------------------------------
	protected ModelAndView createEditModelAndView(final Application application) {
		ModelAndView result;

		result = this.createEditModelAndView(application, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Application application, final String messageCode) {
		ModelAndView result;
		Collection<String> creditCardMakes;
		Customisation customisation;
		double vat;

		customisation = this.customisationService.find();
		creditCardMakes = customisation.getCreditCardMakes();
		vat = this.customisationService.find().getVAT();

		result = new ModelAndView("application/edit");
		result.addObject("application", application);
		result.addObject("brandName", creditCardMakes);
		result.addObject("vat", vat);
		result.addObject("messageCode", messageCode);

		return result;
	}

}
