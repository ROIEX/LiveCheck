package com.danmatrix.livechecker;

import java.util.Comparator;

public class ViewersComparator implements Comparator<StreamModel> {

	@Override
	public int compare(StreamModel a, StreamModel b) {
		if(a.getViewers()<b.getViewers())
			return 1;
		if(a.getViewers()==b.getViewers())
			return 0;
		return -1;
	}
	
}
