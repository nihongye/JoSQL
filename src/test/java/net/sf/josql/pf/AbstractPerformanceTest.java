package net.sf.josql.pf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * the performance test framework invoke excuteQuery {@link #loop},after
 * invoke,calculate the query avg execute time(exclude max and min)
 * 
 * @author nihongye
 * 
 */
public abstract class AbstractPerformanceTest {

	protected List<Long> queryExecuteTimes = new ArrayList<Long>();
	protected long loop = 12;

	public List<Long> getTimes() {
		return queryExecuteTimes;
	}

	public void runTest() throws Exception {
		initObjects();
		long hits = 0;
		for (int i = 0; i < loop; i++) {
			hits += executeQuery();
		}
		Collections.sort(queryExecuteTimes);
		long total = 0;
		for (int i = 1; i < queryExecuteTimes.size() - 1; i++) {
			total += queryExecuteTimes.get(i);
		}
		long avgTime = total / (loop - 2);
		String info = String.format("avg execute time = %1$d(ms);"
		        + "all time = %2$s;hits = %3$,d", avgTime,
		        Arrays.toString(getTimes().toArray()), hits);
		System.out.println(info);
	}

	protected abstract void initObjects();

	/**
	 * execute query,add the query execute time to queryExecuteTimes,return the
	 * hits
	 * 
	 * @return the hits
	 * @throws Exception
	 */
	protected abstract long executeQuery() throws Exception;

	public void setLoop(long loop) {
    	this.loop = loop;
    }

	
}
