
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CategoryTranslationRepository;
import domain.CategoryTranslation;

@Service
@Transactional
public class CategoryTranslationService {

	// Managed repository ------------------------------
	@Autowired
	private CategoryTranslationRepository	categoryTranslationRepository;

	// Supporting services -----------------------------
	@Autowired
	private CustomisationService			customisationService;


	// Constructors ------------------------------------
	public CategoryTranslationService() {
		super();
	}

	// Simple CRUD methods -----------------------------
	protected CategoryTranslation findOne(final int categoryTranslationId) {
		CategoryTranslation result;

		result = this.categoryTranslationRepository.findOne(categoryTranslationId);
		Assert.notNull(result);

		return result;
	}

	protected Collection<CategoryTranslation> findAll() {
		Collection<CategoryTranslation> results;

		results = this.categoryTranslationRepository.findAll();

		return results;
	}

	protected CategoryTranslation create() {
		CategoryTranslation result;

		result = new CategoryTranslation();

		return result;
	}

	protected CategoryTranslation save(final CategoryTranslation categoryTranslation) {
		Assert.notNull(categoryTranslation);
		Assert.isTrue(this.validLanguage(categoryTranslation));

		CategoryTranslation result;

		result = this.categoryTranslationRepository.save(categoryTranslation);

		return result;
	}

	protected void delete(final CategoryTranslation categoryTranslation) {
		Assert.notNull(categoryTranslation);
		Assert.isTrue(categoryTranslation.getId() != 0);

		this.categoryTranslationRepository.delete(categoryTranslation);
	}

	// Other business methods --------------------------
	public CategoryTranslation findByLanguageCategory(final int categoryId, final String language) {
		CategoryTranslation result;

		result = this.categoryTranslationRepository.findByLanguageCategory(categoryId, language);

		return result;
	}

	// Private methods ---------------------------------
	private boolean validLanguage(final CategoryTranslation categoryTranslation) {
		Collection<String> languages;

		languages = this.customisationService.find().getLanguages();

		return languages.contains(categoryTranslation.getLanguage());
	}

}
