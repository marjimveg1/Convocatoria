
package forms;

import domain.Category;

public class CategoryForm {

	// Attributes -----------------------------
	private int			id;
	private Category	parent;
	private String		en_name;
	private String		es_name;


	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public Category getParent() {
		return this.parent;
	}

	public void setParent(final Category parent) {
		this.parent = parent;
	}

	public String getEn_name() {
		return this.en_name;
	}

	public void setEn_name(final String en_name) {
		this.en_name = en_name;
	}

	public String getEs_name() {
		return this.es_name;
	}

	public void setEs_name(final String es_name) {
		this.es_name = es_name;
	}

}
