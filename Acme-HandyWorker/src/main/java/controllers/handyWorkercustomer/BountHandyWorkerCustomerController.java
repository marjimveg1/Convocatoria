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

import services.BountService;
import controllers.AbstractController;
import domain.Bount;

@Controller
@RequestMapping("/bount/handyWorker,customer")
public class BountHandyWorkerCustomerController extends AbstractController {

	@Autowired
	private BountService	bountService;


	// Constructors -----------------------------------------------------------
	public BountHandyWorkerCustomerController() {

	}

	// Display ----------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int bountId) {
		ModelAndView result;
		Bount bount;

		bount = this.bountService.findOne(bountId);

		result = new ModelAndView("bount/display");
		result.addObject("bount", bount);

		return result;
	}

}
