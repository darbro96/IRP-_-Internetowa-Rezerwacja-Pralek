/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.utp.pralki.entities;

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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "washer")
public class Washer {
    @Column(name = "id_washer")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer idWasher;
    @Column(name="no_washer")
    private int noWasher;
    @OneToOne
    @JoinColumn(name="laundry_id_laundry")
    private Laundry laundry;

    public Integer getIdWasher() {
        return idWasher;
    }

    public void setIdWasher(Integer idWasher) {
        this.idWasher = idWasher;
    }

    public int getNoWasher() {
        return noWasher;
    }

    public void setNoWasher(int noWasher) {
        this.noWasher = noWasher;
    }

    public Laundry getLaundry() {
        return laundry;
    }

    public void setLaundry(Laundry laundry) {
        this.laundry = laundry;
    }
    
    
}
