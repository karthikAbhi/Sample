package com.nash.usblib;

public enum BarcodeType {

    UPC_A(65),
    UPC_E(66),
    JAN13(67),
    JAN8(68),
    CODE39(69),
    ITF(70),
    CODABAR(71),
    CODE93(72),
    CODE128(73);

    private int barcode_type;

    BarcodeType(int type){
        this.barcode_type = type;
    }
    public int getBarcode_type() {
        return this.barcode_type;
    }
}
