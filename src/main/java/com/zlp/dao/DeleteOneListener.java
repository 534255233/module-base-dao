package com.zlp.dao;


import com.mongodb.async.SingleResultCallback;
import com.mongodb.client.result.DeleteResult;

public class DeleteOneListener implements SingleResultCallback<DeleteResult> {
	
	private boolean result;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	@Override
	public void onResult(final DeleteResult result, final Throwable arg1) {
		setResult(result.getDeletedCount() >= 0);
	}

}
