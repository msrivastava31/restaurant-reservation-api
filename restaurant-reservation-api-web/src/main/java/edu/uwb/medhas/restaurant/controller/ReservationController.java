/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uwb.medhas.restaurant.controller;

import java.io.IOException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwb.medhas.restaurant.service.ReservationServiceLocal;
import edu.uwb.medhas.restaurant.model.Reservation;
import edu.uwb.medhas.restaurant.service.dto.Error;
import java.util.regex.Pattern;

/**
 *
 * @author Medha Srivastava <medhas31@uw.edu>
 */
@WebServlet(name = "ReservationController", urlPatterns = {"/reservation/*"})
public class ReservationController extends HttpServlet {
    Pattern idPattern = Pattern.compile("/reservation/([a-zA-Z0-9-]+)");
    
    @EJB
    private ReservationServiceLocal reservationService;
    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Extract the reservation id from the request url
        String reservationId = ControllerUtils.extractId(request.getRequestURL().toString(), idPattern);
        
        //check if reservation ID is null, if yes, send error
        if (reservationId == null) {
            Error error = new Error(400, "Bad request, please check your request body and request parameters");
            ControllerUtils.sendError(error, response);
        }
        
        try {
            //get the reservation object corresponding to the reservation Id
            Reservation reservation = reservationService.getReservation(reservationId);
            ControllerUtils.sendResponse(reservation, response);
        } catch (EJBException ejbex) {
            Error error = new Error(400, ejbex.getMessage());
            ControllerUtils.sendError(error, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Reservation reservation = null;
        
        try {
            //get the HTTP request body and convert the JSON request to Reservation object
            String requestBody = ControllerUtils.getBody(request);
            reservation = ControllerUtils.convertJSONToObject(requestBody, Reservation.class);
        } catch (IOException ioex) {
            System.out.println(ioex);
        } finally {
            if (reservation == null) {
                Error error = new Error(400, "Bad request, please check your request body and request parameters");
                ControllerUtils.sendError(error, response);
            }
        }
        
        try {
            //create Reservation
            reservationService.createReservation(reservation);
            ControllerUtils.sendResponse(reservation, response);
        } catch (EJBException ejbex) {
            Error error = new Error(400, ejbex.getMessage());
            ControllerUtils.sendError(error, response);
        }
    }

    /**
     * Handles the HTTP <code>PUT</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Reservation reservation = null;
        String reservationId = null;
        try {
            //Extract the reservation id from the request url
            reservationId = ControllerUtils.extractId(request.getRequestURL().toString(), idPattern);
            
            //get the HTTP request body and convert the JSON request to Reservation object
            String requestBody = ControllerUtils.getBody(request);
            reservation = ControllerUtils.convertJSONToObject(requestBody, Reservation.class);
            
            reservation.setId(reservationId);
        } catch (IOException ioex) {
            System.out.println(ioex);
        } finally {
            if (reservation == null || reservationId == null) {
                Error error = new Error(400, "Bad request, please check your request body and request parameters");
                ControllerUtils.sendError(error, response);
            }
        }
        
        try {
            //update resrvation details
            reservationService.updateReservation(reservation);
            ControllerUtils.sendResponse(reservation, response);
        } catch (EJBException ejbex) {
            Error error = new Error(400, ejbex.getMessage());
            ControllerUtils.sendError(error, response);
        }
    }

    /**
     * Handles the HTTP <code>DELETE</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reservationId = ControllerUtils.extractId(request.getRequestURL().toString(), idPattern);
        
        //check if reservation ID is null, if yes, send error
        if (reservationId == null) {
            Error error = new Error(400, "Bad request, please check your request body and request parameters");
            ControllerUtils.sendError(error, response);
        }
        
        try {
            //Delete reservation from DynamoDB
            reservationService.cancelReservation(reservationId);
        } catch (EJBException ejbex) {
            Error error = new Error(400, ejbex.getMessage());
            ControllerUtils.sendError(error, response);
        }
    }
    
    

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet to Create, Update or Cancel a Reservation";
    }// </editor-fold>
}
