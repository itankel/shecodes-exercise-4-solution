package com.ifat.example.useawssdk.withec2.shecodesexerciese;

import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static java.lang.Thread.sleep;


public class AppWorkWithEc2 {
    private Ec2Service ec2Service = new Ec2Service();

    //as requested flow: create ec2 instance with user Data . check the initiation of it.
    // and terminate the ec2 instance created
    @SneakyThrows
    public void mainFlow(String amiId, String userData) {
        System.out.println("start mainFlow");

        String encodedUserData = Base64.getEncoder().encodeToString(userData.getBytes(StandardCharsets.UTF_8));
        System.out.println(" user data =" + encodedUserData);

        String instanceCreatedId = ec2Service.createInstance(amiId, encodedUserData);
        System.out.println("-------------------after create ec2 instance");

        String instancePublicDnsName = ec2Service.findRunningEC2InstancePublicDnsName(instanceCreatedId);
        System.out.println("-------------------after find instancePublicDns is " + instancePublicDnsName);

        sleep(3000);// to give time for the initiation

        //check if the apache2 succeeded
        boolean validResponse = ValidateWebClient
                .sendGetRequestWithRetry("http://" + instancePublicDnsName + "/index.html", 8);
        if (validResponse) {
            System.out.println(" creation success");
        } else {
            System.out.println("creation failure");
        }
        // terminate the instance as requested
        ec2Service.terminateInstanceIfExists(instanceCreatedId);

        ec2Service.close();
        System.out.println("end mainFlow");

    }

    //ami-0c2b8ca1dad447f8a  --> I managed to create
    //ami-0472eef47f816e45d -> I managed to create too
    public static void main(String[] args) {
        String userData =
                "#!/bin/bash\n" +
                        "sudo apt update -y\n" +
                        "sudo apt install -y apache2\n" +
                        "echo \"Hello world from $(hostname -f)\" >/var/www/html/index.html";
        AppWorkWithEc2 appWorkWithEc2 = new AppWorkWithEc2();
        appWorkWithEc2.mainFlow("ami-0472eef47f816e45d", userData);
    }

}
