/**
 * 
 */
package com.singpost.spe.esb.transformers.sn;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;

import com.singpost.spe.esb.config.SPEConfig;
import com.singpost.spe.esb.constants.SPEApp;
import com.singpost.spe.esb.constants.SPEConfigKey;
import com.singpost.spe.esb.transformers.base.SPEBaseMessageTransformer;

/**
 * @author X-VEVISW
 *
 */
public class SPESnFormatter extends SPEBaseMessageTransformer {

	/* (non-Javadoc)
	 * @see org.mule.transformer.AbstractMessageTransformer#transformMessage(org.mule.api.MuleMessage, java.lang.String)
	 */
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		
		if ( logger.isDebugEnabled() )
		{
			this.logger.debug("Message" + message);
			this.logger.debug(message.getPayload());
		}	
		
		BufferedReader br = null;
		
		String orgMsgFilename = message.getOutboundProperty("originalFilename");
		if (StringUtils.isEmpty(orgMsgFilename)){
			//TODO: throw exception
		}
		SPEConfig speConf = getSpeConfig();
		if (speConf == null ){
			//TODO: throw exception
		}
		
		Object payloadObj = message.getPayload();

		FileOutputStream fos = null;
		PrintWriter out=null;
		
		try {
			if ( payloadObj instanceof InputStream ){
				
				br = new BufferedReader(new InputStreamReader((InputStream) payloadObj,SPEApp.Encoding._utf8.value()));
				
			}else if (payloadObj instanceof byte[] ){
				
				br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream((byte[])payloadObj),SPEApp.Encoding._utf8.value()));
	
			}else if (payloadObj instanceof String ){
				
				br = new BufferedReader(new InputStreamReader(IOUtils.toInputStream((String)payloadObj, outputEncoding),SPEApp.Encoding._utf8.value()));
				
			}else{
				//TODO - handle exception
			}
			
			String currentLine;
			int counter=1;
			while ((currentLine = br.readLine()) != null) {
				if ('M' == currentLine.charAt(0)){
					if (out != null){
						out.flush();
						out.close();
					}
					if (fos != null){
						fos.flush();
						fos.close();
					}
					fos = new FileOutputStream(getSpeConfig().get(SPEConfigKey._snInboundFolder)+File.separator+counter+"_"+orgMsgFilename);
					out = new PrintWriter(new OutputStreamWriter(fos,"UTF-8"));
					counter++;
				}
				out.println(currentLine);
				out.flush();
					
			}
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(fos);
		
		}catch(IOException ioe){
			//TODO handle....
			this.logger.error("Error ....");
			ioe.printStackTrace();
		} finally {
			if (out != null) IOUtils.closeQuietly(out);
			if (fos != null) IOUtils.closeQuietly(fos);			
		}
		
		return "";
			
	}
	

}
