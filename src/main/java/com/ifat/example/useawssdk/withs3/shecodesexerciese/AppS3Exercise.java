package com.ifat.example.useawssdk.withs3.shecodesexerciese;

import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

import static java.lang.System.exit;

//to create jar use
// mvn package
// to execute use
//mvn exec:java -Dexec.mainClass="com.ifat.example.myapp.App"
public class AppS3Exercise {


  public static void main(String[] args) {

    if (args.length!=2) {
      System.out.println("usage : AppS3Exercise <bucket_name>");
      exit(0);
    }
    String bucketName = args[1];
    System.out.format("Creating a bucket named %s\n",
            bucketName);

    Region region = Region.US_EAST_1;
    S3Client s3 = S3Client.builder()
            .region(region)
            .build();

    createBucket (s3, bucketName);
    s3.close();
  }

  public static void createBucket( S3Client s3Client, String bucketName) {

    try {
      S3Waiter s3Waiter = s3Client.waiter();
      CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
              .bucket(bucketName)
              .build();

      s3Client.createBucket(bucketRequest);
      HeadBucketRequest bucketRequestWait = HeadBucketRequest.builder()
              .bucket(bucketName)
              .build();

      // Wait until the bucket is created and print out the response.
      WaiterResponse<HeadBucketResponse> waiterResponse = s3Waiter.waitUntilBucketExists(bucketRequestWait);
      waiterResponse.matched().response().ifPresent(System.out::println);
      System.out.println(bucketName +" is ready");

    } catch (S3Exception e) {
      System.err.println(e.awsErrorDetails().errorMessage());
      exit(1);
    }
  }

}