/**
 * 
 */
package com.singpost.spe.esb.transport.http.transformers;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transport.tcp.TcpMessageReceiver;

import com.singpost.spe.esb.transformers.base.SPEBaseMessageTransformer;



/**
 * @author X-VEVISW
 *
 */
public class HttpRequestXmlDecoder extends SPEBaseMessageTransformer {
	
	private boolean disableDecoding = false;

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {

		final String muleMessage = "muleMessage";
		
		String postXML = "";
		if ( this.logger.isDebugEnabled() )
			this.logger.debug("Entering HttpRequestXMLToString--------------------");
		
		
		try
        {
//            String httpMethod = (String) message.getProperty("http.method",PropertyScope.INBOUND);
//            String contentType = (String) message.getProperty("Content-Type",PropertyScope.INBOUND);
            
			Object payloadObj = message.getPayload();
			if (payloadObj instanceof HashMap){
				HashMap postData = (HashMap)payloadObj;
				postXML=(String) postData.get(muleMessage);
			}else {
				//TODO handle exception
            	throw new TransformerException(this, new Exception("Unexpected post data "));
			}
			
            if (postXML != null && postXML.length() > 0)
            {
            	
            	//System.out.println("Output encoding "+outputEncoding);
            	if (!disableDecoding){
            		postXML = URLDecoder.decode(postXML,outputEncoding);
            	}
            	
            	//System.out.println("postXML"+postXML);
            	//postXML= StringEscapeUtils.unescapeXml(encodedPayloadInString);
            	//System.out.println("postXML after decoding"+postXML);
                if ( this.logger.isDebugEnabled() )
                	this.logger.debug("Decoded xml -===============\n"+postXML);
                message.setInvocationProperty("successMsg", "<statusMessage>Ok</statusMessage><requestID>123</requestID>");
                
            }else{
            	//TODO handle exception
            	throw new TransformerException(this, new Exception("Unexpected post data "));
            }
            
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

	public boolean isDisableDecoding() {
		return disableDecoding;
	}

	public void setDisableDecoding(boolean disableDecoding) {
		this.disableDecoding = disableDecoding;
	}
	
	
	
	
	

}
