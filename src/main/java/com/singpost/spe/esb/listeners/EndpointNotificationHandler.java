package com.singpost.spe.esb.listeners;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleMessage;
import org.mule.api.context.notification.EndpointMessageNotificationListener;
import org.mule.api.endpoint.OutboundEndpoint;
import org.mule.api.transport.PropertyScope;
import org.mule.context.notification.EndpointMessageNotification;

import com.singpost.spe.esb.config.SPEConfig;
import com.singpost.spe.esb.constants.SPEConfigKey;
import com.singpost.spe.esb.exception.ApplicationException;
import com.singpost.spe.ext.utils.SPEFileUtils;

public class EndpointNotificationHandler implements
		EndpointMessageNotificationListener<EndpointMessageNotification> {

	private SPEConfig speConfig;
	private Log log=LogFactory.getLog(EndpointNotificationHandler.class);
	
	/**
	 * @return the speConfig
	 */
	public SPEConfig getSpeConfig() {
		return speConfig;
	}

	/**
	 * @param speConfig the speConfig to set
	 */
	public void setSpeConfig(SPEConfig speConfig) {
		this.speConfig = speConfig;
	}

	@Override
	public void onNotification(EndpointMessageNotification notification) {
		// TODO Auto-generated method stub

		
		if ( notification.getImmutableEndpoint() instanceof OutboundEndpoint)
		{
			String builderName=notification.getImmutableEndpoint().getEndpointBuilderName();
			if (builderName!= null &&  builderName.equals("ftpOutbound") )
			{
				if ( notification.getAction() == EndpointMessageNotification.MESSAGE_DISPATCH_END)
				{
					poExitFlow(notification.getSource());
					log.info("In EndpointNotification Handler with action: "+notification.getAction() +" name: "+notification.getActionName()+" Endpoint Name:"+notification.getEndpoint());
				}
			}
		}
	}
	
	public void poExitFlow(MuleMessage message)
	{
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
	}
}
