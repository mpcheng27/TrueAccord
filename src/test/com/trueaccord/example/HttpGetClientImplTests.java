package com.trueaccord.example;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the HttpGetClientImpl.
 * 
 * @author mpcheng
 *
 */
public class HttpGetClientImplTests {

	private static HttpGetClient client;

	@BeforeClass
	public static void setup() {
		client = new HttpGetClientImpl();
	}

	@Test
	public void testGetDebts() throws IOException, InterruptedException {
		String body = client.fetchEndPoint(HttpGetClient.DEBT_ENDPOINT);
		assertNotNull(body);
	}

	@Test
	public void testGetPaymentPlans() throws IOException, InterruptedException {
		String body = client.fetchEndPoint(HttpGetClient.PAYMENT_PLANS_ENDPOINT);
		assertNotNull(body);
	}

	@Test
	public void testGetPayments() throws IOException, InterruptedException {
		String body = client.fetchEndPoint(HttpGetClient.PAYMENTS_ENDPOINT);
		assertNotNull(body);
	}
}
