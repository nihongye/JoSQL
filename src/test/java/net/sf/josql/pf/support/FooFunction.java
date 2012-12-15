package net.sf.josql.pf.support;

import java.lang.reflect.Method;

public class FooFunction {
	private Method nameMethod;
	private Method ageMethod;

	public Object f(Object obj, String name) {
		try {
			if (nameMethod == null) {
				nameMethod = obj.getClass().getMethod("getName");
				ageMethod = obj.getClass().getMethod("getAge");
			}
			if ("name".equals(name)) {
				return nameMethod.invoke(obj);
			} else if ("age".equals(name)) {
				return ageMethod.invoke(obj);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
