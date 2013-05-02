/**
 * 
 */
package com.singpost.spe.esb.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author X-VEVISW
 *
 */
public class WMSUtils {

	public static String generateNewPoFileName(String poNumber){
		SimpleDateFormat wmsNewPOCsvFilenameFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		if (org.apache.commons.lang.StringUtils.contains(poNumber, '-')){
			poNumber=org.apache.commons.lang.StringUtils.replaceOnce(poNumber,"-", "");
		}
			
		final String wmsNewPOCsvFilename = "SGADISO"+poNumber+wmsNewPOCsvFilenameFormat.format(date)+".csv"; 
		return wmsNewPOCsvFilename;
	}
}
