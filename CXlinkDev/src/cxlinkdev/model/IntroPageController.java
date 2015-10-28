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



/**
 *
 * @author Dell-N7110
 */
public class IntroPageController implements Initializable, ControlledScreen {
    
    ScreensController myController;
    private CXCom cxCom;
    
    @FXML private MenuButton menuButton;
    
    @FXML private MenuItem cxConnectSelect;
    @FXML private MenuItem cxnConnectSelect;
    @FXML private MenuItem cxnSolidConnectSelect;
    
    @FXML private Button refreshButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
    }
    
    
    public void loadPortMenuButton() throws UnsupportedEncodingException    {
        try {
            this.cxCom = new CXCom();
            try {
                this.cxCom.portCheck();
            } catch (InterruptedException ex) {
                Logger.getLogger(IntroPageController.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Could not complete portCheck");
            }
        } catch (SerialPortException | UnsupportedEncodingException ex) {
            Logger.getLogger(IntroPageController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Could not complete CXCominit");
        }
        int i;
        for (i=0; this.cxCom.getControllerType(i)!= 0; i++)  {
            int controllerType = this.cxCom.getControllerType(this.cxCom.getPortArrayPosition());
            if(controllerType!=0){
                this.menuButton.getItems().clear();
                if ( controllerType ==0 ) {
                    MenuItem menuItem = new MenuItem("No CX detected");
                    this.menuButton.getItems().add(menuItem);
                }   else    {        

                    MenuItem menuItem = new MenuItem("CX");
                    menuItem.setOnAction((ActionEvent e) -> {
                        myController.setScreen(CXlinkDev.screen2ID);
                    }); 
                    this.menuButton.getItems().add(menuItem);
                }
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
