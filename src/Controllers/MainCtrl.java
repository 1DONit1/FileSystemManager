package Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class MainCtrl {

    private double MinWidth = 900;
    private double MinHeight = 700;
    private String Title = "RFMan";
    private String ViewPath = "/MyViews/MainView.fxml";



    private Stage MainStage;
    private Parent MainParent;
    private Scene MainScene;


    public static void SendMessage(Alert.AlertType alertType,String Title, String Header, String Content){
        Alert MyAlert = new Alert(alertType,Content);
        MyAlert.setHeaderText(Header);
        MyAlert.setTitle(Title);
        MyAlert.showAndWait();
    }

    public MainCtrl(Stage MainStage) throws Exception{
        this.MainStage = MainStage;
        this.initStage();
        this.setProperties();
        this.MainStage.show();
    }

    private void initStage(){
        try {
            FXMLLoader loader = new FXMLLoader(MainCtrl.class.getResource(this.ViewPath));
            this.MainParent = loader.load();
            this.MainScene = new Scene(this.MainParent);
        }catch (Exception e){
            SendMessage(Alert.AlertType.ERROR,"TEST","TEST",MainCtrl.class.getResource(this.ViewPath).toString());
        }
    }

    private void setProperties(){
        this.MainStage.getIcons().add(new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/5/54/RSATU_logo.svg/1200px-RSATU_logo.svg.png"));
        this.MainStage.setMinWidth(this.MinWidth);
        this.MainStage.setMinHeight(this.MinHeight);
        this.MainStage.setTitle(this.Title);
        this.MainStage.setScene(this.MainScene);
    }






}
