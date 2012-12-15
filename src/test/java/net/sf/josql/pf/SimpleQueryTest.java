package net.sf.josql.pf;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.sf.josql.pf.support.Foo;

import org.josql.Query;
import org.josql.QueryExecutionException;
import org.josql.QueryParseException;
import org.josql.QueryResults;
/**
 * a simple test to find bottleneck compare with SimpleNativeTest
 * to run the test,must use jvm options:<b>-server -Xms512m -Xmx512m -verbose:gc</b>
 * @author nihongye
 *
 */
public class SimpleQueryTest extends AbstractPerformanceTest {
	private Random random = new Random(System.currentTimeMillis());
	private List<Foo> users;

	@SuppressWarnings("unchecked")
	protected long executeQuery() throws QueryParseException,
			QueryExecutionException {
		// Create a new Query.
		Query q = new Query();
		String sql = "select * from net.sf.josql.pf.support.Foo "
				+ "	where name = 'someone'" + "	and age > 10";
		// Parse the SQL you are going to use.
		q.parse(sql);
		// Execute the query.
		long begin = System.currentTimeMillis();
		QueryResults qr = q.execute(users);
		// Get the query results.
		List<Foo> res = qr.getResults();
		long end = System.currentTimeMillis();
		queryExecuteTimes.add(end - begin);
		return res.size();

	}

	protected void initObjects() {
		users = new ArrayList<Foo>();
		for (int i = 0; i < 100 * 10000; i++) {
			users.add(new Foo("someone", random.nextInt(100)));
		}
	}

	public static void main(String[] args) throws Exception {
		new SimpleQueryTest().runTest();
	}

}
