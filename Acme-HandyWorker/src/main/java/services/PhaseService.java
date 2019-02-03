
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PhaseRepository;
import domain.FixUpTask;
import domain.HandyWorker;
import domain.Phase;

@Service
@Transactional
public class PhaseService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private PhaseRepository		phaseRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private HandyWorkerService	handyWorkerService;

	@Autowired
	private FixUpTaskService	fixUpTaskService;

	@Autowired
	private UtilityService		utilityService;


	// Constructor ------------------------------------------------------------

	public PhaseService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Phase create() {
		Phase result;

		result = new Phase();

		return result;
	}

	public Phase save(final Phase phase) { // Updating
		return this.save(null, phase);
	}

	public Phase save(FixUpTask fixUpTask, final Phase phase) { // Creating
		Assert.notNull(phase);
		this.utilityService.checkDate(phase.getStartMoment(), phase.getEndMoment());

		Phase result;
		boolean isUpdating;

		isUpdating = this.phaseRepository.exists(phase.getId());

		if (isUpdating) {
			this.checkOwner(phase);
			fixUpTask = this.fixUpTaskService.findByPhaseId(phase.getId());
		} else
			Assert.isTrue(this.checkHandyWorkerAccess(fixUpTask));

		Assert.notNull(fixUpTask);
		this.checkPhaseDate(fixUpTask, phase);

		result = this.phaseRepository.save(phase);

		if (!isUpdating)
			this.fixUpTaskService.addNewPhase(fixUpTask, result);

		return result;
	}

	public void delete(final Phase phase) {
		Assert.notNull(phase);
		Assert.isTrue(this.phaseRepository.exists(phase.getId()));
		this.checkOwner(phase);

		FixUpTask fixUpTask;

		fixUpTask = this.fixUpTaskService.findByPhaseId(phase.getId());
		this.fixUpTaskService.removePhase(fixUpTask, phase);

		this.phaseRepository.delete(phase);
	}

	public Phase findOne(final int phaseId) {
		Phase result;

		result = this.phaseRepository.findOne(phaseId);
		Assert.notNull(result);

		return result;
	}

	// Other business methods -------------------------------------------------

	public Page<Phase> findByFixUpTaskId(final int fixUpTaskId, final Pageable pageable) {
		Page<Phase> result;

		result = this.phaseRepository.findByFixUpTaskId(fixUpTaskId, pageable);
		Assert.notNull(result);

		return result;
	}

	public boolean checkHandyWorkerAccess(final FixUpTask fixUpTask) {
		boolean result;
		HandyWorker principal;
		FixUpTask workableFixUpTask;

		principal = this.handyWorkerService.findByPrincipal();
		if (principal == null)
			result = false;
		else {
			workableFixUpTask = this.fixUpTaskService.findWorkableFixUpTask(fixUpTask.getId(), principal.getId());
			result = workableFixUpTask != null;
		}

		return result;
	}

	private void checkOwner(final Phase phase) {
		HandyWorker principal;
		int principalId, ownerId;

		principal = this.handyWorkerService.findByPrincipal();
		principalId = principal.getId();
		ownerId = this.handyWorkerService.findPhaseCreator(phase);

		Assert.isTrue(principalId == ownerId);
		this.utilityService.checkIsSpamMarkAsSuspicious(phase.getDescription() + phase.getTitle(), principal);
		this.utilityService.checkActorIsBanned(principal);
	}

	private void checkPhaseDate(final FixUpTask fixUpTask, final Phase phase) {
		Assert.isTrue(phase.getStartMoment().after(fixUpTask.getStartDate()) && phase.getStartMoment().before(fixUpTask.getEndDate()));
		Assert.isTrue(phase.getEndMoment().after(fixUpTask.getStartDate()) && phase.getEndMoment().before(fixUpTask.getEndDate()));
	}
}
