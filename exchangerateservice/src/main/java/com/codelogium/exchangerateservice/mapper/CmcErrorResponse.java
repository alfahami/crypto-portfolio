package com.codelogium.exchangerateservice.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public class CmcErrorResponse {

    @JsonProperty("status") // Matches the "status" field in the JSON
    private Status status;

    // Getter and Setter for 'status'
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // Inner class to represent the 'status' object
    public static class Status {
        @JsonProperty("timestamp") // Matches the "timestamp" field in the JSON
        private Instant timestamp;

        @JsonProperty("error_code") // Matches the "error_code" field in the JSON
        private int errorCode;

        @JsonProperty("error_message") // Matches the "error_message" field in the JSON
        private String errorMessage;

        @JsonProperty("elapsed") // Matches the "elapsed" field in the JSON
        private int elapsed;

        @JsonProperty("credit_count") // Matches the "credit_count" field in the JSON
        private int creditCount;

        @JsonProperty("notice") // Matches the "notice" field in the JSON
        private String notice;

        // Getters and Setters
        public Instant getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Instant timestamp) {
            this.timestamp = timestamp;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public int getElapsed() {
            return elapsed;
        }

        public void setElapsed(int elapsed) {
            this.elapsed = elapsed;
        }

        public int getCreditCount() {
            return creditCount;
        }

        public void setCreditCount(int creditCount) {
            this.creditCount = creditCount;
        }

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }

        @Override
        public String toString() {
            return "Status{" +
                    "timestamp=" + timestamp +
                    ", errorCode=" + errorCode +
                    ", errorMessage='" + errorMessage + '\'' +
                    ", elapsed=" + elapsed +
                    ", creditCount=" + creditCount +
                    ", notice='" + notice + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CmcErrorResponse{" +
                "status=" + status +
                '}';
    }
}