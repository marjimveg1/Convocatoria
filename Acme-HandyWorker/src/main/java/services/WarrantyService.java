
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.WarrantyRepository;
import domain.Administrator;
import domain.Warranty;

@Service
@Transactional
public class WarrantyService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private WarrantyRepository		warrantyRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private UtilityService			utilityService;


	// Constructor ------------------------------------------------------------

	public WarrantyService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Warranty create() {
		Warranty result;

		result = new Warranty();

		return result;
	}

	public Collection<Warranty> findAll() {
		Collection<Warranty> result;

		result = this.warrantyRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Page<Warranty> findAll(final Pageable pageable) {
		Page<Warranty> result;

		result = this.warrantyRepository.findAll(pageable);
		Assert.notNull(result);

		return result;
	}

	public Warranty save(final Warranty warranty) {
		Assert.notNull(warranty);
		Assert.isTrue(!warranty.getFinalMode());

		Warranty result;
		Administrator principal;

		principal = this.administratorService.findByPrincipal();
		this.utilityService.checkActorIsBanned(principal);
		this.utilityService.checkIsSpamMarkAsSuspicious(warranty.getLaws() + warranty.getTerms() + warranty.getTitle(), principal);

		result = this.warrantyRepository.save(warranty);

		return result;
	}

	public void delete(final Warranty warranty) {
		Assert.notNull(warranty);
		Assert.isTrue(this.warrantyRepository.exists(warranty.getId()));
		Assert.isTrue(!warranty.getFinalMode());

		Administrator principal;

		principal = this.administratorService.findByPrincipal();
		this.utilityService.checkActorIsBanned(principal);

		this.warrantyRepository.delete(warranty);
	}

	public Warranty findOne(final int warrantyId) {
		Warranty result;

		result = this.warrantyRepository.findOne(warrantyId);
		Assert.notNull(result);

		return result;
	}

	// Other business methods -------------------------------------------------

	public void makeFinal(final Warranty warranty) {
		Administrator principal;

		principal = this.administratorService.findByPrincipal();
		this.utilityService.checkActorIsBanned(principal);

		warranty.setFinalMode(true);
	}

	public Collection<Warranty> findFinalWarranties() {
		Collection<Warranty> result;

		result = this.warrantyRepository.findFinalWarranties();
		Assert.notNull(result);

		return result;
	}
}
