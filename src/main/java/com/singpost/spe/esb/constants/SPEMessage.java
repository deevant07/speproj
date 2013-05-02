/**
 * 
 */
package com.singpost.spe.esb.constants;

/**
 * @author X-VEVISW
 *
 */
public interface SPEMessage {

	static enum PropertyKeys {
		_originalFilename("originalFilename"),
		_wmsNewPoFileName("wmsNewPoFileName");
		
		private final String key;
		
		private PropertyKeys(final String value){
			this.key=value;
		}
		
		public String toString(){
			return key;
		}
	}
}
