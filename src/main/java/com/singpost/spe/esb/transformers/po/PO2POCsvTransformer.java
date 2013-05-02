package com.singpost.spe.esb.transformers.po;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.config.i18n.Message;
import org.mule.config.i18n.MessageFactory;

import com.singpost.spe.esb.constants.SPEMessage;
import com.singpost.spe.esb.exception.ApplicationException;
import com.singpost.spe.esb.transformers.base.SPEBaseMessageTransformer;
import com.singpost.spe.esb.utils.WMSUtils;

public class PO2POCsvTransformer extends SPEBaseMessageTransformer {
	
	
	/**
	 * tempFolderPath - holds the location of the folder 
	 * where the generated Csv files will be stored
	 */
	private String tempFolderPath;
	private String originalFileName;
	
	
	
	public String getTempFolderPath() {
		return tempFolderPath;
	}

	public void setTempFolderPath(String tempFolderPath) {
		this.tempFolderPath = tempFolderPath;
	}
	
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		
		Object payload=message.getPayload();
		
		if( payload instanceof byte[] )
		{
			//return transformXML2CSV((byte[])payload);
			//System.out.println("MESSAGE PAYLOAD IS IN BYTES");
			//Handle exception ...expecting FileInputStream
			this.logger.error("FilenputStream expected where the input is received as byte array");
			Message msg=MessageFactory.createStaticMessage("FilenputStream expected where the input is received as byte array");
			throw new TransformerException(msg);			
			
		} else if (payload instanceof FileInputStream){
			
			//Generate the name of the CSV file that will hold the PO in WMS format
			//first get poNumber - it will be part of the file-name that gets pushed to WMS
			//String poNumber= getPoNumber(message);
			//String outboundFileName = WMSUtils.generateNewPoFileName(poNumber);
			String originalFilename = (String) message.getInboundProperty(SPEMessage.PropertyKeys._originalFilename.toString());
			originalFilename=originalFilename.replaceFirst("SP-", "SP");
			String outboundFileName = com.singpost.spe.esb.utils.StringUtils.removeExtension(originalFilename)+".csv";
			
			
			//set this as a message property - will be retrieved in the subsequent steps in the flow
			message.setOutboundProperty(SPEMessage.PropertyKeys._wmsNewPoFileName.toString(), outboundFileName);
			
			//return transformXML2CSV((FileInputStream)payload,outboundFileName,outputEncoding);
			
			File poCsvFile =null; 
			
			FileInputStream poCsvFileStream = null;
			try {
				poCsvFile=com.singpost.spe.esb.templates.SPETemplate.getPoAsWmsCsv((FileInputStream)payload,outboundFileName,outputEncoding,this.getSpeConfig());
				if (poCsvFile != null)
				{
					poCsvFileStream = new FileInputStream(poCsvFile);
					return poCsvFileStream;
				}
				else
				{
					Message msg=MessageFactory.createStaticMessage("Encountered null while transforming PO xml to CSV in com.singpost.spe.esb.templates.SPETemplate.getPoAsWmsCsv");
					throw new TransformerException(msg);
				}
			} catch (FileNotFoundException e) {
				Message msg=MessageFactory.createStaticMessage("FileNotFoundException with error message:"+e.getMessage());
				throw new TransformerException(msg,e);
			}
		}else
		{			
			Message msg=MessageFactory.createStaticMessage("Unknown Payload type :"+payload);
			throw new TransformerException(msg);
		}
	}
//	private Object transformXML2CSV(FileInputStream poCXmlFileStream,String outboundFileName, String encoding)
//	{
//		Writer csvFileWrite=null;
//		
//		try {		
//				
//				Map<String, NodeModel> root = new HashMap<String, NodeModel>();
//				
//				// Create input source for the inputstream xml
//				InputSource newPoXmlSource=new InputSource(poCXmlFileStream);
//				
//				//Parse the xml input stream
//				root.put("doc",freemarker.ext.dom.NodeModel.parse(newPoXmlSource));
//				
//				if( this.logger.isDebugEnabled() )
//				{
//					this.logger.debug("tempFolder Path "+getTempFolderPath());
//				}	
//				String tempFileName = outboundFileName;//getOriginalFileName();
//				
//				
//				//filename to be sent to WMS should be of this format
//				//SGADISOYYYYMMDDHHMM
//				
//				if( this.logger.isDebugEnabled() )
//				{
//					this.logger.debug("tempFileName "+tempFileName);
//				}
//				
//				File fileOut=new File(getTempFolderPath()+"/"+tempFileName);			
//				csvFileWrite=new FileWriter(fileOut);
//				
//				//Get the template from cache and process with the inputdata present in root
//				Template tmplt=getTemplate(encoding);
//				tmplt.process(root, csvFileWrite);
//				
//				csvFileWrite.flush();          
//	            
//				
//	            FileInputStream fis=new FileInputStream(fileOut);             
//	            
//	            return fis;
//		            
//		} catch (IOException e) {		
//			this.logger.error("IOException received while loading/processing template",e);
//			return null;
//		} catch (TemplateException e) {		
//			this.logger.error("TemplateException received while loading/processing template",e);
//			return null;
//		} catch (SAXException e) {
//			this.logger.error("SAXException received while parsing input xml",e);
//			return null;		
//		} catch (ParserConfigurationException e) {
//			this.logger.error("ParserConfigurationException received while parsing input xml",e);
//			return null;		
//		}
//		finally
//		{
//			 if (poCXmlFileStream != null)
//			 {
//	         	try {
//					poCXmlFileStream.close();
//				} catch (IOException e) {
//					this.logger.error("IOException while closing the stream",e);
//				}
//			 }
//			 if ( csvFileWrite != null )
//			 {
//				 try {
//					csvFileWrite.close();
//				} catch (IOException e) {
//					this.logger.error("IOException while closing the stream",e);
//				}
//			 }
//		}
//	}
//	
	private String getPoNumber(MuleMessage message){
		
		//a bad way to get the poNumber
		//assumption - the orignial filename that gets saved initially 
		//has the poNumber in the begining...strip the filename to get the PO
		String originalFilename = (String) message.getInboundProperty(SPEMessage.PropertyKeys._originalFilename.toString());
		String poNumber = "";
		if (!StringUtils.isEmpty(originalFilename)){
			poNumber = originalFilename.substring(0,originalFilename.indexOf('_'));
		}
		
		if (StringUtils.isEmpty(poNumber)){
			//if we don't get the poNumber
			//use a unique value so that the file name is unique
			poNumber = Calendar.getInstance().getTimeInMillis()+"";
		}
		return poNumber;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}
}
