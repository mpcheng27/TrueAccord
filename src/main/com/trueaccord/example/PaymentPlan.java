package com.trueaccord.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Represents a payment plan. Each payment plan is associated with exactly one
 * Debt.
 * 
 * @author mpcheng
 *
 */
public class PaymentPlan {

	/**
	 * Frequency representing the frequency of a payment plan.
	 * 
	 * @author mpcheng
	 */
	public enum Frequency {
		WEEKLY, BI_WEEKLY
	}

	private static final int maxPossibleInstallments = 10000;
	private final int id;
	private final int debtId;
	private final BigDecimal amountToPay;
	private final BigDecimal installmentAmount;
	private final Frequency frequency;
	private final Date startDate;
	private final List<Payment> payments;

	/**
	 * Inner class that represents payments made to the payment plan.
	 * 
	 * @author mpcheng
	 */
	class Payment {
		private final Date paymentDate;
		private final BigDecimal amountPaid;

		public Payment(Date paymentDate, BigDecimal amountPaid) {
			this.paymentDate = paymentDate;
			this.amountPaid = amountPaid;
		}

		/**
		 * Returns the payment date.
		 * 
		 * @return the payment date.
		 */
		public Date getPaymentDate() {
			return paymentDate;
		}

		/**
		 * Returns the amount paid.
		 * 
		 * @return the amount paid.
		 */
		public BigDecimal getAmountPaid() {
			return amountPaid;
		}
	}

	/**
	 * Represents a payment plan.
	 * 
	 * @param id                - the payment plan id.
	 * @param debtId            - the associated debt id.
	 * @param amountToPay       - the amount to pay for the debt to be forgiven.
	 * @param installmentAmount - the installment amount.
	 * @param frequency         - the frequency of payments.
	 * @param startDate         - the start date of the payment plan.
	 */
	public PaymentPlan(int id, int debtId, BigDecimal amountToPay, BigDecimal installmentAmount, Frequency frequency,
			Date startDate) {
		super();
		this.id = id;
		this.debtId = debtId;
		this.amountToPay = amountToPay;
		this.installmentAmount = installmentAmount;
		this.frequency = frequency;
		this.startDate = startDate;
		this.payments = new ArrayList<PaymentPlan.Payment>();
	}

	/**
	 * Returns the id of this payment plan.
	 * 
	 * @return the id of this payment plan.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the associated debt id.
	 * 
	 * @return the associated debt id.
	 */
	public int getDebtId() {
		return debtId;
	}

	/**
	 * Returns the amount to pay for the debt to be forgiven.
	 * 
	 * @return the amount to pay for the debt to be forgiven.
	 */
	public BigDecimal getAmountToPay() {
		return amountToPay;
	}

	/**
	 * Returns the installment amount for each payment.
	 * 
	 * @return the installment amount for each payment.
	 */
	public BigDecimal getInstallmentAmount() {
		return installmentAmount;
	}

	/**
	 * Returns the frequency for each installment payment that is due.
	 * 
	 * @return the frequency for each installment payment that is due.
	 */
	public Frequency getFrequency() {
		return frequency;
	}

	/**
	 * Returns the frequency as number of days as an int.
	 * 
	 * @return the frequency as number of days as an int.
	 */
	public int getFrequencyInDays() {
		switch (this.frequency) {
		case WEEKLY:
			return 7;
		case BI_WEEKLY:
			return 14;
		}
		throw new RuntimeException("Unknown frequency type: " + this.frequency);
	}

	/**
	 * Returns the start date of the payment plan.
	 * 
	 * @return the start date of the payment plan.
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Returns the remaining amount due on the debt as a BigDecimal.
	 * 
	 * @return the remaining amount due on the debt as a BigDecimal.
	 */
	public BigDecimal getRemainingAmount() {
		BigDecimal remainingAmount = this.amountToPay.add(BigDecimal.ZERO);// Copy the amount to pay
		for (Payment payment : payments) {
			remainingAmount = remainingAmount.subtract(payment.getAmountPaid());
		}
		return remainingAmount;
	}

	/**
	 * Returns the last payment date. Returns null if no payments were made.
	 * 
	 * @Date the last payment date. Returns null if no payments were made.
	 */
	public Date getLastPaymentDate() {
		if (payments.isEmpty()) {
			return null;
		}
		long lastPayment = 0;
		for (Payment payment : payments) {
			if (payment.getPaymentDate().getTime() > lastPayment) {
				lastPayment = payment.getPaymentDate().getTime();
			}
		}
		return new Date(lastPayment);
	}

	/**
	 * Returns the next payment due date. It is based off the payment plan start
	 * date and the installment frequency. It should be the next installment date
	 * after the latest payment, even if this date is in the past. Payments that
	 * were made on days outside of the expected payment schedule, still go toward
	 * paying off the remaining balance, but do not change/delay the payment
	 * schedule.
	 * 
	 * @return the next payment due date. Returns null if the debt has been paid
	 *         off.
	 */
	public Date getNextPaymentDueDate() {
		if (getRemainingAmount().doubleValue() <= 0.0) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.startDate);
		calendar.add(Calendar.DAY_OF_YEAR, getFrequencyInDays()); // Earliest is first install period

		if (payments.isEmpty() || getLastPaymentDate() == null) {
			return calendar.getTime();
		}
		long lastPaymentDateTime = getLastPaymentDate().getTime();

		for (int i = 0; i < maxPossibleInstallments; i++) {
			if (calendar.getTime().getTime() > lastPaymentDateTime) {
				return calendar.getTime();
			}
			calendar.add(Calendar.DAY_OF_YEAR, getFrequencyInDays());
		}
		throw new RuntimeException("Exceeded the max possible number of installments of :" + maxPossibleInstallments);
	}

	/**
	 * Processes a payment for the payment plan.
	 * 
	 * @param date   - the date the payment was made.
	 * @param amount - the amount of the payment is for.
	 */
	public void processPayment(Date date, BigDecimal amount) {
		this.payments.add(new Payment(date, amount));
	}
}
