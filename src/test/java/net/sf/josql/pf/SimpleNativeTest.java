package net.sf.josql.pf;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.sf.josql.pf.support.Foo;

import org.josql.QueryExecutionException;

/**
 * a simple test use java reflect to run the test,must use jvm
 * options:<b>-server -Xms512m -Xmx512m -verbose:gc</b>
 * 
 * @author nihongye
 * 
 */
public class SimpleNativeTest extends AbstractPerformanceTest {
	private Random random = new Random(System.currentTimeMillis());
	private List<Foo> users;

	protected long executeQuery() throws Exception {
		List<Foo> res = new ArrayList<Foo>();
		Method nameMethod = Foo.class.getMethod("getName");
		Method ageMethod = Foo.class.getMethod("getAge");
		long begin = System.currentTimeMillis();
		for (Foo item : users) {
			String name = (String) nameMethod.invoke(item);
			Integer age = (Integer) ageMethod.invoke(item);
			if (name.equals("nihongye") && age > 10) {
				res.add(item);
			}
		}
		long end = System.currentTimeMillis();
		queryExecuteTimes.add(end - begin);
		return res.size();

	}

	protected void initObjects() {
		users = new ArrayList<Foo>();
		for (int i = 0; i < 100 * 10000; i++) {
			users.add(new Foo("nihongye", random.nextInt(100)));
		}
	}

	public static void main(String[] args) throws Exception,
	        QueryExecutionException, IOException, InterruptedException {
		new SimpleNativeTest().runTest();
	}
}
