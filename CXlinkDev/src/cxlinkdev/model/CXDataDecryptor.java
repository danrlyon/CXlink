/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cxlinkdev.model;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.scene.chart.XYChart;
import jssc.SerialPortException;

/**
 *
 * @author Dell-N7110
 */
public class CXDataDecryptor {

    //Current Values
    private int versionNumber;
    private int soc;
    private float socPercent;
    private int disconADJ;
    private boolean loadDisconnected;   //boolean load status
    private float batteryVoltage;
    private float batteryEndCharge;
    private int statusByte;
    private boolean boostMode;          //boolean status bits
    private boolean equalMode;
    private boolean voltage24V;
    private float loadCurrent;
    private float voltageBatteryWires;
    private float pwm;
    private float nightHours;
    private float nightHoursLastNight;
    private int specBits;
    private boolean loadOff;
    private boolean isNight;
    private int internalTemp;
    private int externalTemp;
    private int pVCurrent;
  
    //Datalogger values
    private int fullCurCX;
    private int cxCurrentRating;
    private int menuState2;
    private int serialInterfaceInfo;
    private boolean buttonLocked;
    private int eveningHours;
    private int morningHours;
    private int menuState1;
    private int lvdInfo;
    private int batteryType;
    private boolean buzzerOn;
    private int nightLightFunction;
    private int nightLevel;
    private int versionNumberDL;
    private int deepDischargeEvents;
    private int weeksWithoutFullBattery;
    private int monthsWithoutFullBattery;
    private int summarySOCMornings;
    private int pVAh;
    private int loadAh;
    private int dataLoggerDays;
   
    
    private int[][] dayData = new int[7][10];  // Day will be first argument, and second will denote the data (7days by 9values)
    private String[][] dayDecoded = new String[7][10];
    private int[][] weekData = new int[7][10];  // Week will be first argument, and second will denote the data (4weeks by 9values)
    private String[][] weekDecoded = new String[7][10];
    private int[][] monthData = new int[12][10];// Month will be first argument, and second will denote the data (12months by 9values)
    private String[][] monthDecoded = new String [12][10];
    
    //private int 
    
    private CXCom cxCom;
    
