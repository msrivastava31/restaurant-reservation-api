/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uwb.medhas.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwb.medhas.restaurant.service.dto.Error;

/**
 *
 * @author Medha Srivastava <medhas31@uw.edu>
 */
public class ControllerUtils {
    /**
     * Gets the body of the request as String
     * 
     * @param request
     * @return String
     * @throws IOException 
     */
    public static String getBody(HttpServletRequest request) throws IOException {
        return request.getReader().lines().collect(Collectors.joining());
    }
    
    /**
     * Extracts the id from the url
     * 
     * @param url
     * @param idPattern
     * @return String
     */
    public static String extractId(String url, Pattern idPattern) {
        // create matcher for pattern p and given string
        Matcher m = idPattern.matcher(url);
        String id = null;
        // if an occurrence of the pattern was found in a given string...
        if (m.find()) {
            id = m.group(1);
        }
        return id;
    }
    
    
    /**
     * Convert a JSON String to an Object
     * 
     * @param <O> - The class type of the object
     * @param json - The JSON string
     * @param clazz - The class of the object
     * @return
     * @throws IOException 
     */
    public static <O> O convertJSONToObject(String json, Class<O> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, clazz);
    }
    
    /**
     * Convert an object to JSON String
     * 
     * @param obj
     * @return
     * @throws IOException 
     */
    public static String convertObjectToJSON(Object obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
    
    /**
     * Send a response back to the client
     * 
     * @param obj
     * @param response
     * @throws IOException 
     */
    public static void sendResponse(Object obj, HttpServletResponse response) throws IOException {
        String json = convertObjectToJSON(obj);
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter pw = response.getWriter()) {
            pw.println(json);
        }
    }
    
    /**
     * Send an error response
     * 
     * @param error
     * @param response
     * @throws IOException 
     */
    public static void sendError(Error error, HttpServletResponse response) throws IOException {
        response.setStatus(error.getId());
        sendResponse(error, response);
    }
}
