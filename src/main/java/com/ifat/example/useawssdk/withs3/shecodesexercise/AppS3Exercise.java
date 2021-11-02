package com.ifat.example.useawssdk.withs3.shecodesexercise;

import static java.lang.System.exit;

//to create jar use
// mvn package
// to execute use
// mvn exec:java -Dexec.mainClass="com.ifat.example.useawssdk.withs3.shecodesexercise.AppS3Exercise" -Dexec.args="testfromcommandmvnline"
public class AppS3Exercise {


    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("needed argument <bucket_name>");
            exit(0);
        }
        String bucketName = args[0];
        System.out.format("Creating a bucket named %s\n", bucketName);

        S3Service s3Service = new S3Service();
        s3Service.createBucket(bucketName);
    }

}