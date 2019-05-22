/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.utp.pralki.entities;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 *
 * @author Darek
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="dormitory")
public class Dormitory {

    @Column(name = "id_dormitory")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer idDormitory;
    @Column(name = "name")
    private String name;
    
    
    public Integer getIdDormitory() {
        return idDormitory;
    }

    public void setIdDormitory(Integer idDormitory) {
        this.idDormitory = idDormitory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
