/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sctg.sudocodetranspiler.Utility;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

/**
 *
 * @author My Laptop
 */
public class FetchFileString {
    public static String getFile(Path filePath){
        
        try {
        String content = Files.readString(filePath, StandardCharsets.UTF_8);
        System.out.println("Contents:\n" + content + "\n\n");
        return content;
        } catch (NoSuchFileException e){
            System.out.println("Error: File is not found\nCheck Location: " + filePath);
        } catch (IOException e){
            System.out.println(e);
        }
        return "";
    }
}
