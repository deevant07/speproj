package com.singpost.spe.esb.templates;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.config.i18n.Message;
import org.mule.config.i18n.MessageFactory;

import com.singpost.spe.esb.constants.SPETemplate;
import com.singpost.spe.esb.exception.ApplicationException;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class SPETemplateFactory {

	private Configuration config;;
	
	private static volatile SPETemplateFactory templateFactory;
	
	protected static final Log log= LogFactory.getLog(SPETemplateFactory.class.getName());
	
	private String ftlBaseFolder;

	private SPETemplateFactory(String baseFolder) throws ApplicationException{
		this.loadTemplates(baseFolder);
	}
	
	public static SPETemplateFactory getInstance() throws ApplicationException{
		if (templateFactory == null){
			return getInstance(null);
		}else{
			return templateFactory;
		}
	}
	//DCL based singleton
	public static SPETemplateFactory getInstance(String baseFolder) throws ApplicationException{
		if (templateFactory == null){
			synchronized (SPETemplateFactory.class){
				if (templateFactory == null){
					templateFactory = new SPETemplateFactory(baseFolder);
				}
			}
		}
		return templateFactory;
		
	}
	
	public Template getTemplate(String templateName, String encoding) throws ApplicationException{
		Template template = null;
		
		try {
			
			template = config.getTemplate(templateName, encoding);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Message msg=MessageFactory.createStaticMessage("Error loading template: "+templateName);
			throw new ApplicationException(msg,e);
		}
		return template;
	}	

	private void loadTemplates(String baseFolder) throws ApplicationException
	{
		try{
			config = null;
			config = new Configuration();

			config.setDirectoryForTemplateLoading(getFtlBaseFolder(baseFolder));
			
			//TODO Loop through all the *.ftl files from the ftlFolder and load them instead of individually loading 
			Template[] tmplts=new Template[5];
			tmplts[0]=config.getTemplate(SPETemplate._purchaseOrderTemplateName);
			tmplts[1]=config.getTemplate(SPETemplate._poAckTemplateName);
			tmplts[2]=config.getTemplate(SPETemplate._poSnTemplateName);
			tmplts[3]=config.getTemplate(SPETemplate._errMsgMailTemplateName);
			tmplts[4]=config.getTemplate(SPETemplate._pwdResetMailTemplateName);

		} catch (IOException e) {
			//log.error("Error loading templates");
			//e.printStackTrace();
			Message msg=MessageFactory.createStaticMessage("Error loading templates ");
			throw new ApplicationException(msg,e);
		}
	}
	
	private static File getFtlBaseFolder(String baseFolder){
		
		//if null fall back to System env
		if (baseFolder == null || "".equals(baseFolder))
			baseFolder = System.getProperty("ftlDir");
		
		//default to tmp location if not found in env
		if (baseFolder == null)
			baseFolder = SPETemplate._templateDefaultFolder;
		
		File dir = new File(baseFolder);
		return dir;
	}
	
	public void setFtlBaseFolder(String ftlBaseFolder) {
		this.ftlBaseFolder = ftlBaseFolder;
	}
	
}
