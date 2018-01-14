/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uwb.medhas.restaurant.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author medhasrivastava
 */
@Entity
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    // variable declaration
    @Id
    private String id; //Reservation Id
    private String name; // name on the Reservation
    private String date; // date of reservation
    private int numGuests; //Number of guests for the reservation
    private String time; // dinner time for the reservation
    
    //Getters & Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }    

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public int getNumGuests() {
        return numGuests;
    }
    public void setNumGuests(int numGuests) {
        this.numGuests = numGuests;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int hashCode() {
        return (id != null) ? id.hashCode() : 31;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.id == null && other.id != null) 
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uwb.medhas.restaurant.model.Reservation[ id=" + id + " ]";
    }
    
}
