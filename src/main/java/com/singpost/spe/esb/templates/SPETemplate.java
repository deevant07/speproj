/**
 * 
 */
package com.singpost.spe.esb.templates;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.transformer.TransformerException;
import org.mule.config.i18n.Message;
import org.mule.config.i18n.MessageFactory;
import org.mule.message.ExceptionMessage;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.singpost.spe.esb.config.SPEConfig;
import com.singpost.spe.esb.constants.SPEConfigKey;
import com.singpost.spe.esb.exception.ApplicationException;

import freemarker.ext.dom.NodeModel;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author X-VEVISW
 * 
 */
public class SPETemplate {

	protected static final Log log = LogFactory.getLog(SPETemplate.class.getName());
	
	public static File getPoAsWmsCsv(FileInputStream poCXmlFileStream, String outboundFileName, String encoding, SPEConfig speConfig ) throws TransformerException{
		
		Writer csvFileWriter=null;
		
		Map<String, NodeModel> root = new HashMap<String, NodeModel>();
		
		String outboundFolderPath = "";
		if (speConfig != null){
			outboundFolderPath = speConfig.get(SPEConfigKey._wmsPoCsvFolder);
		}else{
			outboundFolderPath="/tmp";
		}
		//filename to be sent to WMS should be of this format
		//SGADISOYYYYMMDDHHMM
		
		if( log.isDebugEnabled() )
		{
			log.debug("newpo wms-csv file = "+outboundFolderPath+"/"+outboundFileName);
		}
		
		
		
		File poCsvFile = new File(outboundFolderPath+"/"+outboundFileName);
		
		try {
			
			// Create input source for the inputstream xml
			InputSource newPoXmlSource=new InputSource(poCXmlFileStream);
			
			//Parse the xml input stream
			root.put("doc",freemarker.ext.dom.NodeModel.parse(newPoXmlSource));
			
			csvFileWriter=new FileWriter(poCsvFile);
			//Get the template from cache and process with the inputdata present in root
			Template tmplt=getTemplate(encoding);
			tmplt.process(root, csvFileWriter);
			
			csvFileWriter.flush();   
			
		} catch (IOException e) {
			//this.logger.error("IOException received while loading/processing template",e);
			Message msg=MessageFactory.createStaticMessage("IOException received while loading/processing template:"+e.getMessage());
			throw new TransformerException(msg,e);
			//return null;
		} catch (TemplateException e) {
			//this.logger.error("TemplateException received while loading/processing template",e);
			//return null;
			Message msg=MessageFactory.createStaticMessage("TemplateException received while loading/processing template:"+e.getFTLInstructionStack());
			throw new TransformerException(msg,e);
		} catch (SAXException e) {
			//this.logger.error("SAXException received while parsing input xml",e);
			//return null;	
			Message msg=MessageFactory.createStaticMessage("SAXException received while parsing input xml:"+e.getMessage());
			throw new TransformerException(msg,e);
		} catch (ParserConfigurationException e) {
			//this.logger.error("ParserConfigurationException received while parsing input xml",e);
			//return null;		
			Message msg=MessageFactory.createStaticMessage("ParserConfigurationException received while parsing input xml:"+e.getMessage());
			throw new TransformerException(msg,e);
		} catch (ApplicationException e) {
			Message msg=MessageFactory.createStaticMessage("ApplicationException receieved on SPETemplateFactory: "+e.getDetailedMessage());
			throw new TransformerException(msg,e);
		}finally
		{
			 if (poCXmlFileStream != null)
			 {
	         	try {
					poCXmlFileStream.close();
				} catch (IOException e) {
					log.error("IOException while closing the stream",e);
				}
			 }
			 if ( csvFileWriter != null )
			 {
				 try {
					csvFileWriter.close();
				} catch (IOException e) {
					log.error("IOException while closing the stream",e);
				}
			 }
		}
		
		
		return poCsvFile;
        
	}
		
