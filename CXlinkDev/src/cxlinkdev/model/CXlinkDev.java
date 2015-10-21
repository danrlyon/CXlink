/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cxlinkdev.model;

import cxlinkdev.*;

import java.io.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.imageio.*;

/**
 *
 * @author Dell-N7110
 */
public class CXlinkDev extends Application {
    
    //Variables for global use
    public static final String space = " ";
    public static final String exclamation = "!";
    
    //FXML files and IDs for each screen
    public static String screen1ID = "main";
    public static String screen1File = "IntroPage.fxml";
    public static String screen2ID = "cxpage";
    public static String screen2File = "CXPage.fxml";
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
       
        //Loads the 3 different screen options. Uncomment for more screens
        ScreensController mainContainer = new ScreensController();
        mainContainer.loadScreen(CXlinkDev.screen1ID, CXlinkDev.screen1File);
        mainContainer.loadScreen(CXlinkDev.screen2ID, CXlinkDev.screen2File);
//        mainContainer.loadScreen(CXlinkDev.screen3ID, CXlinkDev.screen3File);
//        mainContainer.loadScreen(CXlinkDev.screen4ID, CXlinkDev.screen4File); 
        
        //Set the IntroPage
        mainContainer.setScreen(CXlinkDev.screen1ID);        
        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        Scene scene = new Scene(root);
        primaryStage.setTitle("CXlink");
        
        //Checks that logo is properly loaded, then loads it
        FileInputStream cxLinkLogo = null; 
        try {
            cxLinkLogo = new FileInputStream("C:/Users/Dell-N7110/repos/CXlink/CXlinkDev/src/cxlinkdev/images/CXLINK-simple.png");
        }   catch (FileNotFoundException ex)    {
                System.out.println("FileNotFoundException caught");
        }            
        primaryStage.getIcons().add(new Image(cxLinkLogo));
        
        //Set the Stage with the IntroPage
        primaryStage.setScene(scene);
        primaryStage.show(); 
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
