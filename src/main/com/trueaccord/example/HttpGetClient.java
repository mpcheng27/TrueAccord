package com.trueaccord.example;

import java.io.IOException;

/**
 * Interface for executing a HTTP GET request and returns the response as a
 * string.
 * 
 * @author mpcheng
 */
public interface HttpGetClient {

	/**
	 * End point for the debts.
	 */
	public static final String DEBT_ENDPOINT = "https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/debts";
	
	/**
	 * End point for the payment plans.
	 */
	public static final String PAYMENT_PLANS_ENDPOINT = "https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payment_plans";
	
	/**
	 * End point for the payments.
	 */
	public static final String PAYMENTS_ENDPOINT = "https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payments";

	/**
	 * Executes a HTTP GET request to the given url and returns the response body as
	 * a string.
	 * 
	 * @param url - the service endpoint to fetch.
	 * @return the response body as a string.
	 * @throws IOException, InterruptedException
	 */
	public String fetchEndPoint(String url) throws IOException, InterruptedException;
}
