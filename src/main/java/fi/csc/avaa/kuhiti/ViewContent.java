/**
 * 
 */
package fi.csc.avaa.kuhiti;

import java.util.Collection;
import java.util.Locale;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import fi.csc.avaa.kuhiti.common.KuhitiCache;
import fi.csc.avaa.kuhiti.model.SubscriptionCost;
import fi.csc.avaa.tools.vaadin.language.Translator;
import fi.csc.avaa.vaadin.tools.VaadinTools;

/**
 * @author jmlehtin
 *
 */
public class ViewContent extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private HorizontalLayout progLayout = new HorizontalLayout();
	private ProgressBar pBar;

	private Translator translator;
	private Locale locale;
	private VerticalLayout mainLayout;
	
	public ViewContent(VaadinRequest request, Translator translator) {
		this.translator = translator;
		this.locale = this.translator.getDefaultLocale();
		JavaScript.eval("window.locale = '" + Translator.getLocaleStr(this.locale) + "';");
		progLayout.setSpacing(true);
		pBar = new ProgressBar();
		pBar.setIndeterminate(true);
		pBar.setEnabled(false);
		Label status = new Label(this.translator.localize("Application.Loading"));
		status.setWidth(100, Unit.PERCENTAGE);
		progLayout.addComponent(status);
		progLayout.addComponent(pBar);
		progLayout.setComponentAlignment(status, Alignment.MIDDLE_CENTER);
		progLayout.setComponentAlignment(pBar, Alignment.MIDDLE_CENTER);
		addComponent(progLayout);
		setComponentAlignment(progLayout, Alignment.MIDDLE_CENTER);
		setWidth(100, Unit.PERCENTAGE);
		setHeight(600, Unit.PIXELS);
		setSpacing(true);
		UI.getCurrent().setPollInterval(100);
		Worker worker = new Worker();
		worker.start();
	}

	public void createMainContent() {
		setHeightUndefined();
		mainLayout = new VerticalLayout();
		mainLayout.setMargin(false);
		mainLayout.setHeightUndefined();
		mainLayout.addComponent(new MainView(translator));
		UI.getCurrent().setContent(mainLayout);
	}

	private class Worker extends Thread {

		@Override
		public void run() {
			UI.getCurrent().getSession().getLockInstance().lock();
			KuhitiCache.initCache();
			Collection<SubscriptionCost> allSubscriptionCosts = KuhitiCache.getDataCache();
			if (allSubscriptionCosts != null && allSubscriptionCosts.size() > 0) {
				removeComponent(progLayout);
				createMainContent();
			} else {
				showError();
			}
			removeComponent(progLayout);
			UI.getCurrent().setPollInterval(-1);
			UI.getCurrent().getSession().getLockInstance().unlock();
		}

		private void showError() {
			removeComponent(progLayout);
			VaadinTools.showError("Error occurred while loading the application", "Please contact AVAA administrators");
		}
	}
}



