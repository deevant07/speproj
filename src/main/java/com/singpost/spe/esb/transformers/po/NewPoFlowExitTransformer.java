/**
 * 
 */
package com.singpost.spe.esb.transformers.po;

import java.io.File;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;

import com.singpost.spe.esb.constants.SPEConfigKey;
import com.singpost.spe.esb.exception.ApplicationException;
import com.singpost.spe.esb.transformers.base.SPEBaseMessageTransformer;
import com.singpost.spe.ext.utils.SPEFileUtils;

/**
 * @author Deevan
 * refactored - X-vevisw
 *
 */
public class NewPoFlowExitTransformer  extends SPEBaseMessageTransformer {
	
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		
		
		/**
		 * Move the csv file created by the ftl process 
		 * to archive folder which will later be moved
		 * to S3 for archiving
		 */
		String srcCsvFileName=message.getProperty("wmsNewPoFileName", PropertyScope.OUTBOUND);
		String srcCsvDirName=getSpeConfig().get(SPEConfigKey._ftlTempFolderPath)+ File.separator;
		String archiveFolder=getSpeConfig().get(SPEConfigKey._archiveFolderPath)+ File.separator;
		//String destFileName=srcCsvFileName;
		
		
		try {
			SPEFileUtils.moveFileToFolder(srcCsvDirName + srcCsvFileName, archiveFolder);
		} catch (ApplicationException e) {
			log.warn("File moving failed from Source: "+(srcCsvDirName + srcCsvFileName)+" To Dest:"+archiveFolder,e);
			e.printStackTrace();
		}
		
		if (log.isDebugEnabled()){
			log.debug("Moved "+srcCsvDirName+srcCsvFileName +" to "+archiveFolder);
		}
		/**
		 * Move the in-process file created by file inbound process 
		 * to archive folder which will eventually be archived by housekeeping jobs
		 */
		String srcXmlFileName=message.getProperty("originalFileName", PropertyScope.INBOUND);
		String srcXmlDirName=getSpeConfig().get(SPEConfigKey._inProcessFolderPath) + File.separator ;
		//String destXmlFileName=srcXmlFileName;
		
		try{
			SPEFileUtils.moveFileToFolder(srcXmlDirName + srcXmlFileName, archiveFolder);
		} catch (ApplicationException e) {
			log.warn("File moving failed from Source: "+(srcXmlDirName + srcXmlFileName)+" To Dest:"+archiveFolder,e);
		}
		
		
		if (log.isDebugEnabled()){
			log.debug("Moved "+srcXmlDirName + srcXmlFileName +" to "+archiveFolder);
		}
		return message.getPayload();
		
	}

}
