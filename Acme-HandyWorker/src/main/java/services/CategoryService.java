
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.CategoryRepository;
import domain.Category;
import domain.CategoryTranslation;
import domain.FixUpTask;
import forms.CategoryForm;

@Service
@Transactional
public class CategoryService {

	// Managed repository ------------------------------
	@Autowired
	private CategoryRepository			categoryRepository;

	// Supporting services -----------------------------
	@Autowired
	private CategoryTranslationService	categoryTranslationService;

	@Autowired
	private CustomisationService		customisatinoService;

	@Autowired
	private FixUpTaskService			fixUpTaskService;


	// Constructors ------------------------------------
	public CategoryService() {
		super();
	}

	// Simple CRUD methods -----------------------------
	public Category findOne(final int categoryId) {
		Category result;

		result = this.categoryRepository.findOne(categoryId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Category> findAll() {
		Collection<Category> results;

		results = this.categoryRepository.findAll();

		return results;
	}

	public Collection<Category> findAllExceptDefault() {
		Collection<Category> results;

		results = this.categoryRepository.findAllExceptDefault();
		Assert.notNull(results);

		return results;
	}

	public Category create() {
		Category result;

		result = new Category();

		result.setCategoriesTranslations(Collections.<CategoryTranslation> emptySet());
		result.setDescendants(Collections.<Category> emptySet());

		return result;
	}

	public Category save(final Category category) {
		Assert.notNull(category);
		Assert.isTrue(category.getCategoriesTranslations().size() == this.customisatinoService.find().getLanguages().size() && this.validLanguages(category));
		this.checkValidParentCategory(category);

		Category root, result, parent_category, old_category, old_parent_category;

		root = this.findRootCategory();

		Assert.isTrue(!category.equals(root) && category.getParent() != null);

		result = this.categoryRepository.save(category);

		parent_category = result.getParent();
		if (category.getId() == 0)
			this.addDescendantCategory(parent_category, result);
		else {
			parent_category = result.getParent();
			// If category::parent changes, the hierarchy must change too
			old_category = category;
			if (!old_category.getParent().equals(parent_category)) {
				// The former category::parent stop to have as descendant to category
				old_parent_category = this.findOne(old_category.getParent().getId());
				this.removeDescendantCategory(old_parent_category, result);
				// The new category::parent has a category as new descendant
				this.addDescendantCategory(parent_category, result);
			}
		}

		return result;
	}

	public void delete(final Category category) {
		Assert.notNull(category);
		Assert.isTrue(category.getId() != 0);

		final Collection<Category> descendant_categories;
		final Category root = this.findRootCategory();
		Collection<FixUpTask> fixUpTasks;

		Assert.isTrue(!category.equals(root));

		descendant_categories = category.getDescendants();

		final Category parent_category = category.getParent();

		// Updating parent's attributes
		this.removeDescendantCategory(parent_category, category);
		if (!descendant_categories.isEmpty()) {
			this.addDescendantCategories(parent_category, descendant_categories);
			// Updating descendant categories's attributes
			for (final Category c : descendant_categories)
				this.updateParent(c, parent_category);
		}

		// Deleting category::categoriesTranslations
		for (final CategoryTranslation c : category.getCategoriesTranslations())
			this.categoryTranslationService.delete(c);

		// Update fixUpTask::category from fix-up task collection.
		fixUpTasks = this.fixUpTaskService.findFixUpTaskByCategory(category.getId());
		if (!fixUpTasks.isEmpty())
			for (final FixUpTask f : fixUpTasks)
				this.fixUpTaskService.updateCategory(f, category.getParent());

		this.categoryRepository.delete(category);
	}

	// Other business methods --------------------------
	public SortedMap<Integer, List<String>> categoriesByLanguage(final Collection<Category> categories, final String language) {
		SortedMap<Integer, List<String>> results;
		String name_category, name_parent_category = "";
		List<String> ls;
		Category parent;

		results = new TreeMap<>();

		for (final Category c : categories) {
			parent = c.getParent();
			ls = new ArrayList<String>();
			name_category = this.categoryTranslationService.findByLanguageCategory(c.getId(), language).getName();

			if (parent != null)
				name_parent_category = this.categoryTranslationService.findByLanguageCategory(parent.getId(), language).getName();

			ls.add(name_category);
			ls.add(name_parent_category);

			results.put(c.getId(), ls);
		}

		return results;

	}

	// Auxiliar methods --------------------------------
	public Category reconstruct(final CategoryForm categoryForm) {
		Category result, root;
		List<CategoryTranslation> categoriesTranslation;
		CategoryTranslation en_category, es_category, en_saved, es_saved;

		root = this.findRootCategory();
		result = null;

		if (categoryForm.getId() == 0) {
			result = this.create();
			categoriesTranslation = new ArrayList<CategoryTranslation>();

			en_category = this.categoryTranslationService.create();
			en_category.setLanguage("en");
			en_category.setName(categoryForm.getEn_name());

			es_category = this.categoryTranslationService.create();
			es_category.setLanguage("es");
			es_category.setName(categoryForm.getEs_name());

			en_saved = this.categoryTranslationService.save(en_category);
			es_saved = this.categoryTranslationService.save(es_category);

			categoriesTranslation.add(en_saved);
			categoriesTranslation.add(es_saved);

			result.setCategoriesTranslations(categoriesTranslation);
			result.setParent(categoryForm.getParent());
		} else if (categoryForm.getId() != root.getId()) {
			result = this.findOne(categoryForm.getId());

			en_saved = this.categoryTranslationService.findByLanguageCategory(categoryForm.getId(), "en");
			en_saved.setName(categoryForm.getEn_name());

			es_saved = this.categoryTranslationService.findByLanguageCategory(categoryForm.getId(), "es");
			es_saved.setName(categoryForm.getEs_name());

			result.setParent(categoryForm.getParent());
		} else
			result = root;

		return result;
	}

	public Category validateParent(final CategoryForm categoryForm, final BindingResult binding) {
		Category result;
		Category root;

		root = this.findRootCategory();

		result = categoryForm.getParent();
		if (categoryForm.getId() == root.getId() && result != null)
			binding.rejectValue("parent", "category.error.root", "Root category has not parent");
		else if (categoryForm.getId() != root.getId() && result == null)
			binding.rejectValue("parent", "category.error.null", "Must be null");

		return result;
	}

	public String validateName(final String nameAttribute, final String valueAttribute, final BindingResult binding) {
		String result;

		result = valueAttribute;
		if (result.equals("") || result.equals(null))
			binding.rejectValue(nameAttribute, "category.error.blank", "Must not be blank");

		return result;
	}

	public Category findRootCategory() {
		Category result;

		result = this.categoryRepository.findRootCategory();

		return result;
	}

	protected void updateParent(final Category category, final Category parent) {
		category.setParent(parent);
	}

	protected void addDescendantCategory(final Category category, final Category descendant) {
		Collection<Category> aux;

		aux = category.getDescendants();
		aux.add(descendant);

		category.setDescendants(aux);
	}

	protected void addDescendantCategories(final Category category, final Collection<Category> descendants) {
		Collection<Category> aux;

		aux = category.getDescendants();
		aux.addAll(descendants);

		category.setDescendants(aux);
	}

	protected void removeDescendantCategory(final Category category, final Category descendant) {
		Collection<Category> aux;

		aux = category.getDescendants();
		aux.remove(descendant);

		category.setDescendants(aux);
	}

	// Private methods ---------------------------------
	private void checkValidParentCategory(final Category category) {
		Collection<Category> all;

		all = this.findAllDescendantCategories(category);

		Assert.isTrue(!all.contains(category.getParent()));
	}

	public Collection<Category> findPossibleParentCategory(final Category category) {
		List<Category> results, descendantCategories;

		descendantCategories = new ArrayList<>(this.findAllDescendantCategories(category));
		results = new ArrayList<>(this.findAll());

		results.removeAll(descendantCategories);

		return results;
	}

	private Collection<Category> findAllDescendantCategories(final Category category) {
		List<Category> results;

		results = new ArrayList<Category>();

		results.addAll(category.getDescendants());
		for (final Category c : category.getDescendants())
			results.addAll(c.getDescendants());

		return results;
	}

	private boolean validLanguages(final Category category) {
		final Map<String, Integer> map;
		Collection<CategoryTranslation> categoriesTranslations;
		boolean result;
		final Collection<String> languages;
		Integer valor;

		map = new HashMap<String, Integer>();
		languages = this.customisatinoService.find().getLanguages();
		categoriesTranslations = category.getCategoriesTranslations();
		result = true;
		valor = 0;

		for (final String s : languages)
			map.put(s, 0);

		for (final CategoryTranslation ct : categoriesTranslations) {
			valor = map.get(ct.getLanguage());
			valor++;
			map.put(ct.getLanguage(), valor);
		}

		for (final Integer i : map.values())
			result = result && i.equals(1);

		return result;
	}

}
