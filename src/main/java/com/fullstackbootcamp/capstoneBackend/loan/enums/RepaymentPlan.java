package com.fullstackbootcamp.capstoneBackend.loan.enums;

/* Note:
 *  - EQUAL_INSTALLMENTS	Fixed monthly payments throughout the loan term. (Common in Murabaha)
 *  - BALLOON_PAYMENT	    Small periodic payments with a large final payment.
 *  - STEP_UP	            Payments increase over time, useful for growing businesses.
 *  - STEP_DOWN	Higher      payments initially, then reduce later.
 *  - LUMP_SUM	Full        loan amount repaid at the end of the term. (Common in Qard Hasan)
 *  - GRACE_PERIOD	        Delayed repayment start, common in student or startup loans.
 *  - REVENUE_BASED	        Repayments are based on business income percentage. (Common in Islamic Musharakah)
 *  - LEASE_TO_OWN	    Monthly payments structured as rent, leading to ownership (Ijara).
 */

public enum RepaymentPlan {
    EQUAL_INSTALLMENTS,
    BALLOON_PAYMENT,
    STEP_UP,
    STEP_DOWN,
    LUMP_SUM,
    GRACE_PERIOD,
    REVENUE_BASED,
    LEASE_TO_OWN
}
