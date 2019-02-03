
package services;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.QuoletRepository;
import domain.Customer;
import domain.FixUpTask;
import domain.Quolet;

@Service
@Transactional
public class QuoletService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private QuoletRepository	quoletRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private CustomerService		customerService;

	@Autowired
	private UtilityService		utilityService;


	// Constructors -----------------------------------------------------------

	public QuoletService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Quolet create() {
		Quolet result;

		result = new Quolet();
		result.setTicker(this.generateValidTickerControlCheck());
		result.setFinalMode(false);

		return result;
	}

	public Quolet findOne(final int quoletId) {
		Quolet result;

		result = this.quoletRepository.findOne(quoletId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Quolet> findAll() {
		Collection<Quolet> result;

		result = this.quoletRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Quolet save(final Quolet quolet) { //Editing
		Assert.notNull(quolet);
		Assert.isTrue(!quolet.getFinalMode());

		Quolet result;
		Customer principal;
		Customer owner;

		principal = this.customerService.findByPrincipal();
		this.utilityService.checkActorIsBanned(principal);
		owner = this.quoletRepository.findCustomerByQuoletid(quolet.getId());
		this.utilityService.checkIsSpamMarkAsSuspicious(quolet.getBody(), principal);

		Assert.isTrue(owner.equals(principal));

		result = this.quoletRepository.save(quolet);

		return result;

	}

	public Quolet save(final Quolet quolet, final FixUpTask fixUpTask) { //Creating
		Assert.notNull(quolet);
		Assert.isTrue(!quolet.getFinalMode());

		Quolet result;
		Customer principal;
		Customer owner;

		principal = this.customerService.findByPrincipal();
		owner = fixUpTask.getCustomer();
		this.utilityService.checkActorIsBanned(principal);
		this.utilityService.checkIsSpamMarkAsSuspicious(quolet.getBody(), principal);

		Assert.isTrue(owner.equals(principal));

		result = this.quoletRepository.save(quolet);

		this.addQuoletToFixUpTask(fixUpTask, result);

		return result;

	}

	public void delete(final Quolet quolet) {
		Assert.notNull(quolet);
		Assert.isTrue(this.quoletRepository.exists(quolet.getId()));
		Assert.isTrue(!quolet.getFinalMode());

		Customer principal;
		Customer owner;

		principal = this.customerService.findByPrincipal();
		this.utilityService.checkActorIsBanned(principal);
		owner = this.quoletRepository.findCustomerByQuoletid(quolet.getId());

		Assert.isTrue(owner.equals(principal));

		this.removeQuoletToFixUpTask(quolet);
		this.quoletRepository.delete(quolet);

	}

	// Other business methods -------------------------------------------------
	protected void delete(final FixUpTask fixUpTask) {
		Assert.notNull(fixUpTask);

		Customer principal;
		Collection<Quolet> quolets;

		principal = this.customerService.findByPrincipal();
		this.utilityService.checkActorIsBanned(principal);

		Assert.isTrue(fixUpTask.getCustomer().equals(principal));

		quolets = fixUpTask.getQuolets();
		for (final Quolet quolet : quolets)
			this.quoletRepository.delete(quolet);

	}

	protected void addQuoletToFixUpTask(final FixUpTask fixUpTask, final Quolet quolet) {
		fixUpTask.getQuolets().add(quolet);
	}

	protected void removeQuoletToFixUpTask(final Quolet quolet) {
		FixUpTask fixUpTask;

		fixUpTask = this.quoletRepository.findFixUpTaskByQuoletid(quolet.getId());
		fixUpTask.getQuolets().remove(quolet);
	}

	public void makeFinal(final Quolet quolet) {
		Customer principal;

		principal = this.customerService.findByPrincipal();
		this.utilityService.checkActorIsBanned(principal);

		quolet.setFinalMode(true);
		quolet.setPublicationMoment(this.utilityService.current_moment());
	}
	public FixUpTask findFixUpTaskByQuoletid(final int id) {
		FixUpTask fixUpTask;

		fixUpTask = this.quoletRepository.findFixUpTaskByQuoletid(id);

		return fixUpTask;
	}
	public Customer findCustomerByQuoletid(final int id) {
		Customer customer;

		customer = this.quoletRepository.findCustomerByQuoletid(id);

		return customer;
	}

	public Collection<Quolet> findQuoletsByCustomerid(final int id) {
		Collection<Quolet> quolets;

		quolets = this.quoletRepository.findQuoletsByCustomerid(id);

		return quolets;
	}

	public Collection<Quolet> findQuoletsPublishedByTask(final int id) {
		Collection<Quolet> quolets;

		quolets = this.quoletRepository.findQuoletsPublishedByTask(id);

		return quolets;
	}
	//	//QUERY A (1) - The average and standard deviation of the number of published QUOLET per CUSTOMER
	//	public Double[] findDataQuoletPerCustomer() {
	//		Double[] result;
	//
	//		result = this.quoletRepository.findDataQuoletPerCustomer();
	//
	//		return result;
	//	}
	//	//QUERY A (2) - The average and standard deviation of the number of published QUOLET per FUT
	//	public Double[] findDataQuoletPerFixUpTask() {
	//		Double[] result;
	//
	//		result = this.quoletRepository.findDataQuoletPerFixUpTask();
	//
	//		return result;
	//	}
	//QUERY B (1) - The ratio of published QUOLET vs total number of QUOLET
	public Double findRatioPublishedQuoletvsAllQuolet() {
		Double result;

		result = this.quoletRepository.findRatioPublishedQuoletvsAllQuolet();

		return result;
	}
	//QUERY B (2) - The ratio of published QUOLET vs total number of FUT
	public Double findRatioPublishedQuoletvsAllFixUpTask() {
		Double result;

		result = this.quoletRepository.findRatioPublishedQuoletvsAllFixUpTask();

		return result;
	}
	//QUERY C (1) - The ratio of unpublished QUOLET vs total number of QUOLET
	public Double findRatioUnpublishedQuoletvsAllQuoles() {
		Double result;

		result = this.quoletRepository.findRatioUnpublishedQuoletvsAllQuoles();

		return result;
	}
	//QUERY C (2) - The ratio of unpublished QUOLET vs total number of FUT
	public Double findRatioUnpublishedQuoletvsAllFixUpTask() {
		Double result;

		result = this.quoletRepository.findRatioUnpublishedQuoletvsAllFixUpTask();

		return result;
	}

	// Hace el calculo sum(x*x): select count(q)*count(q) from FixUpTask f join f.quolets q where q.finalMode=true group by f;
	// count(x): select count(f) from FixUpTask f;
	// avg(x): select count(q)*1.0/(select count(f) from FixUpTask f) from Quolet q where q.finalMode=true

	public Double[] findDataQuoletPerFixUpTask() {
		final Double[] result = new Double[2];

		final Double d1 = this.quoletRepository.findAvgQuoletPerFixUpTask();
		final Double d2 = this.StandarDeviation();

		result[0] = d1;
		result[1] = d2;

		return result;
	}

	private Double StandarDeviation() {
		Double result, w_avg;
		Long w_tam, w_sum = 0l;
		final List<Long> elements = this.quoletRepository.findElementSum();

		if (elements != null && !elements.isEmpty()) {
			for (final Long a : elements)
				w_sum += a;

			w_tam = this.quoletRepository.findTam();
			w_avg = this.quoletRepository.findAvgQuoletPerFixUpTask();

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

		numbers = String.format("%02d", year) + "" + String.format("%02d", month) + "" + String.format("%02d", day) + "-";
		counter = 0;

		do {
			result = numbers + this.createRandomLetters();
			counter++;
		} while (!(this.existTicker(result) == null) && counter < 650000);

		return result;
	}
	protected String existTicker(final String ticker) {
		String result;

		result = this.quoletRepository.existTicker(ticker);

		return result;
	}
	private String createRandomLetters() {
		String result, characters;
		Random randomNumber;

		result = "";
		randomNumber = new Random();
		characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

		for (int i = 0; i <= 5; i++)
			result += characters.charAt(randomNumber.nextInt(characters.length()));

		return result;
	}

}
