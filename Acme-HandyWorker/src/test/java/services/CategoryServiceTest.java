
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Category;
import domain.CategoryTranslation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class CategoryServiceTest extends AbstractTest {

	// Service under testing ---------------------------------
	@Autowired
	private CategoryService				categoryService;

	// Supporting services -----------------------------------
	@Autowired
	private CategoryTranslationService	categoryTranslationService;


	// Test --------------------------------------------------
	@Test
	public void testCreate() {
		super.authenticate("admin1");

		Category category;

		category = this.categoryService.create();

		Assert.notNull(category);
		Assert.notNull(category.getDescendants());
		Assert.notNull(category.getCategoriesTranslations());
		Assert.isNull(category.getParent());

		super.unauthenticate();
	}

	/* Se inserta con exito un objeto en la BD */
	@Test
	public void positiveTestSave_uno() {
		super.authenticate("admin1");

		Category category;
		final Category saved, parent;
		//final Collection<Category> all;

		category = this.categoryService.create();
		category.setParent(this.getParent());
		category.setCategoriesTranslations(this.categoriesTranslation());

		saved = this.categoryService.save(category);

		Assert.isTrue(saved != null && saved.getId() != 0);
		//		all = this.categoryService.findAll();
		parent = saved.getParent();

		//		Assert.isTrue(all.contains(saved));
		Assert.isTrue(parent.getDescendants().contains(saved));

		super.unauthenticate();
	}

	/*
	 * Se edita con exito un categoryTranslation: se modifica el
	 * padre del objeto y se actualizar correctamente la jerarquia
	 */
	@Test
	public void positiveTestSave_dos() {
		super.authenticate("admin1");

		int new_parentId, edited_CategoryId;
		final Category edited, saved_edited, parent, new_parent;
		final Collection<Category> all;

		edited_CategoryId = super.getEntityId("category25");
		new_parentId = super.getEntityId("category3");

		edited = this.categoryService.findOne(edited_CategoryId);

		parent = edited.getParent();
		new_parent = this.categoryService.findOne(new_parentId);

		edited.setParent(new_parent);

		saved_edited = this.categoryService.save(edited);

		all = this.categoryService.findAll();

		Assert.isTrue(all.contains(saved_edited));
		Assert.isTrue(!parent.getDescendants().contains(saved_edited));
		Assert.isTrue(new_parent.getDescendants().contains(saved_edited));

		super.unauthenticate();
	}

	/* Test negativo: category = null */
	@Test(expected = IllegalArgumentException.class)
	public void negativeTestSave_uno() {
		super.authenticate("admin1");

		Category category, saved;
		Collection<Category> all;

		category = null;

		saved = this.categoryService.save(category);

		all = this.categoryService.findAll();

		Assert.isTrue(!all.contains(saved));

		super.unauthenticate();
	}

	/* Test negativo: categoria sin padre */
	@Test(expected = IllegalArgumentException.class)
	public void negativeTestSave_dos() {
		super.authenticate("admin1");

		Category category, saved;
		Collection<Category> all;

		category = this.categoryService.create();
		category.setCategoriesTranslations(this.categoriesTranslation());

		saved = this.categoryService.save(category);

		all = this.categoryService.findAll();

		Assert.isTrue(!all.contains(saved));

		super.unauthenticate();
	}

	/* Test invalido: numero de categoyTranslation insuficiente */
	@Test(expected = IllegalArgumentException.class)
	public void negativeTestSave_tres() {
		super.authenticate("admin1");

		Category category, saved;
		Collection<Category> all;

		category = this.categoryService.create();
		category.setParent(this.getParent());
		category.setCategoriesTranslations(this.invalid_number_categoriesTranslation());

		saved = this.categoryService.save(category);

		all = this.categoryService.findAll();

		Assert.isTrue(!all.contains(saved));

		super.unauthenticate();
	}

	/*
	 * Test invalido: numero de categoyTranslation correcto pero ambas
	 * categoriesTranslation son del mismo idioma
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negativeTestSave_cuatro() {
		super.authenticate("admin1");

		Category category, saved;
		final Category showed;

		category = this.categoryService.create();
		category.setParent(this.getParent());
		category.setCategoriesTranslations(this.invalid_categoriesTranslation());

		saved = this.categoryService.save(category);

		showed = this.categoryService.findOne(saved.getId());

		Assert.isNull(showed);

		super.unauthenticate();
	}

	/*
	 * Se trata de formar un ciclo en la jerarquía de categorias
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negativeTestSave_cinco() {
		int id_edited, id_new_parent;
		Category edited, new_parent, saved;

		id_edited = super.getEntityId("category2");
		id_new_parent = super.getEntityId("category5");

		edited = this.categoryService.findOne(id_edited);
		new_parent = this.categoryService.findOne(id_new_parent);

		edited.setParent(new_parent);

		saved = this.categoryService.save(edited);

		Assert.isNull(saved);
	}

	/* Test negativo: category = null */
	@Test(expected = IllegalArgumentException.class)
	public void negativeTestDelete_uno() {
		super.authenticate("admin1");

		Category category;
		Collection<Category> all;

		category = null;

		this.categoryService.delete(category);

		all = this.categoryService.findAll();

		Assert.isTrue(!all.contains(category));

		super.unauthenticate();
	}

	/* Test negativo: category.id = 0 */
	@Test(expected = IllegalArgumentException.class)
	public void negativeTestDelete_dos() {
		super.authenticate("admin1");

		Category category;
		Collection<Category> all;

		category = this.categoryService.create();
		category.setParent(this.getParent());
		category.setCategoriesTranslations(this.categoriesTranslation());

		this.categoryService.delete(category);

		all = this.categoryService.findAll();

		Assert.isTrue(!all.contains(category));

		super.unauthenticate();
	}

	@Test
	public void positiveTestDelete_uno() {
		super.authenticate("admin1");

		final int id = super.getEntityId("category3");
		Category category, parent;
		Collection<Category> categories, all;

		category = this.categoryService.findOne(id);

		parent = category.getParent();
		categories = category.getDescendants();

		this.categoryService.delete(category);

		all = this.categoryService.findAll();

		Assert.isTrue(!all.contains(category));

		for (final Category c : categories) {
			Assert.isTrue(c.getParent().equals(parent));
			Assert.isTrue(parent.getDescendants().contains(c));
		}

		super.unauthenticate();
	}

	// private methods -----------------------------------------
	private List<CategoryTranslation> categoriesTranslation() {
		List<CategoryTranslation> results;

		CategoryTranslation ct1, ct2, ct1_saved, ct2_saved;

		ct1 = this.categoryTranslationService.create();
		ct1.setLanguage("es");
		ct1.setName("Arreglar videoconsola");

		ct2 = this.categoryTranslationService.create();
		ct2.setLanguage("en");
		ct2.setName("Repair videoconsole");

		ct1_saved = this.categoryTranslationService.save(ct1);
		ct2_saved = this.categoryTranslationService.save(ct2);

		results = new ArrayList<CategoryTranslation>();
		results.add(ct1_saved);
		results.add(ct2_saved);

		return results;
	}

	private List<CategoryTranslation> invalid_categoriesTranslation() {
		List<CategoryTranslation> results;

		CategoryTranslation ct1, ct2, ct1_saved, ct2_saved;

		ct1 = this.categoryTranslationService.create();
		ct1.setLanguage("en");
		ct1.setName("Formatear PC");

		ct2 = this.categoryTranslationService.create();
		ct2.setLanguage("en");
		ct2.setName("Format PC");

		ct1_saved = this.categoryTranslationService.save(ct1);
		ct2_saved = this.categoryTranslationService.save(ct2);

		results = new ArrayList<CategoryTranslation>();
		results.add(ct1_saved);
		results.add(ct2_saved);

		return results;
	}

	private List<CategoryTranslation> invalid_number_categoriesTranslation() {
		List<CategoryTranslation> results;

		CategoryTranslation ct, saved;

		ct = this.categoryTranslationService.create();
		ct.setLanguage("es");
		ct.setName("Instalar programas PC");

		saved = this.categoryTranslationService.save(ct);

		results = new ArrayList<CategoryTranslation>();
		results.add(saved);

		return results;
	}

	private Category getParent() {
		Category result;
		int id;

		id = super.getEntityId("category2");
		result = this.categoryService.findOne(id);

		return result;
	}

}
