/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cxlinkdev.model;

//import cxlinkdev.model.CXCom;
//import cxlinkdev.model.CXlinkDev;
//import cxlinkdev.model.ScreensController;
import static cxlinkdev.model.CXlinkDev.selectedPort;
import static cxlinkdev.model.CXlinkDev.space;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import jssc.SerialPortException;
/**
 * FXML Controller class
 *
 * @author Dell-N7110
 */
public class CXNsolidPageController implements Initializable, ControlledScreen {
            
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
    @FXML private Button batteryVoltageButton;
    @FXML private Button ampHoursButton;
    @FXML private Button pVVoltageButton;
    @FXML private Button systemCurrentsButton;
    @FXML private Button morningSOCButton;
    @FXML private Button temperatureButton;
    
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
    @FXML private StackPane barChartStackPane;
    
    /*List of ScrollPane by fx:id*/
    @FXML private ScrollPane barChartScrollPane;
    
    /*List of Line Charts by fx:id*/
    @FXML private LineChart lineChart;
    @FXML private final BarChart<String, Number> batteryVoltages;
    @FXML private final BarChart ampHours;
    @FXML private final BarChart pVVoltages;
    @FXML private final BarChart systemCurrents;
    @FXML private final BarChart morningSOC;
    @FXML private BarChart externalTemp;
    
    
    ScreensController myController;
    
    private CXNsolidDataDecryptor solidDecryptor;
    
    //List of the series' needed for the charts
    private final XYChart.Series displayedSeries1 = new XYChart.Series();
    private final XYChart.Series displayedSeries2 = new XYChart.Series();
    private final XYChart.Series batteryMinSeries = new XYChart.Series();
    private final XYChart.Series batteryMaxSeries = new XYChart.Series();
    private final XYChart.Series loadAmpHoursSeries = new XYChart.Series();
    private final XYChart.Series chargeAmpHoursSeries = new XYChart.Series();
    private final XYChart.Series pVMinSeries = new XYChart.Series();
    private final XYChart.Series pVMaxSeries = new XYChart.Series();
    private final XYChart.Series loadMaxCurrentSeries = new XYChart.Series();
    private final XYChart.Series chargeMaxCurrentSeries = new XYChart.Series();
    private final XYChart.Series morningSOCSeries = new XYChart.Series();
    private final XYChart.Series minExternalTempSeries = new XYChart.Series();
    private final XYChart.Series maxExternalTempSeries = new XYChart.Series();
    private final XYChart.Series maxLoadCurrentSeries = new XYChart.Series();
    private final XYChart.Series maxChargeCurrentSeries = new XYChart.Series();
    
    private final CategoryAxis xAxis = new CategoryAxis();
    private final NumberAxis yAxis = new NumberAxis();

    public CXNsolidPageController() {        
        
        //Set up all of the charts
        
        this.ampHours = new BarChart<>(xAxis,yAxis);
        this.pVVoltages = new BarChart<>(xAxis,yAxis);
        this.systemCurrents = new BarChart<>(xAxis,yAxis);
        this.morningSOC = new BarChart<>(xAxis,yAxis);
        this.externalTemp = new BarChart<>(xAxis,yAxis);
        this.batteryVoltages = new BarChart<String, Number>(xAxis,yAxis);
//        this.batteryVoltages.addEventHandler(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent e) {
//                System.out.println("Action Event BarChart!");
//            }
//        });
        this.xAxis.setLabel("Date");
        System.out.println("initialized CXNsolid page controller");
    }

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
    private void handleBatteryVoltageSelect(ActionEvent event) throws InterruptedException {        
        final String[][] dayData = this.solidDecryptor.getDayDecoded();
        final String[][] monthData = this.solidDecryptor.getMonthDecoded();
        Timeline tl = new Timeline();
        tl.getKeyFrames().add(new KeyFrame(Duration.millis(500), (ActionEvent actionEvent) -> {
            int i=0;
            int seriesCount=0;
            this.batteryVoltages.setTitle("Battery Voltages");        
            this.yAxis.setLabel("V");
            if (this.batteryVoltages.getData().size()==1) this.batteryVoltages.getData().add(this.displayedSeries2);
            for (XYChart.Series<String, Number> series : this.batteryVoltages.getData()) {
                if(seriesCount==0) {
                    i=0;
                    for (XYChart.Data<String, Number> data : series.getData()) {
                        data.setYValue(Float.parseFloat(dayData[i][2]));
                        i++;                        
                    }
                    series.setName("MAX");                  
                }else if(seriesCount==1) {
                    i=0;
                    for (XYChart.Data<String, Number> data : series.getData()) {
                        data.setYValue(Float.parseFloat(dayData[i][3]));
                        i++;
                    }
                    series.setName("MIN");
                }else if(seriesCount>1)     {
                    System.out.println("Too many series!");
                }
                seriesCount++;
            }
        }));
        tl.setCycleCount(1);
        tl.play();     
    }
    
