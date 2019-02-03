
package controllers.customerhandyWorkerreferee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.WarrantyService;
import controllers.AbstractController;
import domain.Warranty;

@Controller
@RequestMapping("warranty/customer,handyWorker,referee")
public class WarrantyMultiuserController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private WarrantyService	warrantyService;


	// Constructors -----------------------------------------------------------

	public WarrantyMultiuserController() {
		super();
	}

	// Display ----------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int warrantyId, @RequestParam final int fixUpTaskId) {
		ModelAndView result;
		Warranty warranty;

		warranty = this.warrantyService.findOne(warrantyId);

		if (warranty.getFinalMode()) {
			result = new ModelAndView("warranty/display");
			result.addObject("warranty", warranty);
			result.addObject("fixUpTaskId", fixUpTaskId);
		} else
			result = new ModelAndView("redirect:/welcome/index.do");

		return result;
	}

}
