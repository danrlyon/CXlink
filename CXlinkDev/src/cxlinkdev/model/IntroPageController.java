/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cxlinkdev.model;

import cxlinkdev.*;
//import cxlinkdev.model.CXCom;
//import cxlinkdev.model.CXlinkDev;
import static cxlinkdev.model.CXlinkDev.space;
import java.io.File;
import java.io.IOException;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jssc.SerialPortException;



/**
 *
 * @author Dell-N7110
 */
public class IntroPageController implements Initializable, ControlledScreen {
    
    ScreensController myController;
    private CXCom cxCom;
    
    @FXML
    private MenuButton menuButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.loadPortMenuButton();
    }
    
    public void loadPortMenuButton()    {
        String[] cxMenuItems;
        try {
            this.cxCom = new CXCom();
        } catch (SerialPortException ex) {
            Logger.getLogger(CXPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            this.cxCom.portCheck();
        } catch (SerialPortException ex) {
            Logger.getLogger(IntroPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if ( this.cxCom.getPortName(0)==null ) {
            MenuItem menuItem = new MenuItem(this.cxCom.getCXType(0));
            this.menuButton.getItems().add(menuItem);
        }   else    {        
            for ( int i=0; this.cxCom.getPortName(i) != null ; i++)    {
                MenuItem menuItem = new MenuItem(this.cxCom.getCXType(i));
                if (!this.cxCom.getCXType(i).equals("No CX Detected"))  {
                    menuItem.setOnAction((ActionEvent e) -> {
                        myController.setScreen(CXlinkDev.screen2ID);
                    });
                } 
                this.menuButton.getItems().add(menuItem);
            }
        }
    }
    
    /**
     *
     * @param screenParent
     */
    @Override
    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }

//    @FXML
//    private void goToScreen2(ActionEvent event){
//       myController.setScreen(CXlinkDev.screen2ID);
//    }
//    
//    @FXML
//    private void goToScreen3(ActionEvent event){
//       myController.setScreen(CXlinkDev.screen3ID);
//    }
//    
//    @FXML
//    private void goToScreen4(ActionEvent event){
//       myController.setScreen(CXlinkDev.screen4ID);
//    }
//    
    
    @FXML private Label label;
    @FXML private MenuItem CXConnectSelect;
    @FXML private MenuItem CXNConnectSelect;
    
    @FXML
    private void handleRefreshSelect(ActionEvent event) throws SerialPortException {
        this.loadPortMenuButton();        
    }
    
    @FXML
    private void handleCXConnectSelect(ActionEvent event) throws IOException, SerialPortException {
        myController.setScreen(CXlinkDev.screen2ID);               
    }
    
    
    
    
}
