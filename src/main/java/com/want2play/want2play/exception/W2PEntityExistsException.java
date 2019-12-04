package com.want2play.want2play.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class W2PEntityExistsException extends RuntimeException {
}
