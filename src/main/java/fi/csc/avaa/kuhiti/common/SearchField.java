package fi.csc.avaa.kuhiti.common;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TextField;

import fi.csc.avaa.tools.Const;
import fi.csc.avaa.tools.vaadin.language.Translator;
import fi.csc.avaa.vaadin.tools.VaadinTools;

public class SearchField extends CustomComponent {
	
	private static final long serialVersionUID = 1L;
	private TextField searchField;
	
	public SearchField(String id, Translator translator, float pixelWidth) {
		searchField = VaadinTools.createTextField(pixelWidth, null, 0, true, null);
		searchField.setId(id);
		searchField.setInputPrompt(translator.localize("Search.Textfield.Prompt"));
		searchField.setTextChangeTimeout(1000);
		searchField.addTextChangeListener(e -> {
			searchField.setValue(e.getText());
			fireComponentEvent();
		});
		setCompositionRoot(searchField);
	}
	
	public String getValue() {
		return searchField.getValue();
	}
	
	public void reset() {
		searchField.setValue(Const.STRING_EMPTY);
	}
}
