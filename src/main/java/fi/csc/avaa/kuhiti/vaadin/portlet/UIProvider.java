/**
 * 
 */
package fi.csc.avaa.kuhiti.vaadin.portlet;

import java.util.Locale;

import com.vaadin.ui.UI;

import fi.csc.avaa.vaadin.portlet.AvaaUIProvider;

/**
 * @author jmlehtin
 *
 */
public class UIProvider extends AvaaUIProvider {

	private static final long serialVersionUID = 1L;

	@Override
	protected Class<? extends UI> getEditModeUI(Locale loc) {
		return EditUI.class;
	}

	@Override
	protected Class<? extends UI> getHelpModeUI(Locale loc) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Class<? extends UI> getViewModeUI(Locale loc) {
		return ViewUI.class;
	}

}
