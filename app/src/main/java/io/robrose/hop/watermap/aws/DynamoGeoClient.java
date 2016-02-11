package io.robrose.hop.watermap.aws;

import com.amazonaws.geo.model.GetPointRequest;
import com.amazonaws.geo.model.GetPointResult;
import com.amazonaws.geo.model.QueryRadiusRequest;
import com.amazonaws.geo.model.QueryRadiusResult;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

/**
 * Created by Robert C on 2/6/2016.
 */
/*
 * Copyright 2012-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.geo.GeoDataManager;
import com.amazonaws.geo.GeoDataManagerConfiguration;
import com.amazonaws.geo.model.GeoPoint;
import com.amazonaws.geo.model.PutPointRequest;
import com.amazonaws.geo.model.PutPointResult;
import com.amazonaws.geo.util.GeoTableUtil;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.Tables;

import io.robrose.hop.watermap.aws.util.Constants;
import io.robrose.hop.watermap.aws.util.WaterPin;
/**
 * This sample demonstrates how to perform a few simple operations with the
 * Amazon DynamoDB service.
 */
public class DynamoGeoClient {
    private static GeoDataManagerConfiguration config;
    private static GeoDataManager geoDataManager;
    private static String accessKey = "AKIAIK3X7CV2F2OP7MYQ";
    private static String secretKey = "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";
    private static boolean initialized = false;
    private static String tableName = "water-safe-locations-table";

    /*
     * Before running the code:
     *      Fill in your AWS access credentials in the provided credentials
     *      file template, and be sure to move the file to the default location
     *      (C:\\Users\\Robert\\.aws\\credentials) where the sample code will load the
     *      credentials from.
     *      https://console.aws.amazon.com/iam/home?#security_credential
     *
     * WARNING:
     *      To avoid accidental leakage of your credentials, DO NOT keep
     *      the credentials file in your source directory.
     */

    static AmazonDynamoDBClient dynamoDB;

    /**
     * The only information needed to create a client are security credentials
     * consisting of the AWS Access Key ID and Secret Access Key. All other
     * configuration, such as the service endpoints, are performed
     * automatically. Client parameters, such as proxies, can be specified in an
     * optional ClientConfiguration object when constructing a client.
     *
     * @see com.amazonaws.auth.BasicAWSCredentials
  //   * @see com.amazonaws.auth.ProfilesConfigFile
     * @see com.amazonaws.ClientConfiguration
     */


    public static void init()  {
        if(initialized)
            return;

        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (C:\\Users\\Robert\\.aws\\credentials).
         */

        AWSCredentials credentials;
        try {
            credentials =new  BasicAWSCredentials( accessKey, secretKey);
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (C:\\Users\\Robert\\.aws\\credentials), and is in valid format.",
                    e);
        }
        dynamoDB = new AmazonDynamoDBClient(credentials);
        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
        dynamoDB.setRegion(usEast1);
        config = new GeoDataManagerConfiguration(dynamoDB, tableName);
        geoDataManager = new GeoDataManager(config);