    @FXML
    private void handleAmpHourSelect(ActionEvent event) throws InterruptedException {
        final String[][] dayData = this.solidDecryptor.getDayDecoded();
        final String[][] monthData = this.solidDecryptor.getMonthDecoded();
        Timeline tl = new Timeline();
        tl.getKeyFrames().add(new KeyFrame(Duration.millis(500), (ActionEvent actionEvent) -> {
            int i=0;
            int seriesCount=0;
            this.batteryVoltages.setTitle("Amp Hours In and Out");        
            this.yAxis.setLabel("Ah");
            if (this.batteryVoltages.getData().size()==1) this.batteryVoltages.getData().add(this.displayedSeries2);
            for (XYChart.Series<String, Number> series : this.batteryVoltages.getData()) {
                if(seriesCount==0) {
                    i=0;
                    for (XYChart.Data<String, Number> data : series.getData()) {
                        data.setYValue(Float.parseFloat(dayData[i][4]));
                        i++;                        
                    }
                    series.setName("Charged");
                    
                }else if(seriesCount==1) {
                    i=0;
                    for (XYChart.Data<String, Number> data : series.getData()) {
                        data.setYValue(Float.parseFloat(dayData[i][5]));
                        i++;
                    }
                    series.setName("Discharged");
                }else if(seriesCount>1) System.out.println("Too many series!");
                seriesCount++;
            }
        }));
        tl.setCycleCount(1);
        tl.play();       
    }
    
    @FXML
    private void handlePVVoltageSelect(ActionEvent event) throws InterruptedException {
        final String[][] dayData = this.solidDecryptor.getDayDecoded();
        final String[][] monthData = this.solidDecryptor.getMonthDecoded();
        Timeline tl = new Timeline();
        tl.getKeyFrames().add(new KeyFrame(Duration.millis(500), (ActionEvent actionEvent) -> {
            int i=0;
            int seriesCount=0;
            this.batteryVoltages.setTitle("Solar Array Voltages");        
            this.yAxis.setLabel("V");
            if (this.batteryVoltages.getData().size()==1) this.batteryVoltages.getData().add(this.displayedSeries2);
            for (XYChart.Series<String, Number> series : this.batteryVoltages.getData()) {
                if(seriesCount==0) {
                    i=0;
                    for (XYChart.Data<String, Number> data : series.getData()) {
                        data.setYValue(Float.parseFloat(dayData[i][6]));
                        i++;                        
                    }
                    series.setName("MAX");                  
                }else if(seriesCount==1) {
                    i=0;
                    for (XYChart.Data<String, Number> data : series.getData()) {
                        data.setYValue(Float.parseFloat(dayData[i][7]));
                        i++;
                    }
                    series.setName("MIN");
                }else if(seriesCount>1)     {
                    System.out.println("Too many series!");
                }
                seriesCount++;
            }
        }));
        tl.setCycleCount(1);
        tl.play();
    }
    
