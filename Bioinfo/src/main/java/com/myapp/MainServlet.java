package com.myapp;

import com.myapp.threads.ExecuteCppProgram;
import com.myapp.threads.ExecuteJavaProgram;
import com.myapp.threads.ExecutePythonScript;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/linkstart")
public class MainServlet extends HttpServlet {
private ArrayList<String> seqList = new ArrayList<>();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        String seq = request.getParameter("seq");
        String action = request.getParameter("adicionar") != null ? "adicionar" : "resultado";

        if ("adicionar".equals(action)) {
            seqList.add(seq);
            response.sendRedirect(request.getContextPath() + "/index.html");
        } else if ("resultado".equals(action)) {
            session.setAttribute("seqList", seqList); // Salva a lista na sessão
            
            for(int i = 0;i<seqList.size();i++){
                Thread pythonNW = new Thread(new ExecutePythonScript(i,seqList.get(i),"NW"));
                Thread pythonSW = new Thread(new ExecutePythonScript(i,seqList.get(i),"SW"));
                pythonNW.start();
                pythonSW.start();
                
                Thread javaNW = new Thread(new ExecuteJavaProgram(i,seqList.get(i),"NW"));
                Thread javaSW = new Thread(new ExecuteJavaProgram(i,seqList.get(i),"SW"));
                javaNW.start();
                javaSW.start();
                
                Thread cppNW = new Thread(new ExecuteCppProgram(i,seqList.get(i),"NW"));
                Thread cppSW = new Thread(new ExecuteCppProgram(i,seqList.get(i),"SW"));
                cppNW.start();
                cppSW.start();
                
            }
            response.sendRedirect(request.getContextPath() + "/result.jsp");
        }
    }
}