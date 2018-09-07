package Models;

import javafx.beans.property.SimpleStringProperty;

import java.io.File;

public class FileInDir {

    private final SimpleStringProperty fileName = new SimpleStringProperty();
    private SimpleStringProperty fileSize = new SimpleStringProperty();
    private String filePath;
    private String filename;

    public FileInDir(File file){
        if(file.isDirectory()){
            this.setFileName("\uD83D\uDCC2 "+file.getName());
            this.setFilename(file.getName());
        } else{
            this.setFileName(file.getName());
            this.setFilename(file.getName());
            this.convertSize(file.length());
        }
        this.setFilePath(file.getPath());
    }

    public String getFilePath() {
        return filePath;
    }

    public SimpleStringProperty getFileSize() {
        return fileSize;
    }

    public SimpleStringProperty getFileName() {
        return fileName;
    }

    private void setFileName(String fileName){
        this.fileName.set(fileName);
    }

    private void setFileSize(String fileSize){
        this.fileSize.set(fileSize);
    }

    private void setFilePath(String filePath){
        this.filePath= filePath;
    }

    private long folderSize(File directory) {
        long length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file);
        }
        return length;
    }

    private void convertSize(Long Size){
        if(Size > 1073741824){
            this.setFileSize(String.valueOf(Size/1073741824)+" ГБайт");
        } else if(Size>1048576){
            this.setFileSize(String.valueOf(Size/1048576)+ " Мбайт");
        }  else if(Size > 1024){
            this.setFileSize(String.valueOf(Size/1024)+ " Кбайт");
        } else {
            this.setFileSize(String.valueOf(Size)+ " Байт");
        }
    }

    public boolean isDirectory(){
        File file = new File(this.filePath);
        return file.isDirectory();
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
