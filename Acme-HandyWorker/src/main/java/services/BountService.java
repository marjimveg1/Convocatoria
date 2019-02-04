
package services;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.BountRepository;
import domain.Bount;
import domain.Customer;
import domain.FixUpTask;

@Service
@Transactional
public class BountService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private BountRepository	bountRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private CustomerService	customerService;

	@Autowired
	private UtilityService	utilityService;


	// Constructors -----------------------------------------------------------

	public BountService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Bount create() {
		Bount result;

		result = new Bount();
		result.setTicker(this.generateValidTickerControlCheck());
		result.setFinalMode(false);

		return result;
	}

	public Bount findOne(final int bountId) {
		Bount result;

		result = this.bountRepository.findOne(bountId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Bount> findAll() {
		Collection<Bount> result;

		result = this.bountRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Bount save(final Bount bount) { //Editing
		Assert.notNull(bount);
		Assert.isTrue(!bount.getFinalMode());

		Bount result;
		Customer principal;
		Customer owner;

		principal = this.customerService.findByPrincipal();
		this.utilityService.checkActorIsBanned(principal);
		owner = this.bountRepository.findCustomerByBountid(bount.getId());
		this.utilityService.checkIsSpamMarkAsSuspicious(bount.getBody(), principal);

		Assert.isTrue(owner.equals(principal));

		result = this.bountRepository.save(bount);

		return result;

	}

	public Bount save(final Bount bount, final FixUpTask fixUpTask) { //Creating
		Assert.notNull(bount);
		Assert.isTrue(!bount.getFinalMode());

		Bount result;
		Customer principal;
		Customer owner;

		principal = this.customerService.findByPrincipal();
		owner = fixUpTask.getCustomer();
		this.utilityService.checkActorIsBanned(principal);
		this.utilityService.checkIsSpamMarkAsSuspicious(bount.getBody(), principal);

		Assert.isTrue(owner.equals(principal));

		result = this.bountRepository.save(bount);

		this.addBountToFixUpTask(fixUpTask, result);

		return result;

	}

	public void delete(final Bount bount) {
		Assert.notNull(bount);
		Assert.isTrue(this.bountRepository.exists(bount.getId()));
		Assert.isTrue(!bount.getFinalMode());

		Customer principal;
		Customer owner;

		principal = this.customerService.findByPrincipal();
		this.utilityService.checkActorIsBanned(principal);
		owner = this.bountRepository.findCustomerByBountid(bount.getId());

		Assert.isTrue(owner.equals(principal));

		this.removeBountToFixUpTask(bount);
		this.bountRepository.delete(bount);

	}

	// Other business methods -------------------------------------------------
	protected void delete(final FixUpTask fixUpTask) {
		Assert.notNull(fixUpTask);

		Customer principal;
		Collection<Bount> bounts;

		principal = this.customerService.findByPrincipal();
		this.utilityService.checkActorIsBanned(principal);

		Assert.isTrue(fixUpTask.getCustomer().equals(principal));

		bounts = fixUpTask.getBounts();
		for (final Bount bount : bounts)
			this.bountRepository.delete(bount);

	}

	protected void addBountToFixUpTask(final FixUpTask fixUpTask, final Bount bount) {
		fixUpTask.getBounts().add(bount);
	}

	protected void removeBountToFixUpTask(final Bount bount) {
		FixUpTask fixUpTask;

		fixUpTask = this.bountRepository.findFixUpTaskByBountid(bount.getId());
		fixUpTask.getBounts().remove(bount);
	}

	public void makeFinal(final Bount bount) {
		Customer principal;

		principal = this.customerService.findByPrincipal();
		this.utilityService.checkActorIsBanned(principal);

		bount.setFinalMode(true);
		bount.setPublicationMoment(this.utilityService.current_moment());
	}
	public FixUpTask findFixUpTaskByBountid(final int id) {
		FixUpTask fixUpTask;

		fixUpTask = this.bountRepository.findFixUpTaskByBountid(id);

		return fixUpTask;
	}
	public Customer findCustomerByBountid(final int id) {
		Customer customer;

		customer = this.bountRepository.findCustomerByBountid(id);

		return customer;
	}

	public Collection<Bount> findBountsByCustomerid(final int id) {
		Collection<Bount> bounts;

		bounts = this.bountRepository.findBountsByCustomerid(id);

		return bounts;
	}

	public Collection<Bount> findBountsPublishedByTask(final int id) {
		Collection<Bount> bounts;

		bounts = this.bountRepository.findBountsPublishedByTask(id);

		return bounts;
	}

	//QUERY B (1) - The ratio of published QUOLET vs total number of QUOLET
	public Double findRatioPublishedBountvsAllBount() {
		Double result;

		result = this.bountRepository.findRatioPublishedBountvsAllBount();

		return result;
	}

	//QUERY C (1) - The ratio of unpublished QUOLET vs total number of QUOLET
	public Double findRatioUnpublishedBountvsAllQuoles() {
		Double result;

		result = this.bountRepository.findRatioUnpublishedBountvsAllQuoles();

		return result;
	}

	// Hace el calculo sum(x*x): select count(q)*count(q) from FixUpTask f join f.bounts q where q.finalMode=true group by f;
	// count(x): select count(f) from FixUpTask f;
	// avg(x): select count(q)*1.0/(select count(f) from FixUpTask f) from Bount q where q.finalMode=true

	public Double[] findDataBountPerFixUpTask() {
		final Double[] result = new Double[2];

		final Double d1 = this.bountRepository.findAvgBountPerFixUpTask();
		final Double d2 = this.StandarDeviation();

		result[0] = d1;
		result[1] = d2;

		return result;
	}

	private Double StandarDeviation() {
		Double result, w_avg;
		Long w_tam, w_sum = 0l;
		final List<Long> elements = this.bountRepository.findElementSum();

		if (elements != null && !elements.isEmpty()) {
			for (final Long a : elements)
				w_sum += a;

			w_tam = this.bountRepository.findTam();
			w_avg = this.bountRepository.findAvgBountPerFixUpTask();

			w_avg = w_avg * w_avg;

			if (w_tam != 0)
				result = Math.sqrt((w_sum * 1.0 / w_tam) - w_avg);
			else
				result = null;
		} else
			result = null;

		return result;
	}

	public String generateValidTickerControlCheck() {
		String numbers, result;
		Integer day, month, year;
		LocalDate currentDate;
		Integer counter;

		currentDate = LocalDate.now();
		year = currentDate.getYear() % 100;
		month = currentDate.getMonthOfYear();
		day = currentDate.getDayOfMonth();

		numbers = String.format("%02d", year) + "-" + String.format("%02d", month) + "" + String.format("%02d", day);
		counter = 0;

		do {
			result = numbers + this.createRandomNumeric();
			counter++;
		} while (!(this.existTicker(result) == null) && counter < 650000);

		return result;
	}
	protected String existTicker(final String ticker) {
		String result;

		result = this.bountRepository.existTicker(ticker);

		return result;
	}

	private String createRandomNumeric() {
		String result, characters;
		Random randomNumber;

		result = "";
		randomNumber = new Random();
		characters = "0123456789";

		final String k = this.randonK();
		final int a = Integer.parseInt(k);
		for (int i = 0; i <= a; i++)
			result += characters.charAt(randomNumber.nextInt(characters.length()));

		return result;
	}

	private String randonK() {

		String result, characters;
		Random randomNumber;

		result = "";
		randomNumber = new Random();
		characters = "234";

		for (int i = 0; i <= 0; i++)
			result += characters.charAt(randomNumber.nextInt(characters.length()));

		return result;

	}

}
