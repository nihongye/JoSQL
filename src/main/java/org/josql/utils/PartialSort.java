package org.josql.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
/**
 * PartialSort see http://en.wikipedia.org/wiki/Partial_sorting 
 * and some idea come from http://code.google.com/p/guava-libraries/wiki/OrderingExplained
 * 
 * @author nihongye
 *
 * @param <T>
 */
public abstract class PartialSort<T> {
	
	private Comparator<? super T> comparator = new Comparator<T>() {
		public int compare(T o1, T o2) {
			return PartialSort.this.compare(o1, o2);
        }
	};
	
	@SuppressWarnings("unchecked")
	public List<T> getTopN(Collection<T> collection,int n){
        T[] array = (T[]) collection.toArray();
        sort(array,n);
        int maxSize = n <= array.length ? n : array.length;
        List<T> list = new ArrayList<T>();
        for(int i = 0;i < maxSize;i++){
        	list.add(array[i]);
        }
        return list;
	}
	
	public void sort(T[] array, int n){
		if(n >= array.length / 2){
			Arrays.sort(array,comparator);
		}else {
			quickfindFirstK(array, 0, array.length - 1, n);
		}
	}

	private final void quickfindFirstK(T[] array, int left,
	        int right, int k) {
		if (right > left) {
			int pivotIndex = (left + right) >>> 1;
			int pivotNewIndex = partition(array, left, right, pivotIndex);
			quickfindFirstK(array, left, pivotNewIndex - 1, k);
			if(pivotNewIndex < left + k){
				quickfindFirstK(array, pivotNewIndex + 1, right, k);
			}
		}
	}

	private int partition(T[] array, int left, int right,
	        int pivoIndex) {
		T pivotValue = array[pivoIndex];
		swapArray(array,pivoIndex,right);
		int storeIndex = left;
		for(int i = left;i < right;i++){
			if(compare(array[i],pivotValue) < 0){
				swapArray(array, i, storeIndex);
				storeIndex++;
			}
		}
		swapArray(array, storeIndex, right);
		return storeIndex;
	}
	
	public abstract int compare(T left, T right);

	private void swapArray(T[] list, int index1, int index2) {
		T tmp = list[index1];
		list[index1] = list[index2];
		list[index2] = tmp;
    }
	
	
	public static final <T> PartialSort<T> from(Comparator<T> comparator){
		return new ComparatorPartialSort<T>(comparator,false);
	}
	
	public static final <T> PartialSort<T> from(Comparator<T> comparator,boolean desc){
		return new ComparatorPartialSort<T>(comparator,desc);
	}
}
