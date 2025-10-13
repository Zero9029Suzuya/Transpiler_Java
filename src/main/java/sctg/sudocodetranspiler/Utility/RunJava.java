/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sctg.sudocodetranspiler.Utility;

import java.nio.file.Path;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 *
 * @author My Laptop
 */
public class RunJava {
    Path javaPath;
    
    public RunJava(Path javaPath){
        this.javaPath = javaPath;
    }
    
    public void Run(){
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        
    }
}
