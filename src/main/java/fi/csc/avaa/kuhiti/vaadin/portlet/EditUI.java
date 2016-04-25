/**
 * 
 */
package fi.csc.avaa.kuhiti.vaadin.portlet;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.util.JavaConstants;
import com.vaadin.annotations.Theme;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import fi.csc.avaa.kuhiti.common.KuhitiConst;
import fi.csc.avaa.tools.vaadin.language.LanguageConst;
import fi.csc.avaa.vaadin.tools.VaadinTools;

/**
 * @author jmlehtin
 *
 */
@Theme("valo")
public class EditUI extends UI {
	private static final long serialVersionUID = 1L;

	@Override
	protected void init(VaadinRequest request) {
		final PortletPreferences portletPrefs = ((PortletRequest) request.getAttribute(JavaConstants.JAVAX_PORTLET_REQUEST)).getPreferences();
		
		VerticalLayout editLayout = new VerticalLayout();
		editLayout.setMargin(true);
		editLayout.setSpacing(true);
		
		OptionGroup langSelect = new OptionGroup("Choose application language");
		langSelect.setMultiSelect(false);
		langSelect.addItem(LanguageConst.getLocaleStrFromLocale(LanguageConst.LOCALE_FI));
		langSelect.addItem(LanguageConst.getLocaleStrFromLocale(LanguageConst.LOCALE_EN));
		editLayout.addComponent(langSelect);
		
		String localeStr =  portletPrefs.getValue(KuhitiConst.PORTLET_PREFERENCE_KEY_LANGUAGE, null);
		if(localeStr != null) {
			langSelect.setValue(localeStr);
		}
		
		Button storeBtn = VaadinTools.createButton("Save", FontAwesome.SAVE, null, null);
		storeBtn.addClickListener(e -> {
			if(langSelect.getValue() == null) {
				VaadinTools.showWarning("Please select language", null);
			} else {
				try {
					portletPrefs.setValue(KuhitiConst.PORTLET_PREFERENCE_KEY_LANGUAGE, langSelect.getValue().toString());
					portletPrefs.store();
					VaadinTools.showNotification("Language selection saved!", null);
				} catch (Exception e1) {
					VaadinTools.showError("Something went wrong when saving the language preference", null);
					e1.printStackTrace();
				}
			}
		});
		editLayout.addComponent(storeBtn);
		setContent(editLayout);
	}
}
