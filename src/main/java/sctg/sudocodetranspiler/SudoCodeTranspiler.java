/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package sctg.sudocodetranspiler;
import java.io.IOException;
import java.io.InputStream;
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
    
    
    //THIS PART IS AI GENERATED HAHHAHAHAHAHA.
    private static void compileAndRunOutput(Path outputJavaPath) {
        try {
            // Compile phase (unchanged, but improved)
            System.out.println("\n--- Compiling " + outputJavaPath.getFileName() + " ---");
            Path classPath = outputJavaPath.getParent();
            String className = outputJavaPath.getFileName().toString().replace(".java", "");

            ProcessBuilder compileBuilder = new ProcessBuilder(
                "javac", outputJavaPath.toString()
            );
            compileBuilder.redirectErrorStream(true);
            compileBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE); // Explicit PIPE

            Process compileProcess = compileBuilder.start();

            // Non-blocking output reading
            Thread compileOutputThread = readProcessOutput(compileProcess.getInputStream(), "COMPILE");
            int compileExitCode = compileProcess.waitFor();
            compileOutputThread.join(); // Wait for output thread

            if (compileExitCode != 0) {
                System.err.println("Compilation failed with exit code: " + compileExitCode);
                return;
            }
            System.out.println("Compilation successful!");

            // Run phase with proper stream handling
            System.out.println("\n--- Running " + className + " ---");
            ProcessBuilder runBuilder = new ProcessBuilder(
                "java", "-cp", classPath.toString(), className
            );

            // CRITICAL: Explicit stream redirection
            runBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT);   // User can type input
            runBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);  // Direct to console
            runBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);   // Direct to console

            Process runProcess = runBuilder.start();

            // For INHERIT, we don't need to read getInputStream() - it goes directly to console
            // Just wait for process to complete
            int runExitCode = runProcess.waitFor();

            System.out.println("\nExecution completed with exit code: " + runExitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

// Helper method for non-blocking process output reading
    private static Thread readProcessOutput(InputStream processInput, String prefix) {
        Thread outputThread = new Thread(() -> {
            try (Scanner scanner = new Scanner(processInput)) {
                while (scanner.hasNextLine()) {
                    System.out.println("[" + prefix + "] " + scanner.nextLine());
                }
            } catch (Exception e) {
                // Stream closed or error - normal when process exits
            }
        });
        outputThread.setDaemon(true); // Don't prevent JVM exit
        outputThread.start();
        return outputThread;
    }
}
