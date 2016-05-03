package fi.csc.avaa.kuhiti.csv;

import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import fi.csc.avaa.kuhiti.common.KuhitiConst;
import fi.csc.avaa.kuhiti.model.SubscriptionCost;
import fi.csc.avaa.tools.Const;
import fi.csc.avaa.tools.csv.SuperCSVDataReader;
import fi.csc.avaa.tools.csv.SuperCSVDataWriter;
import fi.csc.avaa.tools.logging.AvaaLogger;
import fi.csc.avaa.tools.vaadin.language.Translator;
import fi.csc.avaa.vaadin.tools.VaadinTools;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static fi.csc.avaa.kuhiti.common.KuhitiConst.*;

public class CSVTools {

	private static AvaaLogger log = new AvaaLogger(CSVTools.class.getName());
	private Translator translator;
	private boolean isAdmin;

	public CSVTools(Translator translator, boolean isAdmin) {
		this.translator = translator;
		this.isAdmin = isAdmin;
	}

	public Resource getSubsciptionCostCSVResource(Collection<SubscriptionCost> data, String[] headers, String downLoadFileName) {
		if (data != null && data.size() > 0) {
			return new StreamResource(() -> {
				String output = getSubscriptionCostCSVContents(data, headers);
				if (output != null) {
					return new ByteArrayInputStream(output.getBytes());
				}
				return new ByteArrayInputStream(Const.STRING_EMPTY.getBytes());
			}, translator.localize(downLoadFileName) + CSV_DOWNLOAD_FILE_EXTENTION);
		} else {
			log.warn("Nothing to write to csv file");
			return new StreamResource(null, Const.STRING_EMPTY);
		}
	}

	private String getSubscriptionCostCSVContents(Collection<SubscriptionCost> data, String[] headers) {

		List<Map<String, Object>> dataToWrite = new ArrayList<>();
		for (SubscriptionCost subscriptionCost : data) {
			final Map<String, Object> rowData = new HashMap<>();

			rowData.put(ORGTYPE_HEADER_KEY, translator.localize(TRANSLATION_KEY_PREFIX_ORGANISATION_TYPE + subscriptionCost.getOrgType()));
			rowData.put(ORGNAME_HEADER_KEY, translator.localize(TRANSLATION_KEY_PREFIX_ORGANISATION_NAME + subscriptionCost.getOrgName()));
			rowData.put(PUBLISHER_HEADER_KEY, subscriptionCost.getPublisher());
			rowData.put(YEAR_HEADER_KEY, subscriptionCost.getYear());
			rowData.put(PRICE_HEADER_KEY, subscriptionCost.getPrice());
			rowData.put(MATERIAL_HEADER_KEY, subscriptionCost.getMaterials());
			rowData.put(MATERIALTYPES_HEADER_KEY, getTranslations(subscriptionCost.getMaterialTypes(), translator, TRANSLATION_KEY_PREFIX_MATERIAL_TYPE));
			rowData.put(ACQUISITIONWAYS_HEADER_KEY, getTranslations(subscriptionCost.getAcquisitionWays(), translator, TRANSLATION_KEY_PREFIX_ACQUISITION_WAY));

			dataToWrite.add(rowData);
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		SuperCSVDataWriter dataWriter = new SuperCSVDataWriter(SuperCSVDataReader.Separator.COMMA);
		OutputStreamWriter writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
		try {
			writer.write('\uFEFF');
		} catch (IOException e) {
			VaadinTools.showError("Unable to write BOM to CSV file", null);
			log.error("Unable to write BOM to CSV file");
		}
		if (dataWriter.writeCSVData(new BufferedWriter(writer), dataToWrite, headers, translator)) {
			return new String(baos.toByteArray(), StandardCharsets.UTF_8);
		}
		VaadinTools.showError(translator.localize("Application.Error.ContactAvaa"), null);
		return null;
	}
}