	public static StringBuffer getPOAckXML(List<String[]> snCsvRecords, String encoding) throws TransformerException {

		if ( log.isDebugEnabled() ){
			log.debug("Entering method getPOAckXML");
		}
		
		Map<String, List<String[]>> rootMap = new HashMap<String, List<String[]>>();
		rootMap.put("snCsv", snCsvRecords);
		
		Writer stringWriter=null;
		try {
 
			Template template = SPETemplateFactory
					.getInstance()
					.getTemplate(
							com.singpost.spe.esb.constants.SPETemplate._poAckTemplateName,
							encoding);

			// poAck is relatively small xml...just holding it in memory
			stringWriter = new StringWriter();
			template.process(rootMap, stringWriter);
			StringBuffer poAckXML = ((StringWriter) stringWriter).getBuffer();
			
			if ( log.isDebugEnabled() )
				log.debug("FTL-Transformed xml is: "+ poAckXML);

			log.info("Exiting getPOAckXML - No issues");
			return poAckXML;

		} catch (IOException e) {
			//log.error("IOException receieved while tranforming the ftl template: "+com.singpost.spe.esb.constants.SPETemplate._poAckTemplateName,e);
			Message msg=MessageFactory.createStaticMessage("IOException receieved while tranforming the ftl template: "+com.singpost.spe.esb.constants.SPETemplate._poAckTemplateName);
			throw new TransformerException(msg,e);
		} catch (TemplateException e) {
			//log.error("TemplateException receieved while tranforming the ftl template: "+com.singpost.spe.esb.constants.SPETemplate._poAckTemplateName,e);
			Message msg=MessageFactory.createStaticMessage("TemplateException receieved while tranforming the ftl template: "+com.singpost.spe.esb.constants.SPETemplate._poAckTemplateName+" FTL Stack:"+e.getFTLInstructionStack());
			throw new TransformerException(msg,e);
		} catch (ApplicationException e) {
			Message msg=MessageFactory.createStaticMessage("ApplicationException receieved on SPETemplateFactory  ");
			throw new TransformerException(msg,e);
			//e.printStackTrace();
		}finally
		{
			if ( stringWriter != null )
			{
				try {
					stringWriter.close();
				} catch (IOException e) {
					log.error("Error while closing the stream: ",e);
					/*Message msg=MessageFactory.createStaticMessage("Error while closing the stream: ");
					throw new TransformerException(msg,e);*/
				}
			}
		}
	}

