/*
 * AbstractController.java
 * 
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import services.CustomisationService;
import domain.Customisation;

@Controller
public class AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private CustomisationService	customisationService;


	// Panic handler ----------------------------------------------------------

	@ExceptionHandler(Throwable.class)
	public ModelAndView panic(final Throwable oops) {
		ModelAndView result;

		result = new ModelAndView("misc/panic");
		result.addObject("name", ClassUtils.getShortName(oops.getClass()));
		result.addObject("exception", oops.getMessage());
		result.addObject("stackTrace", ExceptionUtils.getStackTrace(oops));

		return result;
	}

	// Protected ancillary methods --------------------------------------------

	protected void setBannerHeader(final ModelAndView modelAndView) {
		Customisation customisation;
		String banner;

		customisation = this.customisationService.find();
		banner = customisation.getBanner();

		modelAndView.addObject("banner", banner);
	}

	protected Pageable newFixedPageable(final int page, final String dir, final String sort) {
		Pageable result;

		if (sort != null && !sort.isEmpty())
			result = new PageRequest(page - 1, 5, Sort.Direction.fromString(dir), sort);
		else
			result = new PageRequest(page - 1, 5);

		return result;
	}

}
