/**
 * 
 */
package com.singpost.spe.esb.transformers.notf;

import java.io.File;

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
public class SPENotificationsUtil {
	
	public static void moveNotfMsgToArchiveDir(String orgMsgFilename, SPEConfig speConfig) throws ApplicationException {
		
		
		//move the original message file to archive folder
		
		String notfArchiveDir = speConfig.get(SPEConfigKey._notfArchiveFolderPath);
		if (StringUtils.isEmpty(notfArchiveDir)){
			Message msg=MessageFactory.createStaticMessage("Notification Archive path is empty ");
			throw new ApplicationException(msg);
		}
		String notfInprocessFolder = speConfig.get(SPEConfigKey._notfInprocessFolder);
		if (StringUtils.isEmpty(notfInprocessFolder)){
			//TODO throw exception
			Message msg=MessageFactory.createStaticMessage("Notification Inprocess path is empty ");
			throw new ApplicationException(msg);
		}
		
		SPEFileUtils.moveFileToFolder(notfInprocessFolder+File.separator+orgMsgFilename, notfArchiveDir);
		
	}
	

}
