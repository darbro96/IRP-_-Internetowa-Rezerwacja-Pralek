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
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="laundry")
public class Laundry {
    @Column(name = "id_laundry")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer idLaundry;
    @Column(name="name")
    private String name;
    @OneToOne
    @JoinColumn(name="dormitory_id_dormitory")
    private Dormitory dormitory;

    public Integer getIdLaundry() {
        return idLaundry;
    }

    public void setIdLaundry(Integer idLaundry) {
        this.idLaundry = idLaundry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Dormitory getDormitory() {
        return dormitory;
    }

    public void setDormitory(Dormitory dormitory) {
        this.dormitory = dormitory;
    }
    
    
}
