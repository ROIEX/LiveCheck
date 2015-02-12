package com.danmatrix.livechecker;

import java.util.Comparator;

public class ChannelNameComparator implements Comparator<StreamModel>{

	@Override
	public int compare(StreamModel a, StreamModel b) {
		return a.getChannelName().compareTo(b.getChannelName());
	}

}

/*
if(a.getViewers()<b.getViewers())
			return 1;
		if(a.getViewers()==b.getViewers())
			return 0;
 */
