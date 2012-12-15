package net.sf.josql.pf.support;

import java.util.HashMap;
import java.util.Map;

public class GetMapValueFunction {
	private Map<String,String[]> expressions = new HashMap<String,String[]>();
	
	@SuppressWarnings("rawtypes")
	public Object f(Map map,String expression){
		String[] properties = expressions.get(expression);
		if(properties == null){
			properties = expression.split("\\.");
			expressions.put(expression, properties);
		}
		for(int i = 0; i < properties.length - 1;i++){
			map = (Map) map.get(properties[i]);
			if(map == null){
				break;
			}
		}
		if(map != null){
			return map.get(properties[properties.length - 1]);
		}
		return map;
	}
}
