/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.utp.pralki.entities;

import java.sql.Date;
import java.sql.Time;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 *
 * @author Darek
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="reservation")
public class Reservation {
    @Column(name = "id_reservation")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer idReservation;
    @OneToOne
    @JoinColumn(name="user_id_user")
    private User user;
    @OneToOne
    @JoinColumn(name="washer_id_washer")
    private Washer washer;
    @Column(name="date")
    private Date date;
    @Column(name="time_start")
    private Time timeStart;
    @Column(name="time_stop")
    private Time timeStop;

    public Integer getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(Integer idReservation) {
        this.idReservation = idReservation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Washer getWasher() {
        return washer;
    }

    public void setWasher(Washer washer) {
        this.washer = washer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Time timeStart) {
        this.timeStart = timeStart;
    }

    public Time getTimeStop() {
        return timeStop;
    }

    public void setTimeStop(Time timeStop) {
        this.timeStop = timeStop;
    }
    
    
}
