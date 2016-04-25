package fi.csc.avaa.kuhiti.csv;

import java.util.Iterator;
import java.util.TreeSet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import fi.csc.avaa.kuhiti.common.KuhitiCache;
import fi.csc.avaa.kuhiti.model.SubscriptionCost;

public class KuhitiDataReader extends XlsxBaseReader<SubscriptionCost> {

	public KuhitiDataReader(String inputFileResourcePath) {
		super(inputFileResourcePath);
	}

	@Override
	protected void read() {
		XSSFSheet sheet = workbook.getSheetAt(0);
		if(sheet != null) {
			Iterator<Row> rowIterator = sheet.iterator();
			Row firstRow = rowIterator.next();
			short minColIdx = firstRow.getFirstCellNum();
			short maxColIdx = firstRow.getLastCellNum();
			
			while(rowIterator.hasNext()) {
				Row row = rowIterator.next();
				SubscriptionCost subscriptionCost = new SubscriptionCost();
				for(short colIdx = minColIdx; colIdx < maxColIdx; colIdx++) {
					Cell cell  = row.getCell(colIdx);
					if(colIdx == minColIdx) {
						if(cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
							String orgType = cell.getStringCellValue();
							subscriptionCost.setOrgType(orgType);
							if(!KuhitiCache.allOrgTypes.contains(orgType)) {
								KuhitiCache.allOrgTypes.add(orgType);
							}
						} else {
							if(cell != null) {
								log.error("Error reading excel row org type column: " + cell.toString() + ". Skipping row.");
							} else {
								log.error("Error reading excel row org type column. It cannot be empty!. Skipping row.");
							}
							subscriptionCost = null;
							break;
						}
					} else if(colIdx == minColIdx+1) {
						if(cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
							String orgName = cell.getStringCellValue();
							subscriptionCost.setOrgName(orgName);
							if(!KuhitiCache.allOrgNames.contains(orgName)) {
								KuhitiCache.allOrgNames.add(orgName);
							}
						} else {
							if(cell != null) {
								log.error("Error reading excel row org name column: " + cell.toString() + ". Skipping row.");
							} else {
								log.error("Error reading excel row org name column. It cannot be empty!. Skipping row.");
							}
							subscriptionCost = null;
							break;
						}
					} else if(colIdx == minColIdx+2) {
						if(cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
							subscriptionCost.setPublisher(cell.getStringCellValue());
						} else {
							if(cell != null) {
								log.error("Error reading excel row publisher column: " + cell.toString() + ". Skipping row.");
							} else {
								log.error("Error reading excel row publisher column. It cannot be empty!. Skipping row.");
							}
							subscriptionCost = null;
							break;
						}
					}
					 else if(colIdx == minColIdx+3) {
					 	if(cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					 		Integer vuosi = (int) cell.getNumericCellValue();
							subscriptionCost.setYear(vuosi);
							if(!KuhitiCache.allYears.contains(vuosi)) {
								KuhitiCache.allYears.add(vuosi);
							}
						} else {
							if(cell != null) {
								log.error("Error reading excel row year column: " + cell.toString() + ". Skipping row.");
							} else {
								log.error("Error reading excel row year column. It cannot be empty!. Skipping row.");
							}
							subscriptionCost = null;
							break;
						}
					} else if(colIdx == minColIdx+4) {
						if(cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							subscriptionCost.setPrice(cell.getNumericCellValue());
						} else {
							if(cell != null) {
								log.debug("Error reading excel row price column: " + cell.toString() + ". Skipping row.");
							} else {
								log.debug("Error reading excel row price column. It is empty. Skipping row.");
							}
							subscriptionCost = null;
							break;
						}
					} else if(colIdx == minColIdx+5) {
						if(cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
							String materialsStr = cell.getStringCellValue();
							TreeSet<String> materials = getSeparateFieldValues(materialsStr);
							subscriptionCost.setMaterials(materials);
						}
					} else if(colIdx == minColIdx+6) {
						if(cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
							String materialTypesStr = cell.getStringCellValue();
							TreeSet<String> materialTypes = getSeparateFieldValues(materialTypesStr);
							subscriptionCost.setMaterialTypes(materialTypes);
							KuhitiCache.allMaterialTypes.addAll(materialTypes);
						}
					} else if(colIdx == minColIdx+7) {
						if(cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
							String acquisitionWaysStr = cell.getStringCellValue();
							TreeSet<String> acquisitionWays = getSeparateFieldValues(acquisitionWaysStr);
							subscriptionCost.setAcquisitionWays(acquisitionWays);
							KuhitiCache.allAcquisitionWays.addAll(acquisitionWays);
						}
					}
				}
				if(subscriptionCost != null) {
					resultData.add(subscriptionCost);		
				}
			}
		}
	}
	
	private TreeSet<String> getSeparateFieldValues(String fieldValue) {
		TreeSet<String> separatedValues = new TreeSet<>();
		if(fieldValue != null) {
			for(String singleVal : fieldValue.split(";")) {	
				separatedValues.add(singleVal.trim());
			}
		}
		return separatedValues;
	}

}
