package com.singpost.spe.esb.exception;

import org.mule.api.MuleException;
import org.mule.config.i18n.Message;

public class ApplicationException extends MuleException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4590618848074810005L;

	public ApplicationException(Message msg) {
		super(msg);
	}
	public ApplicationException(Message msg,Throwable t) {
		super(msg,t);
	}
}
