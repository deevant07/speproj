package com.singpost.spe.esb.exception;

import java.io.InputStream;

import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.exception.AbstractMessagingExceptionStrategy;

public class ExceptionListener extends AbstractMessagingExceptionStrategy {

	//private String mListenerName;

	public ExceptionListener(final MuleContext inMuleContext) {
		super(inMuleContext);
	}

	@Override
	protected void logException(final Throwable inException) {
		/* Never log exceptions here. */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mule.exception.AbstractMessagingExceptionStrategy#doHandleException
	 * (java.lang.Except ion, org.mule.api.MuleEvent,
	 * org.mule.api.exception.RollbackSourceCallback)
	 */
	@Override
	protected void doHandleException(final Exception inException,
			final MuleEvent inEvent) {			
		
		
		this.logger.error("Exception receieved with message:"+inException.getMessage());
		this.logger.error("Payload Message at Exception:");
		inException.printStackTrace();
		this.logger.error("####################################################");
		try {
			if (!( inEvent.getMessage().getPayload() instanceof InputStream ) )
				this.logger.error(inEvent.getMessage().getPayloadAsString());
		} catch (Exception e) {
			this.logger.error("Exception while converting Payload to String",e);			
		}
		this.logger.error("####################################################");
		/*
		 * Let the superclass handle transactions, routing etc of the exception.
		 */		
		super.doHandleException(inException, inEvent);
	}

	/*public String getListenerName() {
		return mListenerName;
	}

	public void setListenerName(String inListenerName) {
		mListenerName = inListenerName;
	}*/
	@Override
	protected boolean isRollback(Throwable t) {
		
		return true;
	}	
}
