/**
 * 
 */
package fi.csc.avaa.kuhiti.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import fi.csc.avaa.kuhiti.csv.KuhitiDataReader;
import fi.csc.avaa.kuhiti.model.SubscriptionCost;

/**
 * @author jmlehtin
 *
 */
public final class KuhitiCache {
	
	private static List<SubscriptionCost> subscriptionCosts = new ArrayList<>();
	public static TreeSet<String> allOrgTypes = new TreeSet<>();
	public static TreeSet<String> allOrgNames = new TreeSet<>();
	public static TreeSet<Integer> allYears = new TreeSet<>();
	public static TreeSet<String> allMaterialTypes = new TreeSet<>();
	public static TreeSet<String> allAcquisitionWays = new TreeSet<>();
	
	public static boolean initCache() {
		return getDataCache() != null;
	}
	
	private static boolean updateCacheFromExcelFile() {
		if(subscriptionCosts != null) {
			subscriptionCosts.clear();
		}
		KuhitiDataReader reader = new KuhitiDataReader(KuhitiConst.DATAFILE_PATH);
		subscriptionCosts = reader.getResultData();
		return subscriptionCosts != null && subscriptionCosts.size() > 0;
	}
	
	public static Collection<SubscriptionCost> getDataCache() {
		if(!updateCacheFromExcelFile()) {
			return null;
		}
		return subscriptionCosts;
	}
}
