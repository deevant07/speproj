/**
 * 
 */
package com.singpost.spe.esb.context;

import org.mule.api.MuleContext;
import org.mule.api.annotations.expressions.Lookup;
import org.mule.api.context.MuleContextAware;

import com.singpost.spe.esb.config.SPEConfig;
import com.singpost.spe.esb.constants.SPEBean;

/**
 * @author X-VEVISW
 *
 */
public class SPEEsbContext implements MuleContextAware {

	private static volatile SPEEsbContext speEsbContext;
	
	private SPEConfig speConfig;

	
	private MuleContext speContext;

	private void init(){
		if (this.speConfig == null && speContext != null){
			this.speConfig = (SPEConfig)this.speContext.getRegistry().lookupObject(SPEBean._speConfig);
		}else{
			//TODO throw exception
			System.out.println("NULL mulecontext");
		}
		
	}
	
	//DCL based singleton
	public static SPEEsbContext getInstance(){
		if (speEsbContext == null){
			synchronized (SPEEsbContext.class){
				if (speEsbContext == null){
					speEsbContext=new SPEEsbContext();
					speEsbContext.init();
				}
			}
		}
		return speEsbContext;
	}
	public SPEConfig getSpeConfig() {
		return this.speConfig;
	}

	@Override
	public void setMuleContext(MuleContext context) {
		this.speContext = context;
		
	}

	

}
