package com.github.miachm.SODS;

/**
 * Created by MiguelPC on 29/03/2017.
 */
public class NotAnOds extends RuntimeException {
    private String message;
    public NotAnOds(String s) {
        this.message = s;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
