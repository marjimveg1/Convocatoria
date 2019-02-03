
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.CategoryTranslation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class CategoryTranslationServiceTest extends AbstractTest {

	// Services under testing ---------------------------------------------
	@Autowired
	private CategoryTranslationService	categoryTranslationService;


	// Tests --------------------------------------------------------------
	@Test
	public void testCreate() {
		super.authenticate("admin1");

		CategoryTranslation categoryTranslation;

		categoryTranslation = this.categoryTranslationService.create();

		Assert.notNull(categoryTranslation);
		Assert.isNull(categoryTranslation.getLanguage());
		Assert.isNull(categoryTranslation.getName());

		super.unauthenticate();
	}

	/* Test positivo en el que se inserta en la BD un objeto valido */
	@Test
	public void positiveTestSave_uno() {
		super.authenticate("admin1");

		CategoryTranslation categoryTranslation, saved;
		Collection<CategoryTranslation> all;

		categoryTranslation = this.categoryTranslationService.create();
		categoryTranslation.setName("Bricomania");
		categoryTranslation.setLanguage("es");

		saved = this.categoryTranslationService.save(categoryTranslation);

		all = this.categoryTranslationService.findAll();

		Assert.isTrue(all.contains(saved));

		super.unauthenticate();
	}

	/*
	 * Test negativo en el que se trata de insertar en la BD un objeto
	 * cuyos atributos CategoryTranslation::language y
	 * CategoryTranslation::name coinciden con los de otro objeto del
	 * mismo tipo
	 */
	@Test(expected = DataIntegrityViolationException.class)
	public void negativeTestSave_uno() {
		super.authenticate("admin1");

		CategoryTranslation categoryTranslation, saved;
		Collection<CategoryTranslation> all;

		categoryTranslation = this.categoryTranslationService.create();
		categoryTranslation.setName("Carpinteria");
		categoryTranslation.setLanguage("es");

		saved = this.categoryTranslationService.save(categoryTranslation);

		all = this.categoryTranslationService.findAll();

		Assert.isTrue(!all.contains(saved));

		super.unauthenticate();
	}

	/*
	 * Test negativo en el se prueba a insertar una categoryTranslation
	 * a null
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negativeTestSave_dos() {
		super.authenticate("admin1");

		CategoryTranslation categoryTranslation, saved;
		Collection<CategoryTranslation> all;

		categoryTranslation = null;

		saved = this.categoryTranslationService.save(categoryTranslation);

		all = this.categoryTranslationService.findAll();

		Assert.isTrue(!all.contains(saved));

		super.unauthenticate();
	}

	/* Test negativo: idioma no soportado */
	@Test(expected = IllegalArgumentException.class)
	public void negativeTestSave_tres() {
		super.authenticate("admin1");

		CategoryTranslation categoryTranslation, saved;
		Collection<CategoryTranslation> all;

		categoryTranslation = this.categoryTranslationService.create();
		categoryTranslation.setName("Menuiserie");
		categoryTranslation.setLanguage("fr");

		saved = this.categoryTranslationService.save(categoryTranslation);

		all = this.categoryTranslationService.findAll();

		Assert.isTrue(!all.contains(saved));

		super.unauthenticate();
	}

	/* Test positivo en el que se borra un objeto valido */
	@Test(expected = IllegalArgumentException.class)
	public void positiveTestDelete_uno() {
		super.authenticate("admin1");

		final int id;
		CategoryTranslation categoryTranslation;

		id = super.getEntityId("categoryTranslation30");
		categoryTranslation = this.categoryTranslationService.findOne(id);
		this.categoryTranslationService.delete(categoryTranslation);

		final CategoryTranslation deleted = this.categoryTranslationService.findOne(id);

		Assert.isNull(deleted);

		super.unauthenticate();
	}

	/*
	 * Test negativo: se intenta borrar un objeto que no existe
	 * en la BD
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negativeTestDelete_uno() {
		super.authenticate("admin1");

		CategoryTranslation categoryTranslation;
		Collection<CategoryTranslation> all;

		categoryTranslation = this.categoryTranslationService.create();
		categoryTranslation.setName("Fibra optica");
		categoryTranslation.setLanguage("es");

		this.categoryTranslationService.delete(categoryTranslation);

		all = this.categoryTranslationService.findAll();

		Assert.isTrue(!all.contains(categoryTranslation));

		super.unauthenticate();
	}

	/*
	 * Test negativo: no se puede borrar un objeto que no existe en
	 * la BD y que, ademas, es nulo
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negativeTestDelete_dos() {
		super.authenticate("admin1");

		CategoryTranslation categoryTranslation;
		Collection<CategoryTranslation> all;

		categoryTranslation = null;

		this.categoryTranslationService.delete(categoryTranslation);

		all = this.categoryTranslationService.findAll();

		Assert.isTrue(!all.contains(categoryTranslation));

		super.unauthenticate();
	}

	@Test
	public void testfindByLanguageCategory() {
		int id;
		String language;
		final CategoryTranslation categoryTranslation;

		id = super.getEntityId("category5");

		language = "es";

		categoryTranslation = this.categoryTranslationService.findByLanguageCategory(id, language);

		Assert.notNull(categoryTranslation);
	}
}
