package fi.csc.avaa.kuhiti.results;

import java.util.Collection;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import fi.csc.avaa.kuhiti.model.SubscriptionCost;
import fi.csc.avaa.tools.search.SearchBean;
import fi.csc.avaa.tools.search.result.ResultControlRow;
import fi.csc.avaa.tools.vaadin.language.Translator;

public class GridControlRow extends ResultControlRow<SubscriptionCost> {

	private static final long serialVersionUID = 1L;

	public GridControlRow(Translator translator, Label label, Button csvButton) {
		super(translator.localize("Search.FoundAmount"), label, csvButton);
	}

	@Override
	protected String getHtml(Collection<SubscriptionCost> searchResults,
			SearchBean queryBean) {
		// TODO Auto-generated method stub
		return null;
	}
}
