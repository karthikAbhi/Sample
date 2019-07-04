package com.nash.usblib;

public enum PDF417ErrorCorrectionMode {

    LEVEL("48"),
    RATIO("49");

    private String mode;

    PDF417ErrorCorrectionMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }
}
