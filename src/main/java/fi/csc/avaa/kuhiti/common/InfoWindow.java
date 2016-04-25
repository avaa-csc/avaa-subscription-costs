/**
 * 
 */
package fi.csc.avaa.kuhiti.common;

import java.util.stream.Collectors;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

import fi.csc.avaa.kuhiti.results.SubscriptionCostGridBean;
import fi.csc.avaa.tools.vaadin.customcomponent.ItemDetailsWindow;
import fi.csc.avaa.tools.vaadin.language.Translator;

/**
 * @author jmlehtin
 *
 */
public class InfoWindow extends ItemDetailsWindow {

	private static final long serialVersionUID = 1L;
	
	private SubscriptionCostGridBean subscriptionCostItem;

	/**
	 * @param translator
	 * @param widthInPixels
	 * @param heightInPixels
	 * @param isModal
	 * @param isDraggable
	 * @param isClosable
	 */
	public InfoWindow(Translator translator, int widthInPixels,
			int heightInPixels, boolean isModal, boolean isDraggable,
			boolean isClosable, SubscriptionCostGridBean subscriptionCostItem) {
		super(translator, widthInPixels, heightInPixels, isModal, isDraggable,
				isClosable);
		baseLayout.setMargin(true);
		this.subscriptionCostItem = subscriptionCostItem;
		init();
	}

	/* (non-Javadoc)
	 * @see fi.csc.avaa.tools.vaadin.customcomponent.ItemDetailsWindow#init()
	 */
	@Override
	protected void init() {
		String orgTypeLabelStr = "<p><span class='bold'>" + translator.localize("Description.OrganisationType") + ":</span><br/>" + (subscriptionCostItem.getOrgType() == null ? translator.localize("Application.NotKnown") : subscriptionCostItem.getOrgType()) + "</p>";
		String orgNameLabelStr = "<p><span class='bold'>" + translator.localize("Description.OrganisationName") + ":</span><br/>" + (subscriptionCostItem.getOrgName() == null ? translator.localize("Application.NotKnown") : subscriptionCostItem.getOrgName()) + "</p>";
		String publisherLabelStr = "<p><span class='bold'>" + translator.localize("Description.Publisher") + ":</span><br/>" + (subscriptionCostItem.getPublisher() == null ? translator.localize("Application.NotKnown") : subscriptionCostItem.getPublisher()) + "</p>";
		String yearLabelStr = "<p><span class='bold'>" + translator.localize("Description.Year") + ":</span><br/>" + (subscriptionCostItem.getYear() == null ? translator.localize("Application.NotKnown") : subscriptionCostItem.getYear()) + "</p>";
		String priceLabelStr = "<p><span class='bold'>" + translator.localize("Description.Price") + ":</span><br/>" + subscriptionCostItem.getPrice() + " " + translator.localize("Description.Euro") + "</p>";
		String materialLabelStr = "<p><span class='bold'>" + translator.localize("Description.Material") + ":</span><br/>" + (subscriptionCostItem.getMaterials() == null ? translator.localize("Application.NotKnown") : subscriptionCostItem.getMaterials().stream().collect(Collectors.joining(", "))) + "</p>";
		String materialTypeLabelStr = "<p><span class='bold'>" + translator.localize("Description.MaterialType") + ":</span><br/>" + (subscriptionCostItem.getMaterialTypes() == null ? translator.localize("Application.NotKnown") : subscriptionCostItem.getMaterialTypes().stream().collect(Collectors.joining(", "))) + "</p>";
		String acquisitionWayLabelStr = "<p><span class='bold'>" + translator.localize("Description.AcquisitionWay") + ":</span><br/>" + (subscriptionCostItem.getAcquisitionWays() == null ? translator.localize("Application.NotKnown") : subscriptionCostItem.getAcquisitionWays().stream().collect(Collectors.joining(", "))) + "</p>";
		baseLayout.addComponent(new Label(orgTypeLabelStr + orgNameLabelStr + publisherLabelStr + yearLabelStr + priceLabelStr + materialLabelStr + materialTypeLabelStr + acquisitionWayLabelStr, ContentMode.HTML));
	}

}
