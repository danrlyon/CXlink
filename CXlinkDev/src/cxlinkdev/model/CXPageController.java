/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cxlinkdev.model;

//import cxlinkdev.model.CXCom;
//import cxlinkdev.model.CXlinkDev;
//import cxlinkdev.model.ScreensController;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import jssc.SerialPortException;
/**
 * FXML Controller class
 *
 * @author Dell-N7110
 */
public class CXPageController implements Initializable, ControlledScreen {
            
    /*List of Buttons by fx:id*/
    @FXML private Button currentValuesButton; 
    @FXML private Button dataLoggerButton;
    @FXML private Button newSettingsButton;
    @FXML private Button currentSystemReadings;
    @FXML private Button currentSystemSettings;
    @FXML private Button currentRefresh;
    @FXML private Button dataDayButton;
    @FXML private Button dataWeekButton;
    @FXML private Button dataMonthButton;
    @FXML private Button dataYearButton;
    @FXML private Button settingsControllerButton;
    @FXML private Button settingsLoadButton;
    
    /*List of TextFields by fx:id*/
    @FXML private TextField currentBatteryVoltage;
    @FXML private TextField currentStateOfCharge;
    @FXML private TextField currentChargeCurrent;
    @FXML private TextField currentLoadCurrent;
    @FXML private TextField currentTodaysEnergy;
    @FXML private TextField currentBatteryChargingState;
    @FXML private TextField currentLoadState;
    @FXML private TextField currentTemperature;
    
    /*List of Labels by fx:id*/
    @FXML private Label batteryVoltageLabel;
    @FXML private Label stateOfChargeLabel;
    @FXML private Label chargeCurrentLabel;
    @FXML private Label loadCurrentLabel;
    @FXML private Label todaysEnergyLabel;
    @FXML private Label batteryChargingStateLabel;
    @FXML private Label loadStateLabel;
    @FXML private Label temperatureLabel;
    /*List of Labels by fx:id*/
    @FXML private Label batteryVoltageUnits;
    @FXML private Label stateOfChargeUnits;
    @FXML private Label chargeCurrentUnits;
    @FXML private Label loadCurrentUnits;
    @FXML private Label todaysEnergyUnits;
    @FXML private Label batteryChargingStateUnits;
    @FXML private Label loadStateUnits;
    @FXML private Label temperatureUnits;
    
    /*List of GridPanes by fx:id*/
    @FXML private GridPane currentValuesGridPane;
    @FXML private GridPane currentValuesSideGridPane;
    
    /*List of BorderPanes by fx:id, 3 main buttons control the BorderPanes*/
    @FXML private BorderPane currentValuesBorderPane;
    @FXML private BorderPane dataLoggerBorderPane;
    @FXML private BorderPane newSettingsBorderPane;
    
    /*List of StackPane by fx:id*/
    @FXML private StackPane cxPageStackPane;
    
    /*List of variables used by CXPageController*/
    private CXCom cxCom;
    private String cxCurrentValues;
    private String cxDataLogger;
    private String test;
    
    final static byte one   = (byte) 0b00000001;
    final static byte two   = (byte) 0b00000010;
    final static byte three = (byte) 0b00000100;
    final static byte four  = (byte) 0b00001000;
    final static byte five  = (byte) 0b00010000;
    final static byte six   = (byte) 0b00100000;
    final static byte seven = (byte) 0b01000000;
    final static byte eight = (byte) 0b10000000;
    
    ScreensController myController;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        this.currentValuesButton.setStyle("-fx-font-weight:bold");
        this.currentSystemReadings.setStyle("-fx-font-weight:bold");
        
