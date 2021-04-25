
package com.trueaccord.example;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;
import static com.trueaccord.example.AppConstants.*;

/**
 * Represents a customer debt. Provides various utility functions in regards to
 * the debt. A debt may or may not be associated with a payment plan.
 * 
 * @author mpcheng
 */
public class Debt {

	private final int id;
	private final BigDecimal amount;
	private PaymentPlan paymentPlan;

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * Constructor for the Debt class.
	 * 
	 * @param id     - the id of the debt.
	 * @param amount - the original amount of the debt.
	 */
	public Debt(int id, BigDecimal amount) {
		this.id = id;
		this.amount = amount;
	}

	/**
	 * Returns the id of this debt.
	 * 
	 * @return the id of this debt.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Returns the amount of this debt.
	 * 
	 * @return the amount of this debt.
	 */
	public BigDecimal getAmount() {
		return this.amount;
	}

	/**
	 * Returns true if the debt is in a payment plan, false otherwise.
	 * 
	 * @return true if the debt is in a payment plan, false otherwise.
	 */
	public boolean isInPaymentPlan() {
		if (this.paymentPlan == null || getRemainingAmount().doubleValue() <= 0.0)
			return false;
		return true;
	}

	/**
	 * Set's a payment plan for this debt.
	 * 
	 * @param paymentPlan - the payment plan to set on the debt.
	 * @throws RuntimeException if a payment plan was already associated with the
	 *                          debt.
	 */
	public void setPaymentPlan(PaymentPlan paymentPlan) {
		if (this.paymentPlan != null) {
			throw new RuntimeException("Debt " + this.id
					+ " is already associated with a payment plan. Cannot assign more than one payment plan to the debt.");
		}
		this.paymentPlan = paymentPlan;
	}

	/**
	 * Returns the remaining amount due on the debt as a BigDecimal. If there is no
	 * payment plan associated with the debt, then it is the original debt amount.
	 * If there is a payment plan then associated, then it is based on the payment
	 * plans amount to pay minus the payments made if any.
	 * 
	 * @return the remaining amount due on the debt as a BigDecimal.
	 */
	public BigDecimal getRemainingAmount() {
		if (this.paymentPlan == null) {
			return this.amount;
		}
		return this.paymentPlan.getRemainingAmount();
	}

	/**
	 * Returns the next payment due date as ISO 8602 UTC date. Returns null if there
	 * is no payment plan or if the debt has been paid off. It is based off the
	 * payment plan start date and the installment frequency. It should be the next
	 * installment date after the latest payment, even if this date is in the past.
	 * Payments that were made on days outside of the expected payment schedule,
	 * still go toward paying off the remaining balance, but do not change/delay the
	 * payment schedule.
	 * 
	 * @return the next payment due date as ISO 8602 UTC date. Returns null if there
	 *         is no payment plan or if the debt has been paid off.
	 */
	public String getNextPaymentDueDate() {
		if (!this.isInPaymentPlan() || this.paymentPlan.getRemainingAmount().doubleValue() <= 0.0) {
			return null;
		}
		Date nextPaymentDueDate = this.paymentPlan.getNextPaymentDueDate();
		return dateFormat.format(nextPaymentDueDate);
	}

	/**
	 * Returns the debt as a JSON line string.
	 * 
	 * @return the debt as a JSON line string.
	 */
	public String asJsonLine() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(ID, this.id);
		jsonObject.put(AMOUNT, this.getAmount());
		jsonObject.put(IS_IN_PAYMENT_PLAN, this.isInPaymentPlan());
		jsonObject.put(REMAINING_AMOUNT, this.getRemainingAmount());
		jsonObject.put(NEXT_PAYMENT_DUE_DATE, this.getNextPaymentDueDate()); // NOTE: JSONObject doesn't print out
																				// next_payment_due_date if its null
		return jsonObject.toString();
	}
}
