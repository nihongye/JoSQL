package net.sf.josql;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.josql.Query;
import org.josql.QueryExecutionException;
import org.josql.QueryParseException;
import org.josql.QueryResults;

public class OrderTest extends TestCase{
	
	private List<User> list = new ArrayList<User>();

	public void setUp(){
		for(int i = 0;i < 100;i++){
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, i);
			Date birthDate = calendar.getTime();
			int age = i;
			User item = new User("anybody"+i,age,birthDate);
			list.add(item );
		}
	}
	
	@SuppressWarnings("unchecked")
	public void testQueryAndOrder() throws QueryParseException, QueryExecutionException{
		String sql = "select * from "+User.class.getName()+"" +
				" order by age desc" +
				" limit 1,10";
		Query query = new Query();
		query.parse(sql);
		QueryResults result = query.execute(list);
        List<User> results = result.getResults();
		assertEquals(10, results.size());
		assertEquals(99, results.get(0).getAge());
	}
}
