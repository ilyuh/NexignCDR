package com.mycompany.nexigncdr;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Ilyuh
 */
public class Report {
    byte tariff = 0;
    String number;
    ArrayList<Call> calls = new ArrayList<>();
    
    void addCall(Call call) {
        if (this.tariff == 0) {
            this.tariff = call.tariff;
            this.number = call.number;
        }            
        if (call.number.equals(this.number)) {
            this.calls.add(call);
        }
    }
    
    private void sortCallsByDateTime() {
        Comparator<Call> c = (c1, c2) -> {
            if (c1.start.isBefore(c2.start))
                return -1;
            if (c1.start.isAfter(c2.start))
                return 1;
            else
                return 0;
        };
        this.calls.sort(c);
    }
    
    private void sortCallsByType() {
        Comparator<Call> c = (c1, c2) -> {
            if (c1.type < c2.type)
                return -1;
            if (c1.type > c2.type)
                return 1;
            else
                return 0;
        };
        this.calls.sort(c);
    }
    
    public void print(PrintStream ps) {
        sortCallsByDateTime();
        //sortCallsByType();
        
        float totalCost = 0;
        if (this.tariff == 06)
            totalCost += 100;
        
        int prevMins = 0;
        String callReport = "|     %02d    | %s | %s | %s |%6.2f |\n";
        
        ps.printf("Tariff index: %02d\n", this.tariff);
        ps.print("----------------------------------------------------------------------------\n");
        ps.printf("Report for phone number %s:\n", this.number);
        ps.print("----------------------------------------------------------------------------\n");
        ps.print("| Call Type |   Start Time        |     End Time        | Duration | Cost  |\n");
        ps.print("----------------------------------------------------------------------------\n");
        for (Call call : calls) {
            ps.printf(callReport, call.type, call.getStart(), call.getEnd(), call.getDuration(), call.getCost(prevMins));
            totalCost += call.getCost(prevMins);
            if (this.tariff == 11 && call.type == 02)
                continue;
            else
                prevMins += call.getFullMinutes();
        }
        ps.print("----------------------------------------------------------------------------\n");
        ps.printf("|                                           Total Cost: |%10.2f rubles |\n", totalCost);
        ps.print("----------------------------------------------------------------------------\n");
        
        ps.close();
    }
}
