
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AdministratorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Administrator;

@Service
@Transactional
public class AdministratorService {

	// Managed repository ----------------------------------
	@Autowired
	private AdministratorRepository	administratorRepository;

	// Supporting repositories -----------------------------

	@Autowired
	private ActorService			actorService;


	// Constructors ----------------------------------------
	public AdministratorService() {
		super();
	}

	// Simple CRUD methods ---------------------------------
	public Administrator findOne(final int administratorId) {
		Administrator result;

		result = this.administratorRepository.findOne(administratorId);
		Assert.notNull(result);

		return result;
	}

	public Administrator create() {
		Administrator result;

		result = new Administrator();
		result.setUserAccount(this.actorService.createUserAccount(Authority.ADMIN));

		return result;
	}

	public Administrator save(final Administrator administrator) {
		Administrator result;

		result = (Administrator) this.actorService.save(administrator);

		return result;
	}

	// Other business methods ------------------------------

	public Administrator findByPrincipal() {
		UserAccount userAccount;
		Administrator result;

		userAccount = LoginService.getPrincipal();
		result = this.findByUserAccount(userAccount.getId());

		return result;
	}

	public Administrator findByUserAccount(final int userAccountId) {
		Administrator result;

		result = this.administratorRepository.findByUserAccount(userAccountId);

		return result;
	}

}
