package Models;

import Controllers.MainCtrl;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;

public class Main extends Application {

    public static void main(String[] args){
        launch(args);
    }

    public void start(Stage MainStage) {
        try {
            MainCtrl main = new MainCtrl(MainStage);
        }catch (InvocationTargetException e){
            MainCtrl.SendMessage(Alert.AlertType.ERROR,"Ошибка запуска!","Произошла ошибка запуска!",e.getCause().getMessage());
        }catch(Exception a){
            MainCtrl.SendMessage(Alert.AlertType.ERROR,"Ошибка запуска!","Произошла ошибка запуска!",a.getCause().getMessage());
        }
    }

}
