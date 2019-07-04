package com.nash.usblib;

public enum PDF417ErrorCorrectionLevel {

    LEVEL0("48"),
    LEVEL1("49"),
    LEVEL2("50"),
    LEVEL3("51"),
    LEVEL4("52"),
    LEVEL5("53"),
    LEVEL6("54"),
    LEVEL7("55"),
    LEVEL8("56");

    private String correctionLevel;

    PDF417ErrorCorrectionLevel(String Level) {
        this.correctionLevel = Level;
    }

    public String getCorrectionLevel() {
        return correctionLevel;
    }
}
