package com.ifat.example.useawssdk.withdynamodb.shecodesexerciese;


//to create : mvn package
// to be executed by
//mvn exec:java -Dexec.mainClass="com.ifat.example.myapp.withdynamodb.shecodedog.DogExercise"
public class AppWorkWithDynamoDB {

    public static void main(String[] args) {
        AppWorkWithDynamoDB appWorkWithDynamoDB = new AppWorkWithDynamoDB();
        appWorkWithDynamoDB.mainFlow();
        appWorkWithDynamoDB.mainFlowMoreGenericWay();

    }

    private void mainFlow() {
        DogService dogService = new DogService();
        dogService.createDogTable();
        dogService.loadDataFromJson("data/DogData.txt");
        dogService.queryDataByBreedAndIntelligence("Poodle", 2);  // this is not what was requested
        dogService.scanDataByIntelligence(9);
        dogService.deleteDog("German Shepard");
        dogService.deleteTable();
        dogService.close();
    }

    private void mainFlowMoreGenericWay() {
        DogServiceUsingGenericService dogServiceUsingGenericService = new DogServiceUsingGenericService();
        dogServiceUsingGenericService.createDogTable();
        dogServiceUsingGenericService.loadDataFromJson("data/DogData.txt");
        dogServiceUsingGenericService.queryData("Boxer", 48);
        dogServiceUsingGenericService.scanData(9);
        dogServiceUsingGenericService.deleteDog("German Shepard");
        dogServiceUsingGenericService.deleteTable();
        dogServiceUsingGenericService.close();
    }
}

