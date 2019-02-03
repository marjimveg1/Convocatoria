/*
 * CustomerController.java
 * 
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.customerhandyWorkerreferee;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CategoryTranslationService;
import services.CustomisationService;
import services.FixUpTaskService;
import services.QuoletService;
import controllers.AbstractController;
import domain.Actor;
import domain.FixUpTask;
import domain.Quolet;

@Controller
@RequestMapping("fixUpTask/customer,handyWorker,referee")
public class FixUpTaskMultiuserController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private FixUpTaskService			fixUpTaskService;

	@Autowired
	private CustomisationService		customisationService;

	@Autowired
	private CategoryTranslationService	categoryTranslationService;

	@Autowired
	private ActorService				actorService;

	@Autowired
	private QuoletService				quoletService;


	// Constructors -----------------------------------------------------------

	public FixUpTaskMultiuserController() {
		super();
	}

	// Display ----------------------------------------------------------------		

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int fixUpTaskId, final Locale locale) {
		ModelAndView result;
		FixUpTask fixUpTask;
		String category;
		double vat;
		boolean isWorkable;
		Actor principal;
		Collection<Quolet> quolets;

		fixUpTask = this.fixUpTaskService.findOne(fixUpTaskId);
		category = this.categoryTranslationService.findByLanguageCategory(fixUpTask.getCategory().getId(), locale.getLanguage()).getName();
		vat = this.customisationService.find().getVAT();
		isWorkable = this.fixUpTaskService.hasAcceptedApplication(fixUpTaskId);
		principal = this.actorService.findPrincipal();

		result = new ModelAndView("fixUpTask/display");
		result.addObject(fixUpTask);
		result.addObject("category", category);
		result.addObject("isWorkable", isWorkable);
		result.addObject("vat", vat);
		result.addObject("principal", principal);

		if (principal != fixUpTask.getCustomer()) {
			quolets = this.quoletService.findQuoletsPublishedByTask(fixUpTaskId);
			result.addObject("quolets", quolets);
		} else
			result.addObject("quolets", fixUpTask.getQuolets());

		return result;
	}
}
