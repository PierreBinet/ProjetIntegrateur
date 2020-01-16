package gui;


import java.awt.image.BufferedImage;

import java.io.IOException;



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
import javafx.stage.Stage;

public class HomePanel extends Application {
	
	private String URL = "/home/katran/Bureau/5SDBD/ProjetIntegrateur/RExtractor/output/JC-";
	private RessourceManager manager;
	private int station_id;
	private int day;
	private String month;
	private String year;	
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

        

        // Create combo boxes and labels -------------------
        // Days 
        Label label_days = new Label("Days");
        label_days.setLayoutX(10);
        label_days.setLayoutY(136);
        root.getChildren().add(label_days); 
        String days[] = { "Monday", "Tuesday", "Wednesday", "Thrusday", "Friday", "Saturday", "Sunday" }; 
        ComboBox combo_box_days = new ComboBox(FXCollections.observableArrayList(days)); 
        combo_box_days.setLayoutX(10);
        combo_box_days.setLayoutY(159);
        root.getChildren().add(combo_box_days);
        combo_box_days.setOnAction(e -> {
        	String day_string = combo_box_days.getValue().toString();
        
	        switch (day_string){
	        case "Monday": day = 1; break;
	        case "Tuesday": day = 2; break;
	        case "Wednesday": day = 3; break;
	        case "Thrusday": day = 4; break;
	        case "Friday": day = 5; break;
	        case "Saturday": day = 6;break;
	        case "Sunday": day = 7; break;
	        default : day = 1; break;
	        };
        });

       	// Months 
        Label label_months = new Label("Months");
        label_months.setLayoutX(140);
        label_months.setLayoutY(136);
        root.getChildren().add(label_months);  
        String months[] = { "January","February","March","April","May","June","July","August","September","October","November","December" }; 
        ComboBox combo_box_months = new ComboBox(FXCollections.observableArrayList(months)); 
        combo_box_months.setLayoutX(140);
        combo_box_months.setLayoutY(159);
        combo_box_months.setOnAction(e -> {
        	String month_string = combo_box_months.getValue().toString();
        
	        switch (month_string){
	        case "January": month = "01"; break;
	        case "February": month = "02"; break;
	        case "March": month = "03"; break;
	        case "April": month = "04"; break;
	        case "May": month = "05"; break;
	        case "June": month = "06";break;
	        case "July": month = "07"; break;
	        case "August": month = "08"; break;
	        case "September": month = "09"; break;
	        case "October": month = "10"; break;
	        case "November": month = "11"; break;
	        case "December": month = "12"; break;
	        default : month = "01"; break;
	        };
        });
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
        combo_box_years.setOnAction(e -> {
        	year = combo_box_years.getValue().toString();
        });
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
        combo_box_stations.setOnAction(e -> {
        station_id = Integer.parseInt(combo_box_stations.getValue().toString());

        });
        root.getChildren().add(combo_box_stations);  
       	
        
        
	     // Button -------------------
    	Button btn = new Button();
        btn.setOnAction((ActionEvent event) -> {
        	BufferedImage image;
			try {

				//System.out.println("DEBUG : day :"+day+" station : "+station_id);
				System.out.println("DEBUG : year :"+year+" month : "+month);
				image = manager.GETFluxRequest(day, station_id, URL+year+month+"-citibike-tripdata.csv");
				Image image1 = SwingFXUtils.toFXImage(image, null);
			    ImageView imageView = new ImageView();
			    imageView.setImage(image1);
				HBox hbox = new HBox(imageView);
	
				// New pane
				Scene scene = new Scene(hbox, 1000, 550);
				
				// New window
				Stage result = new Stage();
				result.setTitle("Result");
				result.setScene(scene);
				result.show();
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