        //this.currentValuesBorderPane.setOpacity(1);
        this.dataLoggerBorderPane.setOpacity(0);
        this.newSettingsBorderPane.setOpacity(0);
        try {
            this.cxCom = new CXCom();
        } catch (SerialPortException ex) {
            Logger.getLogger(CXPageController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    @FXML
    private void goToScreen1(ActionEvent event){
       myController.setScreen(CXlinkDev.screen1ID);
    }
    
    @FXML
    private void loadCurrentValues(ActionEvent event){
       myController.setScreen(CXlinkDev.screen1ID);   
    }
//    @FXML
//    private void goToScreen3(ActionEvent event){
//       myController.setScreen(CXlinkDev.screen3ID);
//    }
    
    @FXML
    private void handleMainMenuSelect(ActionEvent event) throws IOException, SerialPortException {
        myController.setScreen(CXlinkDev.screen1ID);
    }
    
    @FXML
    private void handleSystemReadingsSelect(ActionEvent event) throws SerialPortException, UnsupportedEncodingException  {
        this.currentValuesButton.setStyle("-fx-font-weight:bold");
        this.dataLoggerButton.setStyle("-fx-font-weight:normal");
        this.newSettingsButton.setStyle("-fx-font-weight:normal");        
        this.currentSystemReadings.setStyle("-fx-font-weight:bold");
        this.currentSystemSettings.setStyle("-fx-font-weight:normal");        
//        this.refreshCurrentValues(event);
    }
    
    @FXML
    private void handleSystemSettingsSelect(ActionEvent event) throws SerialPortException, UnsupportedEncodingException  {
        this.currentValuesButton.setStyle("-fx-font-weight:bold");
        this.dataLoggerButton.setStyle("-fx-font-weight:normal");
        this.newSettingsButton.setStyle("-fx-font-weight:normal");        
        this.currentSystemReadings.setStyle("-fx-font-weight:normal");
        this.currentSystemSettings.setStyle("-fx-font-weight:bold");        
        //this.refreshCurrentValues(event);
    }
    
    
    @FXML
    private void refreshCurrentValues(ActionEvent event) throws SerialPortException, UnsupportedEncodingException {
        
        float batteryVoltage;
        int stateOfCharge = 0;
        int chargeCurrent = 0;
        int loadCurrent = 0;
        int todaysEnergy = 0;
        int batteryChargingState = 0;
        byte batteryChargingStateByte = (byte) 0b00000000;
        String batteryChargingStateString;
        int loadState = 0;
        int temperature = 0;
        String menuState;
        byte menuStateByte = (byte) 0b111111111;
        
        
        this.cxCom.setStatus(1);
        this.cxCom.portCheck();
        //this.cxCom.printStatusCX(space,cxCom.getPortName());        
       // this.cxCurrentValues = this.cxCom.getCXCurrentValues(cxCom.getPortName());
        //this.cxDataLogger = this.cxCom.getCXDataLoggerValues("!", this.cxCom.getPortName());
        System.out.println(this.cxCom.getDataLoggerValues());
        
        //Set the TextFields to the most current data
        //Set current battery voltage
        batteryVoltage = (Float.parseFloat(this.cxCurrentValues.substring(13, 16)));
        batteryVoltage = (float) ((batteryVoltage*0.032)+9);
        this.currentBatteryVoltage.setText(Float.toString(batteryVoltage));
        
        //this.currentBatteryVoltage.setText(this.cxCurrentValues.substring(13, 15) +"."+ this.cxCurrentValues.substring(15, 16));
        //Set current SOC values, (first MENUSTATE is needed
        menuState = (this.cxDataLogger.substring(40,42));
        menuStateByte = CXCom.hexStringToByte(menuState);
//        System.out.println(menuState);
        System.out.println(menuStateByte);
//        System.out.println(menuStateInt[1]);
        
        if ( "1".equals(this.cxCurrentValues.substring(5,6)) ){
            this.currentStateOfCharge.setText(this.cxCurrentValues.substring(5, 8));
        } else this.currentStateOfCharge.setText(this.cxCurrentValues.substring(6, 8));
        //Set current charge current
        if ( "00".equals(this.cxCurrentValues.substring(59, 61)) )  {
            this.currentChargeCurrent.setText(this.cxCurrentValues.substring(61, 62));
        }else if ( "0".equals(this.cxCurrentValues.substring(59, 60)) ) {
            this.currentChargeCurrent.setText(this.cxCurrentValues.substring(60, 62));
        }else this.currentChargeCurrent.setText(this.cxCurrentValues.substring(59, 62));
        //Set current load current
        if ( "00".equals(this.cxCurrentValues.substring(25, 27)) )  {
            this.currentLoadCurrent.setText(this.cxCurrentValues.substring(27, 28));
        }else if ( "0".equals(this.cxCurrentValues.substring(25, 26)) ) {
            this.currentLoadCurrent.setText(this.cxCurrentValues.substring(26, 28));
        }else this.currentLoadCurrent.setText(this.cxCurrentValues.substring(25, 28));
        //Set current today's energy
        this.currentTodaysEnergy.setText("NA");
        //Set Battery Charging State
        batteryChargingStateString = this.cxCurrentValues.substring(21, 24);
        batteryChargingState = (int) Integer.valueOf(batteryChargingStateString);
        batteryChargingStateByte = (byte) batteryChargingState;
        if ( (batteryChargingState & 1) == 1 ){
            this.currentBatteryChargingState.setText("BOOST");
        } else if ( (batteryChargingState & 2) == 2 ) {
            this.currentBatteryChargingState.setText("EQUALIZE");
        } else {
            this.currentBatteryChargingState.setText("FLOAT");    
        }
        
    } 

    @FXML
    private void loadSystemReadings()   {
        
    }
        
    @FXML
    private void handleCurrentValuesSelect(ActionEvent event) throws SerialPortException, UnsupportedEncodingException  {
        //Set selected button to bold
        this.currentValuesButton.setStyle("-fx-font-weight:bold");
        this.dataLoggerButton.setStyle("-fx-font-weight:normal");
        this.newSettingsButton.setStyle("-fx-font-weight:normal");        
        this.currentSystemReadings.setStyle("-fx-font-weight:bold");
        this.currentSystemSettings.setStyle("-fx-font-weight:normal");
        
        //Set each borderPane to proper opacity, then remove and reload in the proper order
        //Optimize in the future
        this.dataLoggerBorderPane.setOpacity(0);
        this.newSettingsBorderPane.setOpacity(0);
        this.currentValuesBorderPane.setOpacity(1);
        this.cxPageStackPane.getChildren().remove(0, 2);
        this.cxPageStackPane.getChildren().setAll(this.newSettingsBorderPane, this.dataLoggerBorderPane, this.currentValuesBorderPane);
    }
    
    
    @FXML
    private void handleDataLoggerSelect(ActionEvent event)   {        
        //Set Selected buttons to bold
        this.currentValuesButton.setStyle("-fx-font-weight:normal");
        this.dataLoggerButton.setStyle("-fx-font-weight:bold");
        this.newSettingsButton.setStyle("-fx-font-weight:normal");
        
        //Set each borderPane to proper opacity, then remove and reload in the proper order
        //Optimize in the future
        this.currentValuesBorderPane.setOpacity(0);
        this.newSettingsBorderPane.setOpacity(0);    
        this.dataLoggerBorderPane.setOpacity(1);
        this.cxPageStackPane.getChildren().remove(0, 2);
        this.cxPageStackPane.getChildren().setAll(this.currentValuesBorderPane, this.newSettingsBorderPane, this.dataLoggerBorderPane);
        
    }
    
    @FXML
    private void handleNewSettingsSelect(ActionEvent event)   {
        //Set selected buttons to bold
        this.currentValuesButton.setStyle("-fx-font-weight:normal");
        this.dataLoggerButton.setStyle("-fx-font-weight:normal");
        this.newSettingsButton.setStyle("-fx-font-weight:bold");
        
        //Set each borderPane to proper opacity, then remove and reload in the proper order
        //Optimize in the future
        this.currentValuesBorderPane.setOpacity(0);
        this.newSettingsBorderPane.setOpacity(1);    
        this.dataLoggerBorderPane.setOpacity(0);
        this.cxPageStackPane.getChildren().remove(0, 2);
        this.cxPageStackPane.getChildren().setAll(this.currentValuesBorderPane, this.dataLoggerBorderPane, this.newSettingsBorderPane);
        
    }
    
}
