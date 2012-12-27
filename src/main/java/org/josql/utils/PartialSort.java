package org.josql.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * PartialSort ,see 
 * http://en.wikipedia.org/wiki/Partial_sorting
 * http://en.wikipedia.org/wiki/Selection_algorithm
 * http://en.wikipedia.org/wiki/Introsort
 * the partial sort algorithm copy from http://code.google.com/p/guava-libraries/ guava-14.0-rc1
 * and made some change like Introsort,when quick select algorithm in worce case,change to use jdk to sort buffer
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
	public List<T> getTopN(Collection<T> collection, int n) {
		T[] array = null;
		if (n >= collection.size() / 2) {
			array = (T[]) collection.toArray();
			Arrays.sort(array, comparator);
		} else {
			array = quickfindFirstK(collection, n);
		}
		return toList(array, n);
	}

	private List<T> toList(T[] array, int n) {
		int maxSize = n <= array.length ? n : array.length;
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < maxSize; i++) {
			list.add(array[i]);
		}
		return list;
	}

	public List<T> sort(T[] array, int n) {
		if (n >= array.length / 2) {
			Arrays.sort(array, comparator);
		} else {
			array = quickfindFirstK(Arrays.asList(array), n);
		}
		return toList(array, n);
	}
	/**
	 * the partial sort algorithm copy from http://code.google.com/p/guava-libraries/ guava-14.0-rc1
	 * and made some change like Introsort,when quick select algorithm in worce case,change to use jdk to sort buffer
	 * @param array
	 * @param k
	 * @return
	 */
	private final T[] quickfindFirstK(Iterable<T> array, int k) {
		/*
		 * Our goal is an O(n) algorithm using only one pass and O(k) additional
		 * memory.
		 * 
		 * We use the following algorithm: maintain a buffer of size 2*k. Every
		 * time the buffer gets full, find the median and partition around it,
		 * keeping only the lowest k elements. This requires n/k
		 * find-median-and-partition steps, each of which take O(k) time with a
		 * traditional quickselect.
		 * 
		 * After sorting the output, the whole algorithm is O(n + k log k). It
		 * degrades gracefully for worst-case input (descending order), performs
		 * competitively or wins outright for randomly ordered input, and
		 * doesn't require the whole collection to fit into memory.
		 */
		Iterator<T> elements = array.iterator();
		int bufferCap = k * 2;
		@SuppressWarnings("unchecked")
		// we'll only put E's in
		T[] buffer = (T[]) new Object[bufferCap];
		T threshold = elements.next();
		buffer[0] = threshold;
		int bufferSize = 1;
		// threshold is the kth smallest element seen so far. Once bufferSize >=
		// k,
		// anything larger than threshold can be ignored immediately.

		while (bufferSize < k && elements.hasNext()) {
			T e = elements.next();
			buffer[bufferSize++] = e;
			threshold = max(threshold, e);
		}
		int compares= 0;
		while (elements.hasNext()) {
			T e = elements.next();
			if (compare(e, threshold) >= 0) {
				continue;
			}

			buffer[bufferSize++] = e;
			if (bufferSize == bufferCap) {
				// We apply the quickselect algorithm to partition about the
				// median,
				// and then ignore the last k elements.
				int left = 0;
				int right = bufferCap - 1;

				int minThresholdPosition = 0;
				// The leftmost position at which the greatest of the k lower
				// elements
				// -- the new value of threshold -- might be found.
				
				// use to track the quick select worse case,if beyond loopMax change to use JDK sort
				int loopCount = 0;
				int loopMax = (int) Math.log(bufferCap) * 4;
				while (left < right) {
					int pivotIndex = (left + right + 1) >>> 1;
					int pivotNewIndex = partition(buffer, left, right,
					        pivotIndex);
					compares += (right - left);
					if (pivotNewIndex > k) {
						right = pivotNewIndex - 1;
					} else if (pivotNewIndex < k) {
						left = Math.max(pivotNewIndex, left + 1);
						minThresholdPosition = pivotNewIndex;
					} else {
						break;
					}
					loopCount++;
					if (loopCount == loopMax) {
						Arrays.sort(buffer, comparator);
						break;
					}
				}
				bufferSize = k;
				threshold = buffer[minThresholdPosition];
				for (int i = minThresholdPosition + 1; i < bufferSize; i++) {
					threshold = max(threshold, buffer[i]);
				}
			}
		}

		Arrays.sort(buffer, 0, bufferSize, comparator);
		bufferSize = Math.min(bufferSize, k);
		T[] result = (T[]) new Object[bufferSize];
		System.arraycopy(buffer, 0, result, 0, bufferSize);
		return result;
	}

	private T max(T threshold, T e) {
		return compare(threshold, e) >= 0 ? threshold : e;
	}

	private int partition(T[] array, int left, int right, int pivoIndex) {
		T pivotValue = array[pivoIndex];
		swapArray(array, pivoIndex, right);
		int storeIndex = left;
		for (int i = left; i < right; i++) {
			if (compare(array[i], pivotValue) < 0) {
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

	public static final <T> PartialSort<T> from(Comparator<T> comparator) {
		return new ComparatorPartialSort<T>(comparator, false);
	}

	public static final <T> PartialSort<T> from(Comparator<T> comparator,
	        boolean desc) {
		return new ComparatorPartialSort<T>(comparator, desc);
	}
}
