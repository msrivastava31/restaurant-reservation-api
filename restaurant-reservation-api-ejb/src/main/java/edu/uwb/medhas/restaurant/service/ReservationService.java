/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uwb.medhas.restaurant.service;

import com.amazonaws.services.dynamodbv2.document.Item;
import edu.uwb.medhas.restaurant.model.Reservation;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.EJBException;

/**
 *
 * @author Medha Srivastava <medhas31@uw.edu>
 */
@Stateless
public class ReservationService implements ReservationServiceLocal {

    private static final String TABLE_NAME = "reservation";

    @EJB
    private AmazonDynamoDBService amazonDynamoDBService;

    /**
     * Update/Insert a reservation entity to Amazon's Dynamo DB
     *
     * @param reservation - The entity
     * @param type - The type of operation, insert or update
     */
    private void upsertReservationItem(Reservation reservation, String type) {
        // create an item from reservation object
        Item item = new Item()
                .withPrimaryKey("id", reservation.getId())
                .withString("name", reservation.getName())
                .withString("date", reservation.getDate())
                .withInt("numGuests", reservation.getNumGuests())
                .withString("time", reservation.getTime());
        try {
            //insert reservation 
            amazonDynamoDBService.putItem(item, TABLE_NAME);
        } catch (RuntimeException rex) {
            System.out.println(rex);
            throw new EJBException("Could not " + type + " reservation in Amazon DB");
        }
    }
    
    /**
     * Get the Reservation Item from Amazon DB
     * 
     * @param id - the reservation id
     * @return item - reservation details corresponsing to the id
     */
    private Item getReservationItem(String id) {
        Item item = null;
        try {
            //get the Reservation Item from Amazon DB
            item = amazonDynamoDBService.getItem(id, "id", TABLE_NAME);
        } catch (RuntimeException rex) {
            System.out.println(rex);
            throw new EJBException("Error getting item from Amazon DB");
        }

        // Check if item was not found
        if (item == null) {
            throw new EJBException("Reservation with id: " + id + " does not exist");
        }
        
        return item;
    }

    /**
     * Creates a new reservation
     *
     * @param reservation - The entity
     */
    @Override
    public synchronized void createReservation(Reservation reservation) {
        // Generate a unique reservation Id
        UUID uuid = UUID.randomUUID();
        String reservationId = uuid.toString();
        
        //set the generated reservation Id
        reservation.setId(reservationId);
        
        //insert reservation in DynamoDB
        upsertReservationItem(reservation, "create");
    }

    /**
     * Updates an existing reservation
     *
     * @param reservation - the entity
     */
    @Override
    public synchronized void updateReservation(Reservation reservation) {
        //check if reservation exists
        getReservationItem(reservation.getId());
        upsertReservationItem(reservation, "update");
    }

    /**
     * Gets the reservation details based on Id
     *
     * @param reservationId - The id of the reservation
     * @return The Reservation Entity
     */
    @Override
    public synchronized Reservation getReservation(String reservationId) {
        // get reservation item from DynamoDB
        Item item = getReservationItem(reservationId);
        
        try {
            Reservation reservation = new Reservation();
            // set reservation details from item
            reservation.setId(reservationId);
            reservation.setName(item.getString("name"));
            reservation.setDate(item.getString("date"));
            reservation.setNumGuests(item.getInt("numGuests"));
            reservation.setTime(item.getString("time"));
            
            //return this reservation object 
            return reservation;
        } catch (RuntimeException rex) {
            System.out.println(rex);
            throw new EJBException("Could not get reservation from Amazon DB");
        }
    }

    /**
     * Deletes the reservation based on Id
     * 
     * @param reservationId - The id of the reservation
     */
    @Override
    public synchronized void cancelReservation(String reservationId) {
        //check if reservation exists
        getReservationItem(reservationId);
        try {
            //delete reservation
            amazonDynamoDBService.deleteItem(reservationId, "id", TABLE_NAME);
        } catch (RuntimeException rex) {
            System.out.println(rex);
            throw new EJBException("Error deleting item from Amazon DB");
        }
    }
}
