
package security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import services.ActorService;
import domain.Actor;

@Service
@Transactional
public class UserAccountService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private UserAccountRepository	userAccountRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService			actorService;


	// Constructors -----------------------------------------------------------

	public UserAccountService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public UserAccount findByActor(final Actor actor) {
		Assert.notNull(actor);

		UserAccount result;

		result = this.userAccountRepository.findByActorId(actor.getId());

		return result;
	}

	public UserAccount save(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		UserAccount result;

		result = this.userAccountRepository.save(userAccount);

		Assert.isTrue(result.getId() != 0);

		return result;
	}

	// Other business methods -------------------------------------------------

	public void changeBan(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		Assert.isTrue(!userAccount.getIsBanned());

		Actor actor;

		actor = this.actorService.findActorByUseraccount(userAccount.getId());

		Assert.isTrue(actor.getIsSuspicious() == true);

		this.actorService.changeBan(actor);

	}

	public boolean existUsername(final String username) {
		boolean result;
		UserAccount userAccount;

		userAccount = this.userAccountRepository.findByUsername(username);
		result = !(userAccount == null);

		return result;
	}

	public void setLogin(final UserAccount userAccount, final String username, final String password) {
		if (!"".equals(username) && username != null)
			userAccount.setUsername(username);

		if (!"".equals(password) && password != null)
			userAccount.setPassword(password);
	}
}
