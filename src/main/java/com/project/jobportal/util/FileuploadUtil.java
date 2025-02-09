package com.project.jobportal.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileuploadUtil {

    public static void savefile(String uploaddir, String filename, MultipartFile multipartFile) throws IOException{
        Path uploadpath= Paths.get(uploaddir);
        if(!Files.exists(uploadpath)){
            Files.createDirectories(uploadpath);
        }

        try(InputStream inputStream=multipartFile.getInputStream();){
            Path path=uploadpath.resolve(filename);
            System.out.println("File name: "+filename);
            System.out.println("file path: "+path);
            Files.copy(inputStream,path, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException ioe) {
            throw new IOException("could not save image file: "+filename,ioe);
        }
    }
}
