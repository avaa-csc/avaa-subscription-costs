package fi.csc.avaa.kuhiti.csv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fi.csc.avaa.tools.logging.AvaaLogger;

public abstract class XlsxBaseReader<T> {

	protected AvaaLogger log = new AvaaLogger(XlsxBaseReader.class.getName());
	protected XSSFWorkbook workbook = null;
	protected List<T> resultData = null;
	
	public XlsxBaseReader(String inputFileResourcePath) {
		try {
	        //Create Workbook instance holding reference to .xlsx file
			 workbook = new XSSFWorkbook(XlsxBaseReader.class.getResourceAsStream(inputFileResourcePath));
			 resultData = new ArrayList<>();
			 read();
		} catch (IOException e) {
			log.error("Unable to read .xlsx file from path " + inputFileResourcePath);
		}
	}

	protected abstract void read();
	
	public List<T> getResultData() {
		return resultData;
	}
	
}
