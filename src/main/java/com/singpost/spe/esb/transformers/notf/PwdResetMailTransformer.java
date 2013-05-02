package com.singpost.spe.esb.transformers.notf;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.config.i18n.Message;
import org.mule.config.i18n.MessageFactory;

import com.singpost.spe.esb.config.SPEConfig;
import com.singpost.spe.esb.exception.ApplicationException;
import com.singpost.spe.esb.templates.SPETemplate;
import com.singpost.spe.esb.transformers.base.SPEBaseMessageTransformer;
import com.singpost.spe.esb.transformers.notf.pr.SPEPwdNotificationsUtil;
import com.singpost.spe.ext.SPEXPathEvaluator;

public class PwdResetMailTransformer extends SPEBaseMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		
		
		byte[] bArrSrc=null;
		boolean xmlValid=false;
		
		
		String orgMsgFilename = message.getInboundProperty("originalFilename");
		if (StringUtils.isEmpty(orgMsgFilename)){
			//TODO: throw exception
		}
		SPEConfig speConf = getSpeConfig();
		if (speConf == null ){
			//TODO: throw exception
		}

		Object src=message.getPayload();
		
		if ( log.isDebugEnabled() )
		{
			log.debug("Payload recieved by PwdResetMailTransformer: "+src);
		}

		try
		{
			if ( src instanceof InputStream ){
				
				bArrSrc=IOUtils.toByteArray((InputStream)src);
				
			}else if (src instanceof byte[] ){
				
				bArrSrc=(byte[])src;
				
			}else if (src instanceof String ){
				
				bArrSrc=src.toString().getBytes();
				
			}else{
				//TODO - handle exception
			}
			
			String xPathValue=SPEXPathEvaluator.evalXpath("//message/@id", bArrSrc);
			if ( log.isDebugEnabled()) log.debug("Notification request - messageId = "+xPathValue);
			
			if ( xPathValue!= null && xPathValue.equals("passwordReset"))
			{
				//moveToInProcessDir(bArrSrc,message);
				
				//move the original message file to pr in-process folder
				try
				{
					SPEPwdNotificationsUtil.movePrMsgToInProcessDir(orgMsgFilename, speConf);
				}catch (ApplicationException e) {
					log.warn("File moving failed for file: "+(orgMsgFilename),e);
				}
				
				xmlValid=true;
				if ( log.isDebugEnabled() )
				{
					log.debug("Valid XML input with id - passwordReset ");
				}
				
				
			}else
			{
				//Right now not handling any other msg type
				//moveToIgnoreDir(bArrSrc,message);	
				log.info("Not handling messages of type "+xPathValue);
				try{
					SPENotificationsUtil.moveNotfMsgToArchiveDir(orgMsgFilename, speConf);
				}catch (ApplicationException e) {
					log.warn("File moving failed for file: "+(orgMsgFilename),e);
				}				
			}
				
		}catch(IOException e){
			Message msg=MessageFactory.createStaticMessage("IOException with error message:"+e.getMessage());
			throw new TransformerException(msg,e);
		}
		
		
		
		//process the pwdResetMsg
		
		StringBuffer mailMsg = null;
		if ( xmlValid )
		{
			String toEmailAddr=SPEXPathEvaluator.evalXpath("//message/user/email", bArrSrc);
			if ( toEmailAddr != null )
			{
				message.setProperty("toEmailID", toEmailAddr, PropertyScope.OUTBOUND);
				message.setProperty("pwdReset", true, PropertyScope.OUTBOUND);
				mailMsg=SPETemplate.getPwdResetMail(bArrSrc,outputEncoding);
				
				return mailMsg.toString();
			}else
			{
				message.setProperty("pwdReset", false, PropertyScope.OUTBOUND);
				log.error("email tag not available in input XML");
			}
			
		}
			
		return null;
	}
	
}
