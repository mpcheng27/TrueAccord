package com.trueaccord.example;

import static com.trueaccord.example.App.extractAttributeAsBigDecimal;
import static com.trueaccord.example.AppConstants.AMOUNT;
import static com.trueaccord.example.AppConstants.ID;
import static com.trueaccord.example.AppConstants.IS_IN_PAYMENT_PLAN;
import static com.trueaccord.example.AppConstants.NEXT_PAYMENT_DUE_DATE;
import static com.trueaccord.example.AppConstants.REMAINING_AMOUNT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * End to end testing with a mock service endpoint.
 * 
 * @author mpcheng
 */
public class AppMockTests extends AbstractAppTests {
	
	@BeforeClass
	public static void setup() throws IOException, InterruptedException {
		app = new App(new HttpGetClientMock());
		debts = app.processDebts();
	}
	
	@Test
	public void testLoadingDebts() throws IOException, InterruptedException {
		assertNotNull(debts);
		assertFalse(debts.isEmpty());
	}

	@Test
	public void validateJsonLineResultsForDebt0() throws IOException, InterruptedException {
		int id = 0;
		JSONObject debtAsJson = fetchDebt(id);
		assertEquals(id, debtAsJson.get(ID));
		assertEquals(BigDecimal.valueOf(100.0), extractAttributeAsBigDecimal(debtAsJson, AMOUNT));
		assertEquals(BigDecimal.valueOf(-2.5), extractAttributeAsBigDecimal(debtAsJson, REMAINING_AMOUNT));
		assertEquals(false, debtAsJson.get(IS_IN_PAYMENT_PLAN));
		// Because org.json.JSONObject doesn't print null attributes, the attribute
		// doesn't exist
		Exception exception = assertThrows(JSONException.class, () -> {
			debtAsJson.get(NEXT_PAYMENT_DUE_DATE);
		});

		String expectedMessage = "not found";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void validateJsonLineResultsForDebt1() throws IOException, InterruptedException {
		int id = 1;
		JSONObject debtAsJson = fetchDebt(id);
		assertEquals(id, debtAsJson.get(ID));
		assertEquals(BigDecimal.valueOf(1000.00), extractAttributeAsBigDecimal(debtAsJson, AMOUNT));
		assertEquals(BigDecimal.valueOf(450.00), extractAttributeAsBigDecimal(debtAsJson, REMAINING_AMOUNT));
		assertEquals(true, debtAsJson.get(IS_IN_PAYMENT_PLAN));
		assertEquals("2021-08-13", debtAsJson.get(NEXT_PAYMENT_DUE_DATE));
	}

	@Test
	public void validateJsonLineResultsForDebt2() throws IOException, InterruptedException {
		int id = 2;
		JSONObject debtAsJson = fetchDebt(id);
		assertEquals(id, debtAsJson.get(ID));
		assertEquals(BigDecimal.valueOf(10000.00), extractAttributeAsBigDecimal(debtAsJson, AMOUNT));
		assertEquals(BigDecimal.valueOf(9000.00), extractAttributeAsBigDecimal(debtAsJson, REMAINING_AMOUNT));
		assertEquals(true, debtAsJson.get(IS_IN_PAYMENT_PLAN));
		assertEquals("2021-01-15", debtAsJson.get(NEXT_PAYMENT_DUE_DATE));
	}

	@Test
	public void validateJsonLineResultsForDebt3() throws IOException, InterruptedException {
		int id = 3;
		JSONObject debtAsJson = fetchDebt(id);
		assertEquals(id, debtAsJson.get(ID));
		assertEquals(BigDecimal.valueOf(100000.00), extractAttributeAsBigDecimal(debtAsJson, AMOUNT));
		assertEquals(BigDecimal.valueOf(34999.00), extractAttributeAsBigDecimal(debtAsJson, REMAINING_AMOUNT));
		assertEquals(true, debtAsJson.get(IS_IN_PAYMENT_PLAN));
		assertEquals("2021-02-12", debtAsJson.get(NEXT_PAYMENT_DUE_DATE));
	}

	@Test
	public void validateJsonLineResultsForDebt4() throws IOException, InterruptedException {
		int id = 4;
		JSONObject debtAsJson = fetchDebt(id);
		assertEquals(id, debtAsJson.get(ID));
		assertEquals(BigDecimal.valueOf(1000000.00), extractAttributeAsBigDecimal(debtAsJson, AMOUNT));
		assertEquals(BigDecimal.valueOf(1000000.00), extractAttributeAsBigDecimal(debtAsJson, REMAINING_AMOUNT));
		assertEquals(false, debtAsJson.get(IS_IN_PAYMENT_PLAN));
		// Because org.json.JSONObject doesn't print null attributes, the attribute
		// doesn't exist
		Exception exception = assertThrows(JSONException.class, () -> {
			debtAsJson.get(NEXT_PAYMENT_DUE_DATE);
		});

		String expectedMessage = "not found";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}
}
