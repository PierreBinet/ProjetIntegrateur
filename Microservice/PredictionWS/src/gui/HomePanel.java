package gui;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.ws.rs.core.Response;

import controller.RessourceManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import ws.flux.PredictionFluxWS;

public class HomePanel extends Application {
	
	private RessourceManager manager;
	private PredictionFluxWS fluxWS;
	public HomePanel(){}
	
	public HomePanel(RessourceManager manager){
		this.manager = manager;
	}
	
    @Override
    public void start(Stage stage) {

        initUI(stage);
        manager = new RessourceManager();
    }

    private void initUI(Stage stage) {
    	Pane root = new Pane();
       
        root.setPadding(new Insets(25));
        
    	// Button -------------------
    	Button btn = new Button();
        btn.setOnAction((ActionEvent event) -> {
        	BufferedImage image;
			try {
				image = manager.GETFluxRequest(2, 3184, "/home/katran/Bureau/5SDBD/ProjetIntegrateur/RExtractor/output/JC-201811-citibike-tripdata.csv");
				Image image1 = SwingFXUtils.toFXImage(image, null);
			     ImageView imageView = new ImageView();
			     imageView.setImage(image1);
			HBox hbox = new HBox(imageView);

			Scene scene = new Scene(hbox, 1000, 550);

	        
			stage.setScene(scene);
			stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
        
        btn.setText("OK");
        btn.setWrapText(true);
        btn.setPrefSize(80, 50);
        btn.setLayoutX(250);
        btn.setLayoutY(274);

        root.getChildren().add(btn);
        

        // Create combo boxes and labels -------------------
        // Days 
        Label label_days = new Label("Days");
        label_days.setLayoutX(10);
        label_days.setLayoutY(136);
        root.getChildren().add(label_days); 
        String days[] = { "Monday", "Tuesday", "Wednesday", "Thrusday", "Friday" }; 
        ComboBox combo_box_days = new ComboBox(FXCollections.observableArrayList(days)); 
        combo_box_days.setLayoutX(10);
        combo_box_days.setLayoutY(159);
        root.getChildren().add(combo_box_days);
       	
       	// Months 
        Label label_months = new Label("Months");
        label_months.setLayoutX(140);
        label_months.setLayoutY(136);
        root.getChildren().add(label_months);  
        String months[] = { "January","February","March","March","April","May","June","July","August","September","October","November","December" }; 
        ComboBox combo_box_months = new ComboBox(FXCollections.observableArrayList(months)); 
        combo_box_months.setLayoutX(140);
        combo_box_months.setLayoutY(159);
        root.getChildren().add(combo_box_months);    
        
       	// Year
        Label label_year = new Label("Year");
        label_year.setLayoutX(265);
        label_year.setLayoutY(136); 
        root.getChildren().add(label_year);  
        String years[] = {"2018","2019"}; 
        ComboBox combo_box_years = new ComboBox(FXCollections.observableArrayList(years)); 
        combo_box_years.setLayoutX(265);
        combo_box_years.setLayoutY(159);
        root.getChildren().add(combo_box_years);  
        
       	// Stations ID
        Label label_stations = new Label("Stations");
        label_stations.setLayoutX(424);
        label_stations.setLayoutY(136); 
        root.getChildren().add(label_stations);  
        String stations[] = {"3184","3185","3186"}; 
        ComboBox combo_box_stations = new ComboBox(FXCollections.observableArrayList(stations)); 
        combo_box_stations.setLayoutX(424);
        combo_box_stations.setLayoutY(159);
        root.getChildren().add(combo_box_stations);  
       	
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Flow App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    // Pour d�poyer sur le web : 
    // https://stackoverflow.com/questions/19102000/javafx-can-it-really-be-deployed-in-a-browser
}