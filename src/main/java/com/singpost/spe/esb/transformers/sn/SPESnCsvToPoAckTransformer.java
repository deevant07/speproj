package com.singpost.spe.esb.transformers.sn;

import java.util.List;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.config.i18n.Message;
import org.mule.config.i18n.MessageFactory;

import com.singpost.spe.esb.constants.SPETransformer;
import com.singpost.spe.esb.templates.SPETemplate;
import com.singpost.spe.esb.transformers.base.SPEBaseMessageTransformer;

public class SPESnCsvToPoAckTransformer extends SPEBaseMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {	
		
		if( this.logger.isDebugEnabled() ){
			this.logger.debug("Message" + message);
			try {
				this.logger.debug("Payload = "+message.getPayloadAsString());
			} catch (Exception e) {
				this.logger.error("Exception while converting payload to String",e);				
			}
		}
		
		List<String []> csvRecords = (List<String []>)message.getPayload();
		
		//Get the po acknowledgement payload - to be sent to Supplizer
		//
		StringBuffer poAckXML = SPETemplate.getPOAckXML(csvRecords, outputEncoding) ;
		
		//set this to the message in the invocation scope
		//to get it while sending the Shipping Confirmation xml
		message.setInvocationProperty(SPETransformer._snCsvRecords, (Object)csvRecords);
		message.setInvocationProperty(SPETransformer._snCsvInProcessFilename, message.getInboundProperty("originalFilename"));
		
		
		this.logger.info("Exting SPESnCsvToPoAckTransformer - no issues");
		return poAckXML.toString();
	}


}
