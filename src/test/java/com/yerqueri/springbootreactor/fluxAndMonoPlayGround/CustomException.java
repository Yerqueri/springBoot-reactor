package com.yerqueri.springbootreactor.fluxAndMonoPlayGround;

import lombok.Data;
import lombok.Getter;

@Getter
public class CustomException extends Throwable {

    private String message;

    public CustomException(Throwable error) {
        this.message= error.getMessage();
    }
}
