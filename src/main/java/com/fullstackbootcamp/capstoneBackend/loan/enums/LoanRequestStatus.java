package com.fullstackbootcamp.capstoneBackend.loan.enums;

/* NOTE:
 *  In LoanRequest Entity, LoanOffer is set for the following condition:
 *  - PENDING: reason == null (loan request send to banker for loan offer back)
 *  - IN_REVIEW: reason != null (Business Owner negotiates offer with banker)
 *  - APPROVED: reason == null (Business Owner accepts loan offer received from banker)
 *  - REJECTED: reason != null (when loan response is sent)
 *  - RESCINDED: reason == banker makes better offer and revokes the previous one
 */

public enum LoanRequestStatus {
    PENDING,
    IN_REVIEW,
    APPROVED,
    REJECTED,
    RESCINDED
}
