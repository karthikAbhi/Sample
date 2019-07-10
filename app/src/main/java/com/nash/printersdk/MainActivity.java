package com.nash.printersdk;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

//Nash - USB library package for Android
import com.nash.usblib.BarcodeType;
import com.nash.usblib.CutCommand;
import com.nash.usblib.FunctionType;
import com.nash.usblib.MyPrinter;
import com.nash.usblib.PDF417ErrorCorrectionLevel;
import com.nash.usblib.PDF417ErrorCorrectionMode;
import com.nash.usblib.PDF417Options;
import com.nash.usblib.QRErrCorrLvl;

import java.io.IOException;
import java.util.Calendar;

import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_ATTACHED;
import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_DETACHED;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MyPrinter printer;
    private Context context;

    /**
     * UI - Button References
     */
    private Button mPrintButton;
    private Button mLFCommandButton;
    private Button mFFCommandButton;
    private Button mPMCommandButton;
    private Button mSMCommandButton;
    private Button mHTCommandButton;
    private Button mInitializePrinterCommandButton;
    private Button mGSMFCommandButton;
    private Button mCutFormCommandButton;
    private Button mFFIPIXELSCommandButton;
    private Button mPDFF2FCommandButton;
    private Button mPDPMCommandButton;
    private Button mPDFNLCommandButton;
    private Button mSLFCommandButton;
    private Button mSGRSCCommandButton;
    private Button mSPPMCommandButton;
    private Button mSelectFontCommandButton;
    //private Button mSelCharCodeTableCommandButton;
    private Button mSpecifyDownldCharCommandButton;
    private Button mSetOrUnsetDCSCommandButton;
    private Button mSetLeftMarginCommandButton;
    private Button mSetWidthOfPrintAreaCommandButton;
    private Button mSpecifyPrintAreaOnPageModeCommandButton;
    private Button mSetPhysicalPositionCommandButton;
    private Button mSetLogicalPositionCommandButton;
    private Button mSetVerticalPhysicalPositionOnPageModeCommandButton;
    private Button mSelectLSB_MSBDirectionInImageCommandButton;
    private Button mSetVerticalLogicalPositionOnPageModeCommandButton;
    private Button mSetPrintPositionOfHRICharCommandButton;
    private Button mSelectHRICharacterSizeCommandButton;
    private Button mSetBarcodeHeightCommandButton;
    private Button mSetBarcodeWidthCommandButton;
    private Button mSetNWAspectBarcode;
    private Button mPrintBarcodeButton;
    private Button mSetPrintDensity;
    //private Button mSpecifyResponseParameterCommandButton;
    private Button mInformSysTimeOfHostCommandButton;
    private Button mPrintBitImageCommandButton;
    private Button mPrintRasterBitImageCommandButton;
    private Button mPrintNVBitImageCommandButton;
    private Button mSelectNVBitImageCommandButton;
    private Button mDefineNVBitImageCommandButton;
    private Button mtUOnOffCommandButton;
    private Button mSDLSCommandButton;
    private Button mTEMOnOffCommandButton;
    private Button mSPDPMCommandButton;
    private Button mSJCommandButton;
    private Button mQRCodeCommandButton;
    private Button mPDBICommandButton;
    private Button mTurnWBRPOnOffCommandButton;
    private Button mSCMCPCommandButton;
    private Button mPDF417CommandButton;

    private RadioGroup mRadioGroupQR;
    private RadioButton mRadioButtonQR;

    private RadioGroup mRadioGroupFT;
    private RadioButton mRadioButtonFT;
    private RadioGroup mRadioGroupCT;
    private RadioButton mRadioButtonCT;

    private Spinner mBarcodeSpinner;
    private int mBarcodeTypeSelected;

    //Control Transfer
    private Button mControlTransfer;

    /**
     * UI - EditText References
     */
    private EditText mSampleTextEditText;
    private EditText mPDFF2FEditText;
    private EditText mPDFNLEditText;
    private EditText mSLFEditText;
    private EditText mSGRSCEditText;
    private EditText mSPPMEditText;
    private EditText mSelectFontEditText;
    private EditText mSelCharCodeTableEditText;
    private EditText mSpecifyDownldCharEditText;
    private EditText mSetOrUnsetDCSEditText;
    private EditText mSetLeftMarginEditText;
    private EditText mSetWidthOfPrintAreaEditText;
    private EditText mSPAPMxaxisEditText,mSPAPMyaxisEditText,mSPAPMxlengthEditText,
            mSPAPMylengthEditText;//mSpecifyPrintAreaOnPageModeEditText
    private EditText mSetPhysicalPositionEditText;
    private EditText mSetLogicalPositionEditText;
    private EditText mSetVerticalPhysicalPositionOnPageModeEditText;
    private EditText mSetVerticalLogicalPositionOnPageModeEditText;
    //Raster Bit - Printing
    private EditText mModeOfRasterBitEditText;
    //NV Bit - Printing
    private EditText mSelectPNVBitEditText, mModePNVBitEditText;
    //NV Bit - Selection
    private EditText mSelectSNVBitEditText, mModeSNVBitEditText, mOffsetSNVBitEditText;
    private EditText mNumberOfNVBitImagesEditText;
    private EditText mSelectLSB_MSBDirectionInImageEditText;
    private EditText mSetPrintPositionOfHRICharEditText;
    private EditText mSelectHRICharacterSizeEditText;
    private EditText mSetBarcodeHeightEditText;
    private EditText mSetBarcodeWidthEditText;
    private EditText mSetNWAspectBarcodeEditText;
    private EditText mBarcodeDataEditText;
    private EditText mSetPrintDensityEditText;
    private EditText mSpecifyResponseParameterEditText;
    private EditText mInformSysTimeOfHostEditText;
    private EditText mFFIPIXELSEditText;
    private EditText mUnderLineEditText;
    private EditText mTEMOnOffEditText;
    private EditText mSPDPMEditText;
    private EditText mSJEditText;
    private EditText mQRSizeEditText, mQRUserDataEditText;
    private EditText mPDBIEditText;
    private EditText mTurnWBRPOnOffEditText;
    private EditText mSCMCPEditText;

    //TODO Control Transfer
    private EditText mControlTransferEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        printer = MyPrinter.getInstance(context);

        // Intent Filters

        //TODO: Make printer specific static within MyPrinter
        IntentFilter filterOnConnection = new IntentFilter(ACTION_USB_DEVICE_ATTACHED);
        IntentFilter filterOffConnection = new IntentFilter(ACTION_USB_DEVICE_DETACHED);

        // Register Broadcast Receivers
        context.registerReceiver(mUsbReceiver1, filterOffConnection);
        context.registerReceiver(mUsbReceiver2, filterOnConnection);

        /***
         *
         * UI Components - EditText Reference Creation
         *
         * ***/
        mSampleTextEditText = findViewById(R.id.sampleTextEditText);
        mPDFF2FEditText = findViewById(R.id.pdff2fEditText);
        mPDFNLEditText = findViewById(R.id.pdfnlEditText);
        mSLFEditText = findViewById(R.id.slfEditText);
        mSGRSCEditText = findViewById(R.id.sgrscEditText);
        mSPPMEditText = findViewById(R.id.sppmEditText);
        mSelectFontEditText =findViewById(R.id.selFontEditText);
        //mSetOrUnsetDCSEditText = findViewById(R.id.setOrUnsetDCSetEditText);
        mSetLeftMarginEditText = findViewById(R.id.setLeftMarginEditText);
        mSetWidthOfPrintAreaEditText = findViewById(R.id.setWidthOfPrintAreaEditText);
        mSPAPMxaxisEditText = findViewById(R.id.spapmxaxisEditText);
        mSPAPMyaxisEditText = findViewById(R.id.spapmyaxisEditText);
        mSPAPMxlengthEditText = findViewById(R.id.spapmxlengthEditText);
        mSPAPMylengthEditText = findViewById(R.id.spapmylengthEditText);
        mSetPhysicalPositionEditText = findViewById(R.id.setPhysicalPositionEditText);
        mSetLogicalPositionEditText = findViewById(R.id.setLogicalPositionEditText);
        mSetVerticalPhysicalPositionOnPageModeEditText = findViewById(R.id.setVPPOnPageModeEditText);
        mSetVerticalLogicalPositionOnPageModeEditText = findViewById(R.id.setVLPOnPageModeEditText);
        mModeOfRasterBitEditText = findViewById(R.id.modeOfRasterBitEditText);
        mSelectPNVBitEditText = findViewById(R.id.selectPNVBitEditText);
        mModePNVBitEditText = findViewById(R.id.modePNVBitEditText);
        mSelectSNVBitEditText = findViewById(R.id.selectSNVBitEditText);
        mModeSNVBitEditText = findViewById(R.id.modeSNVBitEditText);
        mOffsetSNVBitEditText = findViewById(R.id.offsetSNVBitEditText);
        mNumberOfNVBitImagesEditText = findViewById(R.id.numberOfNVBitImagesEditText);
        mSelectLSB_MSBDirectionInImageEditText = findViewById(R.id.selectLSB_MSBDirInImgEditText);
        mSetPrintPositionOfHRICharEditText = findViewById(R.id.setPosHRICharEditText);
        mSelectHRICharacterSizeEditText = findViewById(R.id.selectHRICharSizeEditText);
        mSetBarcodeHeightEditText = findViewById(R.id.setBarcodeHeightEditText);
        mSetBarcodeWidthEditText = findViewById(R.id.setBarcodeWidthEditText);
        mSetNWAspectBarcodeEditText = findViewById(R.id.setNWAspectOfBarcodeEditText);
        mBarcodeDataEditText = findViewById(R.id.barcodeDataEditText);
        mSetPrintDensityEditText = findViewById(R.id.setPrintDensityEditText);
        mFFIPIXELSEditText = findViewById(R.id.ffinPixelsEditText);
        mUnderLineEditText = findViewById(R.id.underLineEditText);
        mTEMOnOffEditText = findViewById(R.id.turnEmphasizedOnOffEditText);
        mSPDPMEditText = findViewById(R.id.selectPrintDirInPageModeEditText);
        mSJEditText = findViewById(R.id.selectJustificationEditText);
        mQRSizeEditText = findViewById(R.id.qrSizeEditText);
        mQRUserDataEditText = findViewById(R.id.qrUserDataEditText);
        //mPDBIEditText = findViewById(R.id.printDownldBitImgEditText);
        mTurnWBRPOnOffEditText = findViewById(R.id.turnBlackWhiteRevPrintModeOnOffEditText);
        mSCMCPEditText = findViewById(R.id.selectCutModeCutPaperEditText);

        //TODO Control Transfer
        mControlTransferEditText = findViewById(R.id.controlTransferEditText);

        /***
         *
         *
         * UI Components - Button Reference Creation
         *
         * ***/
        //Print Text
        mPrintButton = findViewById(R.id.printButton);
        //LF Command
        mLFCommandButton = findViewById(R.id.lfCommandButton);
        //FF Command
        mFFCommandButton = findViewById(R.id.ffCommandButton);
        //Print data and Feed form to forward Command
        mPDFF2FCommandButton = findViewById(R.id.pdff2fButton);
        //Print data on feed n lines Command
        mPDFNLCommandButton = findViewById(R.id.pdfnlButton);
        //Print data on page mode Command
        mPDPMCommandButton = findViewById(R.id.pdpmButton);
        //Set the line feed amount Command
        mSLFCommandButton = findViewById(R.id.slfButton);
        //Set the gap of right side of the characters Command (14.07)
        mSGRSCCommandButton = findViewById(R.id.sgrscButton);
        //Set parameters of Print mode Command (14.08)
        mSPPMCommandButton = findViewById(R.id.sppmButton);
        //Select the font Command (14.09)
        mSelectFontCommandButton = findViewById(R.id.selFontButton);
        //Select character code table Command (14.10)
        //mSelCharCodeTableCommandButton = findViewById(R.id.selCharCodeTableButton);
        //Specify download character Command (14.11)
        //mSpecifyDownldCharCommandButton = findViewById(R.id.specifyDownldCharButton);
        //Set or unset download character set Command (14.12)
        //mSetOrUnsetDCSCommandButton = findViewById(R.id.setOrUnsetDCSetButton);
        //Page Mode Command (14.13)
        mPMCommandButton = findViewById(R.id.pageModeButton);
        //Standard Mode Command (14.14)
        mSMCommandButton = findViewById(R.id.standardModeButton);
        //Horizontal Tab Command (14.15)
        mHTCommandButton = findViewById(R.id.horizontalTabButton);
        //Set the left side margin Command (14.16)
        mSetLeftMarginCommandButton = findViewById(R.id.setLeftMarginButton);
        //Set the width of print area Command (14.17)
        mSetWidthOfPrintAreaCommandButton = findViewById(R.id.setWidthOfPrintAreaButton);
        //Set the print area on page mode (14.18)
        mSpecifyPrintAreaOnPageModeCommandButton = findViewById(R.id.specifyPrintAreaOnPageModeButton);
        //Set the physical position (14.19)
        mSetPhysicalPositionCommandButton = findViewById(R.id.setPhysicalPositionButton);
        //Set the logical position (14.20)
        mSetLogicalPositionCommandButton = findViewById(R.id.setLogicalPositionButton);
        //Set the vertical physical position in page mode (14.21)
        mSetVerticalPhysicalPositionOnPageModeCommandButton = findViewById(R.id.setVPPOnPageModeButton);
        //Set the vertical logical position in page mode (14.22)
        mSetVerticalLogicalPositionOnPageModeCommandButton = findViewById(R.id.setVLPOnPageModeButton);
        //Print bit image (14.23)
        mPrintBitImageCommandButton = findViewById(R.id.printBitImageButton);
        //Print raster bit image (14.24)
        mPrintRasterBitImageCommandButton = findViewById(R.id.printRasterBitImageButton);
        //Print NV Bit Image (14.25)
        mPrintNVBitImageCommandButton = findViewById(R.id.printNVBitImageButton);
        //Select NV Bit Image (14.25)
        mSelectNVBitImageCommandButton = findViewById(R.id.selectNVBitImageButton);
        //Define NV Bit Image (14.26)
        mDefineNVBitImageCommandButton = findViewById(R.id.defineNVBitImageButton);
        //Select LSB-MSB direction in image (14.27)
        mSelectLSB_MSBDirectionInImageCommandButton = findViewById(R.id.selectLSB_MSBDirInImgButton);
        //Set print position of HRI characters (14.28)
        mSetPrintPositionOfHRICharCommandButton = findViewById(R.id.setPosHRICharButton);
        //Select HRI character size (14.29)
        mSelectHRICharacterSizeCommandButton = findViewById(R.id.selectHRICharSizeButton);
        //Set barcode height (14.30)
        mSetBarcodeHeightCommandButton = findViewById(R.id.setBarcodeHeightButton);
        //Set barcode width (14.31)
        mSetBarcodeWidthCommandButton = findViewById(R.id.setBarcodeWidthButton);
        //Set N:W aspect of the barcode (14.32)
        mSetNWAspectBarcode = findViewById(R.id.setNWAspectOfBarcodeButton);
        //Barcode Spinner
        mBarcodeSpinner = findViewById(R.id.spinner_barcode_types);
        //Print Barcode (14.33)
        mPrintBarcodeButton = findViewById(R.id.printBarcodeButton);
        //Initialize Printer Command
        mInitializePrinterCommandButton = findViewById(R.id.initializePrinterButton);
        //Set print density (14.40)
        mSetPrintDensity = findViewById(R.id.setPrintDensityButton);
        //Cue the Marked form Command
        mGSMFCommandButton = findViewById(R.id.gsMFButton);
        //Cut form Command
        mCutFormCommandButton = findViewById(R.id.cutFormButton);
        //Inform system time of the host (14.51)
        mInformSysTimeOfHostCommandButton = findViewById(R.id.infoSysTimeHostButton);
        //Feed form in pixels Command (14.54)
        mFFIPIXELSCommandButton = findViewById(R.id.ffinPixelsButton);
        //Turn Underline button ON/OFF (14.70)
        mtUOnOffCommandButton = findViewById(R.id.underLineButton);
        //Select default line spacing (14.71)
        mSDLSCommandButton = findViewById(R.id.setDefaultLineSpaceButton);
        //Turn emphasized mode on/off (14.72)
        mTEMOnOffCommandButton = findViewById(R.id.turnEmphasizedOnOffButton);
        //Select print direction in page mode (14.73)
        mSPDPMCommandButton = findViewById(R.id.selectPrintDirInPageModeButton);
        //Select justification (14.74)
        mSJCommandButton = findViewById(R.id.selectJustificationButton);
        //Print QR Code (14.75) Part - 2
        mRadioGroupQR = findViewById(R.id.radioGroup_QRType);
        mRadioButtonQR = findViewById(R.id.qr_L);
        mQRCodeCommandButton = findViewById(R.id.qrCodeButton);
        //Print downloaded bit image (14.77)
        //mPDBICommandButton = findViewById(R.id.printDownldBitImgButton);
        //Turn white/black reverse print mode on/off (14.78)
        mTurnWBRPOnOffCommandButton = findViewById(R.id.turnBlackWhiteRevPrintModeOnOffButton);
        //Select cut mode and cut paper (14.79)
        mRadioGroupFT = findViewById(R.id.radioGroup_FunctionType);
        mRadioGroupCT = findViewById(R.id.radioGroup_CutType);
        mRadioButtonFT = findViewById(R.id.FT_a);
        mRadioButtonCT = findViewById(R.id.full_cut);
        mSCMCPCommandButton = findViewById(R.id.selectCutModeCutPaperButton);
        mPDF417CommandButton = findViewById(R.id.pdf417Button);

        //Control Transfer
        mControlTransfer = findViewById(R.id.controlTransferButton);

        /***
         *
         * IMPLEMENTATION
         *
         * ***/

        //Print data and feed a line (14.01)
        mLFCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.LF();
            }
        });
        //Print data and feed a page on page mode (14.02)
        mFFCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.FF();
            }
        });
        //Print data on page mode (14.03)
        mPDPMCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.ESC_FF();
            }
        });
        //Print data and feed the form to forward (14.04)
        mPDFF2FCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.ESC_J(mPDFF2FEditText.getText().toString());
            }
        });
        //Print data and feed n lines (14.05)
        mPDFNLCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.ESC_D(mPDFNLEditText.getText().toString());
            }
        });
        mSLFCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.ESC_3(mSLFEditText.getText().toString());
            }
        });
        mSGRSCCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.ESC_SP(mSGRSCEditText.getText().toString());
            }
        });
        //Need to implement parameters
        mSPPMCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.ESC_PM(mSPPMEditText.getText().toString());
            }
        });
        //Need to implement parameters
        mSelectFontCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.ESC_M(mSelectFontEditText.getText().toString());
            }
        });
        //TODO - Praveen need to provide more information
        /*mSelCharCodeTableCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] a = new byte[1];
                a[0] = (byte)0;//Default - 0
                printer.ESC_t(a);
            }
        });*/
        //TODO - Need to talk to Reddy sir
        /*mSpecifyDownldCharCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Will be implemented later
                Toast.makeText(getApplicationContext(),"Will be implemented later",Toast.LENGTH_SHORT).show();
            }
        });
        mSetOrUnsetDCSCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.ESC_SDC(mSetOrUnsetDCSEditText.getText().toString());
            }
        });*/


        mPMCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.PAGE_MODE();
            }
        });

        mSMCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.STANDARD_MODE();
            }
        });
        mHTCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.HT();
            }
        });
        //Set the left side margin Command (14.16)
        mSetLeftMarginCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.GS_L(mSetLeftMarginEditText.getText().toString());
            }
        });
        //Set the width of print area Command (14.17)
        mSetWidthOfPrintAreaCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.GS_W(mSetWidthOfPrintAreaEditText.getText().toString());
            }
        });
        //Set the print area on page mode (14.18)
        mSpecifyPrintAreaOnPageModeCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                printer.ESC_W(mSPAPMxaxisEditText.getText().toString(),
                        mSPAPMyaxisEditText.getText().toString(),
                        mSPAPMxlengthEditText.getText().toString(),
                        mSPAPMylengthEditText.getText().toString());
            }
        });
        //Set the physical position (14.19)
        mSetPhysicalPositionCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.ESC_PP(mSetPhysicalPositionEditText.getText().toString());
            }
        });
        //Set the logical position (14.20)
        mSetLogicalPositionCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.ESC_LP(mSetLogicalPositionEditText.getText().toString());
            }
        });
        //Set the vertical physical position in page mode (14.21)
        mSetVerticalPhysicalPositionOnPageModeCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.GS_VPP(mSetVerticalPhysicalPositionOnPageModeEditText.getText().toString());
            }
        });
        //Set the vertical logical position on page mode (14.22)
        mSetVerticalLogicalPositionOnPageModeCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.GS_VLP(mSetVerticalLogicalPositionOnPageModeEditText.getText().toString());
            }
        });
        //Print bit image (14.23)
        mPrintBitImageCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickBitImage();
            }
        });

        //Print raster bit image (14.24)
        mPrintRasterBitImageCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickRasterBitImage();
            }
        });
        //Print NV Bit Image (14.25)
        mPrintNVBitImageCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.FS_PP(mSelectPNVBitEditText.getText().toString(),
                        mModePNVBitEditText.getText().toString());
            }
        });
        //Select NV Bit Image (14.25)
        mSelectNVBitImageCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.FS_PS(mSelectSNVBitEditText.getText().toString(),
                        mModeSNVBitEditText.getText().toString(),
                        mOffsetSNVBitEditText.getText().toString());
            }
        });
        //Define NV Bit Image (14.26)
        mDefineNVBitImageCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(Integer.parseInt(mNumberOfNVBitImagesEditText.getText().toString()) > 0){
                        pickNVBitImage();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),
                                "Enter number of NV bit Images to be defined",
                                Toast.LENGTH_SHORT).show();
                    }
                }catch (NumberFormatException e){
                    Log.e("Error",e.getMessage());
                }


            }
        });

        //Select LSB-MSB direction in image (14.27)
        mSelectLSB_MSBDirectionInImageCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.DC2_DIR(mSelectLSB_MSBDirectionInImageEditText.getText().toString());
            }
        });

        //Set print position of HRI characters (14.28)
        mSetPrintPositionOfHRICharCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.GS_H(mSetPrintPositionOfHRICharEditText.getText().toString());
            }
        });

        //Select HRI character size (14.29)
        mSelectHRICharacterSizeCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.GS_F(mSelectHRICharacterSizeEditText.getText().toString());
            }
        });

        //Set barcode height (14.30)
        mSetBarcodeHeightCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.GS_h(mSetBarcodeHeightEditText.getText().toString());
            }
        });

        //Set barcode width (14.31)
        mSetBarcodeWidthCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.GS_w(mSetBarcodeWidthEditText.getText().toString());
            }
        });

        //Set N:W aspect of the barcode (14.32)
        mSetNWAspectBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.DC2_ARB(mSetNWAspectBarcodeEditText.getText().toString());
            }
        });
        
        //Print Barcode (14.33)
        ArrayAdapter<String> barcodeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.barcode_types));
        barcodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBarcodeSpinner.setAdapter(barcodeAdapter);

        mBarcodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mBarcodeTypeSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mBarcodeTypeSelected = 0;
            }
        });

        mPrintBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch(mBarcodeTypeSelected){
                    case 0: printer.GS_k(BarcodeType.UPC_A, mBarcodeDataEditText.getText().toString());
                    break;
                    case 1: printer.GS_k(BarcodeType.UPC_E, mBarcodeDataEditText.getText().toString());
                    break;
                    case 2: printer.GS_k(BarcodeType.JAN13, mBarcodeDataEditText.getText().toString());
                    break;
                    case 3: printer.GS_k(BarcodeType.JAN8, mBarcodeDataEditText.getText().toString());
                    break;
                    case 4: printer.GS_k(BarcodeType.CODE39, mBarcodeDataEditText.getText().toString());
                    break;
                    case 5: printer.GS_k(BarcodeType.ITF, mBarcodeDataEditText.getText().toString());
                    break;
                    case 6: printer.GS_k(BarcodeType.CODABAR, mBarcodeDataEditText.getText().toString());
                    break;
                    case 7: printer.GS_k(BarcodeType.CODE93, mBarcodeDataEditText.getText().toString());
                    break;
                    default: printer.GS_k(BarcodeType.UPC_A, mBarcodeDataEditText.getText().toString());
                    break;
                }

                /*switch(mBarcodeTypeSelected){
                    case 0: printer.GS_k(BarcodeType.UPC_A, "72527273070");
                        break;
                    case 1: printer.GS_k(BarcodeType.UPC_E, "01150000066");
                        break;
                    case 2: printer.GS_k(BarcodeType.JAN13, "122245678931");
                        break;
                    case 3: printer.GS_k(BarcodeType.JAN8, "1245678");
                        break;
                    case 4: printer.GS_k(BarcodeType.CODE39, "1AE% ");
                        break;
                    case 5: printer.GS_k(BarcodeType.ITF, "1456");
                        break;
                    case 6: printer.GS_k(BarcodeType.CODABAR, "A12345A");
                        break;
                    case 7: printer.GS_k(BarcodeType.CODE93, "A12345A");
                        break;
                    default: printer.GS_k(BarcodeType.UPC_A, "72527273070");
                        break;
                }*/

                //printBarcode();
                //printer.GS_k(BarcodeType.UPC_A, "72527273070");
                //printer.GS_k(BarcodeType.UPC_E, "01150000066");
                //printer.GS_k(BarcodeType.JAN13, "122245678931");
                //printer.GS_k(BarcodeType.JAN8, "1245678");
                //printer.GS_k(BarcodeType.CODE39, "1AE% ");
                //printer.GS_k(BarcodeType.ITF, "1456");
                //printer.GS_k(BarcodeType.CODABAR, "A12345A");
                //printer.GS_k(BarcodeType.CODE93, "A12345A");


            }
        });

        //Initialize the printer (14.34)
        mInitializePrinterCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.ESC_INIT();
            }
        });

        //Set print density (14.40)
        mSetPrintDensity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.DC2_PD(mSetPrintDensityEditText.getText().toString());
            }
        });

        /*Cue the marked form(feed the form to print start position)
         * (14.41)
         */
        mGSMFCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.GS_MF();
            }
        });

        //Cut the form (14.43)
        mCutFormCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.ESC_I();
            }
        });

        //Inform system time of the host (14.51)
        mInformSysTimeOfHostCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] a = new byte[6];
                a[0] = (byte)Calendar.getInstance().get(Calendar.SECOND);//d1 - second
                a[1] = (byte)Calendar.getInstance().get(Calendar.MINUTE);//d2 - minute
                a[2] = (byte)Calendar.getInstance().get(Calendar.HOUR);//d3 - hour
                a[3] = (byte)Calendar.getInstance().get(Calendar.DAY_OF_MONTH);//d4 - day
                a[4] = (byte)Calendar.getInstance().get(Calendar.MONTH);//d5 - month
                a[5] = (byte)Calendar.getInstance().get(Calendar.YEAR);//d6 - year

                printer.GS_i(a);
            }
        });

        //Download the firmware (14.52)

        //Feed the form in pixels (14.54)
        mFFIPIXELSCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.GS_d(mFFIPIXELSEditText.getText().toString());
            }
        });

        //Turn Underline button ON/OFF (14.70)
        mtUOnOffCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.ESC_hyphen(mUnderLineEditText.getText().toString());
            }
        });

        //Select default line spacing (14.71)
        mSDLSCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.ESC_2();
            }
        });

        //Turn emphasized mode on/off (14.72)
        mTEMOnOffCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.ESC_E(mTEMOnOffEditText.getText().toString());
            }
        });

        //Select print direction in page mode (14.73)
        mSPDPMCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.ESC_T(mSPDPMCommandButton.getText().toString());
            }
        });

        //Select justification (14.74)
        mSJCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.ESC_a(mSJEditText.getText().toString());
            }
        });

        //Set up and print the symbol (14.75)

        //Print QR Code (14.75) Part - 2
        mQRCodeCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRadioButtonQR.getText().toString().equals("L")){
                    printer.QrCode( mQRSizeEditText.getText().toString(), QRErrCorrLvl.L,
                            mQRUserDataEditText.getText().toString());
                }
                else if(mRadioButtonQR.getText().toString().equals("M")){
                    printer.QrCode( mQRSizeEditText.getText().toString(), QRErrCorrLvl.M,
                            mQRUserDataEditText.getText().toString());
                }
                else if(mRadioButtonQR.getText().toString().equals("Q")){
                    printer.QrCode( mQRSizeEditText.getText().toString(), QRErrCorrLvl.Q,
                            mQRUserDataEditText.getText().toString());
                }
                else if(mRadioButtonQR.getText().toString().equals("H")){
                    printer.QrCode( mQRSizeEditText.getText().toString(), QRErrCorrLvl.H,
                            mQRUserDataEditText.getText().toString());
                }
            }
        });


        /*//Print downloaded bit image (14.77)
        mPDBICommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.GS_FS(mPDBIEditText.getText().toString());
            }
        });*/

        //Turn white/black reverse print mode on/off (14.78)
        mTurnWBRPOnOffCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.GS_B(mTurnWBRPOnOffEditText.getText().toString());
            }
        });

        //Select cut mode and cut paper (14.79)
        mSCMCPCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRadioButtonCT.getText().toString().equals("Full Cut")){
                    if(mRadioButtonFT.getText().toString().equals("A")){
                        printer.GS_V(FunctionType.A, CutCommand.FULLCUT, mSCMCPEditText.getText().toString());
                    }else if(mRadioButtonFT.getText().toString().equals("B")){
                        printer.GS_V(FunctionType.B, CutCommand.FULLCUT, mSCMCPEditText.getText().toString());
                    }else if(mRadioButtonFT.getText().toString().equals("C")){
                        printer.GS_V(FunctionType.C, CutCommand.FULLCUT, mSCMCPEditText.getText().toString());
                    }
                }else if(mRadioButtonCT.getText().toString().equals("Partial Cut")){
                    if(mRadioButtonFT.getText().toString().equals("A")){
                        printer.GS_V(FunctionType.A, CutCommand.PARTIALCUT, mSCMCPEditText.getText().toString());
                    }else if(mRadioButtonFT.getText().toString().equals("B")){
                        printer.GS_V(FunctionType.B, CutCommand.PARTIALCUT, mSCMCPEditText.getText().toString());
                    }else if(mRadioButtonFT.getText().toString().equals("C")){
                        printer.GS_V(FunctionType.C, CutCommand.PARTIALCUT, mSCMCPEditText.getText().toString());
                    }
                }
            }
        });


        //Basic Print Command
        mPrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.printText(mSampleTextEditText.getText().toString());
                //printer.printAdditionalBarcode(ExtraBarcodeType.CODE128, "500", "300",
                //       "0905", ImageMode.NORMAL);
                //TODO: UI
                /*printer.printPDF417("5","24",
                        PDF417ErrorCorrectionMode.LEVEL,
                        PDF417ErrorCorrectionLevel.LEVEL0,
                        PDF417Options.STANDARD,
                        "Hello there.. Karthik");*/

                //printer.code93(BarcodeType.CODE93,"ABCDEFG");
                //printer.code128(BarcodeType.CODE128,mSampleTextEditText.getText().toString(),
                        //CODE128Subset.SUBSETC);


            }
        });

        //PDF417 Command
        mPDF417CommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer.printPDF417("3",
                        "10",
                        PDF417ErrorCorrectionMode.LEVEL,
                        PDF417ErrorCorrectionLevel.LEVEL0,
                        PDF417Options.STANDARD,
                        "Nash Industries (I) Pvt. Ltd.");
            }
        });

        //TODO Control Transfer
        mControlTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] dataReceivedFromPrinter = new byte[16];  //For Understanding purpose only
                dataReceivedFromPrinter = printer.controlTransfer(mControlTransferEditText.getText().
                        toString());
                Toast.makeText(getApplicationContext(),dataReceivedFromPrinter.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        /*
        Change button to btn
         */
    }

    // Defining the Callback methods here
    public void onItemSelected(AdapterView parent, View view, int pos,
                               long id) {
        Toast.makeText(getApplicationContext(),
                mBarcodeSpinner.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG)
                .show();
    }

    /***
     * Supporting Methods
     */
    private void pickBitImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop","true");
        intent.putExtra("scale",false);
        intent.putExtra("return-data",true);
        startActivityForResult(intent, 1);
    }

    private void pickRasterBitImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop","true");
        intent.putExtra("scale",true);
        intent.putExtra("return-data",true);
        startActivityForResult(intent, 2);
    }

    private void pickNVBitImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        //intent.putExtra("return-data",true);
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){
            return;
        }
        //TODO - Bit Image Selection
        if(requestCode == 1){
            final Bundle extras = data.getExtras();
            if(extras != null){
                //Get Image from Gallery
                //mBmp = extras.getParcelable("data");
            }
        }
        //Raster Bit Image Selection
        else if(requestCode == 2){
            final Bundle extras = data.getExtras();
            if(extras != null){
                //Get Image from Gallery
                Bitmap bmp = extras.getParcelable("data");
                printer.GS_v(bmp, mModeOfRasterBitEditText.getText().toString());
            }
        }
        //NV Bit Image Selection
        else if(requestCode == 3){
            /**
             * Case 1: Number of NV Bit Images = 1
             * Case 2: Number of NV Bit Images > 1
             */
            //Case 1:
            if(data.getData() != null) {
                Uri uri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.
                            getBitmap(context.getContentResolver(), uri);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                printer.FS_Q(new Bitmap[]{bitmap}, mNumberOfNVBitImagesEditText.getText().toString());
            }
            //Case 2:
            else if(data.getClipData() != null){

                ClipData clipData = data.getClipData();
                int numberOfImages = clipData.getItemCount();
                Bitmap[] bitmapArray = new Bitmap[numberOfImages];
                for(int i = 0; i < numberOfImages; i++){
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri uri = item.getUri();
                    try{
                        bitmapArray[i] = MediaStore.Images.Media.
                                getBitmap(context.getContentResolver(),uri);

                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                printer.FS_Q(bitmapArray, mNumberOfNVBitImagesEditText.getText().toString());
            }
            else{
                Toast.makeText(getApplicationContext(),"Invalid!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Callback method for Radiogroup - Function Type
     * @param view - Radiobutton selected
     */
    public void RadioButtonFuncTypeSelected(View view){
        int radioButtonId = mRadioGroupFT.getCheckedRadioButtonId();
        mRadioButtonFT = findViewById(radioButtonId);

        Toast.makeText(this, "Function Mode: "+ mRadioButtonFT.getText(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Callback method for Radiogroup - Cut Type
     * @param view
     */
    public void RadioButtonCutTypeSelected(View view){
        int radioButtonId = mRadioGroupCT.getCheckedRadioButtonId();
        mRadioButtonCT = findViewById(radioButtonId);

        Toast.makeText(this, "Cut Mode: "+ mRadioButtonCT.getText(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Callback method for Radiogroup - QR Correction Type
     * @param view
     */
    public void RadioButtonQRTypeSelected(View view){
        int radioButtonId = mRadioGroupQR.getCheckedRadioButtonId();
        mRadioButtonQR = findViewById(radioButtonId);

        Toast.makeText(this, "QR Correction Level: "+ mRadioButtonQR.getText(), Toast.LENGTH_SHORT).show();
    }

    //Device Disconnected Receiver
    BroadcastReceiver mUsbReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // TODO: Check for the custom VID and PID for the USB here
            Toast.makeText(context.getApplicationContext(),"Device Disconnected!",
                    Toast.LENGTH_SHORT).show();
        }
    };
    //Device connected Receiver
    BroadcastReceiver mUsbReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context.getApplicationContext(),"Device Connected!",
                    Toast.LENGTH_SHORT).show();

            if(printer == null){
                printer = MyPrinter.getInstance(context);
            }
            else{
                printer.establishUSBConnection(context);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(printer == null){
            printer = MyPrinter.getInstance(context);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "In onPause()");
        Log.i(TAG, "Going to onStop()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "In onStop()");
        Log.i(TAG, "Going to onDestroy()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "In onRestart()");
        Log.i(TAG, "Going to onStart()");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "In onStart()");
        Log.i(TAG, "Going to onResume()");
    }

    @Override
    protected void onDestroy() {
        if(mUsbReceiver1 != null || mUsbReceiver2 != null){
            context.unregisterReceiver(mUsbReceiver1);
            context.unregisterReceiver(mUsbReceiver2);
        }
        super.onDestroy();
        Log.i(TAG, "In onDestroy()");
        Log.i(TAG, "Going to Activity Shutdown");
    }
}

