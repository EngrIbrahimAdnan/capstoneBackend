package com.fullstackbootcamp.capstoneBackend.loan.enums;


/* NOTE:
 *  In LoanRequest Entity, RejectionSource is set for the following condition:
 *  - NONE: Loan request has no decision reached yet
 *  - BUSINESS_OWNER: Business owner rejects loan offer (finds one better)
 *  - BANKER: Banker rejects loan request (business doesn't meet the minimum requirement for loan request)
 */

/* REVIEW:
 *  Might add a fourth value System but don't see any immediate use for it:
 *  - SYSTEM: rejected for logistical reasons
 */

public enum RejectionSource {
    NONE,
    BUSINESS_OWNER,
    BANKER,
}
