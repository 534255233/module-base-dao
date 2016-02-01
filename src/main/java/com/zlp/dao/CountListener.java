package com.zlp.dao;

import com.mongodb.async.SingleResultCallback;

public class CountListener implements SingleResultCallback<Long> {
	
	private long result;
	
	public long getResult() {
		return result;
	}

	public void setResult(long result) {
		this.result = result;
	}

	@Override
	public void onResult(final Long result, Throwable t) {
		setResult(result);
	}

}
