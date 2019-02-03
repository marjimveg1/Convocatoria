
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.HandyWorkerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Application;
import domain.HandyWorker;
import domain.Phase;

@Service
@Transactional
public class HandyWorkerService {

	// Managed repository ---------------------------------------------
	@Autowired
	private HandyWorkerRepository	handyWorkerRepository;

	// Supporting services -------------------------------------------

	@Autowired
	private ActorService			actorService;


	//Constructor ----------------------------------------------------
	public HandyWorkerService() {
		super();
	}
	//Simple CRUD methods -------------------------------------------

	public HandyWorker create() {
		HandyWorker result;
		Collection<Application> applications;

		result = new HandyWorker();
		applications = new ArrayList<Application>();

		result.setApplications(applications);
		result.setUserAccount(this.actorService.createUserAccount(Authority.HANDYWORKER));

		return result;
	}

	public HandyWorker save(final HandyWorker handyWorker) {
		final HandyWorker result;
		String make;
		result = (HandyWorker) this.actorService.save(handyWorker);

		//To add default make,it's just in case  it was blank

		if (result.getMake() == "")
			if (result.getMiddleName() == null) {
				result.setMiddleName("");
				make = result.getName() + " " + result.getMiddleName();
				result.setMake(make);
			} else {
				make = result.getName() + " " + result.getMiddleName();
				result.setMake(make);
			}
		return result;

	}
	public HandyWorker findOne(final int idHandyWorker) {
		HandyWorker result;

		Assert.isTrue(idHandyWorker != 0);

		result = this.handyWorkerRepository.findOne(idHandyWorker);

		return result;
	}
	public Collection<HandyWorker> findAll() {
		Collection<HandyWorker> result;

		result = this.handyWorkerRepository.findAll();

		Assert.notNull(result);

		return result;
	}

	//Other business methods-------------------------------------------

	public HandyWorker findByPrincipal() {
		HandyWorker result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();

		result = this.findByUserAccount(userAccount.getId());

		return result;
	}

	public HandyWorker findByUserAccount(final int userAccountId) {
		HandyWorker result;

		result = this.handyWorkerRepository.findByUserAccount(userAccountId);

		return result;
	}

	public int findPhaseCreator(final Phase phase) {
		int result;

		result = this.handyWorkerRepository.findPhaseCreatorId(phase);

		return result;
	}

	//Req 12.5.10
	public Collection<HandyWorker> atLeast10Application() {
		final Collection<HandyWorker> result;

		result = this.handyWorkerRepository.atLeast10Application();

		return result;
	}

}
