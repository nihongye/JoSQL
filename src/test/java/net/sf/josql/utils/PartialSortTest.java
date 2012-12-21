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
		int count = 10000;
		Integer[] array = createIntegers(count,10000);
		PartialSort<Integer> partialSort = PartialSort.from(comparator);
		int k = 1000;
		partialSort.sort(array, k);
		verifyLeastResult(array,k);
	}
	
	public void testTopLeastPerformance(){
		int count = 100 * 10000;
		Integer[] array = createIntegers(count,1000 * 10000);
		PartialSort<Integer> partialSort = PartialSort.from(comparator);
		int k = 1000;
		long begin = System.currentTimeMillis();
		partialSort.sort(array, k);
		long end  = System.currentTimeMillis();
		System.out.println("pay "+(end - begin)+" millis to sort top 1000 of array which contain "+count+" element");
	}

	
	public void testTopGreatest(){
		int count = 10000;
		Integer[] array = createIntegers(count,10000);
		PartialSort<Integer> partialSort = PartialSort.from(comparator,true);
		int k = 300;
		partialSort.sort(array, k);
		verifyGreatestResult(array,k);
	}
	
	public void testFallbackToFullSort(){
		int count = 10000;
		Integer[] array = createIntegers(count,100000);
		PartialSort<Integer> partialSort = PartialSort.from(comparator);
		int k = 10000 / 2 + 1;
		partialSort.sort(array, k);
		verifyLeastResult(array,k);
	}
	
	public void testFallbackToFullSortDesc(){
		int count = 10000;
		Integer[] array = createIntegers(count,100000);
		PartialSort<Integer> partialSort = PartialSort.from(comparator,true);
		int k = 10000 / 2 + 1;
		partialSort.sort(array, k);
		verifyGreatestResult(array,k);
	}
	
	public void testGetTopN(){
		int count = 10000;
		Integer[] array = createIntegers(count,100000);
		PartialSort<Integer> partialSort = PartialSort.from(comparator,true);
		int k = 200;
		List<Integer> list = partialSort.getTopN(Arrays.asList(array), k);
		verifyGreatestResult((Integer[])list.toArray(new Integer[0]),k);
	}
	
	public void testKBigThanArray(){
		int count = 10;
		Integer[] array = createIntegers(count,100000);
		PartialSort<Integer> partialSort = PartialSort.from(comparator);
		int k = 300;
		partialSort.sort(array, k);
		verifyLeastResult(array,k);
	}
	
	
	
	
	private void verifyGreatestResult(Integer[] list,int k) {
	    Integer[] tops = new Integer[k];
	    System.arraycopy(list, 0, tops, 0, k);
	    //sort the list
	    Arrays.sort(list,Collections.reverseOrder());
	    for(int i = 0;i < k;i++){
	    	assertEquals(list[i], tops[i]);
	    }
    }


	private void verifyLeastResult(Integer[] list,int k) {
		Integer[] tops = new Integer[k > list.length ? list.length : k];
	    System.arraycopy(list, 0, tops, 0, tops.length);
	    //sort the list
	    Arrays.sort(list);
	    for(int i = 0;i < tops.length;i++){
	    	assertEquals(list[i], tops[i]);
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
