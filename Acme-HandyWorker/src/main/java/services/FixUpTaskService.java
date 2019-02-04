
package services;

import java.util.Collection;
import java.util.Collections;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.FixUpTaskRepository;
import domain.Application;
import domain.Category;
import domain.Customer;
import domain.FixUpTask;
import domain.HandyWorker;
import domain.Phase;
import domain.Bount;

@Service
@Transactional
public class FixUpTaskService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private FixUpTaskRepository	fixUpTaskRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private UtilityService		utilityService;

	@Autowired
	private CustomerService		customerService;

	@Autowired
	private HandyWorkerService	handyWorkerService;

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private BountService		bountService;


	// Constructor ------------------------------------------------------------

	public FixUpTaskService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public FixUpTask create() {
		FixUpTask result;

		result = new FixUpTask();
		result.setTicker(this.utilityService.generateValidTicker());
		result.setApplications(Collections.<Application> emptySet());
		result.setPhases(Collections.<Phase> emptySet());
		result.setCustomer(this.customerService.findByPrincipal());
		result.setPublicationMoment(this.utilityService.current_moment());
		//TODO: CONTROL CHECK
		result.setBounts(Collections.<Bount> emptySet());

		return result;
	}

	public FixUpTask save(final FixUpTask fixUpTask) {
		Assert.notNull(fixUpTask);
		Assert.isTrue(fixUpTask.getWarranty().getFinalMode());
		Assert.isTrue(fixUpTask.getApplications().isEmpty()); // You cannot update a FixUpTaks with an Application associated
		Assert.isTrue(this.utilityService.current_moment().before(fixUpTask.getStartDate()));
		this.utilityService.checkDate(fixUpTask.getStartDate(), fixUpTask.getEndDate());

		FixUpTask result;
		Customer principal;

		principal = this.customerService.findByPrincipal();
		this.checkByPrincipal(fixUpTask, principal);
		this.utilityService.checkIsSpamMarkAsSuspicious(fixUpTask.getAddress() + fixUpTask.getDescription(), principal);
		this.utilityService.checkActorIsBanned(principal);

		result = this.fixUpTaskRepository.save(fixUpTask);

		if (!this.fixUpTaskRepository.exists(fixUpTask.getId()))
			this.customerService.addFixUpTask(result.getCustomer(), result);

		return result;
	}

	public void delete(final FixUpTask fixUpTask) {
		Assert.notNull(fixUpTask);
		Assert.isTrue(this.fixUpTaskRepository.exists(fixUpTask.getId()));
		Assert.isTrue(fixUpTask.getApplications().isEmpty()); // You cannot delete a FixUpTaks with an Application associated

		Customer principal;

		principal = this.customerService.findByPrincipal();
		this.checkByPrincipal(fixUpTask, principal);
		this.utilityService.checkActorIsBanned(principal);

		this.customerService.removeFixUpTask(fixUpTask.getCustomer(), fixUpTask);

		//TODO control check
		this.bountService.delete(fixUpTask);
		this.fixUpTaskRepository.delete(fixUpTask);
	}

	public FixUpTask findOne(final int fixUpTaskId) {
		FixUpTask result;

		result = this.fixUpTaskRepository.findOne(fixUpTaskId);
		Assert.notNull(result);

		return result;
	}

	public Collection<FixUpTask> findAll() {
		Collection<FixUpTask> result;

		result = this.fixUpTaskRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Page<FixUpTask> findAll(final Pageable pageable) {
		Page<FixUpTask> result;

		result = this.fixUpTaskRepository.findAll(pageable);
		Assert.notNull(result);

		return result;
	}

	// Other business methods -------------------------------------------------

	public boolean hasAcceptedApplication(final int fixUpTaskId) {
		boolean result;
		Application application;

		application = this.applicationService.findAcceptedApplication(fixUpTaskId);
		result = application != null;

		return result;
	}

	public Page<FixUpTask> findByCustomerPrincipal(final Pageable pageable) {
		Page<FixUpTask> result;
		Customer principal;

		principal = this.customerService.findByPrincipal();
		result = this.findByCustomerId(principal.getId(), pageable);

		return result;
	}

	public Page<FixUpTask> findByCustomerId(final int customerId, final Pageable pageable) {
		Page<FixUpTask> result;

		result = this.fixUpTaskRepository.findByCustomerId(customerId, pageable);
		Assert.notNull(result);

		return result;
	}

	public Page<FixUpTask> findWorkableByHandyWorkerPrincipal(final Pageable pageable) {
		Page<FixUpTask> result;
		HandyWorker principal;

		principal = this.handyWorkerService.findByPrincipal();
		result = this.fixUpTaskRepository.findWorkableByHandyWorkerPrincipal(principal.getId(), pageable);
		Assert.notNull(result);

		return result;
	}

	public FixUpTask findByPhaseId(final int phaseId) {
		FixUpTask result;

		result = this.fixUpTaskRepository.findByPhaseId(phaseId);

		return result;
	}

	public Integer findIdByPhaseId(final int phaseId) {
		Integer result;

		result = this.fixUpTaskRepository.findIdByPhaseId(phaseId);

		return result;
	}

	public Double[] findDataNumberFixUpTaskPerUser() {
		Double[] result;

		result = this.fixUpTaskRepository.findDataNumberFixUpTaskPerUser();

		return result;
	}

	public Double[] findDataMaximumPrice() {
		Double[] result;

		result = this.fixUpTaskRepository.findDataMaximumPrice();

		return result;
	}

	protected FixUpTask findWorkableFixUpTask(final int fixUpTaskId, final int handyWorkerId) {
		FixUpTask result;

		result = this.fixUpTaskRepository.findWorkableFixUpTask(fixUpTaskId, handyWorkerId);

		return result;
	}

	protected Collection<FixUpTask> findFixUpTaskByCategory(final int categoryId) {
		Collection<FixUpTask> results;

		results = this.fixUpTaskRepository.findFixUpTaskByCategory(categoryId);

		return results;
	}

	protected String existTicker(final String ticker) {
		String result;

		result = this.fixUpTaskRepository.existTicker(ticker);

		return result;
	}

	protected void addNewPhase(final FixUpTask fixUpTask, final Phase phase) {
		fixUpTask.getPhases().add(phase);
	}

	protected void removePhase(final FixUpTask fixUpTask, final Phase phase) {
		fixUpTask.getPhases().remove(phase);
	}

	protected void addApplication(final FixUpTask fixUpTask, final Application application) {
		fixUpTask.getApplications().add(application);
	}

	protected void updateCategory(final FixUpTask fixUpTask, final Category category) {
		fixUpTask.setCategory(category);
	}

	private void checkByPrincipal(final FixUpTask fixUpTask, final Customer principal) {
		Assert.isTrue(principal.equals(fixUpTask.getCustomer()));
	}
}
