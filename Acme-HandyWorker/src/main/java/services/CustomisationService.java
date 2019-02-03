
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CustomisationRepository;
import domain.Customisation;

@Service
@Transactional
public class CustomisationService {

	// Managed repository ------------------------------
	@Autowired
	private CustomisationRepository	customisationRepository;


	// Supporting services -----------------------------

	// Constructors ------------------------------------
	public CustomisationService() {
		super();
	}

	// Simple CRUD methods -----------------------------
	public Customisation save(final Customisation customisation) {
		Assert.notNull(customisation);
		Assert.isTrue(customisation.getId() != 0);

		final Customisation result;
		final Customisation find;

		find = this.find();

		Assert.isTrue(find.getId() == customisation.getId());

		result = this.customisationRepository.save(customisation);

		return result;
	}

	// Other business methods --------------------------
	public Customisation find() {
		Customisation result;
		Customisation[] all;

		all = this.customisationRepository.find();
		Assert.isTrue(all.length == 1);

		result = all[0];

		return result;
	}
}
