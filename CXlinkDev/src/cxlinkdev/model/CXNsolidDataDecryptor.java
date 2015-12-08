/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cxlinkdev.model;

import java.util.Arrays;
import jssc.SerialPortException;

/**
 *
 * @author Dell-N7110
 */
public class CXNsolidDataDecryptor {
    /*Datalogger information to be deciphered in same order as  *
    * CXN50_solid_data-logger_Statusbits_2015-09-21             *
    * data types must be one size larger than required to prevent negative numbers*/
    private int numberLVDDays;                  //Number of days with LVD (2bytes)
    private int numberMonthsNoFullCharge;     //Number of months without a full charge (1byte) ?Consecutive?
    private int sumMorningSOCs;                 //Sum of SOCs in the morning (2bytes) ?all SOCs?
    private long ahCharge;                      //Total amp hours charging  (4bytes)
    private long ahLoad;                        //Total load amp hours (4bytes)
    private int totalDaysActive;                //Total number of days (2bytes) ?Days on, when does reset happen?consec?
    private long dateBytes;                     //Date, decrypted into variables below
    private byte currentDay;                    //Day, month, and year pulled from datalogger
    private byte currentMonth;                  //  b0-b6 = year, b7-b10
    private byte currentYear;                   //
    private short inverter;                     // Example inverter = 24 AND 700W inverter => 24*70 =1680Wh 
    private int[][] dayData = new int[31][15];  // Day will be first argument, and second will denote the data (31days by 19values)
    private String[][] dayDecoded = new String[31][15];
    private int[][] monthData = new int[24][15];// Month will be first argument, and second will denote the data (24months by 19values)
    private String[][] monthDecoded = new String [24][15];
    
    /*Current Values information*/
    private int loadCurrentDigits;              // Load current in digits ?what does in digits mean?
    private float loadCurrent;                    // Load current in 10mA
    private int chargeCurrentDigits;            // Charge current in digits
    private float chargeCurrent;                  // Charge current in 10mA
    private int internalTempDigits;             // Internal temperature in digits
    private int batteryVoltageDigits;           // Battery voltage in digits (with current) ?huh?
    private int firmwareVersion;                // Firmware version
    private int loadState;                      // Load state, TBD
    private int inverterState;                  // Inverter state
    private int chargeState;                    // Charge state
    private int externalTempError;              // External temperature error: 16 ?huh?
    private float batteryVoltage;                 // Battery voltage in mV
    private float endOfChargeVoltage;             // End of charge voltage in mV
    private int socPercent;                     // State of charge percentage
    private int internalTemp;                   // Internal temperature °C
    private int externalTemp;                   // External temperature °C
    private float pwm;                            // 0 to 7812. Divide by 7812 to get duty cycle
    private int minutesSinceReset;

    //Variables to store settings
    private int lvdVoltageDependentDC;
    private int lvdVoltageDependentAC;
    private int lvdMode;
    
    CXCom cxCom;
    
    private String cxCurrentValues;
    private String cxDataLogger;
    
