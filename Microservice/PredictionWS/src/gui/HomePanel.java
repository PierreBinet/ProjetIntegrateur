package gui;


import java.io.IOException;

import controller.RessourceManager;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class HomePanel extends Application {
	
	private RessourceManager manager;
	
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
            try {
            	System.out.println(manager.MyGETRequest());
				
			} catch (IOException e) {
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
    
    // Pour dépoyer sur le web : 
    // https://stackoverflow.com/questions/19102000/javafx-can-it-really-be-deployed-in-a-browser
}