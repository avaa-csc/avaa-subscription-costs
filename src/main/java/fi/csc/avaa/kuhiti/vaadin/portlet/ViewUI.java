/**
 * 
 */
package fi.csc.avaa.kuhiti.vaadin.portlet;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;

import fi.csc.avaa.kuhiti.ViewContent;
import fi.csc.avaa.vaadin.portlet.AvaaUI;
import fi.csc.avaa.vaadin.tools.VaadinTools;

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
		translator.setDefaultLocale(VaadinSession.getCurrent().getLocale());
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
