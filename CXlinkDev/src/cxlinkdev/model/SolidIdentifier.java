/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cxlinkdev.model;

import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 *
 * @author Dell-N7110
 */
public class SolidIdentifier {
   static SerialPort serialPort;
    static String type;
    static String currentValues;
    static String dataLoggerValues;
    
    //args[0] = port name, args[1] = baud rate

    /**
     *
     * @param portName
     */
        public SolidIdentifier(String portName) {
        serialPort = new SerialPort(portName); //args in the future
        try {
            serialPort.openPort();//Open port
            setBaudRate(19200);//Set Params
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
            serialPort.setEventsMask(mask);//Set mask
            serialPort.addEventListener(new SerialPortReader());//Add SerialPortEventListener
            serialPort.writeString(" ");
            try {
                Thread.sleep(500);                 //Optimize this value, .5 secs for now
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }            
            
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }
    
    public static void setBaudRate(int baudRate)    {       
        try {
            serialPort.setParams(baudRate, 8, 1, 0);//Set params
        } catch (SerialPortException ex) {
            Logger.getLogger(CXIdentifier.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public static void retrieveCurrentValues()   {        
        try {
            serialPort.writeString(" ");
        } catch (SerialPortException ex) {
            Logger.getLogger(CXIdentifier.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public static String retrieveType()   {
        return type;
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
                if(event.getEventValue() == 65){//Check bytes count in the input buffer
                    try {
                        String buffer = serialPort.readString(65);
                        System.out.println(buffer);
                        type = "CX";
                        serialPort.closePort();
                    }
                    catch (SerialPortException ex) {
                        System.out.println(ex);
                    }
                } else if(event.getEventValue() == 83)  {
                    try {
                        String buffer = serialPort.readString(83);
                        System.out.println(buffer);
                        type = "CXN";
                        serialPort.closePort();
                    }
                    catch (SerialPortException ex) {
                        System.out.println(ex);
                    }
                } else if(event.getEventValue() == 96)  {
                    try {
                        String buffer = serialPort.readString(96);
                        System.out.println(buffer);
                        type = "CXNsolid";
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
