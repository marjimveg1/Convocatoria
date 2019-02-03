
package services;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.UserAccount;
import domain.Actor;
import domain.Administrator;
import domain.CreditCard;

@Service
@Transactional
public class UtilityService {

	// Managed repository ------------------------------------------------------

	// Supporting services -----------------------------------------------------

	@Autowired
	private FixUpTaskService		fixUpTaskService;

	@Autowired
	private CustomisationService	customisationService;

	@Autowired
	private ActorService			actorService;


	// Constructors ------------------------------------------------------------

	public UtilityService() {
		super();
	}

	// Simple CRUD methods -----------------------------------------------------

	// Other business methods --------------------------------------------------

	public void checkEmailActors(final Actor actor) {
		if (actor instanceof Administrator)
			Assert.isTrue(actor.getEmail().matches("[A-Za-z_.]+[\\w]+@[a-zA-Z0-9.-]+|[\\w\\s]+[\\<][A-Za-z_.]+[\\w]+@[a-zA-Z0-9.-]+[\\>]|[A-Za-z_.]+[\\w]+@|[\\w\\s]+[\\<][A-Za-z_.]+[\\w]+@+[\\>]"));
		else
			Assert.isTrue(actor.getEmail().matches("[A-Za-z_.]+[\\w]+@[a-zA-Z0-9.-]+|[\\w\\s]+[\\<][A-Za-z_.]+[\\w]+@[a-zA-Z0-9.-]+[\\>]"));
	}

	public void checkEmailRecords(final String email) {
		Assert.isTrue(email.matches("[A-Za-z_.]+[\\w]+@[a-zA-Z0-9.-]+|[\\w\\s]+[\\<][A-Za-z_.]+[\\w]+@[a-zA-Z0-9.-]+[\\>]"));
	}

	public String getValidPhone(String phone) {
		String countryCode, result;

		if (phone != null && !phone.equals("")) {
			phone = phone.trim();

			if (phone.matches("(([0-9]{1,3}\\ )?([0-9]+))")) {
				countryCode = this.customisationService.find().getCountryCode();
				result = countryCode + " " + phone;
			} else
				result = phone;
		} else
			result = null;

		return result;
	}

	public String generateValidTicker() {
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
		} while (!(this.fixUpTaskService.existTicker(result) == null) && counter < 650000);

		return result;
	}

	public void checkDate(final Date start, final Date end) {
		Assert.isTrue(start.before(end));
	}

	public Date current_moment() {
		Date result;

		result = new Date(System.currentTimeMillis() - 1);

		return result;
	}

	public void checkUsername(final Actor actor) {
		Assert.isTrue(!actor.getUserAccount().getUsername().equals("System"));
	}

	public void checkAttachments(final String attachments) {
		List<String> attachmentList;

		Assert.notNull(attachments);
		attachmentList = this.getSplittedString(attachments);

		for (final String at : attachmentList)
			try {
				new URL(at);
			} catch (final MalformedURLException e) {
				throw new IllegalArgumentException("Invalid URL");
			}
	}

	public void checkTags(final String tags) {
		List<String> tagsList;

		Assert.notNull(tags);
		tagsList = this.getSplittedString(tags);

		for (final String t : tagsList)
			try {
				new String(t);
			} catch (final Throwable oops) {
				throw new IllegalArgumentException("Invalid Tag");
			}
	}

	public List<String> getSplittedString(final String string) {
		List<String> result;
		String[] stringsArray;

		result = new ArrayList<>();
		stringsArray = string.split("\r");

		for (String at : stringsArray) {
			at = at.trim();
			if (!at.isEmpty())
				result.add(at);
		}

		return result;
	}

	// Private methods ---------------------------------------------------------

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

	//------------------CREDIT CARD------------------------------------
	public CreditCard createnewCreditCard() {
		CreditCard creditCard;

		creditCard = new CreditCard();
		creditCard.setBrandName("XXX");
		creditCard.setCvvCode(123);
		creditCard.setExpirationMonth("01");
		creditCard.setExpirationYear("00");
		creditCard.setHolderName("XXX");
		creditCard.setNumber("XXXXXXXXXXXXXXX");

		return creditCard;
	}
	public boolean checkCreditCard(final CreditCard creditCard) {
		boolean res;
		res = true;

		Integer cvvCode;
		String expirationMonth;
		String expirationYear;
		String holderName;
		String number;

		cvvCode = creditCard.getCvvCode();
		expirationMonth = creditCard.getExpirationMonth();
		expirationYear = creditCard.getExpirationYear();
		holderName = creditCard.getHolderName();
		number = creditCard.getNumber();

		final Pattern holderNamePattern = Pattern.compile("[a-zA-Z]*");
		final Pattern expirationPattern = Pattern.compile("[0-9][0-9]");
		final Pattern numberPattern = Pattern.compile("\\d{9,16}");
		final Pattern cvvPattern = Pattern.compile("[1-9][0-9][0-9]");

		final Matcher holderNameMat = holderNamePattern.matcher(holderName);
		final Matcher expirationMonthMat = expirationPattern.matcher(expirationMonth);
		final Matcher expirationYearMat = expirationPattern.matcher(expirationYear);
		final Matcher numberMat = numberPattern.matcher(number);
		final Matcher cvvMat = cvvPattern.matcher(cvvCode.toString());

		if (!holderNameMat.matches() || !expirationMonthMat.matches() || !expirationYearMat.matches() || !numberMat.matches() || !cvvMat.matches())
			res = false;
		return res;

	}
	public Boolean checkIfCreditCardChanged(final CreditCard creditCard) {
		String brandName;
		String expirationMonth;
		String expirationYear;
		String holderName;
		String number;
		Integer cvvCode;
		Boolean result;

		result = false;
		brandName = creditCard.getBrandName();
		expirationMonth = creditCard.getExpirationMonth();
		expirationYear = creditCard.getExpirationYear();
		holderName = creditCard.getHolderName();
		number = creditCard.getNumber();
		cvvCode = creditCard.getCvvCode();

		//If creditCard is changed
		if (!(brandName.equals("XXX") && expirationMonth.equals("XXX") && expirationYear.equals("XXX") && holderName.equals("XXX") && number.equals("XXXXXXXXXXXXXXX") && cvvCode.equals(123)))
			Assert.isTrue(this.checkCreditCard(creditCard));

		result = true;
		return result;

	}

	public boolean checkIsSpamMarkAsSuspicious(final String string, final Actor actor) {
		boolean res = false;
		final Collection<String> spamWords = this.customisationService.find().getSpamWords();

		for (final String sw : spamWords)
			if (string.contains(sw)) {
				res = true;
				break;
			}

		if (res)
			this.actorService.markAsSuspicious(actor);
		return res;
	}

	public void checkActorIsBanned(final Actor actor) {
		UserAccount userAccount;

		userAccount = actor.getUserAccount();

		Assert.isTrue(!userAccount.getIsBanned());
	}

}
