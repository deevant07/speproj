/**
 * 
 */
package com.singpost.spe.esb.constants;

/**
 * @author X-VEVISW
 *
 */
public interface SPEApp {

	static enum Encoding {
		_utf8("UTF-8");
		
		private final String key;
		
		private Encoding(final String value){
			this.key=value;
		}
		
		public String value(){
			return key;
		}
	}
}
