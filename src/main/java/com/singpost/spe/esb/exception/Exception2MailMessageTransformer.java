/**
 * 
 */
package com.singpost.spe.esb.exception;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.message.ExceptionMessage;
import org.mule.transformer.AbstractMessageTransformer;

/**
 * @author DEEVKUMA
 *
 */
public class Exception2MailMessageTransformer extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		
		try {
				String msg="";
				Object exMessage=message.getOriginalPayload();
				if ( exMessage instanceof ExceptionMessage )
				{
					ExceptionMessage exMsgObj=(ExceptionMessage)exMessage;
					if ( exMsgObj.getPayload() instanceof byte[])
					{
						String payloadStr=new String((byte[])exMsgObj.getPayload());
						String[] payLoadId=payloadStr.split("payloadID=\"");
						if ( payLoadId!= null && payLoadId.length>1 )
						{
							String strPayLoad=payLoadId[1];
							String[] content=strPayLoad.split("\"");
							if ( content!= null && content.length > 1 )
							{
								String finalPayLoadId=content[0];
								msg="payloadID from cXML: "+finalPayLoadId;
							}
							String[] orderIdArr=payLoadId[1].split("orderID=\"");
							String[] orderCntntArr=orderIdArr[1].split("\"");
							if ( orderCntntArr!= null && orderCntntArr.length>1 )
							{
								String strOrderId=orderCntntArr[0];
								msg+="\norderID from cXML: "+strOrderId;
							}
							
						}					
					}				
					msg+="\nException Receieved: ";
					msg+=exMsgObj.getException().getMessage();
					
				}
				return msg;
		} catch (Exception e) {
			logger.error("Exception while creating mail message",e);
			//e.printStackTrace();
			return e.getMessage();
		}
		
		
		
	}

	
}
