
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.UserAccount;
import utilities.AbstractTest;
import domain.Administrator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class AdministratorServiceTest extends AbstractTest {

	// Services under testing --------------------------------
	@Autowired
	private AdministratorService	administratorService;


	// Supporting test ---------------------------------------

	// Test
	@Test
	public void testCreate() {
		Administrator admin;

		admin = this.administratorService.create();

		Assert.notNull(admin);
		Assert.notNull(admin.getUserAccount());
		Assert.isNull(admin.getAddress());
		Assert.isNull(admin.getEmail());
		Assert.isNull(admin.getMiddleName());
		Assert.isNull(admin.getName());
		Assert.isNull(admin.getPhoneNumber());
		Assert.isNull(admin.getPhotoLink());
		Assert.isNull(admin.getSurname());
		Assert.isTrue(admin.getIsSuspicious() == false);
	}

	/* Test negativo: admin = null */
	@Test(expected = IllegalArgumentException.class)
	public void negativeTestSave_uno() {
		Administrator admin, saved;

		admin = null;

		saved = this.administratorService.save(admin);

		Assert.isNull(saved);
	}

	/*
	 * Test negativo: customer1 trata de editar los datos personales
	 * de otro actor, lo cual no debe de poder hacerse.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negativeTestSave_dos() {
		super.authenticate("customer1");

		int id;
		Administrator admin, saved;

		id = super.getEntityId("administrator1");

		admin = this.administratorService.findOne(id);
		admin.setName("Rodolfo");
		admin.setEmail("rodolfo@us.es");

		saved = this.administratorService.save(admin);

		Assert.isNull(saved);

		super.unauthenticate();
	}

	/* Formato del correo: "identifier@domain" */
	@Test
	public void positiveTestSave_uno() {
		Administrator admin, saved, showed;
		UserAccount userAccount;

		admin = this.administratorService.create();
		admin.setAddress("Calle Viruela");
		admin.setEmail("hola@gmail.com");
		admin.setName("admin2");
		admin.setPhoneNumber("+34 678345611");
		admin.setSurname("Surname 2");

		userAccount = admin.getUserAccount();
		userAccount.setUsername("customer7");
		userAccount.setPassword("customer7");

		saved = this.administratorService.save(admin);

		showed = this.administratorService.findOne(saved.getId());

		Assert.notNull(showed);
	}

	/* Formato del correo: "alias <identifier@domain>" */
	@Test
	public void positiveTestSave_dos() {
		Administrator admin, saved, showed;
		UserAccount userAccount;

		admin = this.administratorService.create();
		admin.setAddress("Calle Viruela");
		admin.setEmail("Alvaro <hola@gmail.com>");
		admin.setName("admin2");
		admin.setPhoneNumber("+34 678345611");
		admin.setSurname("Surname 2");

		userAccount = admin.getUserAccount();
		userAccount.setUsername("customer7");
		userAccount.setPassword("customer7");

		saved = this.administratorService.save(admin);

		showed = this.administratorService.findOne(saved.getId());

		Assert.notNull(showed);
	}

	/* Formato del correo: "identifier@" */
	@Test
	public void positiveTestSave_tres() {
		Administrator admin, saved, showed;
		UserAccount userAccount;

		admin = this.administratorService.create();
		admin.setAddress("Calle Viruela");
		admin.setEmail("Alvcalgon@");
		admin.setName("admin2");
		admin.setPhoneNumber("+34 678345611");
		admin.setSurname("Surname 2");

		userAccount = admin.getUserAccount();
		userAccount.setUsername("customer7");
		userAccount.setPassword("customer7");

		saved = this.administratorService.save(admin);

		showed = this.administratorService.findOne(saved.getId());

		Assert.notNull(showed);
	}

	/* Formato del correo: "alias <identifier@domain>" */
	@Test
	public void positiveTestSave_cuatro() {
		Administrator admin, saved, showed;
		UserAccount userAccount;

		admin = this.administratorService.create();
		admin.setAddress("Calle Viruela");
		admin.setEmail("Callejon <Alvcalgon@>");
		admin.setName("admin2");
		admin.setPhoneNumber("+34 678345611");
		admin.setSurname("Surname 2");

		userAccount = admin.getUserAccount();
		userAccount.setUsername("customer7");
		userAccount.setPassword("customer7");

		saved = this.administratorService.save(admin);

		showed = this.administratorService.findOne(saved.getId());

		Assert.notNull(showed);
	}

	@Test
	public void positiveTestSave_cinco() {
		super.authenticate("admin1");

		Administrator admin, saved, showed;

		final int id = super.getEntityId("administrator1");
		final String nombre = "Lola";
		final String apellidos = "Reina Alvarez";

		admin = this.administratorService.findOne(id);
		admin.setName(nombre);
		admin.setSurname(apellidos);

		saved = this.administratorService.save(admin);

		showed = this.administratorService.findOne(saved.getId());

		Assert.notNull(showed);
		Assert.isTrue(saved.getId() == id);
		Assert.isTrue(showed.getName().equals(nombre));
		Assert.isTrue(showed.getSurname().equals(apellidos));

		super.unauthenticate();
	}
}
