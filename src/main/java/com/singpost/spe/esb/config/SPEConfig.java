/**
 * 
 */
package com.singpost.spe.esb.config;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.singpost.spe.esb.constants.SPEConfigKey;
import com.singpost.spe.esb.exception.ApplicationException;
import com.singpost.spe.esb.templates.SPETemplateFactory;

/**
 * @author X-VEVISW
 *
 */
public class SPEConfig {
	
	
		
	private Properties propertiesMap;


	/**
	 * @return the propertiesMap
	 */
	public Properties getPropertiesMap() {
		return propertiesMap;
	}

	/**
	 * @param propertiesMap the propertiesMap to set
	 * @throws ApplicationException 
	 */
	public void setPropertiesMap(Properties propertiesMap) throws ApplicationException {
		this.propertiesMap = propertiesMap;
		
		String ftlRootFolder = (String)propertiesMap.getProperty(SPEConfigKey._ftlRootFolder.toString());
		if (StringUtils.isNotEmpty(ftlRootFolder))
			SPETemplateFactory.getInstance(ftlRootFolder);
	}

	public String getProperty(String key)
	{
		
		//TODO handle exceptions
		return propertiesMap.getProperty(key);
	}
	
	public String get(SPEConfigKey key)
	{
		
		//TODO handle exceptions
		return propertiesMap.getProperty(key.toString());
	}
	
	
	/**
//	 * wmsPoCsvFolder - holds the folder location (fullpath) where new po, in wms-csv format, will be created
//	 */
//	private String wmsPoCsvFolder;
//	
//	
//	/**
//	 * ftlRootFolder - Holds the absolute path of the folder containing all ftl templates
//	 */
//	private String ftlRootFolder;
//	
//	/**
//	 * snCsvFolder - holds the location of the folder that contains shipNotices in Cxml format
//	 */
//	private String snCxmlFolder;
//	
//
//	public String getWmsPoCsvFolder() {
//		return wmsPoCsvFolder;
//	}
//
//	public void setWmsPoCsvFolder(String wmsPoCsvFolder) {
//		this.wmsPoCsvFolder = wmsPoCsvFolder;
//	}
//
//	public String getFtlRootFolder() {
//		return ftlRootFolder;
//	}
//
//	public void setFtlRootFolder(String ftlRootFolder) throws ApplicationException {
//		//This method also loads all templates
//		
//		SPETemplateFactory.getInstance(ftlRootFolder);
//		
//		this.ftlRootFolder = ftlRootFolder;
//		
//		
//	}
//
//	public String getSnCxmlFolder() {
//		return snCxmlFolder;
//	}
//
//	public void setSnCxmlFolder(String snCxmlFolder) {
//		this.snCxmlFolder = snCxmlFolder;
//	}


}
