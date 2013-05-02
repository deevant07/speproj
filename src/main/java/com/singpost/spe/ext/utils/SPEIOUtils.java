/**
 * 
 */
package com.singpost.spe.ext.utils;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;

/**
 * @author X-VEVISW
 *
 */
public class SPEIOUtils {
	
	public static byte[] toByteArray(File file){
		
		FileInputStream fis = null;
		
		try {
			fis =  new FileInputStream(file);
			return IOUtils.toByteArray(fis);
			
	   } catch (java.io.IOException e){
		   
		   	System.out.println(" IO exception condition"+e);
	   
	   } finally {
			SPEFileUtils.closeInputStream(fis);
	   }
		return null;
	   
	}

}
