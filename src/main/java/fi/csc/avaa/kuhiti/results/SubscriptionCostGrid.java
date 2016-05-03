/**
 * 
 */
package fi.csc.avaa.kuhiti.results;

import static fi.csc.avaa.kuhiti.results.SubscriptionCostGridBean.FieldName.ACQUISITION_WAYS;
import static fi.csc.avaa.kuhiti.results.SubscriptionCostGridBean.FieldName.MATERIALS;
import static fi.csc.avaa.kuhiti.results.SubscriptionCostGridBean.FieldName.MATERIAL_TYPES;
import static fi.csc.avaa.kuhiti.results.SubscriptionCostGridBean.FieldName.ORG_NAME;
import static fi.csc.avaa.kuhiti.results.SubscriptionCostGridBean.FieldName.ORG_TYPE;
import static fi.csc.avaa.kuhiti.results.SubscriptionCostGridBean.FieldName.PRICE;
import static fi.csc.avaa.kuhiti.results.SubscriptionCostGridBean.FieldName.PUBLISHER;
import static fi.csc.avaa.kuhiti.results.SubscriptionCostGridBean.FieldName.YEAR;

import java.text.DecimalFormat;
import java.util.Set;

import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.renderers.NumberRenderer;

import fi.csc.avaa.kuhiti.common.InfoWindow;
import fi.csc.avaa.kuhiti.common.KuhitiConst;
import fi.csc.avaa.kuhiti.model.SubscriptionCost;
import fi.csc.avaa.kuhiti.results.SubscriptionCostGridBean.FieldName;
import fi.csc.avaa.kuhiti.results.SubscriptionCostGridBean.GridColumnName;
import fi.csc.avaa.tools.StringTools;
import fi.csc.avaa.tools.vaadin.customcomponent.AvaaBaseGrid;
import fi.csc.avaa.tools.vaadin.language.Translator;

/**
 * @author jmlehtin
 *
 */
public class SubscriptionCostGrid extends AvaaBaseGrid<SubscriptionCostGridBean, SubscriptionCost> {

	private static final long serialVersionUID = 1L;

	public SubscriptionCostGrid(Translator translator) {
		super(translator, SubscriptionCostGridBean.class);
		init();
		setStyleName("subscriptioncost-grid");
		setColumnOrder(ORG_TYPE.getValue(), ORG_NAME.getValue(), PUBLISHER.getValue(), YEAR.getValue(), PRICE.getValue());
		setVisible(true);
	}

	@Override
	protected void addListeners() {
		addSelectionListener(e -> {
		    // Get the item of the selected row
			@SuppressWarnings("unchecked")
			SubscriptionCostGridBean selectedItemId = (SubscriptionCostGridBean) getSelectedRow();
			if(selectedItemId != null) {
		    	InfoWindow window = new InfoWindow(translator, 600, 500, true, false, true, selectedItemId);
				UI.getCurrent().addWindow(window);
				window.focus();
			}
		});
	}

	@Override
	protected void setColumns() {
		setGridColumn(ORG_TYPE);
		setGridColumn(ORG_NAME);
		setGridColumn(PUBLISHER);
		setGridColumn(YEAR);
		setGridColumn(PRICE);
		wrapperContainer.removeContainerProperty(MATERIALS.getValue());
		wrapperContainer.removeContainerProperty(MATERIAL_TYPES.getValue());
		wrapperContainer.removeContainerProperty(ACQUISITION_WAYS.getValue());
		
		setCellStyleGenerator(cellReference -> {
			if(cellReference != null && cellReference.getPropertyId() != null) {
				FieldName fName = FieldName.fromValue(String.valueOf(cellReference.getPropertyId()));
				if(fName != null) {
					switch (fName) {
					case PRICE:
						return "price";
					case PUBLISHER:
						return "publisher grid-cell-wrap";
					case YEAR:
						return "year";
					case ORG_NAME:
						return "orgname grid-cell-wrap";
					case ORG_TYPE:
						return "orgtype grid-cell-wrap";
					default:
						break;
					
					}
					return null;
				}
			}
			return null;
		});
	}
	
	@Override
	public boolean setModelsToContainer() {
		wrapperContainer.removeAllItems();
		if(models.size() > 0) {
			for(SubscriptionCost subscriptionCost : models) {
				SubscriptionCostGridBean gridItem = convertToGridBean(subscriptionCost);
				container.addBean(gridItem);
			}
			setGridHeightInPixels(650);
			setVisible(true);
			return true;
		}
		setVisible(false);
		return false;
	}
	
