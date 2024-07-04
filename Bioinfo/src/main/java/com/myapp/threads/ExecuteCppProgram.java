package com.myapp.threads;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ExecuteCppProgram implements Runnable {

    private String cppFilePath;
    private String outputFilePath;
    private String seq1;
    private String seq2;
    private String cppFileName;

    public ExecuteCppProgram(ArrayList<String> seqList, String cppFileName) {
        this.seq1 = seqList.get(0);
        this.seq2 = seqList.get(1);
        this.cppFileName = cppFileName;

        // Define o caminho do arquivo C++ com base no nome do arquivo
        if ("NW".equals(cppFileName)) {
            // Se não estiver usando dockerlembre de atulizar seu caminho:
            cppFilePath = "/Bioinfo/src/main/java/codes/cpp/NW.cpp";
            outputFilePath = "/Bioinfo/src/main/java/respostas/NW/resultado_c_plus.txt";
        
        } else if ("SW".equals(cppFileName)) {
            // Se não estiver usando docker lembre de atulizar seu caminho:
            cppFilePath = "/Bioinfo/src/main/java/codes/cpp/SW.cpp";
            outputFilePath = "/Bioinfo/src/main/java/respostas/SW/resultado_c_plus_SW.txt";
        }
    }

    @Override
    public void run() {
        try {
            // Diretório de saída para o arquivo executável
            // Se não estiver usando docker lembre de atulizar seu caminho:
            String outputDir = "/Bioinfo/src/main/java/codes/cpp";
            String exeFilePath = outputDir + "/" + cppFileName + "_executable.exe";

            // Compila o arquivo C++
            if ("NW".equals(cppFileName)) {
                System.out.println("Cpp    NW executando o arquivo...");
            } else if ("SW".equals(cppFileName)) {
                System.out.println("Cpp    SW executando o arquivo...");
            }

            ProcessBuilder compileProcessBuilder = new ProcessBuilder("g++", "-o", exeFilePath, cppFilePath);
            Process compileProcess = compileProcessBuilder.start();
            
            // Captura a saída de erro da compilação
            Scanner compileErrorStream = new Scanner(compileProcess.getErrorStream()).useDelimiter("\\A");
            int compileExitCode = compileProcess.waitFor();
            if (compileExitCode != 0) {
                if ("NW".equals(cppFileName)) {
                    System.out.println("Cpp    NW Erro na compilacao. Codigo de retorno: " + compileExitCode);
               } else if ("SW".equals(cppFileName)) {
                  System.out.println("Cpp    SW Erro na compilacao. Codigo de retorno: " + compileExitCode);
               }
               compileErrorStream.close();
               return;
            }
            if ("NW".equals(cppFileName)) {
                System.out.println("Cpp    NW compilacao bem-sucedida.");
            } else if ("SW".equals(cppFileName)) {
                System.out.println("Cpp    SW compilacao bem-sucedida.");
            }

            // Cria um arquivo para armazenar a saída
            File outputFile = new File(outputFilePath);
            FileWriter fileWriter = new FileWriter(outputFile, true);

            ProcessBuilder runProcessBuilder = new ProcessBuilder(exeFilePath, seq1, seq2);
            runProcessBuilder.redirectErrorStream(true);

            Process runProcess = runProcessBuilder.start();
            int runExitCode = runProcess.waitFor();

            Scanner runOutputStream = new Scanner(runProcess.getInputStream()).useDelimiter("\\A");
            String runOutput = runOutputStream.hasNext() ? runOutputStream.next() : "";
            fileWriter.write(runOutput);

            if (runExitCode != 0) {
                // System.out.println("Erro na execuçao. Codigo de retorno: " + runExitCode);
            } else {
                // System.out.println("Programa C++ executado com sucesso.");
            }
            compileErrorStream.close();
            runOutputStream.close();
            fileWriter.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}