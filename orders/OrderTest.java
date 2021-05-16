package orders;

import static org.junit.Assert.*;
import middle.OrderException;

import org.junit.Before;
import org.junit.Test;

import catalogue.Basket;
import catalogue.Product;

public class OrderTest {
	Order order = null;
	Basket basket = null;
	Product pr = null;
	
	@Before
	public void setup() {
		order = new Order();
		pr = new Product("0001", "Tablet", 69.8, 1);
		basket = new Basket();
		basket.add(pr);
	}

	@Test
	public void testGetOrderToPick() throws OrderException {
		assertNull(order.getOrderToPick());
		order.newOrder(basket);
		assertTrue(order.getOrderToPick().get(0).equals(pr));
	}
	
	@Test
	public void testInformOrderToPicked() throws OrderException {
		assertNotNull(order.informOrderPicked(1));
	}
	
	@Test
	public void testInformOrderCollected() throws OrderException {
		assertNotNull(order.informOrderCollected(1));
	}
}
