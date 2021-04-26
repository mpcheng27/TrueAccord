package com.trueaccord.example;

import static com.trueaccord.example.AppConstants.AMOUNT;
import static com.trueaccord.example.AppConstants.AMOUNT_TO_PAY;
import static com.trueaccord.example.AppConstants.DATE;
import static com.trueaccord.example.AppConstants.DEBT_ID;
import static com.trueaccord.example.AppConstants.ID;
import static com.trueaccord.example.AppConstants.INSTALLMENT_FREQUENCY;
import static com.trueaccord.example.AppConstants.START_DATE;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Main application which processes all debts, associated payments plans if any
 * and their payments, and then outputs the debts with new fields:
 * is_in_payment_plan, remaining_amount, and next_payment_due_date in JSON Lines
 * format.
 * 
 * @author mpcheng
 */
public class App {

	private HttpGetClient client;
	private DateFormat dateFormat;

	/**
	 * Default constructor for App.
	 * 
	 * @param client - the HttpGetClient to use for fetching endpoints.
	 */
	public App(HttpGetClient client) {
		this.client = client;
		this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	}

	/**
	 * Fetches and processes all the debts and returns them
	 * 
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public Map<Integer, Debt> processDebts() throws IOException, InterruptedException {
		Map<Integer, Debt> debts = new TreeMap<Integer, Debt>();
		String responseString = client.fetchEndPoint(HttpGetClient.DEBT_ENDPOINT);
		JSONArray debtsAsJson = new JSONArray(responseString);
		debtsAsJson.forEach(object -> {
			JSONObject json = (JSONObject) object;
			Debt debt = new Debt(json.getInt(ID), json.getBigDecimal(AMOUNT));
			debts.put(debt.getId(), debt);
		});
		Collection<PaymentPlan> paymentPlans = this.processPaymentPlans();
		for (PaymentPlan paymentPlan : paymentPlans) {
			if (!debts.containsKey(paymentPlan.getDebtId())) {
				throw new RuntimeException(
						"Unknown debt id " + paymentPlan.getDebtId() + " from payment plan " + paymentPlan.getId());
			}
			debts.get(paymentPlan.getDebtId()).setPaymentPlan(paymentPlan);
		}
		return debts;
	}

	private Collection<PaymentPlan> processPaymentPlans() throws IOException, InterruptedException {
		Map<Integer, PaymentPlan> paymentPlans = new HashMap<Integer, PaymentPlan>();
		String responseString = client.fetchEndPoint(HttpGetClient.PAYMENT_PLANS_ENDPOINT);
		JSONArray paymentPlansAsJson = new JSONArray(responseString);
		paymentPlansAsJson.forEach(object -> {
			JSONObject json = (JSONObject) object;
			Date startDate;
			try {
				startDate = dateFormat.parse(json.getString(START_DATE));
			} catch (JSONException | ParseException e) {
				throw new RuntimeException(e);
			}
			PaymentPlan paymentPlan = new PaymentPlan(json.getInt(ID), json.getInt(DEBT_ID),
					json.getBigDecimal(AMOUNT_TO_PAY), json.getBigDecimal("installment_amount"),
					json.getEnum(PaymentPlan.Frequency.class, INSTALLMENT_FREQUENCY), startDate);
			paymentPlans.put(paymentPlan.getId(), paymentPlan);
		});

		processPayments(paymentPlans);
		return paymentPlans.values();
	}

	private void processPayments(Map<Integer, PaymentPlan> paymentPlans) throws IOException, InterruptedException {
		String responseString = client.fetchEndPoint(HttpGetClient.PAYMENTS_ENDPOINT);
		JSONArray paymentsAsJson = new JSONArray(responseString);
		paymentsAsJson.forEach(object -> {
			JSONObject json = (JSONObject) object;
			int paymentPlanId = json.getInt("payment_plan_id");
			if (!paymentPlans.containsKey(paymentPlanId)) {
				throw new RuntimeException("Unknown payment plan id " + paymentPlanId);
			}
			Date paymentDate;
			try {
				paymentDate = dateFormat.parse(json.getString(DATE));
			} catch (JSONException | ParseException e) {
				throw new RuntimeException(e);
			}
			paymentPlans.get(paymentPlanId).processPayment(paymentDate, json.getBigDecimal(AMOUNT));
		});
	}

	/**
	 * Main entry point to execute the debt processing.
	 * 
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		App app = new App(new HttpGetClientImpl());
		Map<Integer, Debt> debts = app.processDebts();
		for (Debt debt : debts.values()) {
			System.out.println(debt.asJsonLine());
		}
	}

	/**
	 * Extracts a JSON number from the org.json.JSONObject as a BigDecimal.
	 * 
	 * @param object
	 * @param key    the key attribute to extract as a BigDecimal.
	 * @return - the attribute as a BigDecimal.
	 */
	public static BigDecimal extractAttributeAsBigDecimal(JSONObject object, String key) {
		return BigDecimal.valueOf(Double.parseDouble(object.get(key).toString()));
	}
}