	public static FileInputStream getSNCxml(List<String[]> scCsvRecords,String tmpFileFQP,
			String encoding) throws TransformerException{

		Map<String, List<String[]>> rootMap = new HashMap<String, List<String[]>>();

		rootMap.put("snCsv", scCsvRecords);

		Writer xmlFileWriter=null;
		try {
			// Get Template for PO Shipping Notices
			Template template = SPETemplateFactory
					.getInstance()
					.getTemplate(
							com.singpost.spe.esb.constants.SPETemplate._poSnTemplateName,
							encoding);

			// TODO try to get out of using tempFile...find other options

			File tmpXMLFile = new File(tmpFileFQP);
			xmlFileWriter = new FileWriter(tmpXMLFile);
			template.process(rootMap, xmlFileWriter);
			xmlFileWriter.flush();

			FileInputStream fis = new FileInputStream(tmpXMLFile);

			return fis;

		} catch (IOException e) {
			//log.error("IOException receieved while tranforming the ftl template: "+com.singpost.spe.esb.constants.SPETemplate._poSnTemplateName,e);
			Message msg=MessageFactory.createStaticMessage("IOException receieved while tranforming the ftl template: "+com.singpost.spe.esb.constants.SPETemplate._poSnTemplateName);
			throw new TransformerException(msg,e);
		} catch (TemplateException e) {
			//log.error("TemplateException receieved while tranforming the ftl template: "+com.singpost.spe.esb.constants.SPETemplate._poSnTemplateName,e);
			Message msg=MessageFactory.createStaticMessage("TemplateException receieved while tranforming the ftl template: "+com.singpost.spe.esb.constants.SPETemplate._poSnTemplateName+" FTL Stack:"+e.getFTLInstructionStack());
			throw new TransformerException(msg,e);
		} catch (ApplicationException e) {
			Message msg=MessageFactory.createStaticMessage("ApplicationException receieved on SPETemplateFactory  ");
			throw new TransformerException(msg,e);
			//e.printStackTrace();
		}
		finally
		{
			if ( xmlFileWriter != null )
			{
				try {
					xmlFileWriter.close();
				} catch (IOException e) {
					log.error("Error while closing the stream: ",e);
					/*Message msg=MessageFactory.createStaticMessage("Error while closing the stream: ");
					throw new TransformerException(msg,e);*/
				}
			}
		}
		//return null;

	}
	public static StringBuffer getErrMsgMailContent(ExceptionMessage exMsgObj,String outputEncoding)
	{
		StringBuffer errMsgMail=null;
		StringWriter stringWriter=null;
		Template tmplt;
		try {
				tmplt = SPETemplateFactory.getInstance().getTemplate(com.singpost.spe.esb.constants.SPETemplate._errMsgMailTemplateName,outputEncoding);
				Map<String, ExceptionMessage> root = new HashMap<String, ExceptionMessage>();
				
				root.put("exMsgObj", exMsgObj);
				
				stringWriter = new StringWriter();
				tmplt.process(root, stringWriter);
				
				errMsgMail = ((StringWriter) stringWriter).getBuffer();
				
		} catch (IOException e) {
			log.error("IOException receieved while tranforming the ftl template: "+com.singpost.spe.esb.constants.SPETemplate._errMsgMailTemplateName,e);			
		} catch (TemplateException e) {
			log.error("TemplateException receieved while tranforming the ftl template: "+com.singpost.spe.esb.constants.SPETemplate._errMsgMailTemplateName,e);			
		} catch (ApplicationException e) {			
			log.error("ApplicationException receieved on SPETemplateFactory: ",e);
		}
		finally
		{
			if ( stringWriter != null )
			{
				try {
					stringWriter.close();
				} catch (IOException e) {
					log.error("Error while closing the stream: ",e);					
				}
			}
		}
		
		return errMsgMail;
	}
	private static Template getTemplate(String encoding) throws ApplicationException
	{
		return SPETemplateFactory.getInstance().getTemplate(com.singpost.spe.esb.constants.SPETemplate._purchaseOrderTemplateName, encoding);
	}
	public static StringBuffer getPwdResetMail(byte[] resetXml,String encoding) throws TransformerException
	{
		Map<String, NodeModel> rootMap = new HashMap<String, NodeModel>();
		ByteArrayInputStream bResetXmlStream;
		InputSource bResetXmlSource;
		Writer stringWriter;
		try {
				bResetXmlStream=new ByteArrayInputStream(resetXml);
				bResetXmlSource=new InputSource(bResetXmlStream);
			
				rootMap.put("doc",freemarker.ext.dom.NodeModel.parse(bResetXmlSource));
			
				Template template = SPETemplateFactory.getInstance().getTemplate(com.singpost.spe.esb.constants.SPETemplate._pwdResetMailTemplateName,
						encoding);
				
				stringWriter = new StringWriter();
				template.process(rootMap, stringWriter);
				StringBuffer pwdResetMail = ((StringWriter) stringWriter).getBuffer();
		
				if ( log.isDebugEnabled() )
					log.debug("FTL-Transformed xml is: "+ pwdResetMail);

				log.info("Exiting getPwdResetMail - No issues");
				
				return pwdResetMail;
				
		} catch (IOException e) {			
			Message msg=MessageFactory.createStaticMessage("IOException received while loading/processing template:"+e.getMessage());
			throw new TransformerException(msg,e);
		} catch (TemplateException e) {			
			Message msg=MessageFactory.createStaticMessage("TemplateException received while loading/processing template:"+e.getFTLInstructionStack());
			throw new TransformerException(msg,e);
		} catch (SAXException e) {
			Message msg=MessageFactory.createStaticMessage("SAXException received while parsing input xml:"+e.getMessage());
			throw new TransformerException(msg,e);
		} catch (ParserConfigurationException e) {
			Message msg=MessageFactory.createStaticMessage("ParserConfigurationException received while parsing input xml:"+e.getMessage());
			throw new TransformerException(msg,e);
		} catch (ApplicationException e) {
			Message msg=MessageFactory.createStaticMessage("ApplicationException receieved on SPETemplateFactory: "+e.getDetailedMessage());
			throw new TransformerException(msg,e);
		}
		
	}
}
