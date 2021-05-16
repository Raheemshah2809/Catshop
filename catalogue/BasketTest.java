package catalogue;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BasketTest {
	Basket basket = null;
	@Before
	public void setUp() throws Exception {
		basket = new Basket();
	}

	@Test
	public void testAddProduct() {
		assertEquals(basket.size(), 0);
		basket.add(new Product("0001", "Tablet", 69.8, 1));
		assertEquals(basket.size(), 1);
	}
	
	@Test
	public void testGetTotalCost() {
		basket.clear();
		basket.add(new Product("0001", "Tablet", 50, 1));
		basket.add(new Product("0001", "Tablet", 50, 2));
		assertEquals(basket.getTotalCost(), 150, 0);
		
	}

}
