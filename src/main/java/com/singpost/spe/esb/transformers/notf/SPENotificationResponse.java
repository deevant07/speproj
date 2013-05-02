/**
 * 
 */
package com.singpost.spe.esb.transformers.notf;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;

import com.singpost.spe.esb.transformers.base.SPEBaseMessageTransformer;

/**
 * @author X-VEVISW
 *
 */
public class SPENotificationResponse extends SPEBaseMessageTransformer {

	/* (non-Javadoc)
	 * @see org.mule.transformer.AbstractMessageTransformer#transformMessage(org.mule.api.MuleMessage, java.lang.String)
	 */
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		
		return "<mailResponse><statusMessage>Queued</statusMessage><requestID></requestID></mailResponse>";
	}

}
