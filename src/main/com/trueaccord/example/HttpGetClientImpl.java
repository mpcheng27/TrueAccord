package com.trueaccord.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

/**
 * Default implementation of the HttpGetClient.
 * 
 * @author mpcheng
 */
public class HttpGetClientImpl implements HttpGetClient {

	private static final int RESPONSE_OK = 200;
	private static final int TIMEOUT = 3;

	@Override
	public String fetchEndPoint(String url) throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).timeout(Duration.ofSeconds(TIMEOUT))
				.build();
		HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
		int statusCode = response.statusCode();
		if (statusCode == RESPONSE_OK)
			return response.body();
		// TODO: It there was more time improve the error handling and use
		// javax.ws.rs.core.Response status codes.
		// Due to time limitations just throw an exception when response is not 200.
		throw new RuntimeException(
				"Failed request with response code: " + statusCode + " and message: " + response.body());
	}

}
