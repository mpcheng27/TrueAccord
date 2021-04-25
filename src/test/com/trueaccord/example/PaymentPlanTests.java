package com.trueaccord.example;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.trueaccord.example.PaymentPlan.Frequency;

/**
 * Tests for PaymentPlan class.
 * 
 * @author mpcheng
 *
 */
public class PaymentPlanTests {
	
	private int debtId = 0;
	private int paymentPlanId = 10;
	
	private BigDecimal amountToPay = BigDecimal.valueOf(1500.00);
	private BigDecimal installmentAmount = BigDecimal.valueOf(50.00);
	
	private DateFormat simpleDateFormat;
	private Frequency frequency = Frequency.WEEKLY;
	private String startDateString = "2021-01-01";
	private Date startDate;
	private PaymentPlan paymentPlan;

	@Before
	public void setup() throws ParseException {
		simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		startDate = simpleDateFormat.parse(startDateString);
		paymentPlan = new PaymentPlan(paymentPlanId, debtId, amountToPay, installmentAmount, frequency, startDate);
	}

	@After
	public void tearDown() {
		paymentPlan = null;
	}
	
	@Test
	public void testPaymentPlanConstructor() throws ParseException {
		PaymentPlan paymentPlan = new PaymentPlan(paymentPlanId, debtId, amountToPay, installmentAmount, frequency, startDate);
		assertEquals(paymentPlanId, paymentPlan.getId());
	}
	
	@Test
	public void testGetDebtId() {
		assertEquals(debtId, paymentPlan.getDebtId());
	}
	
	@Test 
	public void testGetAmountToPay() {
		assertEquals(amountToPay, paymentPlan.getAmountToPay());
	}
	@Test
	public void testGetInstallmentAmount() {
		assertEquals(installmentAmount, paymentPlan.getInstallmentAmount());
	}
	
	@Test
	public void testGetFrequency() {
		assertEquals(frequency, paymentPlan.getFrequency());
	}
	
	@Test
	public void testGetFrequencyInDays() {
		assertEquals(7, paymentPlan.getFrequencyInDays());
	}
	
	@Test
	public void testGetStartDate() {
		assertEquals(startDate, paymentPlan.getStartDate());
	}
	
	@Test
	public void testGetRemainingAmountNoPayments() throws ParseException {
		assertEquals(BigDecimal.valueOf(1500.00), paymentPlan.getRemainingAmount());
	}
	
	@Test
	public void testGetRemainingAmount() throws ParseException {
		paymentPlan.processPayment(simpleDateFormat.parse("2021-01-08"), installmentAmount);
		paymentPlan.processPayment(simpleDateFormat.parse("2021-01-15"), installmentAmount);
		assertEquals(BigDecimal.valueOf(1400.00), paymentPlan.getRemainingAmount());
	}
	
	@Test
	public void testLastPaymentDateWithNoPayments() {
		assertNull(paymentPlan.getLastPaymentDate());
	}
	
	@Test
	public void testLastPaymentDateWithTwoPayments() throws ParseException {
		paymentPlan.processPayment(simpleDateFormat.parse("2021-01-15"), installmentAmount);
		paymentPlan.processPayment(simpleDateFormat.parse("2021-01-08"), installmentAmount);
		assertEquals(simpleDateFormat.parse("2021-01-15"), paymentPlan.getLastPaymentDate());
	}
	
	@Test
	public void testGetNextPaymentDueDateNoPayments() throws ParseException {
		assertEquals(simpleDateFormat.parse("2021-01-08"), paymentPlan.getNextPaymentDueDate());
	}
	
	@Test
	public void testGetNextPaymentDueDateOnePaymentBeforeInstallment() throws ParseException {
		paymentPlan.processPayment(simpleDateFormat.parse("2021-01-02"), installmentAmount);
		assertEquals(simpleDateFormat.parse("2021-01-08"), paymentPlan.getNextPaymentDueDate());
	}
	
	@Test
	public void testGetNextPaymentDueDateThreePayments() throws ParseException {
		paymentPlan.processPayment(simpleDateFormat.parse("2021-01-22"), installmentAmount);
		paymentPlan.processPayment(simpleDateFormat.parse("2021-01-15"), installmentAmount);
		paymentPlan.processPayment(simpleDateFormat.parse("2021-01-08"), installmentAmount);
		assertEquals(simpleDateFormat.parse("2021-01-29"), paymentPlan.getNextPaymentDueDate());
	}
	
	@Test
	public void testGetNextPaymentDueDateOnePaymentLate() throws ParseException {
		paymentPlan.processPayment(simpleDateFormat.parse("2021-02-01"), installmentAmount);
		assertEquals(simpleDateFormat.parse("2021-02-05"), paymentPlan.getNextPaymentDueDate());
	}
	
	@Test
	public void testGetNextPaymentDueDatePaidOff() throws ParseException {
		paymentPlan.processPayment(simpleDateFormat.parse("2021-02-01"), amountToPay);
		assertNull(paymentPlan.getNextPaymentDueDate());
	}
	
	@Test
	public void testGetNextPaymentDueDateTwoPaymentsSameWeek() throws ParseException {
		paymentPlan.processPayment(simpleDateFormat.parse("2021-01-14"), installmentAmount);
		paymentPlan.processPayment(simpleDateFormat.parse("2021-01-13"), installmentAmount);
		paymentPlan.processPayment(simpleDateFormat.parse("2021-01-08"), installmentAmount);
		assertEquals(simpleDateFormat.parse("2021-01-15"), paymentPlan.getNextPaymentDueDate());
	}
}