    /**
     *
     * @throws jssc.SerialPortException
     */
    public CXNsolidDataDecryptor() throws SerialPortException {
        
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
        while ( this.cxDataLogger==null && i < 100 )    {
            this.cxDataLogger = this.cxCom.getCXDataLoggerValues();
            try {
                Thread.sleep(100);                 //Optimize this value, .5 secs for now
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            i++;
        }
    }
    
    public int numberLVDDays(){return this.numberLVDDays;}//Number of days with LVD (2bytes)
    
    public int getNumberMonthsNoFullCharge(){return this.numberMonthsNoFullCharge;}     //Number of months without a full charge (1byte) ?Consecutive?
    public int getSumMorningSOCs(){return this.sumMorningSOCs;}                 //Sum of SOCs in the morning (2bytes) ?all SOCs?
    public long getAhCharge(){return this.ahCharge;}                      //Total amp hours charging  (4bytes)
    public long getAhLoad(){return this.ahLoad;}                        //Total load amp hours (4bytes)
    public int getTotalDaysActive(){return this.totalDaysActive;}                //Total number of days (2bytes) ?Days on, when does reset happen?consec?
    public long getDateBytes(){return this.dateBytes;}                     //Date, decrypted into variables below
    public byte getCurrentDay(){return this.currentDay;}                    //Day, month, and year pulled from datalogger
    public byte getCurrentMonth(){return this.currentMonth;}                  //  b0-b6 = year, b7-b10
    public byte getCurrentYear(){return this.currentYear;}                   //
    public short getInverter(){return this.inverter;}                     // Example inverter = 24 AND 700W inverter => 24*70 =1680Wh 
    public int[][] getDayData(){return this.dayData;}  // Day will be first argument, and second will denote the data (31days by 19values)
    public String[][] getDayDecoded(){return this.dayDecoded;}
    public int[][] getMonthData(){return this.monthData;}// Month will be first argument, and second will denote the data (24months by 19values)
    public String[][] getMonthDecoded(){return this.monthDecoded;}
    
    /*Current Values information*/
    public int getLoadCurrentDigits(){return this.loadCurrentDigits;}
    public float getLoadCurrent(){return this.loadCurrent;}                    
    public int getChargeCurrentDigits(){return this.chargeCurrentDigits;}            
    public float getChargeCurrent(){return this.chargeCurrent;}                 
    public int getInternalTempDigits(){return this.internalTempDigits;}             
    public int getBatteryVoltageDigits(){return this.batteryVoltageDigits;}           
    public int getFirmwareVersion(){return this.firmwareVersion;}                
    public int getLoadState(){return this.loadState;}                      
    public int getInverterState(){return this.inverterState;}                 
    public int getChargeState(){return this.chargeState;}                   
    public int getExternalTempError(){return this.externalTempError;}              
    public float getBatteryVoltage(){return this.batteryVoltage;}                 
    public float getEndOfChargeVoltage(){return this.endOfChargeVoltage;}             
    public int getSOCPercent(){return this.socPercent;}                     
    public int getInternalTemp(){return this.internalTemp;}                   
    public int getExternalTemp(){return this.externalTemp;}                   
    public float getPWM(){return this.pwm;}                            
    public int getMinutesSinceReset(){return this.minutesSinceReset;}
    
    public String getCurrentValues()  {
        return this.cxCurrentValues;
    }
    
    public String getDataLogger()   {
        return this.cxDataLogger;
    }
    
    

    //Variables to store settings
//    public int lvdVoltageDependentDC(){;}
//    public int lvdVoltageDependentAC(){;}
//    public int lvdMode(){;}
    
    public void decryptCurrentValues()  {
        // Splits up the values, then stores each in it's own variable
        String[] splitCurrentValues = this.cxCurrentValues.split(";");
        int i;        
        this.loadCurrentDigits = Integer.parseInt(splitCurrentValues[0].trim());
        this.loadCurrent = Float.parseFloat(splitCurrentValues[1]);
        this.loadCurrent *= 0.01;
        this.chargeCurrentDigits = Integer.parseInt(splitCurrentValues[2]);
        this.chargeCurrent = Float.parseFloat(splitCurrentValues[3]);
        this.chargeCurrent *= 0.01;
        this.internalTempDigits = Integer.parseInt(splitCurrentValues[4]);
        this.batteryVoltageDigits = Integer.parseInt(splitCurrentValues[5]);
        this.firmwareVersion = Integer.parseInt(splitCurrentValues[6]);
        this.loadState = Integer.parseInt(splitCurrentValues[7]);
        this.inverterState = Integer.parseInt(splitCurrentValues[8]);
        this.chargeState = Byte.parseByte(splitCurrentValues[9]);
        //this.externalTempError = Integer.parseInt(splitCurrentValues[10]);
        this.batteryVoltage = Float.parseFloat(splitCurrentValues[10]);
        this.batteryVoltage *= 0.001;
        this.endOfChargeVoltage = Float.parseFloat(splitCurrentValues[11]);
        this.endOfChargeVoltage *= 0.001;
        this.socPercent = Integer.parseInt(splitCurrentValues[12]);
        this.internalTemp = Integer.parseInt(splitCurrentValues[13]);
        this.externalTemp = Integer.parseInt(splitCurrentValues[14]);
        this.pwm = Integer.parseInt(splitCurrentValues[15]);
        this.pwm /= 7812;
        this.minutesSinceReset = Integer.parseInt(splitCurrentValues[17]);        
    }
    
    public void decryptDataLogger() {
        //Parse and store data in a readable format
        String replace = this.cxDataLogger.replace("!", " ");
        String trim = replace.trim();
        String[] splitDataLogger = trim.split(";");        
        int i;        
        this.numberLVDDays = Integer.parseInt("0000"
                +splitDataLogger[128]+splitDataLogger[129], 16);
        this.numberMonthsNoFullCharge = Integer.parseInt("000000"
                +splitDataLogger[130], 16);
        this.sumMorningSOCs = Integer.parseInt("0000"+splitDataLogger[131]
                +splitDataLogger[132], 16);
        this.ahCharge = Long.parseLong(splitDataLogger[133]+splitDataLogger[134]
                +splitDataLogger[135]+splitDataLogger[136], 16);
        this.ahLoad = Long.parseLong(splitDataLogger[137]+splitDataLogger[138]
                +splitDataLogger[139]+splitDataLogger[140], 16);
        this.totalDaysActive = Integer.parseInt("00"+splitDataLogger[141]
                +splitDataLogger[142]+splitDataLogger[143], 16);
        //Daily datalogger values to be deciphered
        for ( i=0;i<31;i++ )    {
            //Loads Date into dayDecoded string array
            this.dayData[i][0] = Integer.parseInt("0000"+splitDataLogger[144+(i*19)]
                    +splitDataLogger[145+(i*19)], 16);
            this.currentYear = (byte) (this.dayData[i][0]&127);
            this.currentMonth = (byte) ((this.dayData[i][0]>>7)&15);
            this.currentDay = (byte) ((this.dayData[i][0]>>11&31));
            this.dayDecoded[i][0]= ("20"+Byte.toString(this.currentYear)+"-"
                    +Byte.toString(this.currentMonth)+"-"
                    +Byte.toString(this.currentDay));
            System.out.println(this.dayDecoded[i][0]);
            //Loads Inverter factor into dayDecoded
            //multiply this by inverter power to get Watthours
            this.dayData[i][1] = Integer.parseInt("000000"
                    +splitDataLogger[146+(i*19)], 16);
            this.dayDecoded[i][1] = Float.toString((float) ((this.dayData[i][1])*0.1));
            //Load Max Voltage
            this.dayData[i][2] = Integer.parseInt("000000"
                    +splitDataLogger[147+(i*19)], 16);
            this.dayDecoded[i][2] = Float.toString((float) (this.dayData[i][2]*0.1));
            //Load min voltage
            this.dayData[i][3] = Integer.parseInt("000000"
                    +splitDataLogger[148+(i*19)], 16);
            this.dayDecoded[i][3] = Float.toString((float) (this.dayData[i][3]*0.1));
            //Load AHin
            this.dayData[i][4] = Integer.parseInt("0000"+splitDataLogger[149+(i*19)]
                    +splitDataLogger[150+(i*19)], 16);
            this.dayDecoded[i][4] = Float.toString((float) (this.dayData[i][4]*0.1));
            //Load AHout
            this.dayData[i][5] = Integer.parseInt("0000"+splitDataLogger[151+(i*19)]
                    +splitDataLogger[152+(i*19)], 16);
            this.dayDecoded[i][5] = Float.toString((float) (this.dayData[i][5]*0.1));
            //Load Max PV voltage
            this.dayData[i][6] = Integer.parseInt("000000"
                    +splitDataLogger[153+(i*19)], 16);
            this.dayDecoded[i][6] = Float.toString((float) (this.dayData[i][6]*0.5));
            //Load Min PV voltage
            this.dayData[i][7] = Integer.parseInt("000000"
                    +splitDataLogger[154+(i*19)], 16);
            this.dayDecoded[i][7] = Float.toString((float) (this.dayData[i][7]*0.5));
            //Load max load current
            this.dayData[i][8] = Integer.parseInt("000000"
                    +splitDataLogger[155+(i*19)], 16);
            this.dayDecoded[i][8] = Float.toString((float) (this.dayData[i][8]*0.5));
            //Load max charge current
            this.dayData[i][9] = Integer.parseInt("000000"
                    +splitDataLogger[156+(i*19)], 16);
            this.dayDecoded[i][9] = Float.toString((float) (this.dayData[i][9]*0.5));
            //Load morning SOC percent
            this.dayData[i][10] = Integer.parseInt("000000"
                    +splitDataLogger[157+(i*19)], 16);
            this.dayDecoded[i][10] = (Float.toString((float) (this.dayData[i][10]*6.6))+"%");
            //Load external temperature max
            this.dayData[i][11] = Integer.parseInt("000000"
                    +splitDataLogger[158+(i*19)], 16);
            this.dayDecoded[i][11] = (Byte.toString((byte) (this.dayData[i][11]))+"°C");            
            //Load external temperature min
            this.dayData[i][12] = Integer.parseInt("000000"
                    +splitDataLogger[159+(i*19)], 16);
            this.dayDecoded[i][12] = (Byte.toString((byte) (this.dayData[i][12]))+"°C");            
            //Load nightlength in minutes
            this.dayData[i][13] = Integer.parseInt("000000"
                    +splitDataLogger[160+(i*19)], 16);
            this.dayDecoded[i][13] = (Integer.toString(this.dayData[i][13]*6)+" Minutes");
            //
            this.dayData[i][14] = Integer.parseInt("0000"+splitDataLogger[161+(i*19)]
                    +splitDataLogger[162+(i*19)], 16); 
            this.dayDecoded[i][14] = Integer.toBinaryString(this.dayData[i][14]);
        }
        //Monthly Datalogger values to be decyphered.
        for ( i=0;i<24;i++ )    {
            this.monthData[i][0] = Integer.parseInt("0000"+splitDataLogger[733+(i*19)]
                    +splitDataLogger[734+(i*19)], 16);
            this.currentYear = (byte) (this.dayData[i][0]&127);
            this.currentMonth = (byte) ((this.dayData[i][0]>>7)&15);
            this.currentDay = (byte) ((this.dayData[i][0]>>11&31));
            this.monthDecoded[i][0]= ("20"+Byte.toString(this.currentYear)+"-"
                    +Byte.toString(this.currentMonth)+"-"
                    +Byte.toString(this.currentDay));
            System.out.println(this.monthDecoded[i][0]);
            this.monthData[i][1] = Integer.parseInt("000000"
                    +splitDataLogger[735+(i*19)], 16);
            this.monthData[i][2] = Integer.parseInt("000000"
                    +splitDataLogger[736+(i*19)], 16);
            this.monthData[i][3] = Integer.parseInt("000000"
                    +splitDataLogger[737+(i*19)], 16);
            this.monthData[i][4] = Integer.parseInt("0000"+splitDataLogger[738+(i*19)]
                    +splitDataLogger[739+(i*19)], 16);
            this.monthData[i][5] = Integer.parseInt("0000"+splitDataLogger[740+(i*19)]
                    +splitDataLogger[741+(i*19)], 16);
            this.monthData[i][6] = Integer.parseInt("000000"
                    +splitDataLogger[742+(i*19)], 16);
            this.monthData[i][7] = Integer.parseInt("000000"
                    +splitDataLogger[743+(i*19)], 16);
            this.monthData[i][8] = Integer.parseInt("000000"
                    +splitDataLogger[744+(i*19)], 16);
            this.monthData[i][9] = Integer.parseInt("000000"
                    +splitDataLogger[745]+(i*19), 16);
            this.monthData[i][10] = Integer.parseInt("000000"
                    +splitDataLogger[746+(i*19)], 16);
            this.monthData[i][11] = Integer.parseInt("000000"
                    +splitDataLogger[747+(i*19)], 16);
            this.monthData[i][12] = Integer.parseInt("000000"
                    +splitDataLogger[748+(i*19)], 16);
            this.monthData[i][13] = Integer.parseInt("000000"
                    +splitDataLogger[749+(i*19)], 16);
            this.monthData[i][14] = Integer.parseInt("0000"+splitDataLogger[750+(i*19)]
                    +splitDataLogger[751+(i*19)], 16);            
        }
        //Decypher settings
        
        
        System.out.println(Arrays.toString(dayData[0]));
        System.out.println(Arrays.toString(this.dayDecoded[0]));
//        System.out.println(Arrays.toString(monthData[0]));
    }    
    
    public static byte hexStringToByte(String s) {
        int len = s.length();
        byte data = (byte) 0b11111111;
        if ( (len == 2) ){
            for (int i = 0; i < len; i += 2) {
                data = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                     + Character.digit(s.charAt(i+1), 16));
            }
        }else   {
            System.out.println("Incorrect data format");
            data= (byte) 0b11111111;
        }
            
        return data;
    }
    public static int hexStringToInt(String s) {
        int len = s.length();
        int data = (int) 0b11111111111111111111111111111111;
        if ( (len == 8) ){
            for (int i = 0; i < len; i += 2) {
                data = (int) ((Character.digit(s.charAt(i), 16) << 12)
                                     + (Character.digit(s.charAt(i+1), 16) << 8)
                                     + (Character.digit(s.charAt(i+1), 16) << 4)
                                     + (Character.digit(s.charAt(i+1), 16)));
            }
        }else   {
            System.out.println("Incorrect data format");
            data= (int) 0b11111111111111111111111111111111;
        }
            
        return data;
    }
        //*Set the TextFields to the most current data
        //Set current battery voltage
//        batteryVoltage = (Float.parseFloat(this.cxCurrentValues.substring(53, 58)));
//        batteryVoltage = (float) (batteryVoltage*0.001);
//        this.currentBatteryVoltage.setText(Float.toString(batteryVoltage));
        
