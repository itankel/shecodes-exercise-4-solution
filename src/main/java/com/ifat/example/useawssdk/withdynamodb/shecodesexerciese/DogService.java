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


public class DogService {
    private DynamoDbServiceForDogItem dynamoDbServiceForDogItem;
    private final String tableName="DogsBreeds";
    private final String tableKey="breed";
    private final String searchAttrName="intelligence";


    public DogService() {
        dynamoDbServiceForDogItem = new DynamoDbServiceForDogItem();
    }

    public void createDogTable() {
        dynamoDbServiceForDogItem.createTable(tableName, tableKey);
    }

    public void loadDataFromJson(String inJsonFileName) {
        dynamoDbServiceForDogItem.setTable(tableName);
        loadRecords(inJsonFileName);
    }

    // this is not scan thus I must provide the key with search value
    public void queryDataByBreedAndIntelligence(String breadVal, int intelligence) {
        dynamoDbServiceForDogItem.setTable(tableName);
        dynamoDbServiceForDogItem.queryData(breadVal, searchAttrName, intelligence);
    }

    public void scanDataByIntelligence(int intelligence) {
        dynamoDbServiceForDogItem.setTable(tableName);
        dynamoDbServiceForDogItem.scanIndex(searchAttrName, intelligence);
    }


    public void deleteDog(String keyVal) {
        dynamoDbServiceForDogItem.setTable(tableName);
        dynamoDbServiceForDogItem.deleteItem(tableName, tableKey, keyVal);
    }

    public void deleteTable() {
        dynamoDbServiceForDogItem.deleteDynamoDBTable(tableName);
    }

    public void close() {
        dynamoDbServiceForDogItem.close();
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
                dynamoDbServiceForDogItem.putRecord(dogItem);
                System.out.println("PutRecord succeeded: " + dogItem);

            }
            parser.close();
        } catch (IOException e) {
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
