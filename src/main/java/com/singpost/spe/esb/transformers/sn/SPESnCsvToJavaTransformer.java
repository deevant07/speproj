package com.singpost.spe.esb.transformers.sn;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.config.i18n.Message;
import org.mule.config.i18n.MessageFactory;
import org.mule.transformer.AbstractMessageTransformer;

import com.singpost.spe.esb.transformers.base.SPEBaseMessageTransformer;

import au.com.bytecode.opencsv.CSVReader;

public class SPESnCsvToJavaTransformer extends SPEBaseMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {

		if ( logger.isDebugEnabled() )
		{
			this.logger.debug("Message" + message);
			this.logger.debug(message.getPayload());
		}	
		
		CSVReader reader = null;

		try {
			
			reader = new CSVReader(new StringReader(message.getPayloadAsString(outputEncoding)));
			
			List<String[]> scDetails = reader.readAll();
			
			if (scDetails != null){
				this.logger.info("converted shipnotice csv file into List of string array");
				return scDetails;
			}

		} catch (Exception e) {
			//this.logger.error("Exception while reading the payload to CSVReader",e);
			Message msg=MessageFactory.createStaticMessage("Exception while reading the payload to CSVReader");
			throw new TransformerException(msg,e);
		}finally
		{
			if ( reader != null )
			{
				try {
					reader.close();
				} catch (IOException e) {
					this.logger.error("IOException while closing the stream",e);
					/*Message msg=MessageFactory.createStaticMessage("IOException while closing the stream");
					throw new TransformerException(msg,e);*/
				}
			}
		}

		Message msg=MessageFactory.createStaticMessage("Error in parsing inbound shipnotice CSV file ");
		throw new TransformerException(msg);
		//TODO handle this exception
	}

}
