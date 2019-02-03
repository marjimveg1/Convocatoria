
package controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Customer;
import domain.HandyWorker;

@Controller
@RequestMapping(value = "/actor")
public class ActorController extends ActorAbstractController {

	// Constructor

	public ActorController() {
		super();
	}

	// Creation

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final String role) {
		Assert.isTrue(role.equals("customer") || role.equals("handyWorker"));
		final ModelAndView result;

		result = this.createActor(role);
		result.addObject("Url", "actor/");

		return result;
	}

	// Register

	@RequestMapping(value = "/registercustomer", method = RequestMethod.POST, params = "save")
	public ModelAndView registerCustomer(@Valid final Customer customer, final BindingResult binding, final HttpServletRequest request) {

		ModelAndView result;

		result = this.registerActor(customer, binding, request);
		result.addObject("Url", "actor/");

		return result;
	}

	@RequestMapping(value = "/registerhandyWorker", method = RequestMethod.POST, params = "save")
	public ModelAndView registerHandyWorker(@Valid final HandyWorker handyworker, final BindingResult binding, final HttpServletRequest request) {

		ModelAndView result;

		result = this.registerActor(handyworker, binding, request);
		result.addObject("Url", "actor/");

		return result;
	}

}
