/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uwb.medhas.restaurant.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import edu.uwb.medhas.restaurant.config.ApplicationConfig;
import java.io.IOException;
/**
 *
 * @author Medha Srivastava <medhas31@uw.edu>
 */
@Singleton
@LocalBean
public class AmazonDynamoDBService {
    private final AmazonDynamoDB client; //AWS DynamoDB Client
    private final DynamoDB dynamoDB ; // DynamoDB Instance

    /**
     * Instantiates the DynamoDB client using credentials (Access Key ID, Secret Access Key);
     * Fetches an instance of DynamoDB using client
     */
    public AmazonDynamoDBService() throws IOException {
        final ApplicationConfig applicationConfig = ApplicationConfig.getInstance();
        final String accessId = applicationConfig.getProperty("aws.access.key");
        final String accessSecret = applicationConfig.getProperty("aws.access.secret");
        
        final BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessId, accessSecret);
        client = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.US_EAST_2)
                .build();
        dynamoDB = new DynamoDB(client);
    }

    /**
     * Gets an item from a given table in Amazon DynamoDb
     * 
     * @param id - the key value of the table
     * @param key - the key name of the table
     * @param tableName - the name of the table
     * @return - the retrieved row
     */
    public synchronized Item getItem(String id, String key, String tableName) {
        final Table table = dynamoDB.getTable(tableName);
        return table.getItem(key, id);
    }
    
    /**
     * Puts an item to a given table in Amazon DynamoDb
     * 
     * @param item - the row to put to the table
     * @param tableName - the table name to put the row to
     */
    public synchronized void putItem(Item item, String tableName) {
        final Table table = dynamoDB.getTable(tableName);
        table.putItem(item);
    }
    
    /**
     * Deletes an item from a given table in Amazon DynamoDb
     * 
     * @param id - the key value of the table
     * @param key - the key name of the table
     * @param tableName - the name of the table
     */
    public synchronized void deleteItem(String id, String key, String tableName) {
        final Table table = dynamoDB.getTable(tableName);
        table.deleteItem(key, id);
    }
}
