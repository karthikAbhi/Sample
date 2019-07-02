package com.nash.usblib;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

import com.askjeffreyliu.floydsteinbergdithering.Utils;
import com.nash.nashprintercommands.Command;

import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Iterator;

import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_ATTACHED;
import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_DETACHED;

public class MyPrinter {

    //Nash Printer Command Reference
    Command myCommand = new Command();
    //Command Validator
    Validator mValidator = new Validator();
    //Cut Command Enum
    CutCommand mCutCommand = CutCommand.FULLCUT;

    String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    //Android Components
    UsbManager mUsbManager;
    UsbDevice mDevice;
    UsbInterface mInterface;
    UsbEndpoint mEndpoint;
    UsbDeviceConnection mConnection;
    String mUsbDevice = "";
    PendingIntent mPermissionIntent;
    Context mContext;

    //Constructor to find and initialise the printer connection
    public MyPrinter(Context context) {

        mContext = context;
        mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);

        //Contains all the UsbDevices list in a Hashmap Datastructure
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

        //deviceList = mUsbManager.getDeviceList();
        while (deviceIterator.hasNext()) {
            mDevice = deviceIterator.next();
            if (mDevice.getVendorId() == 12232) {
                //Device Found
                Toast.makeText(context.getApplicationContext(), "Printer Connected" + mUsbDevice, Toast.LENGTH_SHORT).show();

                mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);

                //Intent-Filter for recognising USB Device
                IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                IntentFilter filterOnConnection = new IntentFilter(ACTION_USB_DEVICE_ATTACHED);
                IntentFilter filterOffConnection = new IntentFilter(ACTION_USB_DEVICE_DETACHED);

                //Register Broadcast Receiver
                context.registerReceiver(mUsbReceiver, filter);
                context.registerReceiver(mUsbReceiver2, filterOnConnection);
                context.registerReceiver(mUsbReceiver1, filterOffConnection);
                mUsbManager.requestPermission(mDevice, mPermissionIntent);
                break;

            }
            else {
                Toast.makeText(context.getApplicationContext(), "Not a Printer", Toast.LENGTH_SHORT).show();
            }
        }
    }

    BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        Toast.makeText(context.getApplicationContext(), "Receiver called", Toast.LENGTH_SHORT).show();
        String action = intent.getAction();
        if (ACTION_USB_PERMISSION.equals(action)) {
            synchronized (this) {
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                Toast.makeText(context.getApplicationContext(), "Receiver called : "+device.getDeviceName() + " "+device.getManufacturerName(), Toast.LENGTH_SHORT).show();

                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    if (device != null) {
                        //call method to set up device communication
                        mInterface = device.getInterface(0);
                        mEndpoint = mInterface.getEndpoint(1);// 0 IN and  1 OUT to printer.
                        mConnection = mUsbManager.openDevice(device);

                        for (int i = 0; i < mInterface.getEndpointCount(); i++) {
                            Log.i("Printer", "EP: "
                                    + String.format("0x%02X", mInterface.getEndpoint(i)
                                    .getAddress()));

                            if (mInterface.getEndpoint(i).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                                Log.i("Printer", "Bulk Endpoint");
                                if (mInterface.getEndpoint(i).getDirection() == UsbConstants.USB_DIR_IN) {
                                    //epIN = mInterface.getEndpoint(i);
                                    Log.i("Printer", "input stream found");
                                }else {
                                    mEndpoint = mInterface.getEndpoint(i);
                                    Log.i("Printer", "outstream found");
                                }
                            } else {
                                Log.i("Printer", "Not Bulk");
                            }
                        }

                    }
                } else {
                    Toast.makeText(context, "PERMISSION DENIED FOR THIS DEVICE",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
        }
    };

    BroadcastReceiver mUsbReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Toast.makeText(context.getApplicationContext(),"Device Disconnected!",
                    Toast.LENGTH_SHORT).show();
            mConnection.close();
        }
    };

    BroadcastReceiver mUsbReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context.getApplicationContext(),"Device Connected!",
                    Toast.LENGTH_SHORT).show();
        }
    };

    public void printText(String dataToPrint){transfer(dataToPrint);}

    //Convert to Byte array
    private void transfer(String dataToPrintInString){

        byte[] printerInput = null;

        try {
            printerInput = dataToPrintInString.getBytes("UTF-8");
            transfer(printerInput);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //Process USB Bulk Transfer
    private void transfer(final byte[] dataToPrintInBytes){

        try{
            if(mInterface == null){
                throw new NullInterfaceException("Interface is Null");
            }else
                try {
                    if (mConnection == null) {
                        throw new NullConnectionException("Connection is Null");
                    }
                    else {
                        boolean status = mConnection.claimInterface(mInterface, true);
                        Log.i("Printer", "claim status " + status);

                        Log.i("Printer", "Before control transfer: ");

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                mConnection.bulkTransfer(mEndpoint,
                                        dataToPrintInBytes,
                                        dataToPrintInBytes.length,
                                        0);
                            }
                        });
                        thread.run();

                    }
                }
                catch (NullConnectionException e) {
                    Log.e("MyPrinter","Null Connection Exception"+ e.getMessage());
                }
        }catch(NullInterfaceException e){
            Log.e("MyPrinter","Null Interface Exception"+ e.getMessage());
        }
    }
    /**
    * Image Related methods
    * */
    private Bitmap scaleBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        int maxWidth = 128;
        int maxHeight = 128;

        Log.v("Pictures", "Width and height are " + width + "--" + height);

        if (width > height) {
            // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int)(height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int)(width / ratio);
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }

        Log.v("Pictures", "after scaling Width and height are " + width + "--" + height);

        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }
    //RGB to Grayscale Conversion
    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    /**
        Implemetation of the printer commands according to our requirement
     */

    //Cut command - Cut the form (14.43)
    public void executeCutCommand(){
        transfer(myCommand.cut_paper);
    }
    //Print data and feed a line (14.01)
    public void LF(){
        transfer(myCommand.LF);
    }
    //Print data and feed a page on page mode (14.02)
    public void FF(){
        transfer(myCommand.FF);
    }
    //Print data on page mode (14.03)
    public void ESC_FF(){
        transfer(myCommand.ESC_FF);
    }
    //Print data and feed the form to forward (14.04)
    public void ESC_J(String n){
        if(mValidator.check(n, 0 , 255)){
            transfer(myCommand.ESC_J);
            transfer(convertString2ByteArray(n));
        }
    }
    //Print data and feed n lines (14.05) (With Parameter)
    public void ESC_D(String n){
        if(mValidator.check(n, 0, 255)){
            transfer(myCommand.ESC_D);
            transfer(convertString2ByteArray(n));
        }
    }
    //Set the line feed amount (14.06) (With Parameter)
    public void ESC_3(String n){
        if(mValidator.check(n, 0, 255)){
            transfer(myCommand.ESC_3);
            transfer(convertString2ByteArray(n));
        }
    }
    //Set the gap of right side of the character (14.07) (With Parameter)
    public void ESC_SP(String n){
        if(mValidator.check(n, 0, 255)){
            transfer(myCommand.ESC_SP);
            transfer(convertString2ByteArray(n));
        }
    }
    //Set parameter of print mode (14.08) (With Parameter)
    public void ESC_PM(String n){
        if(mValidator.check(n, 0, 255)){
            transfer(myCommand.ESC_PM);
            transfer(convertString2ByteArray(n));
        }
    }
    //Select the font (14.09) (With Parameter)
    public void ESC_M(String n){
        if(mValidator.check(n,0,1)){
            transfer(myCommand.ESC_M);
            transfer(convertString2ByteArray(n));
        }
    }
    //Select character code table (14.10) (With Parameter)
    public void ESC_t(byte[] n){
        transfer(myCommand.ESC_t);
        transfer(n);
    }
    //Specify download character (14.11) (With Parameter)
    public void ESC_DC(){
        transfer(myCommand.ESC_DC);
    }
    //Set or unset download character set (14.12) (With Parameter)
    public void ESC_SDC(String n){
        if(mValidator.check(n, 0, 255)){
            transfer(myCommand.ESC_SDC);
            transfer(convertString2ByteArray(n));
        }
    }
    //Set the print mode to page mode (14.13)
    public void PAGE_MODE(){
        transfer(myCommand.ESC_L);
    }
    //Set the print mode to standard mode (14.14)
    public void STANDARD_MODE(){
        transfer(myCommand.ESC_S);
    }
    //Tabbing Horizontal (14.15)
    public void HT(){
        transfer(myCommand.HT);
    }
    //Set the left side margin (14.16) (With Parameter)
    public void GS_L(String n){
        if(mValidator.check(n, 0, 576)){
            transfer(myCommand.GS_L);
            transfer(convertString2TwoByteArray(n));
        }
    }
    //Set the width of print area (14.17) (With Parameter)
    public void GS_W(String n){
        if(mValidator.check(n, 0, 576)){
            transfer(myCommand.GS_W);
            transfer(convertString2TwoByteArray(n));
        }
    }
    //Specify the print area on page mode (14.18) (With Parameter)
    public void ESC_W(String x, String y, String xLength, String yLength){

        if(mValidator.check(x, 0, 576)
                && mValidator.check(y, 0, 576)
                && mValidator.check(xLength, 0, 576)
                && mValidator.check(yLength, 0, 576)){

            transfer(myCommand.ESC_W);
            transfer(convertString2TwoByteArray(x));
            transfer(convertString2TwoByteArray(y));
            transfer(convertString2TwoByteArray(xLength));
            transfer(convertString2TwoByteArray(yLength));

        }

    }
    //Set the physical position (14.19) (With Parameter)
    public void ESC_PP(String n){
        if(mValidator.check(n, 0, 576)){
            transfer(myCommand.ESC_PP);
            transfer(convertString2TwoByteArray(n));
        }
    }
    //Set the logical position (14.20) (With Parameter)
    public void ESC_LP(String n){
        if(mValidator.check(n, 0, 576)){
            transfer(myCommand.ESC_LP);
            transfer(convertString2TwoByteArray(n));
        }
    }
    //Set the vertical physical position in page mode (14.21) (With Parameter)
    public void GS_VPP(String n){
        if(mValidator.check(n, 0, 576)){
            transfer(myCommand.GS_VPP);
            transfer(convertString2TwoByteArray(n));
        }
    }
    //Set the vertical logical position on page mode (14.22) (With Parameter)
    public void GS_VLP(String n){
        if(mValidator.check(n, 0, 576)){
            transfer(myCommand.GS_VLP);
            transfer(convertString2TwoByteArray(n));
        }
    }
    //Print bit image (14.23) (With Parameter)
    public void ESC_BIMG(Bitmap bmp, byte[] n){
        /*
        Here, In Bit Image Implementation.
        m = 0,1,32 or 33
         */

        /***Need to implement***/
        transfer(myCommand.ESC_BIMG);
        transfer(n);

    }
    //Print raster bit image (14.24) (With Parameter)
    public void GS_v(Bitmap bmp, String n){
        if(mValidator.check(n, 0, 3)){
            transfer(myCommand.GS_v);
            transfer(convertString2ByteArray(n));
            transfer(Utils_1.decodeRasterBitImage(Utils.floydSteinbergDithering(bmp)));
        }
    }
    //Print NV Bit Image (14.25) (With Parameter)
    public void FS_PP(String select, String mode){
        if(mValidator.check(select, 1, 255)
                && mValidator.check(mode, 0, 3)){
            transfer(myCommand.FS_P);
            transfer(convertString2ByteArray(select));
            transfer(convertString2ByteArray(mode));
        }
    }
    //Select NV Bit Image (14.25) (With Parameter)
    public void FS_PS(String select, String mode, String offset){
        if(mValidator.check(select, 1, 255)
                && mValidator.check(mode, 4, 7)
                && mValidator.check(offset, 0, 255)){

            transfer(myCommand.FS_P);
            transfer(convertString2ByteArray(select));
            transfer(convertString2ByteArray(mode));
            transfer(convertString2ByteArray(offset));
        }
    }

    /**
     * Define NV Bit Image (14.26) (With Parameter)
     * TODO - Image size should be as per document
     */
    public void FS_Q(Bitmap[] bmp, String n){
        try{
            if(bmp.length == Integer.parseInt(n)){
                if(mValidator.check(n, 1, 255)){
                    transfer(myCommand.FS_Q);
                    transfer(convertString2ByteArray(n));
                    //Transfer Image
                    for(int z = 0; z < Integer.parseInt(n); z++)
                        transfer(Utils_1.decodeNVBitImage(Utils.floydSteinbergDithering(
                                RotateBitmap(getResizedBitmap(bmp[z]), 90))));
                }
            }else{
                throw new NVBitException("Incorrect Number of NV Bit images provided!");
            }
        }catch (NVBitException e){
            Log.e("Error",e.getMessage());
        }
    }
    //Select LSB-MSB direction in image (14.27) (With Parameter)
    public void DC2_DIR(String n){
        if(mValidator.check(n, 0, 255)){
            transfer(myCommand.DC2_DIR);
            transfer(convertString2ByteArray(n));
        }
    }
    //Set print position of HRI characters (14.28) (With Parameter)
    public void GS_H(String n){
        if(mValidator.check(n,0,3)){
            transfer(myCommand.GS_H);
            transfer(convertString2ByteArray(n));
        }
    }
    //Select HRI character size (14.29) (With Parameter)
    public void GS_F(String n){
        if(mValidator.check(n,0,1)){
            transfer(myCommand.GS_F);
            transfer(convertString2ByteArray(n));
        }
    }
    //Set barcode height (14.30) (With Parameter)
    public void GS_h(String n){
        if(mValidator.check(n,0,255)){
            transfer(myCommand.GS_h);
            transfer(convertString2ByteArray(n));
        }
    }
    //Set barcode width (14.31) (With Parameter)
    public void GS_w(String n){
        if(mValidator.check(n,2,6)){
            transfer(myCommand.GS_w);
            transfer(convertString2ByteArray(n));
        }
    }
    //Set N:W aspect of the barcode (14.32) (With Parameter)
    public void DC2_ARB(String n){
        if(mValidator.check(n,0,2)){
            transfer(myCommand.DC2_ARB);
            transfer(convertString2ByteArray(n));
        }
    }
    //Print Barcode (14.33) (With Parameter)
    public void GS_k(BarcodeType barcodeType, String barcodeData) {
        try {
            if (mValidator.check_barcode(barcodeType, barcodeData)) {
                transfer(myCommand.GS_k);
                transfer(convertString2ByteArray(Integer.toString(barcodeType.getBarcode_type())));
                transfer(convertString2ByteArray(String.valueOf(barcodeData.length())));
                transfer(barcodeData);
            } else {
                throw new ValueOutOfBoundException("Invalid Input :" + barcodeData);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    //Initialize the printer (14.34)
    public void ESC_INIT(){
        transfer(myCommand.ESC_INIT);
    }
    //Set the print density (14.40) (With Parameter)
    public void DC2_PD(String n){
        if(mValidator.check(n, 65, 135)){
            transfer(myCommand.DC2_PD);
            transfer(convertString2ByteArray(n));
        }
    }
    /*Cue the marked form(feed the form to print start position)
     * (14.41)
     */
    public void GS_MF(){
        transfer(myCommand.GS_MF);
    }
    //Cut the form (14.43)
    public void ESC_I(){
        transfer(myCommand.ESC_I);
    }

    //Inform system time of the host (14.51) (With Parameter)
    public void GS_i(byte[] n){
        transfer(myCommand.GS_i);
        transfer(n);
    }
    //Download the firmware (14.52)
    public void ACK(){
        transfer(myCommand.ACK);
    }
    //Feed the form in pixels (14.54) (With Parameter)
    public void GS_d(String n){
        if(mValidator.check(n, 0, 255)){
            transfer(myCommand.GS_d);
            transfer(convertString2ByteArray(n));
        }
    }
    //Turn Underline button ON/OFF (14.70)
    public void ESC_hyphen(String n){
        if(mValidator.check(n,0,2)){
            transfer(myCommand.ESC_hyphen);
            transfer(convertString2ByteArray(n));
        }
    }
    //Select default line spacing (14.71)
    public void ESC_2(){
        transfer(myCommand.ESC_2);
    }
    //Turn emphasized mode on/off (14.72)
    public void ESC_E(String n){
        if(mValidator.check(n, 0,255)){
            transfer(myCommand.ESC_E);
            transfer(convertString2ByteArray(n));
        }
    }
    //Select print direction in page mode (14.73)
    public void ESC_T(String n){
        if(mValidator.check(n, 0,3)){
            transfer(myCommand.ESC_T);
            transfer(convertString2ByteArray(n));
        }
    }
    //Select justification (14.74)
    public void ESC_a(String n){
        if(mValidator.check(n, 0,2)){
            transfer(myCommand.ESC_a);
            transfer(convertString2ByteArray(n));
        }
    }
    //Print downloaded bit image (14.77)
    public void GS_FS(String n){
        if(mValidator.check(n, 0,3)){
            transfer(myCommand.GS_FS);
            transfer(convertString2ByteArray(n));
        }
    }
    //Turn white/black reverse print mode on/off (14.78)
    public void GS_B(String n){
        if(mValidator.check(n, 0,255)){
            transfer(myCommand.GS_B);
            transfer(convertString2ByteArray(n));
        }
    }
    //Select cut mode and cut paper (14.79)
    public void GS_V(FunctionType functionType, CutCommand mode, String n){
        if(functionType.equals(FunctionType.C)){
            if(mode == CutCommand.FULLCUT){
                if(mValidator.check(n, 0,255)){
                    transfer(myCommand.GS_V);
                    transfer(new byte[]{0x61});
                    transfer(convertString2ByteArray(n));
                }
            }
            else if(mode == CutCommand.PARTIALCUT){
                if(mValidator.check(n, 0,255)){
                    transfer(myCommand.GS_V);
                    transfer(new byte[]{0x62});
                    transfer(convertString2ByteArray(n));
                }
            }
        }
        else if(functionType.equals(FunctionType.B)){
            if(mode == CutCommand.FULLCUT){
                if(mValidator.check(n, 0,255)){
                    transfer(myCommand.GS_V);
                    transfer(new byte[]{0x41});
                    transfer(convertString2ByteArray(n));
                }
            }
            else if(mode == CutCommand.PARTIALCUT){
                if(mValidator.check(n, 0,255)){
                    transfer(myCommand.GS_V);
                    transfer(new byte[]{0x42});
                    transfer(convertString2ByteArray(n));
                }
            }
        }
        else if(functionType.equals(FunctionType.A)){
            if(mode == CutCommand.FULLCUT){
                transfer(myCommand.GS_V);
                transfer(new byte[]{0x30});
            }
            else if(mode == CutCommand.PARTIALCUT){
                    transfer(myCommand.GS_V);
                    transfer(new byte[]{0x31});
            }
        }
        else{
            throw new InvalidParameterException();
        }
    }


    /*
    Extra Utility methods
     */
    //Resize the Bitmap Image
    public Bitmap getResizedBitmap(Bitmap bm) {

        int width = (bm.getWidth()/8)*8;
        int height = (bm.getHeight()/8)*8;

        float scaleWidth = ((float) width / bm.getWidth());
        float scaleHeight = ((float) height / bm.getHeight());

        // CREATE A MATRIX FOR THE MANIPULATION

        Matrix matrix = new Matrix();

        // RESIZE THE BIT MAP

        matrix.postScale(scaleWidth, scaleHeight);

        // RECREATE THE NEW BITMAP

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
                matrix, false);

        return resizedBitmap;

    }

    public static Bitmap RotateBitmap(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        matrix.postScale(-1,1,0,0);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    /***
     * Convert String to Byte Array
     * Note: tmp = 0 (Default)
     */
    /*** Merge these methods later ***/
    public byte[] convertString2ByteArray(String n){
        byte[] tmp = new byte[1];
        tmp[0] = (byte) Integer.parseInt(n);
        return tmp;
    }
    public byte[] convertString2TwoByteArray(String n){
        byte[] tmp = new byte[2];//nL and nH
        int temp = Integer.parseInt(n);
        tmp[0] = (byte) (temp % 256);
        temp = temp/256;
        tmp[1] = (byte) (temp % 256);
        return tmp;
    }

    /**
     * Vendor Requests (OR) Control Transfer
     */
    public byte[] controlTransfer(String Choice) {

        //Read Buffer
        byte[] buff = new byte[16];

        switch(Choice){
            /**
             * Reply Printer Status (15.1)
             */
            case "1":
                mConnection.controlTransfer(0xc1,
                        0x07,0,0,buff,10,1000);//10
                return buff;
            /**
             * Reply Firmware Version (15.2)
             */
            case "2":
                mConnection.controlTransfer(0xc1,
                        0x03,0,0,buff,12,1000);//12
                return buff;
            /**
             * Reply Flash memory free space (15.3)
             */
            case "3":
                mConnection.controlTransfer(0xc1,
                        0x05,0,0,buff,4,1000);//4
                return buff;
            /**
             * Reset Printer (15.4)
             */
            case "4":
                mConnection.controlTransfer(0xc1,
                    0x07,0,0,buff,10,1000);//10
                return buff;
            /**
             * Reply Traceability information (15.5)
             */
            case "5":
                mConnection.controlTransfer(0xc1,
                        0x11,0,0,buff,12,1000);//12
                return buff;

            default:
                Log.e("Error","Invalid Control Request");
                return buff;
        }
    }
}