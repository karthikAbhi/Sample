package com.nash.usblib;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imrankst1221@gmail.com
 *
 */

public class Utils_1 {
    // UNICODE 0x23 = #
    public static final byte[] UNICODE_TEXT = new byte[] {0x23, 0x23, 0x23,
            0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,
            0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,
            0x23, 0x23, 0x23};

    private static String hexStr = "0123456789ABCDEF";
    private static String[] binaryArray = { "0000", "0001", "0010", "0011",
            "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011",
            "1100", "1101", "1110", "1111" };

    public static byte[] decodeBitmap(Bitmap bmp, String mode){
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();

        List<String> list = new ArrayList<String>(); //binaryString list
        StringBuffer sb;


        int bitLen = bmpWidth / 8;
        int zeroCount = bmpWidth % 8;

        String zeroStr = "";
        if (zeroCount > 0) {
            bitLen = bmpWidth / 8 + 1;
            for (int i = 0; i < (8 - zeroCount); i++) {
                zeroStr = zeroStr + "0";
            }
        }

        for (int i = 0; i < bmpHeight; i++) {
            sb = new StringBuffer();
            for (int j = 0; j < bmpWidth; j++) {
                int color = bmp.getPixel(j, i);

                int redValue = Color.red(color);
                int blueValue = Color.blue(color);
                int greenValue = Color.green(color);

                int r = (color >> 15) & 0xff;
                int g = (color >> 8) & 0xff;
                int b = color & 0xff;

                // if color close to white，bit='0', else bit='1'
                if (r > 0 && g > 0 && b > 0)
                    sb.append("0");
                else
                    sb.append("1");
            }
            if (zeroCount > 0) {
                sb.append(zeroStr);
            }
            list.add(sb.toString());
        }

        List<String> bmpHexList = binaryListToHexStringList(list);
        String commandHexString = "1D7630"+mode;

        String widthHexString = Integer
                .toHexString(bmpWidth % 8 == 0 ? bmpWidth / 8
                        : (bmpWidth / 8 + 1));

        if(widthHexString.length() == 4){
            widthHexString = widthHexString.substring(2,3) + widthHexString.substring(0,1);
        }
        else if (widthHexString.length() == 3) {
            widthHexString = "0"+widthHexString;
            widthHexString = widthHexString.substring(2,3) + widthHexString.substring(0,1);
        }
        else if (widthHexString.length() == 2) {
            widthHexString = widthHexString + "00";
        }
        else if (widthHexString.length() == 1){
            widthHexString = "0" + widthHexString + "00";
        }
        else{
            Log.e("decodeBitmap error", "width is incorrect");
        }

        String heightHexString = Integer.toHexString(bmpHeight);

        if(heightHexString.length() == 4){
            heightHexString = heightHexString.substring(2,3) + heightHexString.substring(0,1);
        }
        else if (heightHexString.length() == 3) {
            heightHexString = "0"+heightHexString;
            Log.e("decodeBitmap error", " width is too large");
            heightHexString = heightHexString.substring(2,4) + heightHexString.substring(0,2);
        }
        else if (heightHexString.length() == 2) {
            heightHexString = heightHexString + "00";
        }
        else if (heightHexString.length() == 1){
            heightHexString = "0" + heightHexString + "00";
        }
        else{
            Log.e("decodeBitmap error", "width is incorrect");
        }

        List<String> commandList = new ArrayList<String>();
        commandList.add(commandHexString+widthHexString+heightHexString);
        commandList.addAll(bmpHexList);

        return hexList2Byte(commandList);
    }

