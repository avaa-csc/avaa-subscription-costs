/**
 * 
 */
package fi.csc.avaa.kuhiti.common;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

import fi.csc.avaa.tools.vaadin.customcomponent.ItemDetailsWindow;
import fi.csc.avaa.tools.vaadin.language.Translator;

/**
 * @author jmlehtin
 *
 */
public class HelpWindow extends ItemDetailsWindow {

	private static final long serialVersionUID = 1L;

	/**
	 * @param translator
	 * @param widthInPixels
	 * @param heightInPixels
	 * @param isModal
	 * @param isDraggable
	 * @param isClosable
	 */
	public HelpWindow(Translator translator, int widthInPixels,
			int heightInPixels, boolean isModal, boolean isDraggable,
			boolean isClosable) {
		super(translator, widthInPixels, heightInPixels, isModal, isDraggable,
				isClosable);
		baseLayout.setSpacing(true);
		baseLayout.setMargin(true);
		init();
	}

	/* (non-Javadoc)
	 * @see fi.csc.avaa.tools.vaadin.customcomponent.ItemDetailsWindow#init()
	 */
	@Override
	protected void init() {
		Label helpLabel = new Label(FontAwesome.QUESTION_CIRCLE.getHtml(), ContentMode.HTML);
		baseLayout.addComponent(helpLabel);
		baseLayout.addComponent(new Label(translator.localize("Help.Text"), ContentMode.HTML));
	}

}
