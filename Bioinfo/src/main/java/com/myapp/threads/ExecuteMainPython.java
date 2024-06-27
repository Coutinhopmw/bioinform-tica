package com.myapp.threads;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ExecuteMainPython implements Runnable {
    private String pythonScriptPath;
    private String outputFilePath;
    private static final String PYTHON_EXECUTABLE = "python3";

    public ExecuteMainPython() {
        pythonScriptPath = "/Bioinfo/src/main/java/com/myapp/main.py"; 
        outputFilePath = "/Bioinfo/src/main/java/com/myapp/resultado.txt";
    }

    @Override
    public void run() {
        try {
            File outputFile = new File(outputFilePath);
            FileWriter fileWriter = new FileWriter(outputFile, true);

            ProcessBuilder runProcessBuilder = new ProcessBuilder(PYTHON_EXECUTABLE, pythonScriptPath);
            runProcessBuilder.redirectErrorStream(true);
            Process runProcess = runProcessBuilder.start();

            Scanner runOutputStream = new Scanner(runProcess.getInputStream()).useDelimiter("\\A");
            String runOutput = runOutputStream.hasNext() ? runOutputStream.next() : "";
            fileWriter.write(runOutput);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
