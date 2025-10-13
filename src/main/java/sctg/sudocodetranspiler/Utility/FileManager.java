package sctg.sudocodetranspiler.Utility;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileManager {
    public static void FinalizeFile(Path filePath, String output) throws IOException{
        try (OutputStream out = Files.newOutputStream(filePath)){
            out.write(output.getBytes());
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