    private String cxCurrentValues;
    private String cxDataLogger;
     
        
    public CXDataDecryptor() throws SerialPortException {
        
        //Load up the raw current values
        this.cxCurrentValues = null;
        this.cxCom = new CXCom();
        this.cxCom.setCXCurrentValues();
        int i = 0;
        while ( this.cxCurrentValues==null && i < 100 )    {
            this.cxCurrentValues = this.cxCom.getCXCurrentValue();
            try {
                Thread.sleep(100);                 //Optimize this value, .5 secs for now
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            i++;
        }       
        this.cxCom.setStatus(1);
        
        //Load up the raw data logger values
        this.cxCom.setCXDataLoggerValues();
        i = 0;
        while ( this.cxDataLogger==null && i < 1000 )    {
            this.cxDataLogger = this.cxCom.getCXDataLoggerValues();
            try {
                Thread.sleep(100);                 //Optimize this value, .5 secs for now
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            i++;
        }
    }
    
    public void decryptCurrentValues()  {
        // Splits up the values, then stores each in it's own variable
        String temp = this.cxCurrentValues.trim();
        String[] splitCurrentValues = temp.split(" ");
        int i;        
        this.versionNumber = Integer.parseInt(splitCurrentValues[0]);
        this.soc = Integer.parseInt(splitCurrentValues[1]);        
        this.disconADJ = Integer.parseInt(splitCurrentValues[2]);
        this.loadDisconnected = this.disconADJ == this.soc;        
        this.batteryVoltage = (float) (((Float.parseFloat(splitCurrentValues[3]))*0.032)+9);
        this.batteryEndCharge = (float)  (((Float.parseFloat(splitCurrentValues[4]))*0.032)+9);
        this.statusByte = Integer.parseInt(splitCurrentValues[5]);
        if ( (this.statusByte&(int)1)==1  ) this.boostMode = true;
        if ( (this.statusByte&(int)2)==2  ) this.equalMode = true;
        if ( (this.statusByte&(int)4)==4  ) this.voltage24V = true;
        this.loadCurrent = Float.parseFloat(splitCurrentValues[6]);
        this.voltageBatteryWires = Integer.parseInt(splitCurrentValues[7]);
        this.pwm = (Float.parseFloat(splitCurrentValues[8]))/255;
        this.nightHours = (Float.parseFloat(splitCurrentValues[9]))/10;
        this.nightHoursLastNight = Float.parseFloat(splitCurrentValues[10]);
        this.specBits = Integer.parseInt(splitCurrentValues[11]);
        if ( (this.specBits&(int)4)==4) this.loadOff=true;
        if ( (this.specBits&(int)16)==16) this.isNight=true;
        this.internalTemp = Integer.parseInt(splitCurrentValues[12]);
        this.externalTemp = Integer.parseInt(splitCurrentValues[13]);
        this.pVCurrent = Integer.parseInt(splitCurrentValues[14]);
        this.pwm /= 7812;
         
    }
    
    public void decryptDataLogger() {
        //Parse and store data in a readable format
        
        //
         
        String replace = this.cxDataLogger.replace("!", " ");
        String trim = replace.trim();
        String[] splitDataLogger = trim.split(" ");        
        int i;        
        this.fullCurCX = Integer.parseInt("000000"
                +splitDataLogger[2], 16);
        if ( this.fullCurCX > 50 ) this.cxCurrentRating = 10;
        else if ( 23<this.fullCurCX&&this.fullCurCX<30 ) this.cxCurrentRating=20;
        else if ( this.fullCurCX<22 ) this.cxCurrentRating=40;
        else this.cxCurrentRating=99;
        this.menuState2 = Integer.parseInt("000000"
                +splitDataLogger[10], 16);
        this.serialInterfaceInfo = this.menuState2&3;
        if ( (this.menuState2&4)==4 ) this.buttonLocked=true;
        else this.buttonLocked=false;                
        this.eveningHours = Integer.parseInt("000000"
                +splitDataLogger[11], 16);
        this.morningHours = Integer.parseInt("000000"
                +splitDataLogger[12], 16);
        this.menuState1 = Integer.parseInt("000000"
                +splitDataLogger[13], 16);
        this.nightLevel = Integer.parseInt("000000"
                +splitDataLogger[14], 16);
        this.versionNumberDL = Integer.parseInt("000000"
                +splitDataLogger[19], 16);
        
        //DataLogger Information
        this.deepDischargeEvents = Integer.parseInt("0000"
            +splitDataLogger[20]+splitDataLogger[21], 16);
        this.weeksWithoutFullBattery = Integer.parseInt("000000"
            +splitDataLogger[22], 16);
        this.monthsWithoutFullBattery = Integer.parseInt("000000"
            +splitDataLogger[23], 16);
        this.summarySOCMornings = Integer.parseInt("0000"
            +splitDataLogger[25]+splitDataLogger[26], 16);
        this.pVAh = Integer.parseInt("00"
            +splitDataLogger[26]+splitDataLogger[27]
            +splitDataLogger[28], 16);
        this.loadAh = Integer.parseInt("00"
            +splitDataLogger[29]+splitDataLogger[30]
            +splitDataLogger[31], 16);
        this.dataLoggerDays = Integer.parseInt("0000"
            +splitDataLogger[32]+splitDataLogger[33], 16);
        
        //Daily datalogger values to be deciphered
        for ( i=0;i<7;i++ )    {
            //Loads Max Battery Voltage into dayDecoded string array
            this.dayData[i][0] = Integer.parseInt("000000"
                    +splitDataLogger[34+(i*10)], 16);            
            this.dayDecoded[i][0]= (Float.toString((float) ((this.dayData[i][0]*0.032)+9)));
            System.out.println(this.dayDecoded[i][0]+" ");
            //Loads Min Battery Voltage into dayDecoded string array
            this.dayData[i][1] = Integer.parseInt("000000"
                    +splitDataLogger[35+(i*10)], 16);
            this.dayDecoded[i][1] = (Float.toString((float) ((this.dayData[i][1]*0.032)+9)));
            //Load PV Amp Hours
            this.dayData[i][2] = Integer.parseInt("000000"
                    +splitDataLogger[36+(i*10)], 16);
            this.dayDecoded[i][2] = Float.toString((float) (this.dayData[i][2]*this.fullCurCX*4)/60); //(amperehours_value_dec * x * 4) / 60
            //Load Load Amp Hours
            this.dayData[i][3] = Integer.parseInt("000000"
                    +splitDataLogger[37+(i*10)], 16);
            this.dayDecoded[i][3] = Float.toString((float) (this.dayData[i][3]*this.fullCurCX*4)/60);
            //Load Max Load current
            this.dayData[i][4] = Integer.parseInt("000000"
                    +splitDataLogger[38+(i*10)], 16);
            this.dayDecoded[i][4] = Float.toString((float) (this.dayData[i][4]/64)*this.fullCurCX);
            //Excess Amp Hours
            this.dayData[i][5] = Integer.parseInt("000000"
                    +splitDataLogger[39+(i*10)], 16);
            this.dayDecoded[i][5] = Float.toString((float) (this.dayData[i][5]*this.fullCurCX*4)/60);
            //Load Max PV current
            this.dayData[i][6] = Integer.parseInt("000000"
                    +splitDataLogger[40+(i*10)], 16);
            this.dayDecoded[i][6] = Float.toString((float) (this.dayData[i][6]/64)*this.fullCurCX);
            //Load evening SOC
            this.dayData[i][7] = Integer.parseInt("0000000"
                    +splitDataLogger[41+(i*10)].charAt(0), 16);
            this.dayDecoded[i][7] = Float.toString((float) (this.dayData[i][7]*6.6));
            if ( Float.parseFloat(this.dayDecoded[i][7])>95 ) this.dayDecoded[i][7]="100";
            //Load morning SOC
            this.dayData[i][8] = Integer.parseInt("0000000"
                    +splitDataLogger[41+(i*10)].charAt(1), 16);
            this.dayDecoded[i][8] = Float.toString((float) (this.dayData[i][8]*6.6));
            if ( Float.parseFloat(this.dayDecoded[i][8])>95 ) this.dayDecoded[i][8]="100";
            //Load max charge current
            this.dayData[i][9] = Integer.parseInt("000000"
                    +splitDataLogger[42+(i*10)], 16);
            this.dayDecoded[i][9] = Float.toString((float) (this.dayData[i][9]*0.5));       
        }
        
        //Weekly datalogger values to be deciphered
        for ( i=0;i<4;i++ )    {
            //Loads Max Battery Voltage into dayDecoded string array
            this.weekData[i][0] = Integer.parseInt("000000"
                    +splitDataLogger[97+(i*10)], 16);            
            this.weekDecoded[i][0]= (Float.toString((float) ((this.weekData[i][0]*0.032)+9)));
            System.out.println(this.weekDecoded[i][0]+" ");
            //Loads Min Battery Voltage into dayDecoded string array
            this.weekData[i][1] = Integer.parseInt("000000"
                    +splitDataLogger[98+(i*10)], 16);
            this.weekDecoded[i][1] = (Float.toString((float) ((this.weekData[i][1]*0.032)+9)));
            //Load PV Amp Hours
            this.weekData[i][2] = Integer.parseInt("000000"
                    +splitDataLogger[99+(i*10)], 16);
            this.weekDecoded[i][2] = Float.toString((float) (this.weekData[i][2]*this.fullCurCX*4)/60); //(amperehours_value_dec * x * 4) / 60
            //Load Load Amp Hours
            this.weekData[i][3] = Integer.parseInt("000000"
                    +splitDataLogger[100+(i*10)], 16);
            this.weekDecoded[i][3] = Float.toString((float) (this.weekData[i][3]*this.fullCurCX*4)/60);
            //Load Max Load current
            this.weekData[i][4] = Integer.parseInt("000000"
                    +splitDataLogger[101+(i*10)], 16);
            this.weekDecoded[i][4] = Float.toString((float) (this.weekData[i][4]/64)*this.fullCurCX);
            //Excess Amp Hours
            this.weekData[i][5] = Integer.parseInt("000000"
                    +splitDataLogger[102+(i*10)], 16);
            this.weekDecoded[i][5] = Float.toString((float) (this.weekData[i][5]*this.fullCurCX*4)/60);
            //Load Max PV current
            this.weekData[i][6] = Integer.parseInt("000000"
                    +splitDataLogger[103+(i*10)], 16);
            this.weekDecoded[i][6] = Float.toString((float) (this.weekData[i][6]/64)*this.fullCurCX);
            //Load evening SOC
            this.weekData[i][7] = Integer.parseInt("0000000"
                    +splitDataLogger[104+(i*10)].charAt(0), 16);
            this.weekDecoded[i][7] = Float.toString((float) (this.weekData[i][7]*6.6));
            if ( Float.parseFloat(this.weekDecoded[i][7])>95 ) this.weekDecoded[i][7]="100";
            //Load morning SOC
            this.weekData[i][8] = Integer.parseInt("0000000"
                    +splitDataLogger[104+(i*10)].charAt(1), 16);
            this.weekDecoded[i][8] = Float.toString((float) (this.weekData[i][8]*6.6));
            if ( Float.parseFloat(this.weekDecoded[i][8])>95 ) this.weekDecoded[i][8]="100";
            //Load max charge current
            this.weekData[i][9] = Integer.parseInt("000000"
                    +splitDataLogger[105+(i*10)], 16);
            this.weekDecoded[i][9] = Float.toString((float) (this.weekData[i][9]*0.5));       
        }
        
        //Daily datalogger values to be deciphered
        for ( i=0;i<12;i++ )    {
            //Loads Max Battery Voltage into dayDecoded string array
            this.monthData[i][0] = Integer.parseInt("000000"
                    +splitDataLogger[133+(i*10)], 16);            
            this.monthDecoded[i][0]= (Float.toString((float) ((this.monthData[i][0]*0.032)+9)));
            System.out.println(this.monthDecoded[i][0]+" ");
            //Loads Min Battery Voltage into dayDecoded string array
            this.monthData[i][1] = Integer.parseInt("000000"
                    +splitDataLogger[134+(i*10)], 16);
            this.monthDecoded[i][1] = (Float.toString((float) ((this.monthData[i][1]*0.032)+9)));
            //Load PV Amp Hours
            this.monthData[i][2] = Integer.parseInt("000000"
                    +splitDataLogger[135+(i*10)], 16);
            this.monthDecoded[i][2] = Float.toString((float) (this.monthData[i][2]*this.fullCurCX*4)/60); //(amperehours_value_dec * x * 4) / 60
            //Load Load Amp Hours
            this.monthData[i][3] = Integer.parseInt("000000"
                    +splitDataLogger[136+(i*10)], 16);
            this.monthDecoded[i][3] = Float.toString((float) (this.monthData[i][3]*this.fullCurCX*4)/60);
            //Load Max Load current
            this.monthData[i][4] = Integer.parseInt("000000"
                    +splitDataLogger[137+(i*10)], 16);
            this.monthDecoded[i][4] = Float.toString((float) (this.monthData[i][4]/64)*this.fullCurCX);
            //Excess Amp Hours
            this.monthData[i][5] = Integer.parseInt("000000"
                    +splitDataLogger[138+(i*10)], 16);
            this.monthDecoded[i][5] = Float.toString((float) (this.monthData[i][5]*this.fullCurCX*4)/60);
            //Load Max PV current
            this.monthData[i][6] = Integer.parseInt("000000"
                    +splitDataLogger[139+(i*10)], 16);
            this.monthDecoded[i][6] = Float.toString((float) (this.monthData[i][6]/64)*this.fullCurCX);
            //Load evening SOC
            this.monthData[i][7] = Integer.parseInt("0000000"
                    +splitDataLogger[140+(i*10)].charAt(0), 16);
            this.monthDecoded[i][7] = Float.toString((float) (this.monthData[i][7]*6.6));
            if ( Float.parseFloat(this.monthDecoded[i][7])>95 ) this.monthDecoded[i][7]="100";
            //Load morning SOC
            this.monthData[i][8] = Integer.parseInt("0000000"
                    +splitDataLogger[140+(i*10)].charAt(1), 16);
            this.monthDecoded[i][8] = Float.toString((float) (this.monthData[i][8]*6.6));
            if ( Float.parseFloat(this.monthDecoded[i][8])>95 ) this.monthDecoded[i][8]="100";
            //Load max charge current
            this.monthData[i][9] = Integer.parseInt("000000"
                    +splitDataLogger[141+(i*10)], 16);
            this.monthDecoded[i][9] = Float.toString((float) (this.monthData[i][9]*0.5));       
        }
                
        //resolve current values whic are dependent on DL calues
        switch (this.lvdInfo)   {
            case 0:
                this.socPercent = (float) this.soc/30;
                if (this.socPercent>95) this.socPercent=100;
                break;
            case 1:
                this.socPercent = (float) this.soc/35;
                if (this.socPercent>95) this.socPercent=100;
                break;
            case 2:
                this.socPercent = (float) this.soc/35;
                if (this.socPercent>95) this.socPercent=100;
                break;
            case 3:
                this.socPercent = (float) this.soc/41;
                if (this.socPercent>95) this.socPercent=100;
                break;
            case 4:
                this.socPercent = (float) this.soc/57;
                if (this.socPercent>95) this.socPercent=100;
                break;
            default:
                this.socPercent=0;
                break;
        }
        this.loadCurrent =(this.loadCurrent/256)*this.fullCurCX;
        this.pVCurrent = (this.pVCurrent/256)*this.fullCurCX;         
        
        System.out.println(Arrays.toString(dayData[0]));
        System.out.println(Arrays.toString(this.dayDecoded[0]));

    } 
    
    //Current Values
    public int getVersionNumber() {return this.versionNumber;}
    public int getSOC() {return this.soc;}
    public int getSOCPercent() {return this.soc;}
    public int getDisconADJ() {return this.disconADJ;}
    public boolean getLoadDisconnected() {return this.loadDisconnected ;}   //boolean load status
    public float getBatteryVoltage() {return this.batteryVoltage ;}
    public float getBatteryEndCharge() {return this.batteryEndCharge ;}
    public int getStatusByte() {return this.statusByte ;}
    public boolean getBoostMode() {return this.boostMode ;}          //boolean status bits
    public boolean getEqualMode() {return this.equalMode ;}
    public boolean getVoltage24V() {return this.voltage24V ;}
    public float getLoadCurrent() {return this.loadCurrent ;}
    public float getVoltageBatteryWires() {return this.voltageBatteryWires ;}
    public float getPWM() {return this.pwm ;}
    public float getNightHours() {return this.nightHours ;}
    public float getNightHoursLastNight() {return this.nightHoursLastNight ;}
    public int getSpecBits() {return this.specBits ;}
    public boolean getLoadOff() {return this.loadOff ;}
    public boolean getIsNight() {return this.isNight ;}
    public int getInternalTemp() {return this.internalTemp ;}
    public int getExternalTemp() {return this.externalTemp ;}
    public int getPVCurrent() {return this.pVCurrent ;}
  
    //Datalogger values
    public int getFullCurCX() {return this.fullCurCX ;}
    public int getCXCurrentRating() {return this.cxCurrentRating ;}
    public int getMenuState2() {return this.menuState2 ;}
    public int getSerialInterfaceInfo() {return this.serialInterfaceInfo ;}
    public boolean getButtonLocked() {return this.buttonLocked ;}
    public int getEveningHours() {return this.eveningHours ;}
    public int getMorningHours() {return this.morningHours ;}
    public int getMenuState1() {return this.menuState1 ;}
    public int getLVDInfo() {return this.lvdInfo ;}
    public int getBatteryType() {return this.batteryType ;}
    public boolean getBuzzerOn() {return this.buzzerOn ;}
    public int getNightLightFunction() {return this.nightLightFunction ;}
    public int getNightLevel() {return this.nightLevel ;}
    public int getVersionNumberDL() {return this.versionNumberDL ;}
    public int getDeepDischargeEvents() {return this.deepDischargeEvents ;}
    public int getWeeksWithoutFullBattery() {return this.weeksWithoutFullBattery ;}
    public int getMonthsWithoutFullBattery() {return this.monthsWithoutFullBattery ;}
    public int getSummarySOCMornings() {return this.summarySOCMornings ;}
    public int getPVAh() {return this.pVAh ;}
    public int getLoadAh() {return this.loadAh ;}
    public int getDataLoggerDays() {return this.dataLoggerDays ;}
   
    
    public int[][] getDayData() {return this.dayData ;}  // Day will be first argument, and second will denote the data (7days by 9values)
    public String[][] getDayDecoded() {return this.dayDecoded ;}
    public int[][] getWeekData() {return this.weekData ;}    // Week will be first argument, and second will denote the data (4weeks by 9values)
    public String[][] getWeekDecoded() {return this.weekDecoded ;}
    public int[][] getMonthData() {return this.monthData ;}// Month will be first argument, and second will denote the data (12months by 9values)
    public String[][] getMonthDecoded() {return this.monthDecoded ;}
    

    
    
    
    
}
