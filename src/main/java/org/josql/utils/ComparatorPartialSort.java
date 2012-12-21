package org.josql.utils;

import java.util.Comparator;

public class ComparatorPartialSort<T> extends PartialSort<T> {

	private final Comparator<T> comparator;
	private final boolean desc;
	
	public ComparatorPartialSort(Comparator<T> comparator, boolean desc) {
	    super();
	    this.comparator = comparator;
	    this.desc = desc;
    }

	@Override
    public int compare(T o1, T o2) {
	    return desc ? comparator.compare(o2, o1) : comparator.compare(o1, o2);
    }

}
