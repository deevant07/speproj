/**
 * 
 */
package com.singpost.spe.ext.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.mule.config.i18n.Message;
import org.mule.config.i18n.MessageFactory;

import com.singpost.spe.esb.exception.ApplicationException;

/**
 * @author X-VEVISW
 *
 */
public class SPEFileUtils extends FileUtils {
	
	/**
	 * @param fqFileName - fully qualified file name; file must exist
	 * @param targetFolder - target folder path where file needs to be moved
	 */
	public static void moveFileToFolder(final String fqFileName, final String targetFolder) throws ApplicationException{
		
		try {
			
			File file = new File(fqFileName);
			File destFolder = new File(targetFolder);
			
			moveToDirectory(file, destFolder, true);
		} catch (IOException e) {
			//TODO handle exception....
			Message msg=MessageFactory.createStaticMessage("IOException while copying file: "+e.getMessage());
			throw new ApplicationException(msg);
		}
		
	}
	
	/**
	 * @param bArr
	 * @param fileName
	 * @param folder
	 * @throws IOException
	 */
	public static void writeBytesToFile(byte[] bArr,String fileName,String folder) throws IOException
	{
		File toFile=new File(folder + File.separator + fileName);
		writeByteArrayToFile(toFile, bArr);	
	}
	
	/**
	 * closeInputStream - closes input stream and suppresses all exceptions
	 * @param is
	 */
	public static void closeInputStream(InputStream is){
		if (is != null){
			try {
				is.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}
