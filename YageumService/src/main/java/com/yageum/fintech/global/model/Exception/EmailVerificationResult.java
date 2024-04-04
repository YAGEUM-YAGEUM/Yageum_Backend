package com.yageum.fintech.global.model.Exception;

public class EmailVerificationResult {

    private boolean verificationStatus;
    private int code;
    private String message;

    private EmailVerificationResult(boolean verificationStatus, int code, String message) {
        this.verificationStatus = verificationStatus;
        this.code = code;
        this.message = message;
    }

    public static EmailVerificationResult of(boolean verificationStatus) {
        String message = verificationStatus ? "Email verification successful" : "Email verification failed";
        int code = verificationStatus ? 200 : 401;
        return new EmailVerificationResult(verificationStatus, code, message);
    }

    public boolean isVerificationStatus() {
        return verificationStatus;
    }

    public String getMessage() {
        return message;
    }
}

