package com.singpost.spe.esb.listeners;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.context.notification.MuleContextNotificationListener;
import org.mule.context.notification.MuleContextNotification;

import com.singpost.spe.esb.exception.ApplicationException;
import com.singpost.spe.esb.expressions.CustomExpressionEvaluator;
import com.singpost.spe.esb.templates.SPETemplateFactory;

public class PropertyInitilizer implements MuleContextNotificationListener<MuleContextNotification> {
	
	
	private String templateDirectory;
	
	protected static final Log log= LogFactory.getLog(PropertyInitilizer.class);

	/**
	 * @return the templateDirectory
	 */
	public String getTemplateDirectory() {
		return templateDirectory;
	}



	/**
	 * @param templateDirectory the templateDirectory to set
	 */
	public void setTemplateDirectory(String templateDirectory) {
		this.templateDirectory = templateDirectory;
	}



	public void onNotification(MuleContextNotification notification) {
		if ( notification.getAction() == MuleContextNotification.CONTEXT_STARTED )
		{			
			try {
			SPETemplateFactory.getInstance(templateDirectory);
			notification.getMuleContext().getExpressionManager().registerEvaluator(new CustomExpressionEvaluator());
			} catch (ApplicationException e) {
				log.error("Exception recieved during startup, exititng the system", e);
				//e.printStackTrace();
				System.exit(-1);
			}
			
		}
		
	}

}
