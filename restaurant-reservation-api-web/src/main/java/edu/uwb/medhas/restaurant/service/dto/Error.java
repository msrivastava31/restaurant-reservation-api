/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uwb.medhas.restaurant.service.dto;

/**
 *
 * @author Medha Srivastava <medhas31@uw.edu>
 */
public class Error {
    private final int id; // error id
    private final String message; //error message

    public Error(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
