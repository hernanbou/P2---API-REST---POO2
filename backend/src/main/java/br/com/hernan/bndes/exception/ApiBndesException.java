package br.com.hernan.bndes.exception;

import org.springframework.http.HttpStatus;

public class ApiBndesException extends RuntimeException {

    private final HttpStatus status;

    public ApiBndesException(String message) {
        this(message, HttpStatus.BAD_GATEWAY);
    }

    public ApiBndesException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
