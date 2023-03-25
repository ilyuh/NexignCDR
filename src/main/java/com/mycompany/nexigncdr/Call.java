/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.nexigncdr;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Ilyuh
 */
public class Call {
    byte type, tariff;
    String number;
    LocalDateTime start, end;
    
    private DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    Call(String call) {
        type = Byte.parseByte(call.substring(0, 2)); // 01 - исходящие, 02 - входящие
        number = call.substring(4, 15);
        start = LocalDateTime.parse(call.substring(17, 31), inputFormatter);
        end = LocalDateTime.parse(call.substring(33, 47), inputFormatter);
        tariff = Byte.parseByte(call.substring(49, 51));
    }
    
    private LocalTime duration() {
        return LocalTime.ofSecondOfDay((int) Duration.between(start, end).toSeconds());
    }
    
    public String getStart() {
        return this.start.format(outputFormatter);
    }
    public String getEnd() {
        return this.end.format(outputFormatter);
    }
    public String getDuration() {
        return this.duration().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
    public int getFullMinutes() {
        return this.duration().toSecondOfDay()/60 + (this.duration().getSecond()==0?0:1); // округляет в большую сторону
    }
    
    public float getCost(int prevMins) {
        float cost = 0;
        int totalMins = prevMins + this.getFullMinutes();
        
        switch (tariff) {
            case 03: // поминутный
                cost = this.getFullMinutes() * 1.5f;
                break;
            case 06: // безлимит 300
                if (prevMins >= 300)
                    cost = this.getFullMinutes() * 1f;
                else if (totalMins >= 300)
                    cost = (totalMins - 300) * 1f;
                break;
            case 11: // обычный
                if (this.type == 02)
                    break;
                if (prevMins >= 100)
                    cost = this.getFullMinutes() * 1.5f;
                else if (totalMins >= 100)
                    cost = (totalMins - 100) * 1.5f + (this.getFullMinutes() - totalMins + 100) * 0.5f;
                else
                    cost = this.getFullMinutes() * 0.5f;
                break;
            default:
                throw new AssertionError();
        }
        return cost;
    }
}
