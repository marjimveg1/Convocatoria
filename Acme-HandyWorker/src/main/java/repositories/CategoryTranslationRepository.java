
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.CategoryTranslation;

@Repository
public interface CategoryTranslationRepository extends JpaRepository<CategoryTranslation, Integer> {

	@Query("select ct from Category c join c.categoriesTranslations ct where c.id=?1 and ct.language=?2")
	CategoryTranslation findByLanguageCategory(int categoryId, String language);

}
