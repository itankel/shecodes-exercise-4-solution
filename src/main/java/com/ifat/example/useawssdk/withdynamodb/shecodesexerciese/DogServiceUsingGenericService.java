package com.ifat.example.useawssdk.withdynamodb.shecodesexerciese;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class DogServiceUsingGenericService {
    private DynamoDbServiceGenericItem dynamoDbServiceGenericItem;
    private final String tableName="AnotherDogsBreeds";
    private final String tableKey="breed";
    private final String searchAttrName="intelligence";


    public DogServiceUsingGenericService() {
        dynamoDbServiceGenericItem = new DynamoDbServiceGenericItem();
    }

    public void createDogTable() {
        dynamoDbServiceGenericItem.createTable(tableName, tableKey);
    }

    public void loadDataFromJson(String inJsonFileName) {
        dynamoDbServiceGenericItem.setTable(tableName,DogItem.class);
        loadRecords(inJsonFileName);
    }
// this is not scan thus I must provide the key with search value
    public void queryData(String breadName,int intelligence) {
        dynamoDbServiceGenericItem.setTable(tableName,DogItem.class);
        dynamoDbServiceGenericItem.queryData(breadName,searchAttrName, intelligence);
    }

    public void scanData(int intelligence) {
        dynamoDbServiceGenericItem.setTable(tableName,DogItem.class);
        dynamoDbServiceGenericItem.scanIndex(searchAttrName,intelligence);
    }


    public void deleteDog(String keyVal) {
        dynamoDbServiceGenericItem.setTable(tableName,DogItem.class);
        dynamoDbServiceGenericItem.deleteItem(tableName,tableKey,keyVal);
    }

    public void deleteTable() {
        dynamoDbServiceGenericItem.deleteDynamoDBTable(tableName);
    }

    public void close() {
        dynamoDbServiceGenericItem.close();
    }


    private void loadRecords(String inputJsonFileName) {
        try {
            JsonParser parser = new JsonFactory().createParser(new File(inputJsonFileName));//"data/DogData.txt"));

            JsonNode rootNode = new ObjectMapper().readTree(parser);
            Iterator<JsonNode> iter = rootNode.withArray("dogBreeds").iterator();

            ObjectNode currentNode;

            while (iter.hasNext()) {
                currentNode = (ObjectNode) iter.next();
                DogItem dogItem = jsonToObject(currentNode);
                dynamoDbServiceGenericItem.putRecord(dogItem);
                System.out.println("PutRecord succeeded: " + dogItem);

            }
            parser.close();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("done");
    }


    private DogItem jsonToObject(ObjectNode currentNode) {

        String breed = currentNode.path("breed").asText();
        String breedType = currentNode.path("breedType").asText();
        String origin = currentNode.path("origin").asText();
        String popularity = currentNode.path("").asText();
        List<String> temperament = jsonArrayToList(currentNode);
        String hypoallergenic = currentNode.path("hypoallergenic").asText();
        int intelligence = currentNode.path("intelligence").asInt();
        String photo = currentNode.path("photo").asText();
        return DogItem.builder().breed(breed).breedType(breedType).origin(origin).popularity(popularity)
                .temperament(temperament)
                .hypoallergenic(hypoallergenic).intelligence(intelligence).photo(photo)
                .build();
    }


    private List<String> jsonArrayToList(ObjectNode currentNode) {
        List<String> temperament = new ArrayList<>();
        Iterator<JsonNode> iterator = currentNode.withArray("temperament").elements();
        JsonNode innerCurrentNode;
        while (iterator.hasNext()) {
            innerCurrentNode = iterator.next();
            temperament.add(innerCurrentNode.asText());
            // System.out.print(iterator.next().toString() + " ");
        }
        return temperament;
    }




}
