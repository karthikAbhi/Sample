package com.nash.usblib;

import android.service.autofill.RegexValidator;
import android.util.Log;

import java.util.regex.Pattern;

public class Validator {

    public Validator() {
    }

    public boolean check(String n, int minBound, int maxBound) {
        try{
            int temp = Integer.parseInt(n);

            try{
                if(temp < minBound || temp > maxBound ) {
                    throw new ValueOutOfBoundException("Incorrect Value "+n);
                }
                else{
                    return true;
                }
            } catch (ValueOutOfBoundException e){
                Log.e("ValueOutOfBoundError", e.getMessage());
            }
        }
        catch (NumberFormatException e){
            Log.e("Error","Invalid Input "+n);
        }
        return false;
    }

    public boolean check_barcode(BarcodeType barcodeType, String userInput){
        if(barcodeType.equals(BarcodeType.UPC_A)||barcodeType.equals(BarcodeType.UPC_E)){
            if((userInput.length() == 11||userInput.length()== 12) &&
                    Pattern.matches("^[0-9]*$", userInput)){
                return true;
            }
        }
        else if(barcodeType.equals(BarcodeType.JAN13)){
            if((userInput.length() == 12||userInput.length()== 13) &&
                    Pattern.matches("^[0-9]*$", userInput)){
                return true;
            }
        }
        else if(barcodeType.equals(BarcodeType.JAN8)){
            if((userInput.length() == 7||userInput.length()== 8) &&
                    Pattern.matches("^[0-9]*$", userInput)){
                return true;
            }
        }
        else if(barcodeType.equals(BarcodeType.CODE39)){
            if((userInput.length() > 0) &&
                    Pattern.matches("^[0-9$%+=./A-Z ]*$", userInput)){
                return true;
            }
        }
        else if(barcodeType.equals(BarcodeType.ITF)){
            if((userInput.length() % 2 == 0) &&
                    Pattern.matches("^[0-9]*$", userInput)){
                return true;
            }
        }
        else if(barcodeType.equals(BarcodeType.CODABAR)){
            if((userInput.length() > 0)) {
                if (Pattern.matches("^[A-D]]", userInput) && Pattern.matches("[A-D]$]", userInput)) {
                    Pattern.matches("[0-9+./:$\\-]*", userInput);
                    return true;
                }
            }
        }
        return false;
    }
}
