/*
 * StringToCategoryTranslationConverter.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.CategoryTranslationRepository;
import domain.CategoryTranslation;

@Component
@Transactional
public class StringToCategoryTranslationConverter implements Converter<String, CategoryTranslation> {

	@Autowired
	CategoryTranslationRepository	categoryTranslationRepository;


	@Override
	public CategoryTranslation convert(final String text) {
		CategoryTranslation result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.categoryTranslationRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
