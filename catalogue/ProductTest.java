package catalogue;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ProductTest {

	Product pr1 = null;
	Product pr2 = null;
	Product pr3 = null;
	
	@Before
	public void setUp() throws Exception {
		pr1 = new Product("0001", "Tablet", 69.8, 1);
		pr2 = new Product("0001", "Tablet", 69.8, 1);
		pr3 = new Product("0002", "Glass", 8.8, 1);
	}

	@Test
	public void testEqualsObject() {
		assertTrue(pr1.equals(pr2));
		assertTrue(!pr1.equals(pr3));
	}
}
