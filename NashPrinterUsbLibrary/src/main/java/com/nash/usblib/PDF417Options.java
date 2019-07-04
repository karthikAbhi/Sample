package com.nash.usblib;

public enum PDF417Options {

    STANDARD("0"),
    TRUNCATED("1");

    private String option;

    PDF417Options(String option) {
        this.option = option;
    }

    public String getOption() {
        return option;
    }
}
