/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.utp.pralki.entities;

import java.awt.Color;
import java.sql.Time;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 *
 * @author Darek
 */
@AllArgsConstructor
@NoArgsConstructor
public class Timetable {
    private Time time;
    private SpecialReservation resToday;
    private SpecialReservation resToday1;
    private SpecialReservation resToday2;
    private SpecialReservation resToday3;
    private SpecialReservation resToday4;
    private SpecialReservation resToday5;
    private SpecialReservation resToday6;
    private SpecialReservation resToday7;
    private SpecialReservation resToday8;

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public SpecialReservation getResToday() {
        return resToday;
    }

    public void setResToday(SpecialReservation resToday) {
        this.resToday = resToday;
    }

    public SpecialReservation getResToday1() {
        return resToday1;
    }

    public void setResToday1(SpecialReservation resToday1) {
        this.resToday1 = resToday1;
    }

    public SpecialReservation getResToday2() {
        return resToday2;
    }

    public void setResToday2(SpecialReservation resToday2) {
        this.resToday2 = resToday2;
    }

    public SpecialReservation getResToday3() {
        return resToday3;
    }

    public void setResToday3(SpecialReservation resToday3) {
        this.resToday3 = resToday3;
    }

    public SpecialReservation getResToday4() {
        return resToday4;
    }

    public void setResToday4(SpecialReservation resToday4) {
        this.resToday4 = resToday4;
    }

    public SpecialReservation getResToday5() {
        return resToday5;
    }

    public void setResToday5(SpecialReservation resToday5) {
        this.resToday5 = resToday5;
    }

    public SpecialReservation getResToday6() {
        return resToday6;
    }

    public void setResToday6(SpecialReservation resToday6) {
        this.resToday6 = resToday6;
    }

    public SpecialReservation getResToday7() {
        return resToday7;
    }

    public void setResToday7(SpecialReservation resToday7) {
        this.resToday7 = resToday7;
    }

    public SpecialReservation getResToday8() {
        return resToday8;
    }

    public void setResToday8(SpecialReservation resToday8) {
        this.resToday8 = resToday8;
    }
    
    
}