        initialized = true;
    }

    public static void main(String[] args) {
        init();

        try {
            config = new GeoDataManagerConfiguration(dynamoDB, tableName);
            geoDataManager = new GeoDataManager(config);
            // Create table if it does not exist yet
            if (Tables.doesTableExist(dynamoDB, tableName)) {
                System.out.println("Table " + tableName + " is already ACTIVE");
            } else {
                // Create a table with a primary hash key named 'name', which holds a string

            	/*
                CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
                    .withKeySchema(new KeySchemaElement().withAttributeName("name").withKeyType(KeyType.HASH))
                    .withAttributeDefinitions(new AttributeDefinition().withAttributeName("name").withAttributeType(ScalarAttributeType.S))
                    .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));
                    TableDescription createdTableDescription = dynamoDB.createTable(createTableRequest).getTableDescription();
                */
                //AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);



                CreateTableRequest createTableRequest = GeoTableUtil.getCreateTableRequest(config);
                CreateTableResult createTableResult = dynamoDB.createTable(createTableRequest);
                System.out.println("Created Table: " + tableName);

                // Wait for it to become active
                System.out.println("Waiting for " + tableName + " to become ACTIVE...");
                Tables.waitForTableToBecomeActive(dynamoDB, tableName);
            }

            // Describe our new table
            DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
            TableDescription tableDescription = dynamoDB.describeTable(describeTableRequest).getTable();
            System.out.println("Table Description: " + tableDescription);

            Map<String, String> m =new HashMap<>();
            m.put("lat", "51.5034070");
            m.put("lng", "-0.1275920");
            m.put("violationCode", "47" );

            // request = new JSONObject(m);
            //try {
            //    putPoint(request);
            //}catch(JSONException f){
            //    System.out.println("Bad JSON");
            //}
            // Scan items for movies with a year attribute greater than 1985
            HashMap<String, Condition> scanFilter = new HashMap<>();
            Condition condition = new Condition()
                    .withComparisonOperator(ComparisonOperator.GT.toString())
                    .withAttributeValueList(new AttributeValue().withN("1985"));
            scanFilter.put("year", condition);
            ScanRequest scanRequest = new ScanRequest(tableName);//.withScanFilter(scanFilter);
            ScanResult scanResult = dynamoDB.scan(scanRequest);
            System.out.println("Result: " + scanResult);

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to AWS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with AWS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
    /*
    private static void userPutPoint(String violCatCode, String contamCode, String violCode, double lat, double lng)  {
        GeoPoint userGeoPoint = new GeoPoint(mLastLocation.latitude, mLastLocation.longitude);//mLastLocation is a lat and long
        AttributeValue rangeKeyAttributeValue = new AttributeValue().withS(UUID.randomUUID().toString());
        AttributeValue testTypeAttributeValue = new AttributeValue().withS(testTypeStr);
        AttributeValue testResultAttributeValue = new AttributeValue().withS(testResultStr);

        PutPointRequest putPointRequest = new PutPointRequest(userGeoPoint, rangeKeyAttributeValue);
        putPointRequest.getPutItemRequest().addItemEntry("userTestType", testTypeAttributeValue);
        putPointRequest.getPutItemRequest().addItemEntry("userTestResult", testResultAttributeValue);

        PutPointResult putPointResult = userGeoDataManager.putPoint(putPointRequest);

        //printPutPointResult(putPointResult, out);
    }*/

    public static WaterPin getPoint(GeoPoint point, String UUID){
        AttributeValue attr= new AttributeValue().withS(UUID);
        GetPointResult result = geoDataManager.getPoint(new GetPointRequest(point, attr));
        return new WaterPin(result.getGetItemResult().getItem());
    }
    public static void insertPoint(double latitude, double longitude, String testType, String testResult){
        GeoPoint geoPoint = new GeoPoint(latitude, latitude);
        AttributeValue rangeKeyAttributeValue = new AttributeValue().withS(UUID.randomUUID().toString());
        AttributeValue testTypeAttributeValue = new AttributeValue().withS(testType);
        AttributeValue testResultAttributeValue = new AttributeValue().withS(testResult);

        PutPointRequest putPointRequest = new PutPointRequest(geoPoint, rangeKeyAttributeValue);
        putPointRequest.getPutItemRequest().addItemEntry("TEST_TYPE", testTypeAttributeValue);
        putPointRequest.getPutItemRequest().addItemEntry("TEST_RESULT", testResultAttributeValue);
        PutPointResult putPointResult = geoDataManager.putPoint(putPointRequest);
    }

    public  static List<WaterPin> getRadialPoints(GeoPoint point, double radius){
       QueryRadiusResult result= geoDataManager.queryRadius(new QueryRadiusRequest(point, radius));
        List<WaterPin> list = new ArrayList<>();
        List<Map<String, AttributeValue>> rawList =result.getItem();
        for (Map<String,AttributeValue> m: rawList) {
            list.add(new WaterPin(m));
        }
        return  list;
    }

}