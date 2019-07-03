package com.nash.usblib;

public enum QRErrCorrLvl {
    L(48),
    M(49),
    Q(50),
    H(51);

    private int correctionlevel;

    QRErrCorrLvl(int correctionlvl) {
        this.correctionlevel = correctionlvl;
    }

    public int getCorrectionlevel() {
        return correctionlevel;
    }
}
