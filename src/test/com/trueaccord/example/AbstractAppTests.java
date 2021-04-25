package com.trueaccord.example;

import static com.trueaccord.example.AppConstants.ID;

import java.io.IOException;
import java.util.Collection;

import org.json.JSONObject;

/**
 * Abstract super class for the AppTests.
 * @author mpcheng
 */
public class AbstractAppTests {
	
	protected static App app;
	protected static Collection<Debt> debts;
	
	/**
	 * Fetches the debt for the given id.
	 * @param id the id of the debt to retrieve.
	 * @return the debt for the given id.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected static JSONObject fetchDebt(int id) throws IOException, InterruptedException {
		// Loop through to validate as if we're processing the json line
		for (Debt debt : debts) {
			JSONObject json = new JSONObject(debt.asJsonLine());
			if (Integer.valueOf(json.get(ID).toString()) == id)
				return json;
		}
		throw new RuntimeException("No debt with id " + id + " was found");
	}
}
