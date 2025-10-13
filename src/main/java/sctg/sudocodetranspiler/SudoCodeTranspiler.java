/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package sctg.sudocodetranspiler;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import sctg.sudocodetranspiler.Utility.*;
import sctg.sudocodetranspiler.Lexer.*;
import sctg.sudocodetranspiler.Parser.Parser;

public class SudoCodeTranspiler {

    public static void main(String[] args) throws IOException {
        Path relativePath = Path.of(System.getProperty("user.dir"));
        Path readFilePath = Path.of(relativePath.toString(), "Program.sc");
        Path writeFilePath = Path.of(relativePath.toString(), "Output.java");
        
        String content = FetchFileString.getFile(readFilePath);
        if (content.isBlank()){
            System.out.println("The File is Missing or Empty");
            return;
        }
        
        LexicalAnalyzer lexer = new LexicalAnalyzer(content);
        lexer.tokenize();
        lexer.PrintTokens();
        System.out.println("\n\n");
                
        Parser parser = new Parser(lexer.getTokens());
        String outputJava = parser.transpile();
        System.out.println(outputJava);
        
        FileManager.FinalizeFile(writeFilePath, outputJava);
        compileAndRunOutput(writeFilePath);
    }
    
    private static void compileAndRunOutput(Path outputJavaPath) {
        try {
            System.out.println("\n--- Compiling Output.java ---");
            ProcessBuilder compileBuilder = new ProcessBuilder(
                "javac", outputJavaPath.toString()
            );
            compileBuilder.redirectErrorStream(true);
            Process compileProcess = compileBuilder.start();
            Scanner compileScanner = new Scanner(compileProcess.getInputStream());
            while (compileScanner.hasNextLine()) {
                String line = compileScanner.nextLine();
                System.out.println(line);
            }
            compileScanner.close();
            int compileExitCode = compileProcess.waitFor();
            if (compileExitCode != 0) {
                System.err.println("Compilation failed with exit code: " + compileExitCode);
                return;
            }
            System.out.println("Compilation successful!");

            System.out.println("\n--- Running Output.java ---");
            ProcessBuilder runBuilder = new ProcessBuilder(
                "java", "-cp", outputJavaPath.getParent().toString(), "Output"
            );
            runBuilder.redirectErrorStream(true);
            Process runProcess = runBuilder.start();
            Scanner runScanner = new Scanner(runProcess.getInputStream());
            while (runScanner.hasNextLine()) {
                String line = runScanner.nextLine();
                System.out.println(line);
            }
            runScanner.close();
            int runExitCode = runProcess.waitFor();
            System.out.println("\nExecution completed with exit code: " + runExitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