    @FXML
    private void handleSystemCurrentsSelect(ActionEvent event) throws InterruptedException {
        final String[][] dayData = this.solidDecryptor.getDayDecoded();
        final String[][] monthData = this.solidDecryptor.getMonthDecoded();
        Timeline tl = new Timeline();
        tl.getKeyFrames().add(new KeyFrame(Duration.millis(500), (ActionEvent actionEvent) -> {
            int i=0;
            int seriesCount=0;
            this.batteryVoltages.setTitle("Max Charge Current and Max Load Current");        
            this.yAxis.setLabel("A");
            if (this.batteryVoltages.getData().size()==1) this.batteryVoltages.getData().add(this.displayedSeries2);
            for (XYChart.Series<String, Number> series : this.batteryVoltages.getData()) {
                if(seriesCount==0) {
                    i=0;
                    for (XYChart.Data<String, Number> data : series.getData()) {
                        data.setYValue(Float.parseFloat(dayData[i][8]));
                        i++;                        
                    }
                    series.setName("Charge");                  
                }else if(seriesCount==1) {
                    i=0;
                    for (XYChart.Data<String, Number> data : series.getData()) {
                        data.setYValue(Float.parseFloat(dayData[i][9]));
                        i++;
                    }
                    series.setName("Load");
                }else if(seriesCount>1)     {
                    System.out.println("Too many series!");
                }
                seriesCount++;
            }
        }));
        tl.setCycleCount(1);
        tl.play();
    }
    
    @FXML
    private void handleSOCSelect(ActionEvent event) throws InterruptedException {
        final String[][] dayData = this.solidDecryptor.getDayDecoded();
        final String[][] monthData = this.solidDecryptor.getMonthDecoded();
        Timeline tl = new Timeline();
        tl.getKeyFrames().add(new KeyFrame(Duration.millis(500), (ActionEvent actionEvent) -> {
            int i=0;
            int seriesCount=0;            
            this.batteryVoltages.setTitle("State of Charge Percentage");        
            this.yAxis.setLabel("%");       
            for (XYChart.Series<String, Number> series : this.batteryVoltages.getData()) {
                if(seriesCount==0) {
                    i=0;
                    for (XYChart.Data<String, Number> data : series.getData()) {
                        data.setYValue(Float.parseFloat(dayData[i][10].replaceAll("%", "")));
                        i++;                        
                    }
                    series.setName("SOC");                  
                }else if(seriesCount==1) {
                    i=0;
                    for (XYChart.Data<String, Number> data : series.getData()) {
                        data.setYValue(0);
                        i++;                        
                    }
                }else if(seriesCount>1)     {
                    System.out.println("Too many series!");
                }
                seriesCount++;
            }
            this.batteryVoltages.getData().remove(this.displayedSeries2.getData());
        }));        
        tl.setCycleCount(1);
        tl.play();
    }
    
