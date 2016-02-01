package com.zlp.dao;

import com.mongodb.async.SingleResultCallback;

public class SaveOneListener implements SingleResultCallback<Void> {
	
	private boolean result;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	@Override
	public void onResult(final Void result, final Throwable t) {
		setResult(Boolean.parseBoolean(result.toString()));
	}
	
	

}
