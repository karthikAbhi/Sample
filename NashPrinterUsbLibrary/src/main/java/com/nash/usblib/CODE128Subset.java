package com.nash.usblib;

public enum CODE128Subset {
    SUBSETA(103),
    SUBSETB(104),
    SUBSETC(105);

    private int subset;

    CODE128Subset(int subset) {
        this.subset = subset;
    }

    public int getSubset() {
        return subset;
    }
}
