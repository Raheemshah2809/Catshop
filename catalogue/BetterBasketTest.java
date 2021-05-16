package catalogue;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BetterBasketTest {
	Product pr1 = null;
	Product pr2 = null;
	
	Basket basket = null;
	
	@Before
	public void setup() {
		pr1 = new Product("0001", "Tablet", 69.8, 1);
		pr2 = new Product("0001", "Tablet", 69.8, 1);
		
		basket = new BetterBasket();
	}
	
	@Test
	public void testAddProduct() {
		basket.add(pr1);
		basket.add(pr2);
		
		assertEquals(basket.size(), 1);
		assertEquals(basket.get(0).getQuantity(), 2);
	}

}
