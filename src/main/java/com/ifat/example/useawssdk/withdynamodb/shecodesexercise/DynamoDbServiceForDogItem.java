package com.ifat.example.useawssdk.withdynamodb.shecodesexercise;

import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

import java.net.URI;
import java.util.*;

public class DynamoDbServiceForDogItem {
    private DynamoDbClient dynamoDbClient;
    private DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<DogItem> mappedTable = null;

    public DynamoDbServiceForDogItem() {
        init();
    }

    private void init() {

        Region region = Region.US_EAST_1;
        dynamoDbClient = DynamoDbClient.builder()
                .endpointOverride(URI.create("http://localhost:8000"))// needed when using a local DB
                .region(region)
                .build();
        enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();

    }


    public void putRecord(DogItem dogItem) {

        try {
            mappedTable.putItem(dogItem);
            System.out.println("PutItem succeeded: " + dogItem);

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("done");
    }


    public void setTable(String tableName) {
        mappedTable = enhancedClient.table(tableName, TableSchema.fromBean(DogItem.class));
    }


    public void queryData(String keyVal, String attName, int attValue) {

        try {
            AttributeValue att = AttributeValue.builder()
                    .n(Integer.toString(attValue))
                    .build();

            Map<String, AttributeValue> expressionValues = new HashMap<>();
            expressionValues.put(":value", att);

            Expression expression = Expression.builder()
                    .expression(attName+" = :value")
                    .expressionValues(expressionValues)
                    .build();

            // Create a QueryConditional object that is used in the query operation.
            QueryConditional queryConditional = QueryConditional
                    .keyEqualTo(Key.builder().partitionValue(keyVal)
                            .build());

            // Get items in the Customer table and write out the ID value.
            Iterator<DogItem> results = mappedTable
                    .query(r -> r
                            .queryConditional(queryConditional)
                            .filterExpression(expression))
                    .items()
                    .iterator();

            while (results.hasNext()) {
                DogItem rec = results.next();
                System.out.println("The dog  record: " + rec.toString());
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Done");
    }

    // scan with one attribute that has the value of attValue
    public void scanIndex( String attributeName,int attValue) {
        try {

            AttributeValue attVal = AttributeValue.builder()
                    .n(Integer.toString(attValue))
                    .build();

            Map<String, AttributeValue> myMap = new HashMap<>();
            myMap.put(":val1", attVal);

            Map<String, String> myExMap = new HashMap<>();
            myExMap.put("#" + attributeName, attributeName);

            Expression expression = Expression.builder()
                    .expressionValues(myMap)
                    .expressionNames(myExMap)
                    .expression("#" + attributeName + " = :val1")
                    .build();

            ScanEnhancedRequest enhancedRequest = ScanEnhancedRequest.builder()
                    .filterExpression(expression)
                    .limit(15)
                    .build();

            // Get items in the Issues table.
            Iterator<DogItem> results = mappedTable
                    .scan(enhancedRequest)
                    .items()
                    .iterator();

            while (results.hasNext()) {
                DogItem dogItem = results.next();
                System.out.println("The record dog  is " + dogItem.toString());
            }


        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }


    public void deleteItem(String tableName, String key, String keyVal) {

        HashMap<String, AttributeValue> keyToGet = new HashMap<>();

        keyToGet.put(key, AttributeValue.builder()
                .s(keyVal)
                .build());

        DeleteItemRequest deleteReq = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(keyToGet)
                .build();

        try {
            dynamoDbClient.deleteItem(deleteReq);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public void deleteDynamoDBTable(String tableName) {

        DeleteTableRequest request = DeleteTableRequest.builder()
                .tableName(tableName)
                .build();

        try {
            dynamoDbClient.deleteTable(request);

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println(tableName + " was successfully deleted!");
    }


    public String createTable(String tableName, String key) {

        DynamoDbWaiter dbWaiter = dynamoDbClient.waiter();
        CreateTableRequest request = CreateTableRequest.builder()
                .attributeDefinitions(AttributeDefinition.builder()
                        .attributeName(key)
                        .attributeType(ScalarAttributeType.S)
                        .build())
                .keySchema(KeySchemaElement.builder()
                        .attributeName(key)
                        .keyType(KeyType.HASH)
                        .build())
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(Long.valueOf(10))
                        .writeCapacityUnits(Long.valueOf(10))
                        .build())
                .tableName(tableName)
                .build();

        String newTable = "";
        try {
            CreateTableResponse response = dynamoDbClient.createTable(request);
            DescribeTableRequest tableRequest = DescribeTableRequest.builder()
                    .tableName(tableName)
                    .build();

            // Wait until the Amazon DynamoDB table is created
            WaiterResponse<DescribeTableResponse> waiterResponse = dbWaiter.waitUntilTableExists(tableRequest);
            waiterResponse.matched().response().ifPresent(System.out::println);

            newTable = response.tableDescription().tableName();
            return newTable;

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }

    public void close() {
        dynamoDbClient.close();
    }


}


//    // Scan the table and retrieve only items where createDate is 2013-11-15.
//    public static void scanIndex(DynamoDbClient ddb, String tableName, String indexName) {
//
//        System.out.println("\n***********************************************************\n");
//        System.out.print("Select items for "+tableName +" where createDate is 2013-11-15!");
//
//        try {
//            // Create a DynamoDbEnhancedClient and use the DynamoDbClient object.
//            DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
//                    .dynamoDbClient(ddb)
//                    .build();
//
//            // Create a DynamoDbTable object based on Issues.
//            DynamoDbTable<Issues> table = enhancedClient.table("Issues", TableSchema.fromBean(Issues.class));
//
//            // Setup the scan based on the index.
//            if (indexName == "CreateDateIndex") {
//                System.out.println("Issues filed on 2013-11-15");
//
//                AttributeValue attVal = AttributeValue.builder()
//                        .s("2013-11-15")
//                        .build();
//
//                // Get only items in the Issues table for 2013-11-15.
//                Map<String, AttributeValue> myMap = new HashMap<>();
//                myMap.put(":val1", attVal);
//
//                Map<String, String> myExMap = new HashMap<>();
//                myExMap.put("#createDate", "createDate");
//
//                Expression expression = Expression.builder()
//                        .expressionValues(myMap)
//                        .expressionNames(myExMap)
//                        .expression("#createDate = :val1")
//                        .build();
//
//                ScanEnhancedRequest enhancedRequest = ScanEnhancedRequest.builder()
//                        .filterExpression(expression)
//                        .limit(15)
//                        .build();
//
//                // Get items in the Issues table.
//                Iterator<Issues> results = table.scan(enhancedRequest).items().iterator();
//
//                while (results.hasNext()) {
//                    Issues issue = results.next();
//                    System.out.println("The record description is " + issue.getDescription());
//                    System.out.println("The record title is " + issue.getTitle());
//                }
//            }
//
//        } catch (DynamoDbException e) {
//            System.err.println(e.getMessage());
//            System.exit(1);
//        }