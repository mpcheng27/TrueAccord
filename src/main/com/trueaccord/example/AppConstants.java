package com.trueaccord.example;

/**
 * Well known constant keys defined here.
 * 
 * @author mpcheng
 */
public interface AppConstants {
	/**
	 * Key for the id of an object.
	 */
	public static final String ID = "id";

	/**
	 * Key for the amount of debt.
	 */
	public static final String AMOUNT = "amount";

	/**
	 * Key for the amount to pay to satisfy the debt.
	 */
	public static final String AMOUNT_TO_PAY = "amount_to_pay";

	/**
	 * Key for the debt id of an object.
	 */
	public static final String DEBT_ID = "debt_id";

	/**
	 * Key for the installment amount for the payment plan.
	 */
	public static final String INSTALLMENT_AMOUNT = "installment_amount";

	/**
	 * Key for the installment frequency of the payment plan.
	 */
	public static final String INSTALLMENT_FREQUENCY = "installment_frequency";

	/**
	 * Key for the start date of the payment plan.
	 */
	public static final String START_DATE = "start_date";

	/**
	 * Key for the associated payment plan id.
	 */
	public static final String PAYMENT_PLAN_ID = "playment_plan_id";

	/**
	 * Key for date of a payment.
	 */
	public static final String DATE = "date";

	/**
	 * Key for whether a debt is in a payment plan.
	 */
	public static final String IS_IN_PAYMENT_PLAN = "is_in_payment_plan";

	/**
	 * Key for the remaining amount outstanding for a debt.
	 */
	public static final String REMAINING_AMOUNT = "remaining_amount";

	/**
	 * Key for the next payment due date.
	 */
	public static final String NEXT_PAYMENT_DUE_DATE = "next_payment_due_date";
}
