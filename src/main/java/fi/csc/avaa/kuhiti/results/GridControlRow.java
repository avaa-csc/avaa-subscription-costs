package fi.csc.avaa.kuhiti.results;

import java.util.Collection;

import com.vaadin.ui.Button;
import fi.csc.avaa.kuhiti.model.SubscriptionCost;
import fi.csc.avaa.tools.search.SearchBean;
import fi.csc.avaa.tools.search.result.ResultControlSubscriptionCostsRow;
import fi.csc.avaa.tools.vaadin.language.Translator;

public class GridControlRow extends ResultControlSubscriptionCostsRow<SubscriptionCost> {

	private static final long serialVersionUID = 1L;

	public GridControlRow(Translator translator, Button button, boolean bottomMargin) {
		super(translator, button, bottomMargin);
	}

	@Override
	protected String getHtml(Collection<SubscriptionCost> searchResults,
			SearchBean queryBean) {
		// TODO Auto-generated method stub
		return null;
	}

}
