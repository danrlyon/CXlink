/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cxlinkdev.model;

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
    
    private final int[] status;
    private final int[] controllerType;         //1=CX, 2=CXN, 3=CXNsolid
    
    private String[] cxCurrentStatus;
    private final String[] cxDataLogger;
  
    /*portName should be dynamically allocated*/
    private final String[] portName = new String[20];
    private final int[] baudrate;
    private int portArrayPosition; 
    
    
    static SerialPort serialPort;
    
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
    
    
    
    public void portCheck() throws SerialPortException {
        this.portArrayPosition = 0;
        String[] portNames = SerialPortList.getPortNames();
        if ( portNames.length != 0 )    {
            for (String portNameTemp : portNames) {
                System.out.println(portNameTemp);
                serialPort = new SerialPort(portNameTemp);
                try {
                    if ( (serialPort.openPort() == true) && (serialPort.setParams(9600, 8, 1, 0) == true ))  {
                        this.portName[this.portArrayPosition] = portNameTemp;
                        this.baudrate[this.portArrayPosition] = 9600;
                        CXCom.serialPort.writeBytes(" ".getBytes());//Write data to port         
                        //THIS GUY is causing the problems, need to create an event listener to listen for RX events instead of using readstring
                        this.cxCurrentStatus[this.portArrayPosition] = CXCom.serialPort.readString(80);

                        if( this.cxCurrentStatus[this.portArrayPosition] != null )   {
                            System.out.println(cxCurrentStatus[this.portArrayPosition]);
                            if ( (this.cxCurrentStatus[this.portArrayPosition].substring(79) == null)&&(this.cxCurrentStatus[this.portArrayPosition].substring(0).equals(" ")) ){
                                this.controllerType[this.portArrayPosition] = 1;
                                this.portArrayPosition++;
                            } else  {
                                this.controllerType[this.portArrayPosition] = 2;
                                this.portArrayPosition++;
                            }                           
                        }
                    } else if ( serialPort.setParams(19200, 8, 1, 0) == true )  {
                        if( serialPort.readBytes()!=null )    {
                            this.portName[this.portArrayPosition] = portNameTemp;
                            this.baudrate[this.portArrayPosition] = 19200;
                            this.controllerType[this.portArrayPosition]= 3;
                            this.portArrayPosition++;
                        }
                    } else      System.out.println("NoPortFound");

                    /*Needs to be able to handle multiple ports*/
                    serialPort.closePort();
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
    


    static class SerialPortReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR()){//If data is available
                if(event.getEventValue() == 10){//Check bytes count in the input buffer
                    //Read data, if 10 bytes available 
                    try {
                        byte buffer[] = serialPort.readBytes(10);
                    }
                    catch (SerialPortException ex) {
                        System.out.println(ex);
                    }
                }
            }
            else if(event.isCTS()){//If CTS line has changed state
                if(event.getEventValue() == 1){//If line is ON
                    System.out.println("CTS - ON");
                }
                else {
                    System.out.println("CTS - OFF");
                }
            }
            else if(event.isDSR()){///If DSR line has changed state
                if(event.getEventValue() == 1){//If line is ON
                    System.out.println("DSR - ON");
                }
                else {
                    System.out.println("DSR - OFF");
                }
            }
        }
    }
}