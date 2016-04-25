package fi.csc.avaa.kuhiti.results;

import java.util.Collection;

import fi.csc.avaa.kuhiti.model.SubscriptionCost;
import fi.csc.avaa.tools.search.SearchBean;
import fi.csc.avaa.tools.search.result.ResultControlRow;
import fi.csc.avaa.tools.vaadin.language.Translator;

public class GridControlRow extends ResultControlRow<SubscriptionCost> {

	private static final long serialVersionUID = 1L;

	public GridControlRow(Translator translator, boolean doPrint,
			boolean doCsv, boolean bottomMargin) {
		super(translator, doPrint, doCsv, bottomMargin);
	}

	@Override
	protected String getHtml(Collection<SubscriptionCost> searchResults,
			SearchBean queryBean) {
		// TODO Auto-generated method stub
		return null;
	}

}
