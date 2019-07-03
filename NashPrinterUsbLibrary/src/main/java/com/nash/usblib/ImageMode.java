package com.nash.usblib;

public enum ImageMode {
    NORMAL("0"),
    DOUBLE_WIDTH("1"),
    DOUBLE_HEIGHT("2"),
    DOUBLE_WIDTH_DOUBLE_HEIGHT("3");

    private String mode;

    ImageMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }
}
