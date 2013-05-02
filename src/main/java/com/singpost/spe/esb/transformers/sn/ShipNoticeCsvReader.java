package com.singpost.spe.esb.transformers.sn;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.config.i18n.Message;
import org.mule.config.i18n.MessageFactory;
import org.mule.transformer.AbstractMessageTransformer;

import au.com.bytecode.opencsv.CSVReader;

public class ShipNoticeCsvReader extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		
		CSVReader shipNoticeCsvReader = null;

		try {
			
			//TODO validate the payload...
			
			shipNoticeCsvReader = new CSVReader(new StringReader(message.getPayloadAsString(outputEncoding)));
			
			final List<String[]> shipNoticeCsvList = shipNoticeCsvReader.readAll();
			
			return shipNoticeCsvList;

		} catch (Exception e) {
			//this.logger.error("Exception while processing CSVReader",e);
			Message msg=MessageFactory.createStaticMessage("Exception while processing CSVReader");
			throw new TransformerException(msg);
		}finally
		{
			if ( shipNoticeCsvReader!= null )
			{
				try {
					shipNoticeCsvReader.close();
				} catch (IOException e) {
					logger.error("IOException while closing the stream",e);
				}
			}
		}

		
	}

}
