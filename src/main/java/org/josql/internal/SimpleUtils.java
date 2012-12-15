package org.josql.internal;

import java.util.Collection;
/**
 * Simple Compare tool to replace Utilities,
 * because instanceof in Utilities is slower than compare integer 
 * @author nihongye
 *
 */
public class SimpleUtils {

	public static final int EXPECTED_COLLECTION = 1;
	public static final int EXPECTED_SPECIAL = 2;
	public static final int EXPECTED_OBJECT = 3;

	public static boolean equals(Object l, Object r, boolean ignoreCase,
			boolean not, int expectedValueType) {
		boolean checkDirect = expectedValueType == EXPECTED_SPECIAL;
		if (expectedValueType == EXPECTED_OBJECT && !(l instanceof Collection)
				&& !(r instanceof Collection)) {
			checkDirect = true;
		}
		if (checkDirect) {
			if (ignoreCase) {
				l = l.toString().toLowerCase();
				r = r.toString().toLowerCase();
			}
			return not ? !l.equals(r) : l.equals(r);
		}
		return complexEquals(l, r, ignoreCase, not);
	}

	private static boolean complexEquals(Object l, Object r,
			boolean ignoreCase, boolean not) {
		return Utilities.matches(l, r, ignoreCase, Utilities.EQ, not);
	}

	public static boolean compare(Object l, Object r, boolean ignoreCase,
			int compareType, int expectedValueType) {
		boolean checkDirect = expectedValueType == EXPECTED_SPECIAL;
		if (expectedValueType == EXPECTED_OBJECT && !(l instanceof Collection)
				&& !(r instanceof Collection)) {
			checkDirect = true;
		}
		if (checkDirect) {
			int result = compare(l, r, ignoreCase);
			if(compareType == Utilities.GT){
				return result > 0;
			}else if(compareType == Utilities.GTE){
				return result >= 0;
			}if(compareType == Utilities.LT){
				return result < 0;
			}else if(compareType == Utilities.LTE){
				return result <= 0;
			}
		}
		
		return complexCompare(l, r, ignoreCase, compareType);
	}
	
	private static boolean complexCompare(Object l, Object r,
			boolean ignoreCase,int compareType) {
		return Utilities.matches(l, r, ignoreCase, compareType,false);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int compare(Object o1, Object o2,boolean ignoreCase) {
		if ((o1 == null) && (o2 == null)) {
			return 0;
		}
		if ((o1 == null) && (o2 != null)) {
			return 1;
		}
		if ((o1 != null) && (o2 == null)) {
			return -1;
		}
		if(ignoreCase){
			return o1.toString().toLowerCase().compareTo(o2.toString().toLowerCase());
		}
		if ((o1 instanceof Number) && (o2 instanceof Number)) {
			return Utilities.getDoubleObject(o1).compareTo(
					Utilities.getDoubleObject(o2));
		}

		if ((o1 instanceof Comparable) && (o2 instanceof Comparable)
				&& (o1.getClass().isAssignableFrom(o2.getClass()))) {
			return ((Comparable) o1).compareTo((Comparable) o2);
		}

		// Force a string comparison.
		String s1 = o1.toString();
		String s2 = o2.toString();
		return s1.compareTo(s2);
	}
}