    @FXML
    private void handleTemperatureSelect(ActionEvent event) throws InterruptedException {
        final String[][] dayData = this.solidDecryptor.getDayDecoded();
        final String[][] monthData = this.solidDecryptor.getMonthDecoded();
        Timeline tl = new Timeline();
        tl.getKeyFrames().add(new KeyFrame(Duration.millis(500), (ActionEvent actionEvent) -> {
            int i=0;
            int seriesCount=0;
            this.batteryVoltages.setTitle("External Temperature Max and Min Degrees Celsius");        
            this.yAxis.setLabel("°C");
            if (this.batteryVoltages.getData().size()==1) this.batteryVoltages.getData().add(this.displayedSeries2);
            for (XYChart.Series<String, Number> series : this.batteryVoltages.getData()) {
                if(seriesCount==0) {
                    i=0;
                    for (XYChart.Data<String, Number> data : series.getData()) {
                        data.setYValue(Float.parseFloat(dayData[i][11].replaceAll("°C", "")));
                        i++;                        
                    }
                    series.setName("MAX");                    
                }else if(seriesCount==1) {
                    i=0;
                    for (XYChart.Data<String, Number> data : series.getData()) {
                        data.setYValue(Float.parseFloat(dayData[i][12].replaceAll("°C", "")));
                        i++;
                    }
                    series.setName("MIN");
                }else if(seriesCount>1)     {
                    System.out.println("Too many series!");
                }
                
                seriesCount++;
            }
            
        }));
        tl.setCycleCount(1);
        tl.play();
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
        String[][] dayData = new String[31][15];
        String[][] monthData = new String[24][15];
        
        this.solidDecryptor = new CXNsolidDataDecryptor();  
        this.solidDecryptor.decryptCurrentValues();
        this.solidDecryptor.decryptDataLogger();
        
        //Load the Current Values
        this.currentBatteryVoltage.setText(String.valueOf(this.solidDecryptor.getBatteryVoltage()));
        this.currentStateOfCharge.setText(String.valueOf(this.solidDecryptor.getSOCPercent()));
        this.currentChargeCurrent.setText(String.valueOf(this.solidDecryptor.getChargeCurrent()));
        this.currentLoadCurrent.setText(String.valueOf(this.solidDecryptor.getLoadCurrent()));
//        this.currentTodaysEnergy.setText(String.valueOf(this.solidDecryptor.ge));
//        this.currentBatteryChargingState.setText(String.valueOf(this.solidDecryptor.getBatteryChargingState));
        this.currentLoadState.setText(String.valueOf(this.solidDecryptor.getLoadState()));
        this.currentTemperature.setText(String.valueOf(this.solidDecryptor.getExternalTemp()));
       
        
        //Load the DataLogger Charts
        int i;
        int j;
        dayData = this.solidDecryptor.getDayDecoded();
        monthData = this.solidDecryptor.getMonthDecoded();
        for (i=0;i<31;i++)  {
           if (!dayData[i][0].equals("200-0-0"))    {
                this.batteryMaxSeries.getData().add(new XYChart.Data<>(dayData[i][0], Float.parseFloat(dayData[i][2])));
                this.batteryMaxSeries.setName("Max");
                System.out.println(dayData[i][0]+Float.parseFloat(dayData[i][2]));
                this.batteryMinSeries.getData().add(new XYChart.Data<>(dayData[i][0], Float.parseFloat(dayData[i][3])));
                this.batteryMinSeries.setName("Min");
                System.out.println(dayData[i][0]+ Float.parseFloat(dayData[i][3]));
                this.chargeAmpHoursSeries.getData().add(new XYChart.Data<>(dayData[i][0], Float.parseFloat(dayData[i][4])));
                this.chargeAmpHoursSeries.setName("Charging");
                System.out.println(dayData[i][0]+ Float.parseFloat(dayData[i][4]));
                this.loadAmpHoursSeries.getData().add(new XYChart.Data<>(dayData[i][0], Float.parseFloat(dayData[i][5])));
                this.loadAmpHoursSeries.setName("Discharging");
                this.pVMaxSeries.getData().add(new XYChart.Data<>(dayData[i][0], Float.parseFloat(dayData[i][6])));
                this.pVMaxSeries.setName("Max");
                this.pVMinSeries.getData().add(new XYChart.Data<>(dayData[i][0], Float.parseFloat(dayData[i][7])));
                this.pVMinSeries.setName("Min");
                this.maxLoadCurrentSeries.getData().add(new XYChart.Data<>(dayData[i][0], Float.parseFloat(dayData[i][8])));
                this.maxLoadCurrentSeries.setName("Discharge");
                this.maxChargeCurrentSeries.getData().add(new XYChart.Data<>(dayData[i][0], Float.parseFloat(dayData[i][9])));
                this.maxChargeCurrentSeries.setName("Charge");
                this.morningSOCSeries.getData().add(new XYChart.Data<>(dayData[i][0], Float.parseFloat(dayData[i][10].replace("%", ""))));
                this.morningSOCSeries.setName("State of Charge");
                this.maxExternalTempSeries.getData().add(new XYChart.Data<>(dayData[i][0], Float.parseFloat(dayData[i][11].replace("°C",""))));
                this.maxExternalTempSeries.setName("Max");
                this.minExternalTempSeries.getData().add(new XYChart.Data<>(dayData[i][0], Float.parseFloat(dayData[i][12].replace("°C",""))));
                this.minExternalTempSeries.setName("Min");
            }
        }
        //Still need to convert monthData        
        this.displayedSeries1.setData(this.batteryMaxSeries.getData());
        this.displayedSeries2.setData(this.batteryMinSeries.getData());
        this.batteryVoltages.getData().addAll(this.displayedSeries1, this.displayedSeries2);
        this.batteryVoltages.setTitle("Battery Voltages");        
        this.yAxis.setLabel("Volts");
        this.yAxis.setAnimated(true);
        this.xAxis.setLabel("Date");
        this.xAxis.setAnimated(true);
        
        
        
        
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
        //Set week chart as default
        int i=0;
        if( !this.barChartStackPane.getChildren().isEmpty() ) {
            this.barChartStackPane.getChildren().removeAll();
        }         
        this.barChartStackPane.getChildren().setAll(
                this.batteryVoltages);
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
