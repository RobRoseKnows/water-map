
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.net.*;
import java.io.*;
import com.amazonaws.*;
import com.amazonaws.util.json.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.geo.GeoDataManager;
import com.amazonaws.geo.GeoDataManagerConfiguration;
import com.amazonaws.geo.model.GeoPoint;
import com.amazonaws.geo.model.PutPointRequest;
import com.amazonaws.geo.model.PutPointResult;
import com.amazonaws.geo.util.GeoTableUtil;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.amazonaws.services.dynamodbv2.util.Tables;

/**
 * This sample demonstrates how to perform a few simple operations with the
 * Amazon DynamoDB service.
 */
public class DatabaseBuilder {
	private static GeoDataManagerConfiguration config;
	private static GeoDataManager geoDataManager;
	/*
	 * Before running the code: Fill in your AWS access credentials in the
	 * provided credentials file template, and be sure to move the file to the
	 * default location (C:\\Users\\Robert\\.aws\\credentials) where the sample
	 * code will load the credentials from.
	 * https://console.aws.amazon.com/iam/home?#security_credential
	 *
	 * WARNING: To avoid accidental leakage of your credentials, DO NOT keep the
	 * credentials file in your source directory.
	 */

	static AmazonDynamoDBClient dynamoDB;
	private static String accessKey = "AKIAIK3X7CV2F2OP7MYQ";
	private static String secretKey = "YpwBdGgvn6cBR6RTHTCoZb3FlCAf7JRgF8arwV33";
	 

	/**
	 * The only information needed to create a client are security credentials
	 * consisting of the AWS Access Key ID and Secret Access Key. All other
	 * configuration, such as the service endpoints, are performed
	 * automatically. Client parameters, such as proxies, can be specified in an
	 * optional ClientConfiguration object when constructing a client.
	 *
	 * @see com.amazonaws.auth.BasicAWSCredentials
	 * @see com.amazonaws.auth.ProfilesConfigFile
	 * @see com.amazonaws.ClientConfiguration
	 */
	private static void init() throws Exception {
		/*
		 * The ProfileCredentialsProvider will return your [default] credential
		 * profile by reading from the credentials file located at
		 * (C:\\Users\\Robert\\.aws\\credentials).
		 */
		AWSCredentials credentials = null;
		try {
			credentials = new BasicAWSCredentials(accessKey, secretKey);
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (C:\\Users\\Robert\\.aws\\credentials), and is in valid format.", e);
		}
		dynamoDB = new AmazonDynamoDBClient(credentials);
		Region usEast1 = Region.getRegion(Regions.US_EAST_1);
		dynamoDB.setRegion(usEast1);
	}

