/**
 * 
 */
package com.singpost.spe.esb.transformers.po;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;

import com.singpost.spe.esb.transformers.base.SPEBaseMessageTransformer;

/**
 * @author X-VEVISW
 *
 */
public class SPETcpRequestXmlDecoder extends SPEBaseMessageTransformer{

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		
		String postXML = "";
		if ( this.logger.isDebugEnabled() )
			this.logger.debug("Entering HttpRequestXMLToString--------------------");
		long t1=System.currentTimeMillis();
		try
        {
			postXML = message.getPayloadAsString();
			long t2=System.currentTimeMillis();
			log.info("Time take for Payload to String :"+(t2-t1));
        }
        catch (Exception e)
        {
            throw new TransformerException(this, e);
        }        
		return postXML;
	}
	
	public boolean isAcceptNull()
    {
        return false;
    }
}
