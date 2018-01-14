/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uwb.medhas.restaurant.service;

import edu.uwb.medhas.restaurant.model.Reservation;
import javax.ejb.Local;

/**
 *
 * @author Medha Srivastava <medhas31@uw.edu>
 */
@Local
public interface ReservationServiceLocal {
    public void createReservation(Reservation reservation);
    public void updateReservation(Reservation reservation);
    public Reservation getReservation(String reservationId);
    public void cancelReservation(String reservationId);
}
