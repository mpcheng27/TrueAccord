package com.trueaccord.example;

import static com.trueaccord.example.AppConstants.AMOUNT;
import static com.trueaccord.example.AppConstants.ID;
import static com.trueaccord.example.AppConstants.IS_IN_PAYMENT_PLAN;
import static com.trueaccord.example.AppConstants.NEXT_PAYMENT_DUE_DATE;
import static com.trueaccord.example.AppConstants.REMAINING_AMOUNT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.trueaccord.example.PaymentPlan.Frequency;

/**
 * Tests for Debt class.
 * 
 * @author mpcheng
 *
 */
public class DebtTests {

	private int debtId = 0;
	private int paymentPlanId = 10;

	private BigDecimal amount = BigDecimal.valueOf(2000.00);
	private BigDecimal amountToPay = BigDecimal.valueOf(1500.00);
	private BigDecimal installmentAmount = BigDecimal.valueOf(100.00);

	private Frequency frequency = Frequency.BI_WEEKLY;
	private String startDateString = "2021-01-01";

	private Debt debt;
	private PaymentPlan paymentPlan;
	private DateFormat simpleDateFormat;

	@Before
	public void setup() throws ParseException {
		debt = new Debt(debtId, amount);
		simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = simpleDateFormat.parse(startDateString);
		paymentPlan = new PaymentPlan(paymentPlanId, debtId, amountToPay, installmentAmount, frequency, startDate);
	}

	@After
	public void tearDown() {
		debt = null;
		paymentPlan = null;
	}

	@Test
	public void testDebtConstructor() {
		Debt debt = new Debt(debtId, amount);
		assertEquals(debtId, debt.getId());
	}

	@Test
	public void testGetAmount() {
		assertEquals(amount, debt.getAmount());
	}

	@Test
	public void testNotInPaymentPlan() {
		assertFalse(debt.isInPaymentPlan());
	}

	@Test
	public void testIsInPaymentPlan() {
		debt.setPaymentPlan(paymentPlan);
		assertTrue(debt.isInPaymentPlan());
	}

	@Test
	public void testIsInPaymentPlanPaidOff() throws ParseException {
		debt.setPaymentPlan(paymentPlan);
		paymentPlan.processPayment(simpleDateFormat.parse("2021-02-01"), amountToPay);
		assertFalse(debt.isInPaymentPlan());
	}

	@Test
	public void testSetPaymentPlanTwice() {
		debt.setPaymentPlan(paymentPlan);
		Exception exception = assertThrows(RuntimeException.class, () -> {
			debt.setPaymentPlan(paymentPlan);
		});

		String expectedMessage = "Cannot assign more than one payment plan to the debt.";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void testGetRemainingAmountWithNoPaymentPlan() {
		assertEquals(amount, debt.getRemainingAmount());
	}

	@Test
	public void testGetRemainingAmountWithPaymentPlanNoPayments() {
		debt.setPaymentPlan(paymentPlan);
		assertEquals(amountToPay, debt.getRemainingAmount());
	}

	@Test
	public void testGetRemainingAmount() throws ParseException {
		debt.setPaymentPlan(paymentPlan);
		paymentPlan.processPayment(simpleDateFormat.parse("2021-01-08"), installmentAmount);
		paymentPlan.processPayment(simpleDateFormat.parse("2021-01-15"), installmentAmount);
		assertEquals(BigDecimal.valueOf(1300.00), debt.getRemainingAmount());
	}

	@Test
	public void testGetNextPaymentDueDateNoPaymentPlan() throws ParseException {
		assertNull(debt.getNextPaymentDueDate());
	}

	@Test
	public void testGetNextPaymentDueDateNoPayments() throws ParseException {
		debt.setPaymentPlan(paymentPlan);
		assertEquals("2021-01-15", debt.getNextPaymentDueDate());
	}

	@Test
	public void testGetNextPaymentDueDateOnePaymentBeforeInstallment() throws ParseException {
		debt.setPaymentPlan(paymentPlan);
		paymentPlan.processPayment(simpleDateFormat.parse("2021-01-02"), installmentAmount);
		assertEquals("2021-01-15", debt.getNextPaymentDueDate());
	}

	@Test
	public void testGetNextPaymentDueDateThreePayments() throws ParseException {
		debt.setPaymentPlan(paymentPlan);
		paymentPlan.processPayment(simpleDateFormat.parse("2021-01-22"), installmentAmount);
		paymentPlan.processPayment(simpleDateFormat.parse("2021-01-15"), installmentAmount);
		paymentPlan.processPayment(simpleDateFormat.parse("2021-01-08"), installmentAmount);
		assertEquals("2021-01-29", debt.getNextPaymentDueDate());
	}

	@Test
	public void testGetNextPaymentDueDateOnePaymentLate() throws ParseException {
		debt.setPaymentPlan(paymentPlan);
		paymentPlan.processPayment(simpleDateFormat.parse("2021-02-01"), installmentAmount);
		assertEquals("2021-02-12", debt.getNextPaymentDueDate());
	}

	@Test
	public void testGetNextPaymentDueDatePaidOff() throws ParseException {
		debt.setPaymentPlan(paymentPlan);
		paymentPlan.processPayment(simpleDateFormat.parse("2021-02-01"), amountToPay);
		assertNull(debt.getNextPaymentDueDate());
	}

	@Test
	public void testGetNextPaymentDueDateThreePaymentsSameInstallment() throws ParseException {
		debt.setPaymentPlan(paymentPlan);
		paymentPlan.processPayment(simpleDateFormat.parse("2021-01-14"), installmentAmount);
		paymentPlan.processPayment(simpleDateFormat.parse("2021-01-13"), installmentAmount);
		paymentPlan.processPayment(simpleDateFormat.parse("2021-01-08"), installmentAmount);
		assertEquals("2021-01-15", debt.getNextPaymentDueDate());
	}

	@Test
	public void testAsJsonLine() throws ParseException {
		debt.setPaymentPlan(paymentPlan);
		paymentPlan.processPayment(simpleDateFormat.parse("2021-01-14"), installmentAmount);
		paymentPlan.processPayment(simpleDateFormat.parse("2021-01-13"), installmentAmount);
		paymentPlan.processPayment(simpleDateFormat.parse("2021-01-08"), installmentAmount);
		String jsonLine = debt.asJsonLine();
		JSONObject jsonObject = new JSONObject(jsonLine);
		assertEquals(debtId, jsonObject.get(ID));
		assertEquals(amount, BigDecimal.valueOf(Double.valueOf(jsonObject.get(AMOUNT).toString())));
		assertEquals(true, jsonObject.get(IS_IN_PAYMENT_PLAN));
		assertEquals(BigDecimal.valueOf(1200.0),
				BigDecimal.valueOf(Double.valueOf(jsonObject.get(REMAINING_AMOUNT).toString())));
		assertEquals("2021-01-15", jsonObject.get(NEXT_PAYMENT_DUE_DATE));
	}
}
