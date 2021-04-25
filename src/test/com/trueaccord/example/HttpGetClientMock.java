package com.trueaccord.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A mock implementation of HttpGetClient that returns json not from a service endpoint but rather mocked json files.
 * @author mpcheng
 */
public class HttpGetClientMock implements HttpGetClient {

	@Override
	public String fetchEndPoint(String url) throws IOException, InterruptedException {
		String filePath = getFileForUrl(url);
		return Files.readString(Path.of(System.getProperty("user.dir") + filePath));
	}

	private String getFileForUrl(String url) {
		switch (url) {
		case HttpGetClient.DEBT_ENDPOINT:
			return "/mock/debt.json";
		case HttpGetClient.PAYMENT_PLANS_ENDPOINT:
			return "/mock/payment_plans.json";
		case HttpGetClient.PAYMENTS_ENDPOINT:
			return "/mock/payments.json";
		}
		throw new RuntimeException("Unknown url for mocking " + url);
	}

}
