/**
 * 
 */
package com.singpost.spe.esb.constants;

/**
 * @author X-VEVISW
 *
 */
public enum SPEConfigKey {
	_wmsPoCsvFolder("wmsPoCsvFolder"),
	_snCxmlFolder("snCxmlFolder"),
	_ftlTempFolderPath("ftlTempFolderPath"),
	_archiveFolderPath("archiveFolderPath"),
	_inProcessFolderPath("inProcessFolderPath"),
	_ftlRootFolder("ftlRootFolder"),
	_delayTimeBeforeSnCall("delayTimeBeforeSnCall"),
	_prInProcessPath("prInProcessPath"),
	_notfArchiveFolderPath("notfArchiveFolderPath"),
	_notfInprocessFolder("notfInprocessFolder"),
	_snInboundFolder("snInboundFolder");
	
	private final String key;
	
	private SPEConfigKey(final String value){
		this.key=value;
	}
	
	public String toString(){
		return key;
	}
}