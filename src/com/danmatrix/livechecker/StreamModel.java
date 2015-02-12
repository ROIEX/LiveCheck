package com.danmatrix.livechecker;

import android.graphics.Bitmap;

public interface StreamModel{
	abstract void setChannelName(String ChannelName);
	abstract boolean isLive();
	abstract String getDescription();
	abstract int getViewers();
	abstract void update();
	abstract boolean postUpdate();
	abstract String getLogoURL();
	abstract String getChannelName();
	abstract boolean isImageAvailable();
	abstract Bitmap GetImage();
	abstract String getExtraData();
}
