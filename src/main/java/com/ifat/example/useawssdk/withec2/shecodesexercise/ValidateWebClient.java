package com.ifat.example.useawssdk.withec2.shecodesexercise;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.lang.Thread.sleep;
import static java.time.temporal.ChronoUnit.SECONDS;

public class ValidateWebClient {

    public static boolean sendOneGetRequest(String uriToCheck) {
        System.out.println(" sendGetRequestWithRetry check validation to uri " + uriToCheck);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        request = HttpRequest.newBuilder()
                .uri(URI.create(uriToCheck))
                .GET()
                .timeout(Duration.of(300, SECONDS))
                .build();

        HttpResponse<String> response;
        boolean returnVal = false;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("response body ===>" + response.body());
            System.out.println("response status is ===>" + response.statusCode());
            if (response.statusCode() == 200) {
                returnVal = true;
            }
            System.out.println("respons is ===>" + returnVal);

        } catch (IOException e) {
            System.out.println(" IOException trying to run the get command");
        } catch (InterruptedException e) {
            System.out.println(" InterruptedException trying to run the get command");
        }
        return returnVal;
    }


    public static boolean sendGetRequestWithRetry(String uriToCheck, int maxNumberOfRetries) {
        System.out.println(" sendGetRequestWithRetry check validation to uri " + uriToCheck);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        request = HttpRequest.newBuilder()
                .uri(URI.create(uriToCheck))
                .GET()
                .build();

        CompletableFuture<HttpResponse<String>> resp;
        boolean returnVal = false;
        int numOfRetries = 0;
        do {
            try {
                System.out.println(" intend to wait to response for the url link ");
                resp = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
                HttpResponse<String> response;
                numOfRetries++;
                System.out.println("this is retry number: " + numOfRetries);
                response = resp.get(5500, TimeUnit.MILLISECONDS);

                System.out.println("response body ===>" + response.body());
                System.out.println("response status is ===>" + response.statusCode());
                if (response.statusCode() == 200) {
                    returnVal = true;
                }
                System.out.println("response is ===>" + returnVal);

            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                System.out.println(" InterruptedException/ExecutionException/TimeoutException trying to run the get command");
            } finally {
                System.out.println(" finally sendGetRequestWithRetry");
            }
            // sleep between retries
            try {
                sleep(4000);
            } catch (InterruptedException e) {
                System.out.println("catch InterruptedException while sleeping");
            }
        }
        while (!returnVal && numOfRetries <= maxNumberOfRetries);
        System.out.println(" end sendGetRequestWithRetry");
        return returnVal;
    }

}

