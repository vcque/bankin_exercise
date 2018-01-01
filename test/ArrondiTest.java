import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import bridge.entities.BridgeTransactions;
import entities.Arrondis;
import entities.ArrondisStats;
import services.ArrondiService;


/**
 *
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 *
 */
public class ArrondiTest {

	final ArrondiService arrondiService = new ArrondiService();
	
	private final static String TEST_DATA = 
			"{\n" + 
			"  \"resources\": [" +
			"{" + 
			"      \"id\": 1," + 
			"      \"description\": \"Prelevement Spotify SA\"," + 
			"      \"raw_description\": \"Prlv 1512 Spotify SA\"," + 
			"      \"amount\": 4.99," + 
			"      \"date\": \"2016-04-06\"," + 
			"      \"updated_at\": \"2016-04-06T09:19:14Z\"," + 
			"      \"is_deleted\": false" + 
			"}," +
			"{" + 
			"      \"id\": 2," + 
			"      \"description\": \"Prelevement Spotify SA\"," + 
			"      \"raw_description\": \"Prlv 1512 Spotify SA\"," + 
			"      \"amount\": 10," + 
			"      \"date\": \"2016-04-06\"," + 
			"      \"updated_at\": \"2016-04-06T09:19:14Z\"," + 
			"      \"is_deleted\": false" + 
			"}," +
			"{" + 
			"      \"id\": 3," + 
			"      \"description\": \"Prelevement Spotify SA\"," + 
			"      \"raw_description\": \"Prlv 1512 Spotify SA\"," + 
			"      \"amount\": -104.99," + 
			"      \"date\": \"2016-04-06\"," + 
			"      \"updated_at\": \"2016-04-06T09:19:14Z\"," + 
			"      \"is_deleted\": false" + 
			"}" +
			"]}"
			;
    
    @Test
    public void simpleArrondiTest() throws Exception {
    	final BridgeTransactions transactions = new ObjectMapper().readValue(TEST_DATA, BridgeTransactions.class);
    	final Arrondis arrondis = arrondiService.computeArrondis(transactions);
    	Assert.assertEquals(5.01d, arrondis.getTransactions().get(0).arrondi, 0.001d);
    }

    @Test
    public void exactArrondiTest() throws Exception {
    	final BridgeTransactions transactions = new ObjectMapper().readValue(TEST_DATA, BridgeTransactions.class);
    	final Arrondis arrondis = arrondiService.computeArrondis(transactions);
    	Assert.assertEquals(0d, arrondis.getTransactions().get(1).arrondi, 0.001d);
    }
    
    @Test
    public void moreThan10ArrondiTest() throws Exception {
    	final BridgeTransactions transactions = new ObjectMapper().readValue(TEST_DATA, BridgeTransactions.class);
    	final Arrondis arrondis = arrondiService.computeArrondis(transactions);
    	Assert.assertEquals(5.01d, arrondis.getTransactions().get(2).arrondi, 0.001d);
    }
    
    @Test
    public void statsArrondiTest() throws Exception {
    	final BridgeTransactions transactions = new ObjectMapper().readValue(TEST_DATA, BridgeTransactions.class);
    	final ArrondisStats stats = arrondiService.aggregateStats(transactions);
    	Assert.assertEquals(3, stats.count);
    	Assert.assertEquals(5.01d * 2d, stats.sum, 0.001d);
    	Assert.assertEquals((5.01d + 5.01d + 0d) / 3, stats.average, 0.001d);
    }
}
