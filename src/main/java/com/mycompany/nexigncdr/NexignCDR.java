/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.nexigncdr;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;

/**
 *
 * @author Ilyuh
 */
public class NexignCDR {

    static String cdrFileName = "cdr.txt";

    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileReader FR = new FileReader(cdrFileName);
        char [] buffer = new char[52];
        
        Report report;
        Call call;
        
        HashMap<String, Report> reports = new HashMap<>();
        
        while (FR.read(buffer) > -1) {
            call = new Call(String.valueOf(buffer));
            
            if (reports.containsKey(call.number))
                reports.get(call.number).addCall(call);
            else {
                report = new Report();
                report.addCall(call);
                reports.put(call.number, report);
            }
            
            buffer = new char[52];
        }
        
        for (Report r : reports.values()) {
            FileOutputStream FOS = new FileOutputStream("reports/".concat(r.number).concat(".txt"), false);
            r.print(new PrintStream(FOS, true));
            FOS.close();
        }
    }
}
