package fi.csc.avaa.kuhiti.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import fi.csc.avaa.tools.vaadin.language.Translator;

public final class KuhitiConst {

	public static final String APPLICATION_NAME = "PUBLISHER_COSTS";
	public static final String DOWNLOAD_FILE_FULL_EXCEL = "FULL_EXCEL";
	public static final String DOWNLOAD_FILE_FULL_CSV = "FULL_CSV";
	public static final String DOWNLOAD_FILE_FILTERED_CSV = "FILTERED_CSV";

//	public static final String DATAFILE_PATH = "/Publisher_Costs_Test_Data.xlsx";
	public static final String DATAFILE_PATH = "/20160613 Kustantajahintatiedot_koottu_v1.0.xlsx";

	public static final String TRANSLATION_KEY_PREFIX_ORGANISATION_TYPE = "Description.OrganisationType.";
	public static final String TRANSLATION_KEY_PREFIX_ORGANISATION_NAME = "Description.OrganisationName.";
	public static final String TRANSLATION_KEY_PREFIX_MATERIAL_TYPE = "Description.MaterialType.";
	public static final String TRANSLATION_KEY_PREFIX_ACQUISITION_WAY = "Description.AcquisitionWay.";

	public static final String TRANSLATION_KEY_NOT_CHOSEN = "Application.NotChosen";

	public static final String ORGTYPE_HEADER_KEY = "Description.OrganisationType";
	public static final String ORGNAME_HEADER_KEY = "Description.OrganisationName";
	public static final String PUBLISHER_HEADER_KEY = "Description.Publisher";
	public static final String YEAR_HEADER_KEY = "Description.Year";
	public static final String PRICE_HEADER_KEY = "Description.Price";
	public static final String MATERIAL_HEADER_KEY = "Description.Material";
	public static final String MATERIALTYPES_HEADER_KEY = "Description.MaterialType";
	public static final String ACQUISITIONWAYS_HEADER_KEY = "Description.AcquisitionWay";

	public static final String XLSL_DOWNLOAD_FILE_EXTENTION = ".xlsx";
	public static final String CSV_DOWNLOAD_FILE_EXTENTION = ".csv";
	public static final String DOWNLOAD_TEXT_FILENAME = "Download.Text.Filename";

	public static final String[] HEADERS_LIST = {ORGNAME_HEADER_KEY, ORGTYPE_HEADER_KEY, PUBLISHER_HEADER_KEY,
			YEAR_HEADER_KEY, PRICE_HEADER_KEY, MATERIAL_HEADER_KEY, MATERIALTYPES_HEADER_KEY, ACQUISITIONWAYS_HEADER_KEY};
	public static final String URI_FRAGMENT_LANG_FI = "fi";

	public static Set<String> getTranslations(Set<String> keys, Translator translator, String keyPrefix) {
		if (keys == null) {
			return null;
		}
		Set<String> translations = new TreeSet<>();
		keys.stream().forEach(k -> translations.add(translator.localize(keyPrefix + k)));
		return translations;
	}

	public static List<String> getTranslations(List<String> keys, Translator translator, String keyPrefix) {
		if (keys == null) {
			return null;
		}
		List<String> translations = new ArrayList<>();
		keys.stream().forEach(k -> translations.add(translator.localize(keyPrefix + k)));
		return translations;
	}
}