	public static void main(String[] args) throws Exception {
		init();

		try {
			String tableName = "water-safe-location-table";
			config = new GeoDataManagerConfiguration(dynamoDB, tableName);
			geoDataManager = new GeoDataManager(config);
			// Create table if it does not exist yet
			try {
				TableUtils.waitUntilExists(dynamoDB, tableName, 2, 1);

				CreateTableRequest createTableRequest = GeoTableUtil.getCreateTableRequest(config);
				CreateTableResult createTableResult = dynamoDB.createTable(createTableRequest);
				System.out.println("Created Table: " + tableName);

				// Wait for it to become active
				System.out.println("Waiting for " + tableName + " to become ACTIVE...");
				TableUtils.waitUntilActive(dynamoDB, tableName);
			} catch (AmazonClientException e) {
				System.out.println("Table " + tableName + " is already ACTIVE");
				System.out.println(e);
			}

			// Describe our new table
			DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
			TableDescription tableDescription = dynamoDB.describeTable(describeTableRequest).getTable();
			System.out.println("Table Description: " + tableDescription);

			Map<String, String> m = new HashMap<String, String>();
			m.put("lat", "51.5034070");
			m.put("lng", "-0.1275920");
			m.put("VIOLATION_CATEGORY_CODE", "47");
			m.put("CONTAMINANT_CODE", "47");
			m.put("VIOLATION_CODE", "47");

			JSONObject request = new JSONObject(m);

			String urlTop = "http://iaspub.epa.gov//enviro//efservice//VIOLATION//WATER_SYSTEM//rows//";
			String urlBottom = "//json";
			getEPAData(urlTop, urlBottom);
			// putPoint(request);

			ScanRequest scanRequest = new ScanRequest(tableName);
			ScanResult scanResult = dynamoDB.scan(scanRequest);
			//System.out.println("Result: " + scanResult);

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

	private static void getEPAData(String url1, String url2) throws Exception {
		// TODO take data from epa api and read into json

		for (int i = 0; i < 2700000; i += 100){
			System.out.println("Test");
			String sapiURL = url1 + Integer.toString(i) + ":" + Integer.toString(i +99) + url2;
			URL apiURL = new URL(sapiURL);
			URLConnection con = apiURL.openConnection();
			try {
				BufferedReader bufcon = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inLine = bufcon.readLine();
				bufcon.close();
				JSONArray jaPull = new JSONArray(inLine);
				if (jaPull.length() > 0) {
					for (int j = 0; j < jaPull.length(); j++) {
						putPoint(jaPull.getJSONObject(j));
					}
				}
			} catch (java.io.FileNotFoundException e) {
				System.out.println("Ended at: " + Integer.toString(i));
				break;
			} catch (Exception f) {
				System.out.println(f);
				System.out.println("Ended at: " + Integer.toString(i));
				break;
			}
		}
	}

	private static JSONObject getZIPData(String url1, String url2) {
		// TODO take data from epa api and read into json

		String sapiURL = url1 + url2;
		URL apiURL = null;
		try {
			apiURL = new URL(sapiURL);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		URLConnection con = null;
		try {
			con = apiURL.openConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			BufferedReader bufcon = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inLine = bufcon.readLine();
			bufcon.close();
			JSONObject jaPull = new JSONObject(inLine).getJSONArray("places").optJSONObject(0);
			return jaPull;

		} catch (java.io.FileNotFoundException e) {
			// System.out.println("Ended at: "+Integer.toString(i));

		} catch (Exception f) {
			System.out.println(f);
			// System.out.println("Ended at: "+Integer.toString(i));

		}
		return null;
	}

	private static void putPoint(JSONObject requestObject) throws IOException, JSONException {
		String zip = requestObject.getJSONObject("WATER_SYSTEM").getJSONObject("WATER_SYSTEM_ROW")
				.getString("ZIP_CODE");
		JSONObject zipObject = getZIPData("http://api.zippopotam.us/us/", zip);
		GeoPoint geoPoint = new GeoPoint(zipObject.getDouble("latitude"), zipObject.getDouble("longitude"));
		AttributeValue rangeKeyAttributeValue = new AttributeValue().withS(UUID.randomUUID().toString());

		AttributeValue addressLine1AttributeValue = new AttributeValue().withS(requestObject
				.getJSONObject("WATER_SYSTEM").getJSONObject("WATER_SYSTEM_ROW").getString("ADDRESS_LINE1"));
		AttributeValue addressLine2AttributeValue = new AttributeValue().withS(requestObject
				.getJSONObject("WATER_SYSTEM").getJSONObject("WATER_SYSTEM_ROW").getString("ADDRESS_LINE2"));
		AttributeValue cityNameAttributeValue = new AttributeValue().withS(
				requestObject.getJSONObject("WATER_SYSTEM").getJSONObject("WATER_SYSTEM_ROW").getString("CITY_NAME"));
		AttributeValue countryCodeAttributeValue = new AttributeValue().withS(requestObject
				.getJSONObject("WATER_SYSTEM").getJSONObject("WATER_SYSTEM_ROW").getString("COUNTRY_CODE"));
		AttributeValue orgNameAttributeValue = new AttributeValue().withS(
				requestObject.getJSONObject("WATER_SYSTEM").getJSONObject("WATER_SYSTEM_ROW").getString("ORG_NAME"));
		AttributeValue outstandingPerformerAttributeValue = new AttributeValue().withS(requestObject
				.getJSONObject("WATER_SYSTEM").getJSONObject("WATER_SYSTEM_ROW").getString("OUTSTANDING_PERFORMER"));
		AttributeValue stateCodeAttributeValue = new AttributeValue().withS(
				requestObject.getJSONObject("WATER_SYSTEM").getJSONObject("WATER_SYSTEM_ROW").getString("STATE_CODE"));
		AttributeValue sourceWaterProtectionCodeAttributeValue = new AttributeValue()
				.withS(requestObject.getJSONObject("WATER_SYSTEM").getJSONObject("WATER_SYSTEM_ROW")
						.getString("SOURCE_WATER_PROTECTION_CODE"));
		AttributeValue zipCodeAttributeValue = new AttributeValue().withS(
				requestObject.getJSONObject("WATER_SYSTEM").getJSONObject("WATER_SYSTEM_ROW").getString("ZIP_CODE"));

		AttributeValue violMeasureAttributeValue = new AttributeValue().withS(requestObject.getString("VIOL_MEASURE"));
		AttributeValue violationIdAttributeValue = new AttributeValue().withS(requestObject.getString("VIOLATION_ID"));
		AttributeValue violationCodeAttributeValue = new AttributeValue()
				.withS(requestObject.getString("VIOLATION_CODE"));
		AttributeValue violationCategoryCodeAttributeValue = new AttributeValue()
				.withS(requestObject.getString("VIOLATION_CATEGORY_CODE"));
		AttributeValue unitOfMeasureAttributeValue = new AttributeValue()
				.withS(requestObject.getString("UNIT_OF_MEASURE"));
		AttributeValue severityIndCntAttributeValue = new AttributeValue()
				.withS(requestObject.getString("SEVERITY_IND_CNT"));
		AttributeValue ruleFamilyCodeAttributeValue = new AttributeValue()
				.withS(requestObject.getString("RULE_FAMILY_CODE"));
		AttributeValue ruleGroupCodeAttributeValue = new AttributeValue()
				.withS(requestObject.getString("RULE_GROUP_CODE"));
		AttributeValue ruleCodeAttributeValue = new AttributeValue().withS(requestObject.getString("RULE_CODE"));
		AttributeValue isMajorViolIndAttributeValue = new AttributeValue()
				.withS(requestObject.getString("IS_MAJOR_VIOL_IND"));
		AttributeValue isHealthBasedIndAttributeValue = new AttributeValue()
				.withS(requestObject.getString("IS_HEALTH_BASED_IND"));
		AttributeValue FacilityIdAttributeValue = new AttributeValue().withS(requestObject.getString("FACILITY_ID"));
		AttributeValue contaminantCodeAttributeValue = new AttributeValue()
				.withS(requestObject.getString("CONTAMINANT_CODE"));
		AttributeValue ComplianceStatusCodeAttributeValue = new AttributeValue()
				.withS(requestObject.getString("COMPLIANCE_STATUS_CODE"));
		AttributeValue StateMCLAttributeValue = new AttributeValue().withS(requestObject.getString("STATE_MCL"));

		PutPointRequest putPointRequest = new PutPointRequest(geoPoint, rangeKeyAttributeValue);
		putPointRequest.getPutItemRequest().addItemEntry("ADDRESS_LINE1", addressLine1AttributeValue);
		putPointRequest.getPutItemRequest().addItemEntry("ADDRESS_LINE2", addressLine2AttributeValue);
		putPointRequest.getPutItemRequest().addItemEntry("CITY_NAME", cityNameAttributeValue);
		putPointRequest.getPutItemRequest().addItemEntry("COUNTRY_CODE", countryCodeAttributeValue);
		putPointRequest.getPutItemRequest().addItemEntry("ORG_NAME", orgNameAttributeValue);
		putPointRequest.getPutItemRequest().addItemEntry("OUTSTANDING_PERFORMER", outstandingPerformerAttributeValue);
		putPointRequest.getPutItemRequest().addItemEntry("STATE_CODE", stateCodeAttributeValue);
		putPointRequest.getPutItemRequest().addItemEntry("SOURCE_WATER_PROTECTION_CODE",
				sourceWaterProtectionCodeAttributeValue);
		putPointRequest.getPutItemRequest().addItemEntry("ZIP_CODE", zipCodeAttributeValue);

		putPointRequest.getPutItemRequest().addItemEntry("VIOL_MEASURE", violMeasureAttributeValue);
		putPointRequest.getPutItemRequest().addItemEntry("VIOLATION_ID", violationIdAttributeValue);
		putPointRequest.getPutItemRequest().addItemEntry("VIOLATION_CODE", violationCategoryCodeAttributeValue);
		putPointRequest.getPutItemRequest().addItemEntry("VIOLATION_CATEGORY_CODE",
				violationCategoryCodeAttributeValue);

		putPointRequest.getPutItemRequest().addItemEntry("UNIT_OF_MEASURE", unitOfMeasureAttributeValue);
		putPointRequest.getPutItemRequest().addItemEntry("SEVERITY_IND_CNT", severityIndCntAttributeValue);
		putPointRequest.getPutItemRequest().addItemEntry("RULE_FAMILY_CODE", ruleFamilyCodeAttributeValue);
		putPointRequest.getPutItemRequest().addItemEntry("RULE_GROUP_CODE", ruleGroupCodeAttributeValue);
		putPointRequest.getPutItemRequest().addItemEntry("RULE_CODE", ruleCodeAttributeValue);

		putPointRequest.getPutItemRequest().addItemEntry("IS_MAJOR_VIOL_IND", isMajorViolIndAttributeValue);
		putPointRequest.getPutItemRequest().addItemEntry("IS_HEALTH_BASED_IND", isHealthBasedIndAttributeValue);
		putPointRequest.getPutItemRequest().addItemEntry("FACILITY_ID", FacilityIdAttributeValue);
		putPointRequest.getPutItemRequest().addItemEntry("CONTAMINANT_CODE", contaminantCodeAttributeValue);
		putPointRequest.getPutItemRequest().addItemEntry("CONTAMINANT_STATUS_CODE", ComplianceStatusCodeAttributeValue);
		putPointRequest.getPutItemRequest().addItemEntry("STATE_MCL", StateMCLAttributeValue);

		PutPointResult putPointResult = geoDataManager.putPoint(putPointRequest);

		// printPutPointResult(putPointResult, out);
	}

	// TODO: Make this for water sites!
	private static Map<String, AttributeValue> newItem(String name, int year, String rating, String... fans) {
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		item.put("name", new AttributeValue(name));
		item.put("year", new AttributeValue().withN(Integer.toString(year)));
		item.put("rating", new AttributeValue(rating));
		item.put("fans", new AttributeValue().withSS(fans));

		return item;
	}

}