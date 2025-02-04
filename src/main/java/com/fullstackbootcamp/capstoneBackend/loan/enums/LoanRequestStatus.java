package com.fullstackbootcamp.capstoneBackend.loan.enums;

/* NOTE:
 *  In LoanRequest Entity, LoanOffer is set for the following condition:
 *  - PENDING: reason == null (loan request send to banker for loan offer back)
 *  - NEW_RESPONSE: reason != null (Business Owner receives offer(s) from selected banks)
 *  - APPROVED: reason == null (Business Owner accepts loan offer received from banker)
 *  - REJECTED: reason != null (when loan response is sent)
 */

public enum LoanRequestStatus {
    PENDING,
    NEW_RESPONSE,
    APPROVED,
    REJECTED,
}
