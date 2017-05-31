package ke.co.ipoint.orderpath;

import ke.co.ipoint.structures.Graph;
import ke.co.ipoint.structures.Order;
import static org.junit.jupiter.api.Assertions.*;
import java.util.function.BooleanSupplier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class AltAppTest{
	
	private AltApp app;
	
	@BeforeEach
	void init() {
		app = new AltApp();
	}
	 
	@Test
	void testAcceptSingleOrder() {
		
		Order order = new Order("Kilimani", 1.0, 0.6, 8.8, 7.6);
		boolean isValid = app.analyzeOrder(order);
		assertTrue("Failed: Single order not accepted, ", isValid);
	 
	  }
}
