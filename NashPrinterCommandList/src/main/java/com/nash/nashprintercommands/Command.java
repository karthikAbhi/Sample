package com.nash.nashprintercommands;

public class Command {
    public byte[] cut_paper = {0x0a, 0x1b, 0x69};
    public byte[] cmdDoublehead = {0x1b, 0x21, 0x31};//Font B(8x16) [Refer Document: 14.8]
    public byte[] cmdnormalfont = {0x1b, 0x21, 0x00};//Font A(12x24) [Refer Document: 14.8]
    public byte[] cmdSpacing100 = {0x1D, 0x4C, 0x64, 0x00}; // third index should be adjusted
    public byte[] cmdSpacing300 = {0x1D, 0x4C, 0x2c, 0x01}; // third index should be adjusted
    public byte[] cmdReset = {0x1b, 0x40};

    public String mManufacturerName = "NASH INDUSTRIES (I)PVT.LTD\n\n";
    public String mAddress = "Goraguntepalya, Bengaluru\n\n";

    //Newly Added Commands
    public byte[] LF = {0x0a};          //Print data and feed a line (14.01)
    public byte[] FF = {0x0c};          //Print data and feed a page on page mode (14.02)
    public byte[] ESC_FF = {0x1b,0x0c}; //Print data on page mode (14.03)
    public byte[] ESC_J = {0x1b,0x4A};  //Print data and feed the form to forward (14.04)
    public byte[] ESC_D = {0x1b,0x64};  //Print data and feed n lines (14.05)
    public byte[] ESC_3 = {0x1b,0x33};  //Set the line feed amount (14.06)
    public byte[] ESC_SP = {0x1b,0x20}; //Set the gap of right side of the character (14.07)
    public byte[] ESC_PM = {0x1b,0x21}; //Set parameter of print mode (14.08)
    public byte[] ESC_M = {0x1b,0x4d};  //Select the font (14.09)
    public byte[] ESC_t = {0x1b,0x74};  //Select character code table (14.10)
    public byte[] ESC_DC = {0x1b,0x26}; //Specify download character (14.11)
    public byte[] ESC_SDC = {0x1b,0x25};//Set or unset download character set (14.12)
    public byte[] ESC_L = {0x1b,0x4c};  //Set the print mode to page mode (14.13)
    public byte[] ESC_S = {0x1b,0x53};  //Set the print mode to standard mode (14.14)
    public byte[] HT = {0x09};          //Tabbing Horizontal (14.15)
    public byte[] GS_L = {0x1d, 0x4c};  //Set the left side margin (14.16)
    public byte[] GS_W = {0x1d, 0x57};  //Set the width of print area (14.17)
    public byte[] ESC_W = {0x1b, 0x57}; //Specify the print area on page mode (14.18)
    public byte[] ESC_PP = {0x1b, 0x24};//Set the physical position (14.19)
    public byte[] ESC_LP = {0x1b, 0x5c};//Set the logical position (14.20)
    public byte[] GS_VPP = {0x1d, 0x24};//Set the vertical physical position in page mode (14.21)
    public byte[] GS_VLP = {0x1d, 0x5c};//Set the vertical logical position on page mode (14.22)
    public byte[] ESC_BIMG = {0x1b, 0x2a};//Print bit image (14.23)
    public byte[] GS_v = {0x1d,0x76,0x30};//Print raster bit image (14.24)
    public byte[] FS_P = {0x1c, 0x70};  //Print NV Bit Image (14.25)
    public byte[] FS_Q = {0x1c, 0x71};  //Define NV Bit Image (14.26)
    public byte[] DC2_DIR = {0x12, 0x3d};//Select LSB-MSB direction in image (14.27)
    public byte[] GS_H = {0x1d,0x48};   //Set print position of HRI characters (14.28)
    public byte[] GS_F = {0x1d, 0x66};  //Select HRI character size (14.29)
    public byte[] GS_h = {0x1d, 0x68};  //Set barcode height (14.30)
    public byte[] GS_w = {0x1d,0x77};   //Set barcode width (14.31)
    public byte[] DC2_ARB = {0x12,0x3a};//Set N:W aspect of the barcode (14.32)
    public byte[] GS_k = {0x1d, 0x6b};  //Print Barcode (14.33)
    public byte[] ESC_INIT = {0x1b,0x40};//Initialize the printer (14.34)
    public byte[] DC2_PD = {0x12, 0x7e};//Set the print density (14.40)
    public byte[] GS_MF = {0x1d, 0x3c}; /*Cue the marked form(feed the form to print start position)
    * (14.41)
    */
    public byte[] ESC_I = {0x1b,0x69};  //Cut the form (14.43)
    public byte[] GS_l = {0x1d,0x6c};   //Specify the response parameter (14.48)
    public byte[] GS_i = {0x1d, 0x69};  //Inform system time of the host (14.51)
    public byte[] ACK = {0x06};         //Download the firmware (14.52)
    public byte[] GS_d = {0x1d, 0x64};  //Feed the form in pixels (14.54)
    public byte[] ESC_hyphen = {0x1b, 0x2d};   //Turn underline mode ON/OFF (14.70)
    public byte[] ESC_2 = {0x1b, 0x32};  //Select default line spacing (14.71)
    public byte[] ESC_E = {0x1b, 0x45};  //Turn emphasized mode on/off (14.72)
    public byte[] ESC_T = {0x1b, 0x54};  //Select print direction in page mode (14.73)
    public byte[] ESC_a = {0x1b, 0x61};  //Select justification (14.74)
    public byte[] GS_C_k = {0x1d, 0x28, 0x6b};  //Set up and print the symbol (14.75)
    public byte[] GS_FS = {0x1d, 0x2f};  //Print downloaded bit image (14.77)
    public byte[] GS_B = {0x1d, 0x42};   //Turn white/black reverse print mode on/off (14.78)
    public byte[] GS_V = {0x1d, 0x56};   //Select cut mode and cut paper (14.79)
}
