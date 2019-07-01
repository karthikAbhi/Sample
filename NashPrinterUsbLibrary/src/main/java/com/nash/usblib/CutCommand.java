package com.nash.usblib;

public enum CutCommand {

    PARTIALCUT("0"),FULLCUT("1");

    private String mode;

    private CutCommand(String value){
        this.mode = value;
    }

    public String getMode() {
        return this.mode;
    }

}