    public static List<String> binaryListToHexStringList(List<String> list) {
        List<String> hexList = new ArrayList<String>();
        for (String binaryStr : list) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < binaryStr.length(); i += 8) {
                String str = binaryStr.substring(i, i + 8);

                String hexString = myBinaryStrToHexString(str);
                sb.append(hexString);
            }
            hexList.add(sb.toString());
        }
        return hexList;
    }

    public static String myBinaryStrToHexString(String binaryStr) {
        String hex = "";
        String f4 = binaryStr.substring(0, 4);
        String b4 = binaryStr.substring(4, 8);
        for (int i = 0; i < binaryArray.length; i++) {
            if (f4.equals(binaryArray[i]))
                hex += hexStr.substring(i, i + 1);
        }
        for (int i = 0; i < binaryArray.length; i++) {
            if (b4.equals(binaryArray[i]))
                hex += hexStr.substring(i, i + 1);
        }
        return hex;
    }

    public static byte[] hexList2Byte(List<String> list) {
        List<byte[]> commandList = new ArrayList<byte[]>();

        for (String hexStr : list) {
            commandList.add(hexStringToBytes(hexStr));
        }
        byte[] bytes = sysCopy(commandList);
        return bytes;
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static byte[] sysCopy(List<byte[]> srcArrays) {
        int len = 0;
        for (byte[] srcArray : srcArrays) {
            len += srcArray.length;
        }
        byte[] destArray = new byte[len];
        int destLen = 0;
        for (byte[] srcArray : srcArrays) {
            System.arraycopy(srcArray, 0, destArray, destLen, srcArray.length);
            destLen += srcArray.length;
        }
        return destArray;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static byte[] decodeNVBitImage(Bitmap bmp) {
        //Get the width and height of the formatted image
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();

        List<String> list = new ArrayList<String>();//binaryString list

        for (int i = 0; i < bmpHeight; i++) {
            //String buffer object to store all the pixel values in string format
            StringBuffer sb = new StringBuffer();

            for (int j = 0; j < bmpWidth; j++) {
                //Get individual pixel values
                int color = bmp.getPixel(j, i);
                //Extract R,G,B values
                int r = (color >> 15) & 0xff;
                int g = (color >> 8) & 0xff;
                int b = color & 0xff;

                // if color close to white，bit='0', else bit='1'
                if (r > 0 && g > 0 && b > 0)
                    sb.append("0");
                else
                    sb.append("1");
            }
            list.add(sb.toString());
        }

        List<String> bmpHexList = binaryListToHexStringList(list);
        String commandHexString = "";

        //Dividing both width and height by 8, As defined in the implementation of firmware
        String widthHexString = Integer
                .toHexString(bmpWidth % 8 == 0 ? bmpWidth / 8
                        : (bmpWidth / 8 + 1));
        String heightHexString = Integer
                .toHexString(bmpHeight % 8 == 0 ? bmpHeight / 8
                        : (bmpHeight / 8 + 1));

        if (widthHexString.length() == 4) {
            widthHexString = widthHexString.substring(2, 3) + widthHexString.substring(0, 1);
        } else if (widthHexString.length() == 3) {
            widthHexString = "0" + widthHexString;
            widthHexString = widthHexString.substring(2, 3) + widthHexString.substring(0, 1);
        } else if (widthHexString.length() == 2) {
            widthHexString = widthHexString + "00";
        } else if (widthHexString.length() == 1) {
            widthHexString = "0" + widthHexString + "00";
        } else {
            Log.e("decodeBitmap error", "width is incorrect");
        }

        if (heightHexString.length() == 4) {
            heightHexString = heightHexString.substring(2, 3) + heightHexString.substring(0, 1);
        } else if (heightHexString.length() == 3) {
            heightHexString = "0" + heightHexString;
            Log.e("decodeBitmap error", " height is too large");
            heightHexString = heightHexString.substring(2, 4) + heightHexString.substring(0, 2);
        } else if (heightHexString.length() == 2) {
            heightHexString = heightHexString + "00";
        } else if (heightHexString.length() == 1) {
            heightHexString = "0" + heightHexString + "00";
        } else {
            Log.e("decodeBitmap error", "height is incorrect");
        }

        List<String> commandList = new ArrayList<String>();

        /***
         * Remember:
         * Here, Height and Width are interchanged according to firmware implementation
         * ***/
        commandList.add(commandHexString + heightHexString + widthHexString);
        commandList.addAll(bmpHexList);


        return hexList2Byte(commandList);
    }

    public static byte[] decodeRasterBitImage(Bitmap bmp) {

        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();

        List<String> list = new ArrayList<String>(); //binaryString list
        StringBuffer sb;

        int bitLen = bmpWidth / 8;
        int zeroCount = bmpWidth % 8;

        String zeroStr = "";
        if (zeroCount > 0) {
            bitLen = bmpWidth / 8 + 1;
            for (int i = 0; i < (8 - zeroCount); i++) {
                zeroStr = zeroStr + "0";
            }
        }

        for (int i = 0; i < bmpHeight; i++) {
            sb = new StringBuffer();
            for (int j = 0; j < bmpWidth; j++) {
                int color = bmp.getPixel(j, i);

                int redValue = Color.red(color);
                int blueValue = Color.blue(color);
                int greenValue = Color.green(color);

                int r = (color >> 15) & 0xff;
                int g = (color >> 8) & 0xff;
                int b = color & 0xff;

                // if color close to white，bit='0', else bit='1'
                if (r > 0 && g > 0 && b > 0)
                    sb.append("0");
                else
                    sb.append("1");
            }
            if (zeroCount > 0) {
                sb.append(zeroStr);
            }
            list.add(sb.toString());
        }

        List<String> bmpHexList = binaryListToHexStringList(list);
        String commandHexString = "";

        String widthHexString = Integer
                .toHexString(bmpWidth % 8 == 0 ? bmpWidth / 8
                        : (bmpWidth / 8 + 1));

        if(widthHexString.length() == 4){
            widthHexString = widthHexString.substring(2,3) + widthHexString.substring(0,1);
        }
        else if (widthHexString.length() == 3) {
            widthHexString = "0"+widthHexString;
            widthHexString = widthHexString.substring(2,3) + widthHexString.substring(0,1);
        }
        else if (widthHexString.length() == 2) {
            widthHexString = widthHexString + "00";
        }
        else if (widthHexString.length() == 1){
            widthHexString = "0" + widthHexString + "00";
        }
        else{
            Log.e("decodeBitmap error", "width is incorrect");
        }

        String heightHexString = Integer.toHexString(bmpHeight);

        if(heightHexString.length() == 4){
            heightHexString = heightHexString.substring(2,3) + heightHexString.substring(0,1);
        }
        else if (heightHexString.length() == 3) {
            heightHexString = "0"+heightHexString;
            Log.e("decodeBitmap error", " width is too large");
            heightHexString = heightHexString.substring(2,4) + heightHexString.substring(0,2);
        }
        else if (heightHexString.length() == 2) {
            heightHexString = heightHexString + "00";
        }
        else if (heightHexString.length() == 1){
            heightHexString = "0" + heightHexString + "00";
        }
        else{
            Log.e("decodeBitmap error", "width is incorrect");
        }

        List<String> commandList = new ArrayList<String>();
        commandList.add(commandHexString+widthHexString+heightHexString);
        commandList.addAll(bmpHexList);

        return hexList2Byte(commandList);
    }
}
