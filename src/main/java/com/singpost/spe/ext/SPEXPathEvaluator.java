/**
 * 
 */
package com.singpost.spe.ext;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.transformer.TransformerException;
import org.mule.config.i18n.Message;
import org.mule.config.i18n.MessageFactory;

import com.singpost.spe.ext.utils.SPEIOUtils;
import com.ximpleware.AutoPilot;
import com.ximpleware.EOFException;
import com.ximpleware.EncodingException;
import com.ximpleware.EntityException;
import com.ximpleware.NavException;
import com.ximpleware.ParseException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

/**
 * @author X-VEVISW
 *
 */
public class SPEXPathEvaluator {
	

	private static final Log log= LogFactory.getLog(SPEXPathEvaluator.class);
	
	public static String evalXpath(String expression, byte[] inp) throws TransformerException {
		
		
		//TODO validate input args
		
		if ( log.isDebugEnabled() )
		{
			log.debug("Expression for XPath Evaluation: "+expression);
		}
		
		String expResult=null;
		VTDGen vg = new VTDGen();
	    VTDNav nav = null;       
	        	    		    	      
      	
		try {
				vg.setDoc(inp);
		        vg.parse(true);
				nav = vg.getNav();
				AutoPilot ap=new AutoPilot(nav);
				ap.selectXPath(expression);
				
				int result = -1;
				int index=expression.indexOf("/@");   
			    while((result = ap.evalXPath())!=-1){	
			    						
					int t=-1;
					if ( index != -1 )
					{
						t = nav.getAttrVal(expression.substring(index+2)); 
						
					}else
					{
						t=nav.getText(); 		
					}
					if (t!=-1)
					{
						expResult=nav.toNormalizedString(t);
									
					}		
					
			    }
			    
		} catch (XPathParseException e) {
			Message msg=MessageFactory.createStaticMessage("XPathParseException with error message:"+e.getMessage());
			throw new TransformerException(msg,e);			
		} catch (NavException e) {
			Message msg=MessageFactory.createStaticMessage("NavException with error message:"+e.getMessage());
			throw new TransformerException(msg,e);
		} catch (XPathEvalException e) {
			Message msg=MessageFactory.createStaticMessage("XPathEvalException with error message:"+e.getMessage());
			throw new TransformerException(msg,e);
		} catch (EncodingException e) {
			Message msg=MessageFactory.createStaticMessage("EncodingException with error message:"+e.getMessage());
			throw new TransformerException(msg,e);
		} catch (EOFException e) {
			Message msg=MessageFactory.createStaticMessage("EOFException with error message:"+e.getMessage());
			throw new TransformerException(msg,e);
		} catch (EntityException e) {
			Message msg=MessageFactory.createStaticMessage("EntityException with error message:"+e.getMessage());
			throw new TransformerException(msg,e);
		} catch (ParseException e) {
			Message msg=MessageFactory.createStaticMessage("ParseException with error message:"+e.getMessage());
			throw new TransformerException(msg,e);
		}	
		
		if ( log.isDebugEnabled() )
		{
			log.debug("Result of XPath Evaluation:"+expResult +" for expression- "+expression);
		}
	   return expResult;   	      
	}
	
	/**
	 * @param fqFileName - Fully qualified (with folder path) file
	 * @param file
	 * @return
	 * @throws TransformerException
	 */
	public static String evalXpath(String expression, File file) throws TransformerException{
		
		return evalXpath(expression,SPEIOUtils.toByteArray(file));
	}
}
