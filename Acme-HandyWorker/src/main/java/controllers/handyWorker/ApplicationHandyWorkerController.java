/*
 * 
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.handyWorker;

import org.displaytag.pagination.PaginatedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ApplicationService;
import services.CustomisationService;
import services.FixUpTaskService;
import services.HandyWorkerService;
import utilities.internal.PaginatedListAdapter;
import controllers.AbstractController;
import domain.Application;
import domain.FixUpTask;

@Controller
@RequestMapping("/application/handyWorker")
public class ApplicationHandyWorkerController extends AbstractController {

	@Autowired
	private ApplicationService		applicationService;

	@Autowired
	private FixUpTaskService		fixUpTaskService;

	@Autowired
	private HandyWorkerService		handyWorkerService;

	@Autowired
	private CustomisationService	customisationService;


	// Constructors -----------------------------------------------------------
	public ApplicationHandyWorkerController() {

	}

	// Application List -----------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(defaultValue = "1", required = false) final int page, @RequestParam(required = false) final String sort, @RequestParam(required = false) final String dir) {
		ModelAndView result;
		Page<Application> applications;
		final Pageable pageable;
		final PaginatedList applicationsAdapted;
		Integer principalId;

		pageable = this.newFixedPageable(page, dir, sort);
		applications = this.applicationService.findApplicationByHandyWorker(pageable);
		applicationsAdapted = new PaginatedListAdapter(applications, sort);
		principalId = null;

		try {
			principalId = this.handyWorkerService.findByPrincipal().getId();
		} catch (final Exception e) {

		}

		result = new ModelAndView("application/list");
		result.addObject("applications", applicationsAdapted);
		result.addObject("principalId", principalId);
		result.addObject("requestURI", "application/handyWorker/list.do");

		return result;
	}
	// Application create -----------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int fixUpTaskId) {
		ModelAndView result;
		Application application;

		try {
			final FixUpTask fixUpTask;

			fixUpTask = this.fixUpTaskService.findOne(fixUpTaskId);
			application = this.applicationService.create(fixUpTask);

			result = this.createEditModelAndView(application);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/application/handyWorker/list.do");

		}

		return result;
	}
	// Application Edit -----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int applicationId) {
		ModelAndView result;
		Application application;
		double vat;

		vat = this.customisationService.find().getVAT();
		application = this.applicationService.findOne(applicationId);

		result = this.createEditModelAndView(application);
		result.addObject("vat", vat);

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

		result = new ModelAndView("application/edit");
		result.addObject("application", application);
		result.addObject("messageCode", messageCode);

		return result;
	}

}
