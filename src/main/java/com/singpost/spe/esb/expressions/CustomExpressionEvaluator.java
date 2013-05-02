package com.singpost.spe.esb.expressions;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;

import org.mule.api.MuleMessage;
import org.mule.api.expression.ExpressionEvaluator;

import com.ximpleware.AutoPilot;
import com.ximpleware.IndexReadException;
import com.ximpleware.NavException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

public class CustomExpressionEvaluator implements ExpressionEvaluator {

	XMLInputFactory xmlInputFactory;
	public CustomExpressionEvaluator() {
		xmlInputFactory = XMLInputFactory.newInstance();
	}
	@Override
	public String getName() {
		return "custom";
	}

	@Override
	public Object evaluate(String expression, MuleMessage message) {
		
		String value="";
		if ( expression != null )
		{
			if ( expression.equals("payloadAsString"))
			{
				try {
					value= message.getPayloadAsString();
				} catch (Exception e) {	
					value="Exception while converting payload to string";
					e.printStackTrace();
				}
			}else if (expression.startsWith("[xpath"))
			{
				int x = expression.indexOf("]");
	            if (x == -1)
	            {
	                throw new IllegalArgumentException("Expression is malformed: " + expression);
	            }
		            
				String[] subExpr=expression.split("-");
				if ( subExpr != null && subExpr.length > 1 )
				{
					if ( subExpr[0].equals("[xpath]"))
						value=evalXpath(subExpr[1],message);
				}
				
			}
			else
			{
				value="UnKnown Expression";
			}
		}
		
		if ( value.length() > 0)
			return value;
		else
			return (value="Null Expression");
		
	}

	private String evalXpath(String expression, MuleMessage message) {
		
		long t1=System.currentTimeMillis();
		String expResult=null;
		Object src=message.getPayload();
		if ( src instanceof InputStream )
		{
			InputStream srcIS=(InputStream)src;			
			VTDGen vg=new VTDGen();
			try {				
					VTDNav nav=vg.loadIndex(srcIS);
					AutoPilot ap=new AutoPilot(nav);
					ap.selectXPath(expression);
					
					int result = -1;
				    int count = 0;
				    while((result = ap.evalXPath())!=-1){
					System.out.print(""+result+"  ");     
					System.out.print("Element name ==> "+nav.toString(result));
					int t = nav.getText(); // get the index of the text (char data or CDATA)
					if (t!=-1)
					{
						expResult=nav.toNormalizedString(t);
						System.out.println(" Text  ==> "+expResult);						
					}
					System.out.println("\n ============================== ");
					count++;
				    }
				    System.out.println("Total # of element "+count);
					
			} catch (IndexReadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XPathParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XPathEvalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NavException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
    
        long t2=System.currentTimeMillis();
        
        System.out.println("Total Time: "+(t2-t1));
		
		return expResult;
	}

}