        //this.currentBatteryVoltage.setText(this.cxCurrentValues.substring(13, 15) +"."+ this.cxCurrentValues.substring(15, 16));
        //Set current SOC values, (first MENUSTATE is needed
//        menuState = (this.cxDataLogger.substring(40,42));
//        menuStateByte = CXCom.hexStringToByte(menuState);
////        System.out.println(menuState);
//        System.out.println(menuStateByte);
////        System.out.println(menuStateInt[1]);
//        
//        if ( "1".equals(this.cxCurrentValues.substring(5,6)) ){
//            this.currentStateOfCharge.setText(this.cxCurrentValues.substring(5, 8));
//        } else this.currentStateOfCharge.setText(this.cxCurrentValues.substring(6, 8));
//        //Set current charge current
//        if ( "00".equals(this.cxCurrentValues.substring(59, 61)) )  {
//            this.currentChargeCurrent.setText(this.cxCurrentValues.substring(61, 62));
//        }else if ( "0".equals(this.cxCurrentValues.substring(59, 60)) ) {
//            this.currentChargeCurrent.setText(this.cxCurrentValues.substring(60, 62));
//        }else this.currentChargeCurrent.setText(this.cxCurrentValues.substring(59, 62));
//        //Set current load current
//        if ( "00".equals(this.cxCurrentValues.substring(25, 27)) )  {
//            this.currentLoadCurrent.setText(this.cxCurrentValues.substring(27, 28));
//        }else if ( "0".equals(this.cxCurrentValues.substring(25, 26)) ) {
//            this.currentLoadCurrent.setText(this.cxCurrentValues.substring(26, 28));
//        }else this.currentLoadCurrent.setText(this.cxCurrentValues.substring(25, 28));
//        //Set current today's energy
//        this.currentTodaysEnergy.setText("NA");
//        //Set Battery Charging State
//        batteryChargingStateString = this.cxCurrentValues.substring(21, 24);
//        batteryChargingState = (int) Integer.valueOf(batteryChargingStateString);
//        batteryChargingStateByte = (byte) batteryChargingState;
//        if ( (batteryChargingState & 1) == 1 ){
//            this.currentBatteryChargingState.setText("BOOST");
//        } else if ( (batteryChargingState & 2) == 2 ) {
//            this.currentBatteryChargingState.setText("EQUALIZE");
//        } else {
//            this.currentBatteryChargingState.setText("FLOAT");    
//        }
//     
    

}
