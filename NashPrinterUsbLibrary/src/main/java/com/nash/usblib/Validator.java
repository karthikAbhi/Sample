package com.nash.usblib;

import android.util.Log;

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
}
