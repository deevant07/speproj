package com.singpost.spe.esb.transformers.sn;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.config.i18n.Message;
import org.mule.config.i18n.MessageFactory;
import com.singpost.spe.esb.constants.SPEConfigKey;

import com.singpost.spe.esb.constants.SPETransformer;
import com.singpost.spe.esb.templates.SPETemplate;
import com.singpost.spe.esb.transformers.base.SPEBaseMessageTransformer;
import com.singpost.spe.ext.utils.SPEFileUtils;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

public class SPESnCxmlTransformer extends SPEBaseMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {

		if ( this.logger.isDebugEnabled() ){
			
			try {

				this.logger.debug("In ScCXmlTransf...........");
				this.logger.debug("Message is ..........."+message);
				this.logger.debug("ScCXmlTransf <- payload is"+message.getPayloadAsString());
				
			} catch (Exception e) {
				
				//TODO handle this correctly - maybe this error can be ignored...hoping the actual code works fine...
				this.logger.error("Exception while converting payload to String",e);
			}
		}
		
		Object obj = message.getInvocationProperty(SPETransformer._snCsvRecords);
		if (obj != null ){
			
			String snCxmlFolder = getSpeConfig().get(SPEConfigKey._snCxmlFolder);
			if (StringUtils.isEmpty(snCxmlFolder)){
				snCxmlFolder = SPETransformer._snCxmlFolderTmpFullPath;
			}

			String tmpFileFQP;
			String snCsvInProcessFilename = message.getInvocationProperty(SPETransformer._snCsvInProcessFilename);
			if (StringUtils.isEmpty(snCsvInProcessFilename)){
				snCsvInProcessFilename="tempSnCxml";
			}else{
				snCsvInProcessFilename=com.singpost.spe.esb.utils.StringUtils.removeExtension(snCsvInProcessFilename);
			}
			tmpFileFQP = snCxmlFolder+ "/"+snCsvInProcessFilename+"_"
					+ Calendar.getInstance().getTimeInMillis() + ".xml";
			if (this.logger.isDebugEnabled()){
				this.logger.info("Temp file that will be created is "+tmpFileFQP);
			}
			FileInputStream scCXml = SPETemplate.getSNCxml((List<String []>)obj, tmpFileFQP, outputEncoding);
			this.logger.info("Got the shipnotice cxml inputstream");
			
			
			//introduce a delay between poAck and SN calls
			//as sometimes ShipNotice eventhandler in supplizer tries to complete before poAck event handler
			delayShipNoticeCall();
			
			return scCXml;
			
		}else{
			//TODO throw exception
			//Suppose to get the csv records of SN in the previous transformation
			Message msg=MessageFactory.createStaticMessage("CSV records might have not been transformed in previous transformer");
			throw new TransformerException(msg);
		}

	}

	private void delayShipNoticeCall(){
		
		this.logger.info("Before making a call to ship notice waiting for po-Ack event handler to complete");
		int sleepTime = 20;
		try{
			sleepTime =  Integer.parseInt(getSpeConfig().get(SPEConfigKey._delayTimeBeforeSnCall));
		}catch(Exception e){
			//Do nothing...set waitTime to default value 20sec
			sleepTime = 20;
		}
		try {
			TimeUnit.SECONDS.sleep(sleepTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
