package net.sf.josql.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;

import org.josql.utils.PartialSort;

public class PartialSortTest extends TestCase{
	
	private Random random = new Random(System.currentTimeMillis());
	private Comparator<Integer> comparator;
	
	public void setUp(){
		comparator = new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				if(o1 != null && o2 != null){
					return o1.compareTo(o2);
				}
				if(o1 == null && o2 == null){
					return 0;
				}
				return o1 != null && o2 == null ? -1 : 1 ;
            }
		};	
	}
	
	public void testTopLeast(){
		int count = 100000;
		Integer[] array = createIntegers(count,100);
		PartialSort<Integer> partialSort = PartialSort.from(comparator);
		int k = 1000;
		Integer[] tops = partialSort.sort(array, k).toArray(new Integer[0]);
		verifyLeastResult(tops,array,k);
	}
	
	public void testTopLeastPerformance(){
		int count = 500 * 10000;
		Integer[] array = createIntegers(count,5);
		PartialSort<Integer> partialSort = PartialSort.from(comparator);
		int k = 100;
		long begin = System.currentTimeMillis();
		List<Integer> sort = partialSort.sort(array, k);
		long end  = System.currentTimeMillis();
		System.out.println("pay "+(end - begin)+" millis to sort top 1000 of array which contain "+count+" element");
//		System.out.println(array.size());
	}

	
	public void testTopGreatest(){
		int count = 10000;
		Integer[] array = createIntegers(count,10000);
		PartialSort<Integer> partialSort = PartialSort.from(comparator,true);
		int k = 300;
		Integer[] tops = partialSort.sort(array, k).toArray(new Integer[0]);
		verifyGreatestResult(tops,array,k);
	}
	
	public void testFallbackToFullSort(){
		int count = 10000;
		Integer[] array = createIntegers(count,100000);
		PartialSort<Integer> partialSort = PartialSort.from(comparator);
		int k = 10000 / 2 + 1;
		Integer[] tops = partialSort.sort(array, k).toArray(new Integer[0]);
		verifyLeastResult(tops,array,k);
	}
	
	public void testFallbackToFullSortDesc(){
		int count = 10000;
		Integer[] array = createIntegers(count,100000);
		PartialSort<Integer> partialSort = PartialSort.from(comparator,true);
		int k = 10000 / 2 + 1;
		Integer[] tops = partialSort.sort(array, k).toArray(new Integer[0]);
		verifyGreatestResult(tops,array,k);
	}
	
	public void testGetTopN(){
		int count = 10000;
		Integer[] array = createIntegers(count,100000);
		PartialSort<Integer> partialSort = PartialSort.from(comparator,true);
		int k = 200;
		Integer[] tops = partialSort.sort(array, k).toArray(new Integer[0]);
		verifyGreatestResult(tops,array,k);
	}
	
	public void testKBigThanArray(){
		int count = 10;
		Integer[] array = createIntegers(count,100000);
		PartialSort<Integer> partialSort = PartialSort.from(comparator);
		int k = 300;
		Integer[] tops = partialSort.sort(array, k).toArray(new Integer[0]);
		verifyLeastResult(tops,array,k);
	}
	
	
	
	
	private void verifyGreatestResult(Integer[] tops,Integer[] array,int k) {
	    //sort the list
	    Arrays.sort(array,Collections.reverseOrder());
	    for(int i = 0;i < tops.length && i < k;i++){
	    	assertEquals(array[i], tops[i]);
	    }
    }


	private void verifyLeastResult(Integer[] tops,Integer[] array, int k) {
		//sort the list
	    Arrays.sort(array);
	    for(int i = 0;i < tops.length && i < k;i++){
	    	assertEquals(array[i], tops[i]);
	    }
    }

	private Integer[] createIntegers(int count,int scope) {
	    Integer[] list = new Integer[count];
		for(int i = 0;i < count;i++){
			list[i] = random.nextInt(scope);
		}
	    return list;
    }
	
}