	private void setGridColumn(FieldName field) {
		Grid.Column gridCol = getColumn(field.getValue());
		GridColumnName column = GridColumnName.getValuefromName(field.name());
		gridCol.setHeaderCaption(translator.localize(column.getValue()));
		gridCol.setEditable(false);
		switch (column) {
		case PRICE:
			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			NumberRenderer decimalRenderer = new NumberRenderer(decimalFormat);
			gridCol.setRenderer(decimalRenderer);
			gridCol.setWidth(100);
			break;
		case PUBLISHER:
			gridCol.setMinimumWidth(300);
			gridCol.setMaximumWidth(500);
			break;
		case YEAR:
			gridCol.setWidth(80);
			break;
		case ORG_NAME:
			gridCol.setMaximumWidth(370);
			gridCol.setMinimumWidth(280);
			break;
		case ORG_TYPE:
			gridCol.setWidth(220);
			break;
		default:
			break;
		}
	}

	@Override
	protected void setGeneratedColumns() {
	}
	
	@Override
	protected SubscriptionCostGridBean convertToGridBean(SubscriptionCost sc) {
		String orgTypeKey = StringTools.getStringOrEmptyValue(sc.getOrgType());
		String orgType = StringTools.isEmptyOrNull(orgTypeKey) ? orgTypeKey : translator.localize(KuhitiConst.TRANSLATION_KEY_PREFIX_ORGANISATION_TYPE + orgTypeKey);
		String orgNameKey = StringTools.getStringOrEmptyValue(sc.getOrgName());
		String orgName = StringTools.isEmptyOrNull(orgNameKey) ? orgNameKey : translator.localize(KuhitiConst.TRANSLATION_KEY_PREFIX_ORGANISATION_NAME + orgNameKey);
		String publisher = StringTools.getStringOrEmptyValue(sc.getPublisher());
		Set<String> material = sc.getMaterials();
		Set<String> materialTypes = KuhitiConst.getTranslations(sc.getMaterialTypes(), translator, KuhitiConst.TRANSLATION_KEY_PREFIX_MATERIAL_TYPE);
		Set<String> acquisitionWays = KuhitiConst.getTranslations(sc.getAcquisitionWays(), translator, KuhitiConst.TRANSLATION_KEY_PREFIX_ACQUISITION_WAY);
		int year = sc.getYear();
		double price = sc.getPrice();
		return new SubscriptionCostGridBean(orgType, orgName, publisher, year, price, material, materialTypes, acquisitionWays);
	}

	@Override
	protected void setCustomHeaders() {
		GridColumnName column1 = GridColumnName.getValuefromName(ORG_TYPE.name());
		String orgTypeHtml = "<div class='v-label v-widget v-has-width'>" + translator.localize(column1.getValue()) + "</div>";
		getDefaultHeaderRow().getCell(ORG_TYPE.getValue()).setHtml(orgTypeHtml);
		
		GridColumnName column2 = GridColumnName.getValuefromName(ORG_NAME.name());
		String orgNameHtml = "<div class='v-label v-widget v-has-width'>" + translator.localize(column2.getValue()) + "</div>";
		getDefaultHeaderRow().getCell(ORG_NAME.getValue()).setHtml(orgNameHtml);
		
		GridColumnName column3 = GridColumnName.getValuefromName(PUBLISHER.name());
		String publisherHtml = "<div class='v-label v-widget v-has-width'>" + translator.localize(column3.getValue()) + "</div>";
		getDefaultHeaderRow().getCell(PUBLISHER.getValue()).setHtml(publisherHtml);
		
		GridColumnName column4 = GridColumnName.getValuefromName(YEAR.name());
		String yearHtml = "<div class='v-label v-widget v-has-width'>" + translator.localize(column4.getValue()) + "</div>";
		getDefaultHeaderRow().getCell(YEAR.getValue()).setHtml(yearHtml);
		
		GridColumnName column5 = GridColumnName.getValuefromName(PRICE.name());
		String priceHtml = "<div class='v-label v-widget v-has-width'>" + translator.localize(column5.getValue()) + "</div>";
		getDefaultHeaderRow().getCell(PRICE.getValue()).setHtml(priceHtml);
	}

}
