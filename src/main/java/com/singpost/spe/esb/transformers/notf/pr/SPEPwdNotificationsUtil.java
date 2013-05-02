/**
 * 
 */
package com.singpost.spe.esb.transformers.notf.pr;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.mule.config.i18n.Message;
import org.mule.config.i18n.MessageFactory;

import com.singpost.spe.esb.config.SPEConfig;
import com.singpost.spe.esb.constants.SPEConfigKey;
import com.singpost.spe.esb.exception.ApplicationException;
import com.singpost.spe.ext.utils.SPEFileUtils;

/**
 * @author X-VEVISW
 *
 */
public class SPEPwdNotificationsUtil {
	
	public static void movePrMsgToInProcessDir(String messageFilename,  SPEConfig speConf) throws ApplicationException{
		
		//move the original message file to in-process folder
		
		String prInProcessPath = speConf.get(SPEConfigKey._prInProcessPath);
		if (StringUtils.isEmpty(prInProcessPath)){
			//TODO throw exception
			Message msg=MessageFactory.createStaticMessage("Password Notification Inprocess path is empty ");
			throw new ApplicationException(msg);
		}
		String notfInprocessFolder = speConf.get(SPEConfigKey._notfInprocessFolder);
		if (StringUtils.isEmpty(notfInprocessFolder)){
			//TODO throw exception
			Message msg=MessageFactory.createStaticMessage("Notification Inprocess  path is empty ");
			throw new ApplicationException(msg);
		}
		
		SPEFileUtils.moveFileToFolder(notfInprocessFolder+File.separator+messageFilename, prInProcessPath);
		
	}

}
