
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Category extends DomainEntity {

	// Constructors

	public Category() {
		super();
	}


	// Attributes ----------------------------------------------------------------------

	// Relationships ----------------------------------------------------------
	private Category						parent;
	private Collection<Category>			descendants;
	private Collection<CategoryTranslation>	categoriesTranslations;


	@NotNull
	@OneToMany
	public Collection<CategoryTranslation> getCategoriesTranslations() {
		return this.categoriesTranslations;
	}
	public void setCategoriesTranslations(final Collection<CategoryTranslation> categoriesTranslations) {
		this.categoriesTranslations = categoriesTranslations;
	}

	@Valid
	@ManyToOne(optional = true)
	public Category getParent() {
		return this.parent;
	}

	public void setParent(final Category parent) {
		this.parent = parent;
	}

	@NotNull
	@OneToMany(mappedBy = "parent")
	public Collection<Category> getDescendants() {
		return this.descendants;
	}

	public void setDescendants(final Collection<Category> descendants) {
		this.descendants = descendants;
	}
}
