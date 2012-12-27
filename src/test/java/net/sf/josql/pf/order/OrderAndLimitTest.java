package net.sf.josql.pf.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.sf.josql.pf.AbstractPerformanceTest;
import net.sf.josql.pf.support.Foo;
import net.sf.josql.pf.support.FooFunction;

import org.josql.Query;
import org.josql.QueryExecutionException;
import org.josql.QueryParseException;
import org.josql.QueryResults;

/**
 * make a test for josql Function,compare to SimpleQueryTest,to find Function
 * bottleneck to run the test,must use jvm options:<b>-server -Xms512m -Xmx512m
 * -verbose:gc</b>
 * 
 * @author nihongye
 * 
 */
public class OrderAndLimitTest extends AbstractPerformanceTest {
	private Random random = new Random(System.currentTimeMillis());
	private List<Foo> users;

	@SuppressWarnings("unchecked")
	protected long executeQuery() throws QueryParseException,
	        QueryExecutionException {
		// Create a new Query.
		Query q = new Query();
		q.addFunctionHandler(new FooFunction());
		String sql = "select * from net.sf.josql.pf.support.Foo "
		        + "	where name like 'someone%'"
		        + "	and age > 1" 
		        + " order by age asc" 
		        + " limit 1,100";
		// Parse the SQL you are going to use.
		q.parse(sql);
		// Execute the query.
		long begin = System.currentTimeMillis();
		QueryResults qr = q.execute(users);
		// Get the query results.
		List<Foo> res = qr.getResults();
		System.out.println(Arrays.toString(res.toArray()));
		long end = System.currentTimeMillis();
		queryExecuteTimes.add(end - begin);
		return res.size();

	}

	public List<Long> getTimes() {
		return queryExecuteTimes;
	}

	protected void initObjects() {
		users = new ArrayList<Foo>();
		for (int i = 0; i < 100 * 10000; i++) {
			users.add(new Foo("someone"+i, random.nextInt(100)));
		}
	}

	public static void main(String[] args) throws Exception {
		OrderAndLimitTest orderAndLimitTest = new OrderAndLimitTest();
		orderAndLimitTest.setLoop(6);
		orderAndLimitTest.runTest();
	}
}
