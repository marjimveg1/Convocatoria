/*
 * CustomerController.java
 * 
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.administrator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import services.CategoryTranslationService;
import controllers.AbstractController;
import domain.Category;
import forms.CategoryForm;

@Controller
@RequestMapping("/category/administrator")
public class CategoryAdministratorController extends AbstractController {

	@Autowired
	private CategoryService				categoryService;

	@Autowired
	private CategoryTranslationService	categoryTranslationService;


	// Constructors -----------------------------------------------------------

	public CategoryAdministratorController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int categoryId, final Locale locale) {
		ModelAndView result;
		Category category, root, parent;
		String language, name_category, name_parent_category = "";
		SortedMap<Integer, List<String>> mapa;
		final List<String> ls = new ArrayList<>();

		try {
			category = this.categoryService.findOne(categoryId);
			parent = category.getParent();
			root = this.categoryService.findRootCategory();

			language = locale.getLanguage();

			mapa = this.categoryService.categoriesByLanguage(category.getDescendants(), language);

			if (!category.equals(root))
				name_parent_category = this.categoryTranslationService.findByLanguageCategory(parent.getId(), language).getName();

			name_category = this.categoryTranslationService.findByLanguageCategory(categoryId, language).getName();
			ls.add(name_category);
			ls.add(name_parent_category);

			mapa.put(categoryId, ls);

			result = new ModelAndView("category/display");
			result.addObject("category", category);
			result.addObject("mapa", mapa);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:list.do");
		}

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final Locale locale) {
		ModelAndView result;
		Collection<Category> categories;
		SortedMap<Integer, List<String>> mapa;
		String language;
		language = locale.getLanguage();

		categories = this.categoryService.findAll();
		mapa = this.categoryService.categoriesByLanguage(categories, language);

		result = new ModelAndView("category/list");
		result.addObject("requestURI", "category/administrator/list.do");
		result.addObject("mapa", mapa);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(final Locale locale) {
		final ModelAndView result;
		CategoryForm categoryForm;

		categoryForm = new CategoryForm();

		result = this.createEditModelAndView(categoryForm, locale);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int categoryId, final Locale locale) {
		final ModelAndView result;
		Category category;
		CategoryForm categoryForm;
		String en_name, es_name;

		category = this.categoryService.findOne(categoryId);

		en_name = this.categoryTranslationService.findByLanguageCategory(categoryId, "en").getName();
		es_name = this.categoryTranslationService.findByLanguageCategory(categoryId, "es").getName();

		categoryForm = new CategoryForm();

		categoryForm.setId(categoryId);
		categoryForm.setParent(category.getParent());
		categoryForm.setEn_name(en_name);
		categoryForm.setEs_name(es_name);

		result = this.createEditModelAndView(categoryForm, locale);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final CategoryForm categoryForm, final BindingResult binding, final Locale locale) {
		ModelAndView result;
		Category category;

		this.categoryService.validateParent(categoryForm, binding);
		this.categoryService.validateName("en_name", categoryForm.getEn_name(), binding);
		this.categoryService.validateName("es_name", categoryForm.getEs_name(), binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(categoryForm, locale);
		else
			try {
				category = this.categoryService.reconstruct(categoryForm);
				this.categoryService.save(category);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(categoryForm, locale, "category.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final CategoryForm categoryForm, final BindingResult binding, final Locale locale) {
		ModelAndView result;
		Category category;

		try {
			category = this.categoryService.findOne(categoryForm.getId());
			this.categoryService.delete(category);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(categoryForm, locale, "category.commit.error");
		}

		return result;
	}

	// Arcillary methods --------------------------
	protected ModelAndView createEditModelAndView(final CategoryForm categoryForm, final Locale locale) {
		ModelAndView result;

		result = this.createEditModelAndView(categoryForm, locale, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final CategoryForm categoryForm, final Locale locale, final String messageCode) {
		ModelAndView result;
		SortedMap<Integer, List<String>> parents;
		Collection<Category> categories;
		Category category;

		categories = this.categoryService.findAll();

		if (categoryForm.getId() != 0) {
			category = this.categoryService.findOne(categoryForm.getId());
			categories = this.categoryService.findPossibleParentCategory(category);
			categories.remove(category);
		} else
			categories = this.categoryService.findAll();

		parents = this.categoryService.categoriesByLanguage(categories, locale.getLanguage());

		result = new ModelAndView("category/edit");
		result.addObject("categoryForm", categoryForm);
		result.addObject("parents", parents);
		result.addObject("messageCode", messageCode);

		return result;
	}

}
