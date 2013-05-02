/**
 * 
 */
package com.singpost.spe.esb.transformers.email;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.message.ExceptionMessage;
import org.mule.transformer.AbstractMessageTransformer;

/**
 * @author DEEVKUMA
 *
 */
public class Exception2MailMessageTransformer extends
		AbstractMessageTransformer {

	/* (non-Javadoc)
	 * @see org.mule.transformer.AbstractMessageTransformer#transformMessage(org.mule.api.MuleMessage, java.lang.String)
	 */
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		
		try {
				StringBuffer msg=new StringBuffer("");
				Object exMessage=message.getOriginalPayload();
				if ( exMessage instanceof ExceptionMessage )
				{
					ExceptionMessage exMsgObj=(ExceptionMessage)exMessage;
					StringBuffer msg1=com.singpost.spe.esb.templates.SPETemplate.getErrMsgMailContent(exMsgObj, outputEncoding);
					if ( msg1 != null )
					{
						return msg1;
					}else
					{
						msg1=new StringBuffer("Encountered null while transforming Exception Obj to Mail Message in com.singpost.spe.esb.templates.SPETemplate.getErrMsgMail");
						logger.error(msg1);
						return msg1;
					}
				}				
				return msg;
		} catch (Exception e) {
			logger.error("Exception while creating mail message",e);
			return e.getMessage();
		}
		
		
		
	}

}
