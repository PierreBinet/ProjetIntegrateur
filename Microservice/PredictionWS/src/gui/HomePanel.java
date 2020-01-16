package gui;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.ws.rs.core.Response;

import controller.RessourceManager;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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

    	Button btn = new Button();
        btn.setText("GetInt");
        btn.setOnAction((ActionEvent event) -> {
        	BufferedImage image;
			try {
				image = manager.MyGETRequest();
				Image image1 = SwingFXUtils.toFXImage(image, null);
			     ImageView imageView = new ImageView();
			     imageView.setImage(image1);
			HBox hbox = new HBox(imageView);

			Scene scene = new Scene(hbox, 200, 100);
			stage.setScene(scene);
			stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });

        HBox root = new HBox();
        root.setPadding(new Insets(25));
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 280, 200);

        stage.setTitle("Int button");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    // Pour d�poyer sur le web : 
    // https://stackoverflow.com/questions/19102000/javafx-can-it-really-be-deployed-in-a-browser
}