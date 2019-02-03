
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ApplicationService;
import services.CustomerService;
import services.FixUpTaskService;
import services.HandyWorkerService;
import services.QuoletService;
import controllers.AbstractController;
import domain.Customer;
import domain.HandyWorker;

@Controller
@RequestMapping("dashboard/administrator")
public class DashboardAdministratorController extends AbstractController {

	// Services ------------------
	@Autowired
	private FixUpTaskService	fixUpTaskService;

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private CustomerService		customerService;

	@Autowired
	private HandyWorkerService	handyWorkerService;

	@Autowired
	private QuoletService		quoletService;


	// Constructors --------------
	public DashboardAdministratorController() {
		super();
	}

	// methods --------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;
		//TODO: dejar una
		//Query A control check
		Double[] findDataQuoletPerFixUpTask;
		//Query B control check
		Double findRatioPublishedQuoletvsAllQuolet, findRatioPublishedQuoletvsAllFixUpTask;
		//Query C control check
		Double findRatioUnpublishedQuoletvsAllQuoles, findRatioUnpublishedQuoletvsAllFixUpTask;

		//Deliverables
		Double[] dataApplicationsPerTask, dataOfApplicationPrice;
		Double[] dataMaximumPrice, dataFixUpTaskPerUser;
		Double ratPendingApp, ratAcceptedApp, ratRejectedApp, ratPendingPeriodApp;
		Collection<Customer> customers;
		Collection<HandyWorker> handyWorkers;

		//control check-----------------------------------------
		//TODO CONTROL CHECK
		//a
		findDataQuoletPerFixUpTask = this.quoletService.findDataQuoletPerFixUpTask();
		//B
		findRatioPublishedQuoletvsAllQuolet = this.quoletService.findRatioPublishedQuoletvsAllQuolet();
		findRatioPublishedQuoletvsAllFixUpTask = this.quoletService.findRatioPublishedQuoletvsAllFixUpTask();
		//C
		findRatioUnpublishedQuoletvsAllQuoles = this.quoletService.findRatioUnpublishedQuoletvsAllQuoles();
		findRatioUnpublishedQuoletvsAllFixUpTask = this.quoletService.findRatioUnpublishedQuoletvsAllFixUpTask();

		// LEVEL C -----------------------------------------

		dataFixUpTaskPerUser = this.fixUpTaskService.findDataNumberFixUpTaskPerUser();
		dataApplicationsPerTask = this.applicationService.findDataOfApplicationPerFixUpTask();
		dataMaximumPrice = this.fixUpTaskService.findDataMaximumPrice();
		dataOfApplicationPrice = this.applicationService.findDataOfApplicationPrice();
		ratPendingApp = this.applicationService.findRatioPendingApplications();
		ratAcceptedApp = this.applicationService.findRatioAcceptedApplications();
		ratRejectedApp = this.applicationService.findRatioRejectedApplications();
		ratPendingPeriodApp = this.applicationService.findRatioPendingApplicationsNotChangeStatus();

		customers = this.customerService.customerMoreThanAverage();
		handyWorkers = this.handyWorkerService.atLeast10Application();

		result = new ModelAndView("dashboard/display");

		//control check-----------------------------------------
		//TODO CONTROL CHECK
		//a
		result.addObject("findDataQuoletPerFixUpTask", findDataQuoletPerFixUpTask);
		//B
		result.addObject("findRatioPublishedQuoletvsAllQuolet", findRatioPublishedQuoletvsAllQuolet);
		result.addObject("findRatioPublishedQuoletvsAllFixUpTask", findRatioPublishedQuoletvsAllFixUpTask);
		//c
		result.addObject("findRatioUnpublishedQuoletvsAllQuoles", findRatioUnpublishedQuoletvsAllQuoles);
		result.addObject("findRatioUnpublishedQuoletvsAllFixUpTask", findRatioUnpublishedQuoletvsAllFixUpTask);

		// LEVEL C
		result.addObject("dataFixUpTaskPerUser", dataFixUpTaskPerUser);
		result.addObject("dataApplicationPerTask", dataApplicationsPerTask);
		result.addObject("dataMaximumPrice", dataMaximumPrice);
		result.addObject("dataOfApplicationPrice", dataOfApplicationPrice);
		result.addObject("ratPendingApp", ratPendingApp);
		result.addObject("ratAcceptedApp", ratAcceptedApp);
		result.addObject("ratRejectedApp", ratRejectedApp);
		result.addObject("ratPendingPeriodApp", ratPendingPeriodApp);

		result.addObject("customers", customers);
		result.addObject("handyWorkers", handyWorkers);

		return result;
	}

}
