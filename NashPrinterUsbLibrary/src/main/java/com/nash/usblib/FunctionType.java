package com.nash.usblib;

public enum FunctionType {

    A("<A>"), B("<B>"), C("<C>");

    String type;

    FunctionType(String setType){
        this.type = setType;
    }

    public String getFunctionType(){
        return this.type;
    }
}
