/**
 * 
 */
package com.singpost.spe.esb.transformers.email;

import org.apache.commons.lang.StringEscapeUtils;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;

import com.singpost.spe.esb.transformers.base.SPEBaseMessageTransformer;

/**
 * @author X-VEVISW
 *
 */
public class SPEEmailTransformer extends SPEBaseMessageTransformer {

	/* (non-Javadoc)
	 * @see org.mule.transformer.AbstractMessageTransformer#transformMessage(org.mule.api.MuleMessage, java.lang.String)
	 */
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		try {
			System.out.println("Payload "+message.getPayloadAsString());
			
			String encodedPayloadInString = message.getPayloadAsString();
			
			String payloadString = StringEscapeUtils.unescapeXml(encodedPayloadInString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "<statusMessage>Ok</statusMessage><requestID>123</requestID>";
	}

}
