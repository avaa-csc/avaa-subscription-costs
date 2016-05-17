/**
 * 
 */
package fi.csc.avaa.kuhiti.vaadin.portlet;

import javax.portlet.PortletRequest;
import javax.servlet.annotation.WebServlet;

import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.model.PortletPreferences;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;

import fi.csc.avaa.kuhiti.ViewContent;
import fi.csc.avaa.kuhiti.common.KuhitiConst;
import fi.csc.avaa.tools.Const;
import fi.csc.avaa.tools.StringTools;
import fi.csc.avaa.tools.vaadin.language.LanguageConst;
import fi.csc.avaa.vaadin.portlet.AvaaUI;
import fi.csc.avaa.vaadin.tools.VaadinTools;

import static com.liferay.portal.kernel.util.JavaConstants.JAVAX_PORTLET_REQUEST;
import static fi.csc.avaa.kuhiti.common.KuhitiConst.*;
import static fi.csc.avaa.tools.Const.STRING_EMPTY;
import static fi.csc.avaa.tools.vaadin.language.LanguageConst.LOCALE_FI;

/**
 * @author jmlehtin
 *
 */
@Theme("valo")
public class ViewUI extends AvaaUI {

	@SuppressWarnings("serial")
	@WebServlet(urlPatterns = {"/VAADIN/*"}, asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = ViewUI.class)
	public static class Servlet extends VaadinServlet {}

	private static final long serialVersionUID = 1L;
	private ViewContent view;

	@Override
	protected void init(VaadinRequest request) {
		super.init(request);
		String uriFragment = Page.getCurrent().getUriFragment();
		if(URI_FRAGMENT_LANG_FI.equals(uriFragment)) {
			translator.setDefaultLocale(LOCALE_FI);
		} else {
			translator.setDefaultLocale(VaadinSession.getCurrent().getLocale());
		}
		if(!StringTools.isEmptyOrNull(uriFragment)) {
			Page.getCurrent().setUriFragment(STRING_EMPTY);
		}
		Page.getCurrent().setTitle(translator.localize("Application.Title"));
		try {
			view = new ViewContent(request, translator);
			setContent(view);
		} catch (Exception e) {
			setContent(VaadinTools.createErrorLayout(e, translator.getDefaultLocaleStr()));
		}
	}

	@Override
	protected void refresh(VaadinRequest request) {
		super.refresh(request);
		init(request);
	}
}
