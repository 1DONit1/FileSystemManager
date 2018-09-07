package Models;

import Controllers.MainCtrl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.awt.Desktop;


public class Table {

    private ObservableList<FileInDir> filesInDir = FXCollections.observableArrayList();
    private TableView<FileInDir> tableView;
    private TableColumn<FileInDir,String>fileName;
    private TableColumn<FileInDir,String>fileSize;
    private String currentPath;
    private TextField textField;

    public Table(TableView tableView,TableColumn nameColumn, TableColumn sizeColumn, TextField textField, String currentPath){
        this.fileName = nameColumn;
        this.fileSize = sizeColumn;
        this.tableView = tableView;
        this.currentPath = currentPath;
        this.textField = textField;
        this.initTable();
        this.addEvent();
        this.updatePath(this.currentPath);
        this.tableView.setPlaceholder(new Label("Пусто!"));
    }

    private void initTable(){
        this.fileName.setCellValueFactory(cellData->cellData.getValue().getFileName());
        this.fileSize.setCellValueFactory(cellData->cellData.getValue().getFileSize());
    }

    private void initFiles(){
            File dir = new File(this.currentPath);
            List<File> files = Arrays.asList(dir.listFiles());
            files.forEach((a) -> {
                this.filesInDir.add(new FileInDir(a));
            });
    }

    public void updatePath(String newPath){
        try {
            this.currentPath = newPath;
            this.filesInDir.clear();
            this.initFiles();
            this.tableView.setItems(this.filesInDir);
            this.textField.setText(this.currentPath);
        }catch (Exception e){
            MainCtrl.SendMessage(Alert.AlertType.ERROR,"Ошибка!","Ошибка обработки!","Введён неверный путь!!"+"\n"+this.currentPath+"\n"+"Детали: "+e.getMessage());
        }
    }

    private void addEvent(){
        this.tableView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    FileInDir clickedFile = tableView.getSelectionModel().getSelectedItem();
                    if(clickedFile.isDirectory()){
                        updatePath(clickedFile.getFilePath());
                    }else{
                        Desktop desktop = Desktop.getDesktop();
                        try {
                            desktop.open(new File(clickedFile.getFilePath()));
                        }catch (IOException e){
                            MainCtrl.SendMessage(Alert.AlertType.ERROR,"Ошибка!","Ошибка открытия файла!","Файл не поддерживается!"+"\n"+currentPath+"\n"+"Детали: "+e.getMessage());
                        }
                    }
                }
            }
        });
        this.tableView.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.DELETE)){
                    try {
                        File file = new File(tableView.getSelectionModel().getSelectedItem().getFilePath());
                        if (file.isDirectory()) {
                            FileUtils.deleteDirectory(file);
                        } else {
                            FileUtils.deleteQuietly(file);
                        }
                        updatePath(currentPath);
                    }catch (IOException e){
                        MainCtrl.SendMessage(Alert.AlertType.ERROR,"Ошибка!","Ошибка удаления файла или директории!","Произошла ошибка при удалении файла или директории!"+"\n"+"Детали: "+e.getMessage());
                    }
                }
            }
        });
    }
    public void copyFile(String newPath){
        try{
            if(!tableView.getSelectionModel().isEmpty()){
                File selectedFile = new File(this.tableView.getSelectionModel().getSelectedItem().getFilePath());
                File newFile = new File(newPath);
                FileUtils.copyToDirectory(selectedFile,newFile);
            }
        }catch (IOException e){
            MainCtrl.SendMessage(Alert.AlertType.ERROR,"Ошибка!","Ошибка копирования файла или директории!","Произошла ошибка при копировании файла или директории в выбранное местоназначения!"+"\n"+"Детали: "+e.getMessage());
        }
    }

    public void  moveFile(String newPath){
        try{
            if(!tableView.getSelectionModel().isEmpty()){
                File selectedFile = new File(this.tableView.getSelectionModel().getSelectedItem().getFilePath());
                File newFile = new File(newPath);
                if(selectedFile.isDirectory()){
                    FileUtils.moveDirectoryToDirectory(selectedFile,newFile,true);
                } else{
                    FileUtils.moveFileToDirectory(selectedFile,newFile,true);
                }
            }
        }catch (IOException e){
            MainCtrl.SendMessage(Alert.AlertType.ERROR,"Ошибка!","Ошибка перемещения файла или директории!","Произошла ошибка при перемещении файла или директории в выбранное местоназначения!"+"\n"+"Детали: "+e.getMessage());
        }
    }
    public String getCurrentPath() {
        return currentPath;
    }

    public void searchFile(String nameFile){
        tableView.getItems().stream()
                .filter(item-> item.getFilename().equals(nameFile))
                .findAny().
                ifPresent(item->{
                    tableView.getSelectionModel().select(item);
                });
    }

    public void createDirectory(String name){
        try{
            File file = new File(this.currentPath+"/"+name);
            FileUtils.forceMkdir(file);
            this.updatePath(this.currentPath);
        }catch (Exception e){
            MainCtrl.SendMessage(Alert.AlertType.ERROR,"Ошибка!","Ошибка создания директории!","Произошла ошибка при создании директории в выбранном местоназначении!"+"\n"+"Детали: "+e.getMessage());
        }
    }

}
