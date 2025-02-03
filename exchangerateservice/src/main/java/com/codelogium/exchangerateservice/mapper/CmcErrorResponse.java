package com.codelogium.exchangerateservice.mapper;

import java.time.Instant;

import lombok.Data;

@Data
public class CmcErrorResponse {
    private Status status;

    @Data
    public static class Status {
        private Instant timestamp;
        private int erroCode;
        private String errorMessage;
        private int elapsed;
        private int creditCount;
        private String notice;
    }
}
