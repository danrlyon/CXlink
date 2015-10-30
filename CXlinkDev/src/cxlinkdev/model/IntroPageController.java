/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cxlinkdev.model;

import cxlinkdev.*;
//import cxlinkdev.model.CXCom;
//import cxlinkdev.model.CXlinkDev;
import static cxlinkdev.model.CXlinkDev.selectedPort;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.Stage;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jssc.SerialPortException;
import jssc.SerialPortList;



/**
 *
 * @author Dell-N7110
 */
public class IntroPageController implements Initializable, ControlledScreen {
    
    ScreensController myController;
       
    @FXML private MenuButton menuButton;
    
    @FXML private MenuItem cxConnectSelect;
    @FXML private MenuItem cxnConnectSelect;
    @FXML private MenuItem cxnSolidConnectSelect;
    
    @FXML private Button refreshButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String[] portNames = SerialPortList.getPortNames();
        String firstButton = null;
        if(portNames.length==1)  {
            try {
                CXIdentifier cxIdentifier;
                cxIdentifier = new CXIdentifier(portNames[0]);
                firstButton = CXIdentifier.retrieveType();
                if(CXIdentifier.serialPort.isOpened()) CXIdentifier.serialPort.closePort();
                if(firstButton==null)   {
                    SolidIdentifier solidIdentifier = new SolidIdentifier(portNames[0]);
                    firstButton = SolidIdentifier.retrieveType();
                    if(SolidIdentifier.serialPort.isOpened()) SolidIdentifier.serialPort.closePort();
                    if(firstButton==null) firstButton = "No CX detected";
                }                
                System.out.println(firstButton);
            } catch (SerialPortException ex) {
                Logger.getLogger(IntroPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (firstButton==null)  {
                MenuItem menuItem = new MenuItem("Error in page init, firstButton is null");
                this.menuButton.getItems().add(menuItem);
            } else  {
                MenuItem menuItem = new MenuItem(portNames[0]+": "+firstButton);
                this.menuButton.getItems().add(menuItem);               
                menuItem.setOnAction((ActionEvent event) -> {
                    //Should Check the selection first, then load appropriate page
                    if( menuItem.getText().endsWith("CXNsolid") )  {
                        myController.setScreen(CXlinkDev.screen4ID);
                    }else if( menuItem.getText().endsWith("CXN") )  {
                        myController.setScreen(CXlinkDev.screen3ID);
                    }else if( menuItem.getText().endsWith("CX") )  {
                        myController.setScreen(CXlinkDev.screen2ID);
                    }else System.out.println("No Controller Connected");                                      
                });
            }        
        } else if (portNames.length>1)    {
            for (String portNameTemp : portNames)  {
                System.out.println("Not capable of handling multiple ports, to be added");
            }
        } else if (portNames.length==0) {
            System.out.println("No Serial Ports detected");
            MenuItem menuItem = new MenuItem("No Serial Ports Detected: Check Connections");
            this.menuButton.getItems().add(menuItem);
        } else System.out.println("Error, portNames can't be negative");
        
    }
    
    
    public void loadPortMenuButton() throws UnsupportedEncodingException    {
        while( !this.menuButton.getItems().isEmpty() )  {
            this.menuButton.getItems().remove(0);
        }
        String[] portNames = SerialPortList.getPortNames();
        String[] menuItems = new String[portNames.length];
       
        //menuItems = null;
        int menuItemPosition = 0;
        if(portNames.length==1)  {
            try {
                CXIdentifier cxIdentifier;
                cxIdentifier = new CXIdentifier(portNames[0]);
                menuItems[0] = CXIdentifier.retrieveType();
                if(CXIdentifier.serialPort.isOpened()) CXIdentifier.serialPort.closePort();
                if(menuItems[0]==null)   {
                    SolidIdentifier solidIdentifier = new SolidIdentifier(portNames[0]);
                    menuItems[0] = SolidIdentifier.retrieveType();
                    if(SolidIdentifier.serialPort.isOpened()) SolidIdentifier.serialPort.closePort();
                    if(menuItems[0]==null) menuItems[0] = "No CX detected";
                }                
                System.out.println(menuItems[0]);
            } catch (SerialPortException ex) {
                Logger.getLogger(IntroPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (menuItems[0]==null)  {
                MenuItem menuItem = new MenuItem("Error in page init, menuItems[0] is null");
                this.menuButton.getItems().add(menuItem);
            } else  {
                MenuItem menuItem = new MenuItem(portNames[0]+": "+menuItems[0]);
                this.menuButton.getItems().add(menuItem);               
                menuItem.setOnAction((ActionEvent event) -> {
                    //Should Check the selection first, then load appropriate page
                    if( menuItem.getText().endsWith("CXNsolid") )  {
                        CXlinkDev.selectedPort=menuItem.getText();
                        myController.setScreen(CXlinkDev.screen4ID);
                    }else if( menuItem.getText().endsWith("CXN") )  {
                        CXlinkDev.selectedPort=menuItem.getText();
                        myController.setScreen(CXlinkDev.screen3ID);
                    }else if( menuItem.getText().endsWith("CX") )  {
                        CXlinkDev.selectedPort=menuItem.getText();
                        myController.setScreen(CXlinkDev.screen2ID);
                    }else System.out.println("No Controller Connected");                                      
                });
            }        
        } else if (portNames.length>1)    {
            menuItemPosition = 0;
            for (String portNameTemp : portNames)  {
                try {
                    CXIdentifier cxIdentifier;
                    cxIdentifier = new CXIdentifier(portNames[0]);
                    menuItems[menuItemPosition] = CXIdentifier.retrieveType();
                    if(CXIdentifier.serialPort.isOpened()) CXIdentifier.serialPort.closePort();
                    if(menuItems[menuItemPosition]==null)   {
                        SolidIdentifier solidIdentifier = new SolidIdentifier(portNames[0]);
                        menuItems[menuItemPosition] = SolidIdentifier.retrieveType();
                        if(SolidIdentifier.serialPort.isOpened()) SolidIdentifier.serialPort.closePort();
                        if(menuItems[menuItemPosition]==null) menuItems[menuItemPosition] = "No CX detected";
                    }                
                    System.out.println(menuItems[menuItemPosition]);
                } catch (SerialPortException ex) {
                    Logger.getLogger(IntroPageController.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (menuItems[menuItemPosition]==null)  {
                    MenuItem menuItem = new MenuItem("Error in page init, menuItems[menuItemPosition] is null");
                    this.menuButton.getItems().add(menuItem);
                } else  {
                    MenuItem menuItem = new MenuItem(portNames[0]+": "+menuItems[menuItemPosition]);
                    this.menuButton.getItems().add(menuItem);               
                    menuItem.setOnAction((ActionEvent event) -> {
                        //Should Check the selection first, then load appropriate page
                        if( menuItem.getText().endsWith("CXNsolid") )  {
                            myController.setScreen(CXlinkDev.screen4ID);
                        }else if( menuItem.getText().endsWith("CXN") )  {
                            myController.setScreen(CXlinkDev.screen3ID);
                        }else if( menuItem.getText().endsWith("CX") )  {
                            myController.setScreen(CXlinkDev.screen2ID);
                        }else System.out.println("No Controller Connected");                                      
                    });
                }
                if( portNames.length>menuItemPosition)menuItemPosition++;
            }
        } else if (portNames.length==0) {
            System.out.println("No Serial Ports detected");
            MenuItem menuItem = new MenuItem("No Serial Ports Detected: Check Connections");
            this.menuButton.getItems().add(menuItem);
        } else System.out.println("Error, portNames can't be negative");
    }
       
    /**
     *
     * @param screenParent
     */
    @Override
    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }

 
    
    
    
    @FXML
    private void handleRefreshSelect(ActionEvent event) throws SerialPortException {
        try {        
            this.loadPortMenuButton();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(IntroPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void handleCXConnectSelect(ActionEvent event) throws IOException, SerialPortException {
        myController.setScreen(CXlinkDev.screen2ID);               
    }
    
    
    
    
}
