package com.zlp.dao;


import com.mongodb.async.SingleResultCallback;
import com.mongodb.client.result.UpdateResult;

public class UpdateOneListener implements SingleResultCallback<UpdateResult> {
	
	private boolean result;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	@Override
	public void onResult(final UpdateResult result, final Throwable arg1) {
		setResult(result.getModifiedCount() >= 0);
	}

}
