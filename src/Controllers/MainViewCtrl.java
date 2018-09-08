package Controllers;

import Models.Table;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.File;


public class MainViewCtrl {


    @FXML private TableView tableViewOne;
    @FXML private TableView tableViewTwo;
    @FXML private TableColumn nameOne;
    @FXML private TableColumn sizeOne;
    @FXML private TableColumn nameTwo;
    @FXML private TableColumn sizeTwo;
    @FXML private TextField textOne;
    @FXML private TextField textTwo;

    private Table tableOne;
    private Table tableTwo;
    private String defaultPath = System.getProperty("user.home") + "\\"+"Desktop";

    private boolean tableOneFocused = false;
    private boolean tableTwoFocused = false;


    @FXML public void initialize(){
        this.tableOne = new Table(this.tableViewOne,this.nameOne,this.sizeOne,this.textOne,this.defaultPath);
        this.tableTwo = new Table(this.tableViewTwo,this.nameTwo,this.sizeTwo,this.textTwo,this.defaultPath);
        this.tableOne.setTwo(this.tableTwo);
        this.tableTwo.setTwo(this.tableOne);
    }


    @FXML private void enteredPathOne(KeyEvent event){
        if(event.getCode().equals(KeyCode.ENTER)){
            this.tableOne.updatePath(this.textOne.getText());
        }
    }

    @FXML private void enteredPathTwo(KeyEvent event){
        if(event.getCode().equals(KeyCode.ENTER)){
            this.tableTwo.updatePath(this.textTwo.getText());
        }
    }

    @FXML private void returnAction(){
        if(this.tableOneFocused){
            File file = new File(this.tableOne.getCurrentPath());
            this.tableOne.updatePath(file.getParent());
        } else if(this.tableTwoFocused){
            File file = new File(this.tableTwo.getCurrentPath());
            this.tableTwo.updatePath(file.getParent());
        }
    }
    @FXML private void selectedFocusOne(){
        this.tableOneFocused = true;
        this.tableTwoFocused = false;
    }
    @FXML private void selectedFocusTwo(){
        this.tableTwoFocused = true;
        this.tableOneFocused = false;
    }
    @FXML private void copyFile(){
        if(this.tableOneFocused){
            tableOne.copyFile(tableTwo.getCurrentPath());
            tableTwo.updatePath(tableTwo.getCurrentPath());
        } else if(this.tableTwoFocused){
            tableTwo.copyFile(tableOne.getCurrentPath());
            tableOne.updatePath(tableOne.getCurrentPath());
        }
    }
    @FXML private void moveFile(){
        if(this.tableOneFocused){
            tableOne.moveFile(tableTwo.getCurrentPath());
            tableTwo.updatePath(this.textTwo.getText());
            tableOne.updatePath(tableOne.getCurrentPath());
        } else if(this.tableTwoFocused){
            tableTwo.moveFile(tableOne.getCurrentPath());
            tableOne.updatePath(this.textOne.getText());
            tableTwo.updatePath(tableTwo.getCurrentPath());
        }
    }
    @FXML private void searchFile(){
        if(this.tableOneFocused){
            tableOne.searchFile(this.textOne.getText());
        } else if(this.tableTwoFocused){
            tableTwo.searchFile(this.textTwo.getText());
        }
    }
    @FXML private void createDirectory(){
        if(this.tableOneFocused){
            tableOne.createDirectory(this.textOne.getText());
        } else if(this.tableTwoFocused){
            tableTwo.createDirectory(this.textTwo.getText());
        }
    }
    @FXML private void changePaths(){
        if(this.textOne.getText().equals(this.textTwo.getText())){
            MainCtrl.SendMessage(Alert.AlertType.INFORMATION,"Уведомление!",null,"Пути совпадают!");
        }else {
            String buff;
            buff = this.textOne.getText();
            this.textOne.setText(this.textTwo.getText());
            this.textTwo.setText(buff);
            this.tableOne.updatePath(this.textOne.getText());
            this.tableTwo.updatePath(buff);
        }
    }
}
