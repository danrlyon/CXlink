/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cxlinkdev.model;

import static cxlinkdev.model.CXlinkDev.selectedPort;
import static cxlinkdev.model.CXlinkDev.controllerType;
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
import jssc.SerialPortTimeoutException;


/**
 *
 * @author Dell-N7110
 */
public final class CXCom {
    
    private int status;
    //private int controllerType;         //1=CX, 2=CXN, 3=CXNsolid    
    static String cxCurrentStatus;     
    static String cxDataLogger;
    private String cxSettings;
    
    static SerialPort serialPort;  
    private int baudrate;
       
    public CXCom() throws SerialPortException {    
                
        
        this.setStatus(0);        
        cxCurrentStatus = null;
        cxDataLogger = null;
        if ( controllerType.equals("CX")||controllerType.equals("CXN") )    {
            this.baudrate=9600;
        } else if ( controllerType.equals("CXNsolid") ) {
            this.baudrate=19200;
        } else this.baudrate=0;
        
        //this.portCheck();
        
    }    
    
    public void setStatus(int tempStatus)   {
        this.status = tempStatus;
    }
        
    public String getCXType()   {
        return controllerType;
    }
    
    public String getCXDataLoggerValues() {
        return this.cxDataLogger;
    }
    
    public void setCXDataLoggerValues(  ) {
        serialPort = new SerialPort(selectedPort);
        cxDataLogger = null;
        try {
            serialPort.openPort();//Open port
            serialPort.setParams(baudrate, 8, 1, 0) ;  //Set Params
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
            //serialPort.setEventsMask(mask);//Set mask
            //serialPort.addEventListener(new SerialPortReader());//Add SerialPortEventListener
            serialPort.writeString("!");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CXCom.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                cxDataLogger = serialPort.readString(4000, 10000);  //Needs to be optimized
            } catch (SerialPortTimeoutException ex) {
                Logger.getLogger(CXCom.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            serialPort.closePort();
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
        
        System.out.println(cxDataLogger);
    }
    
    public void setBaudRate(int baudTemp)   {
        this.baudrate = baudTemp;
    }
      
    
    public void setCXCurrentValues(){        
        serialPort = new SerialPort(selectedPort);
        cxCurrentStatus = null;
        try {
            serialPort.openPort();//Open port
            serialPort.setParams(baudrate, 8, 1, 0) ;  //Set Params
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
            //serialPort.setEventsMask(mask);//Set mask
            //serialPort.addEventListener(new SerialPortReader());//Add SerialPortEventListener
            serialPort.writeString(" ");
            try {
                cxCurrentStatus = serialPort.readString(96, 1000);
            } catch (SerialPortTimeoutException ex) {
                Logger.getLogger(CXCom.class.getName()).log(Level.SEVERE, null, ex);
            }
            serialPort.closePort();
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }       
        System.out.println(cxCurrentStatus);
    }
    
    public String getCXCurrentValue()   {
        return cxCurrentStatus;
    }
    
    
    
//    public String getCXDataLoggerValues(String args, String portNameTemp)    {
//        System.out.println(portNameTemp);
//        SerialPort serialPort;
//        serialPort = new SerialPort(portNameTemp);
//        try {
//            serialPort.openPort();//Open port
//            if ( serialPort.setParams(9600, 8, 1, 0) == false ) System.out.println ("Baudcheck false");//Set params
//            else this.portName[this.portArrayPosition] = portNameTemp;
//            /*Needs to be able to handle multiple ports*/
//            serialPort.writeBytes("!".getBytes());       //(args.getBytes());//Write data to port
//            this.cxDataLogger = serialPort.readString(767);
//            serialPort.closePort();
//        }
//        catch (SerialPortException ex) {
//            System.out.println(ex);
//        }
//                    
//        return this.cxDataLogger;
//    }   
    
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
    
   /*
     * In this class must implement the method serialEvent, through it we learn about 
     * events that happened to our port. But we will not report on all events but only 
     * those that we put in the mask. In this case the arrival of the data and change the 
     * status lines CTS and DSR
     */
    static class SerialPortReader implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent event) {
            String rxString;
            
            if(event.isRXCHAR()){//If data is available
                if(event.getEventValue() == 65 && controllerType.equals("CX") ){//Check bytes count in the input buffer
                    try {
                        String buffer = serialPort.readString(65);
                        System.out.println(buffer + "string read from serial port \n");
                        cxCurrentStatus = buffer;
                        serialPort.closePort();
                    }
                    catch (SerialPortException ex) {
                        System.out.println(ex);
                    }
                } else if(event.getEventValue() == 83 && controllerType.equals("CXN") )  {
                    try {
                        String buffer = serialPort.readString(83);
                        System.out.println(buffer + "string read from serial port \n");
                        cxCurrentStatus = buffer;
                        serialPort.closePort();
                    }
                    catch (SerialPortException ex) {
                        System.out.println(ex);
                    }
                } else if(event.getEventValue() == 96 && controllerType.equals("CXNsolid"))  {
                    try {
                        String buffer = serialPort.readString(96);
                        System.out.println(buffer + "string read from serial port \n");
                        cxCurrentStatus = buffer;
                        serialPort.closePort();
                    }
                    catch (SerialPortException ex) {
                        System.out.println(ex);
                    }
                }               
            }else ;
        
        }
    }
}