/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cxlinkdev.model;

import static cxlinkdev.model.CXlinkDev.space;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortException;
import jssc.SerialPortList;
import jssc.SerialPortEventListener;


/**
 *
 * @author Dell-N7110
 */
public final class CXCom {
    
    private int[] status;
    private int[] controllerType;         //1=CX, 2=CXN, 3=CXNsolid
    
    private String[] cxCurrentStatus;
    private String[] cxDataLogger;
  
    /*portName should be dynamically allocated*/
    private final String[] portName = new String[20];
    private final int[] baudrate;
    private int portArrayPosition;
  
    //private SerialPort serialPort;
    
    InputStream inputStream;
    private Object args;
    
    public CXCom() throws SerialPortException {    
        this.status = new int[20];
        this.controllerType = new int[20];
        this.cxCurrentStatus = new String[20];
        this.cxDataLogger = new String[20];
        this.portArrayPosition=0;
        this.baudrate = new int[20];
        this.setStatus(0);
        //this.portCheck();
        
    }
    
    public int getPortArrayPosition() {
        return this.portArrayPosition;
    }
    
    public void setStatus(int tempStatus)   {
        this.status[this.portArrayPosition] = tempStatus;
    }
    
    public void setPortName(int arrayIndex, String tempPortName)    {
        this.portName[arrayIndex] = tempPortName;
    }
    
    public String getPortName(int position)     {
        return this.portName[position];
    }
    
    public int getControllerType(int position)  {
        return this.controllerType[position];
        }
    
    public String getCXType(int position)   {
        String cxType;
        switch (this.controllerType[position])    {
            case 1: cxType = "CX";
                break;
            case 2: cxType = "CXN";
                break;
            case 3: cxType = "CXNsolid";
                break;
            default: cxType = "No CX Detected";
                break;
        }
        return cxType;
    }
    
    public String getDataLoggerValues() {
        return this.cxDataLogger[this.portArrayPosition];
    }
    
    public String getCXCurrentValues(String portNameTemp){
        //String[] portNames = SerialPortList.getPortNames();
        System.out.println(portNameTemp);
        SerialPort serialPort;
        serialPort = new SerialPort(portNameTemp);
        try {
            serialPort.openPort();//Open port
            if ( serialPort.setParams(9600, 8, 1, 0) == false ) System.out.println ("Baudcheck false");//Set params
            else this.portName[this.portArrayPosition] = portNameTemp;
            /*Needs to be able to handle multiple ports*/
            serialPort.closePort();
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
                    
        return this.cxCurrentStatus[this.portArrayPosition];
    }
    
    public String getCXDataLoggerValues(String args, String portNameTemp)    {
        System.out.println(portNameTemp);
        SerialPort serialPort;
        serialPort = new SerialPort(portNameTemp);
        try {
            serialPort.openPort();//Open port
            if ( serialPort.setParams(9600, 8, 1, 0) == false ) System.out.println ("Baudcheck false");//Set params
            else this.portName[this.portArrayPosition] = portNameTemp;
            /*Needs to be able to handle multiple ports*/
            serialPort.writeBytes("!".getBytes());       //(args.getBytes());//Write data to port
            this.cxDataLogger[this.portArrayPosition] = serialPort.readString(767);
            serialPort.closePort();
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
                    
        return this.cxDataLogger[this.portArrayPosition];
    }
    
    
    //Further notice, portCheck will only check for CX positive ground controllers
    //in the future it will autodetect and autoconnect to any CX device
    public void portCheck() throws SerialPortException, UnsupportedEncodingException {
        this.portArrayPosition = -1;
        String[] portNames = SerialPortList.getPortNames();
        if ( portNames.length !=0 )    {
            for (String portNameTemp : portNames) {
                this.portArrayPosition++;
                System.out.println(portNameTemp);   //Can be deleted, used for debug
                SerialPort serialPort;
                serialPort = new SerialPort(portNameTemp);
                try {
                    serialPort.openPort();//Open serial port
                    serialPort.setParams(SerialPort.BAUDRATE_9600, 
                             SerialPort.DATABITS_8,
                             SerialPort.STOPBITS_1,
                             SerialPort.PARITY_NONE);//Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);
                    String buffer = new String() ;
                    serialPort.writeString(space);
                    buffer=serialPort.readString(65);
                    System.out.println(buffer);
                    if( !buffer.substring(64).equals("0") ) {
                        this.controllerType[this.portArrayPosition] = 1;
                    }
                    else    {    
                        this.controllerType[this.portArrayPosition] = 2;
                        System.out.println("Go to bed");
                    }
                    this.cxCurrentStatus[this.portArrayPosition] = buffer;
                    serialPort.closePort();//Close serial port  
                }
                catch (SerialPortException ex) {
                    System.out.println(ex);
                } 
            }  
        } else  {
            System.out.println("NoPortFound");
            this.controllerType[0] = 0;
        }
    }
    
    
    public static void printPorts(String[] args) {
        String[] portNames = SerialPortList.getPortNames();
        for (String portName : portNames) {
            System.out.println(portName);
        }
    }
    
    public void printStatusCX(String args, String portName) throws UnsupportedEncodingException {
        SerialPort serialPort;
        serialPort = new SerialPort(portName);
        byte[] temp;
        try {
            serialPort.openPort();//Open serial port
            serialPort.setParams(SerialPort.BAUDRATE_9600, 
                                 SerialPort.DATABITS_8,
                                 SerialPort.STOPBITS_1,
                                 SerialPort.PARITY_NONE);//Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);
            serialPort.writeBytes(args.getBytes());//Write data to port
            cxCurrentStatus[this.portArrayPosition] = serialPort.readString(64);
            serialPort.closePort();//Close serial port
            System.out.println( cxCurrentStatus[this.portArrayPosition] );
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
       
    }
    
    public void readCX(String args) throws UnsupportedEncodingException {
        SerialPort serialRead = new SerialPort("COM1");
        try {
            serialRead.openPort();//Open serial port
            serialRead.setParams(9600, 8, 1, 0);//Set params.
            byte[] buffer;
            buffer = serialRead.readBytes();
            cxCurrentStatus[this.portArrayPosition] = new String(buffer, "UTF-8");
            serialRead.closePort();//Close serial port
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
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
    


//    static class SerialPortReader implements SerialPortEventListener {
//
//        @Override
//        public void serialEvent(SerialPortEvent event) {
//            String buffer = null;
//            if(event.isRXCHAR()){//If data is available
//                if(event.getEventValue() == 10){//Check bytes count in the input buffer
//                    //Read data, if 10 bytes available 
//                    try {
//                        cxCurrentStatus[0] = serialPort.readString();
//                    }
//                    catch (SerialPortException ex) {
//                        System.out.println(ex);
//                    }
//                }
//            }
//            else if(event.isCTS()){//If CTS line has changed state
//                if(event.getEventValue() == 1){//If line is ON
//                    System.out.println("CTS - ON");
//                }
//                else {
//                    System.out.println("CTS - OFF");
//                }
//            }
//            else if(event.isDSR()){///If DSR line has changed state
//                if(event.getEventValue() == 1){//If line is ON
//                    System.out.println("DSR - ON");
//                }
//                else {
//                    System.out.println("DSR - OFF");
//                }
//            }
//        }
//    }
}