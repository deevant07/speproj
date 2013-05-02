/**
 * 
 */
package com.singpost.spe.esb.constants;

/**
 * @author X-VEVISW
 *
 */
public interface SPETemplate {

//	static final String _templateDirectory = "D:\\SingPost\\application-files\\ftl-templates";
//	static final String _tempFolderPath = "D:\\SingPost\\application-files\\newpo\\temp";
	
	final static String _poAckTemplateName = "spe-po-ack.ftl";
	final static String _poSnTemplateName = "spe-po-ship-confirm.ftl";
	final static String _purchaseOrderTemplateName="PurchaseOrderXpathcsv.ftl";
	final static String _templateDefaultFolder="/tmp/ftl-templates";
	final static String _errMsgMailTemplateName="spe-devops-email-msg.ftl";
	final static String _pwdResetMailTemplateName="pwd-reset-mail.ftl";
	
}
