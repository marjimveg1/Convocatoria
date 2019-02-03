/*
 * 
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.handyWorkercustomer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.QuoletService;
import controllers.AbstractController;
import domain.Quolet;

@Controller
@RequestMapping("/quolet/handyWorker,customer")
public class QuoletHandyWorkerCustomerController extends AbstractController {

	@Autowired
	private QuoletService	quoletService;


	// Constructors -----------------------------------------------------------
	public QuoletHandyWorkerCustomerController() {

	}

	// Display ----------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int quoletId) {
		ModelAndView result;
		Quolet quolet;

		quolet = this.quoletService.findOne(quoletId);

		result = new ModelAndView("quolet/display");
		result.addObject("quolet", quolet);

		return result;
	}

}
