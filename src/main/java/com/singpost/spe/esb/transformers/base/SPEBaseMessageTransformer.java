/**
 * 
 */
package com.singpost.spe.esb.transformers.base;

import org.mule.transformer.AbstractMessageTransformer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.singpost.spe.esb.config.SPEConfig;

/**
 * @author X-VEVISW
 *
 */
public abstract class SPEBaseMessageTransformer extends AbstractMessageTransformer {

	protected final Log log= LogFactory.getLog(this.getClass());
	
	private SPEConfig speConfig;

	public SPEConfig getSpeConfig() {
		return speConfig;
	}

	public void setSpeConfig(SPEConfig speConfig) {
		this.speConfig = speConfig;
	}
	

}
