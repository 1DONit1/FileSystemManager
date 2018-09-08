package Models;

import Controllers.MainCtrl;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import org.apache.commons.io.FileUtils;


import java.io.File;


public class ActionService extends Service<Void> {

    private ProgressIndicator progressIndicator = new ProgressIndicator();
    private String action;
    private File source;
    private File dest;
    private Table one;
    private Table two;
    private Dialog dialog;

    public ActionService(File source,File dest,String action,Table one,Table two){
        this.source = source;
        this.dest = dest;
        this.action = action;
        this.one = one;
        this.two = two;
        this.initService();
    }
    private void updateTable(){
        Platform.runLater(()->{
            this.one.updatePath(this.one.getCurrentPath());
            this.two.updatePath(this.two.getCurrentPath());
        });
    }
    private void initService(){
        this.progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        this.progressIndicator.progressProperty().bind(this.workDoneProperty());
        this.dialog = new Dialog();
        this.dialog.setHeaderText("Операция выполняется ожидайте...");
        Platform.runLater(()->{
            this.dialog.getDialogPane().getContent().setStyle("-fx-padding: 5%");
        });
        this.dialog.getDialogPane().setContent(this.progressIndicator);
        this.dialog.getDialogPane().getButtonTypes().add(new ButtonType("Прервать", ButtonBar.ButtonData.OK_DONE));
        this.dialog.setTitle("Ожидайте..");
        this.dialog.setResultConverter((a)->{
            this.cancel();
            return null;
        });
        this.setOnSucceeded(success->{
                this.updateTable();
                this.dialog.close();
                MainCtrl.SendMessage(Alert.AlertType.INFORMATION,"Успешно!","Операция завершена успешно!",null);
                this.cancel();
        });
        this.setOnCancelled(cancel->{
            this.updateTable();
            this.dialog.close();
            MainCtrl.SendMessage(Alert.AlertType.INFORMATION,"Операция прервана!","Операция была прервана!",null);
            this.cancel();
        });
        this.setOnFailed(fail->{
                this.updateTable();
                this.getException().printStackTrace();
                this.dialog.close();
                MainCtrl.SendMessage(Alert.AlertType.ERROR,"Ошибка","При выполнении операции произошла ошибка!",getException().toString());
                this.cancel();
        });
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(()->{
                    dialog.show();
                });
                switch (action) {
                    case "Move": {
                        if (source.isDirectory()) {
                            FileUtils.moveDirectoryToDirectory(source, dest, true);
                        } else {
                            FileUtils.moveFileToDirectory(source, dest, true);
                        }
                    }
                    break;
                    case "Copy":{
                        FileUtils.copyToDirectory(source,dest);
                    }
                    break;
                    case "Delete":{
                        if (source.isDirectory()) {
                            FileUtils.deleteDirectory(source);
                        } else {
                            FileUtils.deleteQuietly(source);
                        }
                    }
                    break;
                }
                return null;
            }
        };
    }
}
