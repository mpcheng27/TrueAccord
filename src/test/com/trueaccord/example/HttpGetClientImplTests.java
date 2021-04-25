package com.trueaccord.example;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

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
	private static final String FAKE_ENDPOINT = "https://idonotexist.idonotexist";

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

	@Test
	public void testGetFakeUrl() throws IOException, InterruptedException {
		assertThrows(Exception.class, () -> {
			client.fetchEndPoint(FAKE_ENDPOINT);
		});
	}
}
