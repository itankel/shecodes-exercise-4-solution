package com.ifat.example.useawssdk.withec2.shecodesexerciese;

import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;
import software.amazon.awssdk.services.ec2.waiters.Ec2Waiter;

import java.util.List;
import java.util.function.Consumer;

// service that use the aws SDK to create Ec2 instance ,
public class Ec2Service {
    private final Ec2Client ec2;
    private String savedPublicDns = "";


    Ec2Service() {
        Region region = Region.US_EAST_1;  //
        ec2 = Ec2Client.builder()
                .region(region)
                .build();
    }

    public String createInstance(String amiId) {
        System.out.println("start createInstance");
        RunInstancesRequest runRequest = singleMicroInstanceBuilder(amiId).build();

        RunInstancesResponse response = ec2.runInstances(runRequest);
        String instanceId = response.instances().get(0).instanceId();
        System.out.println("created instanceof amid=" + amiId + " returned Id=" + instanceId);
        System.out.println("end createInstance");
        return instanceId;
    }


    public String createInstance(String amiId, String userData) {
        System.out.println("start createInstance");
        RunInstancesRequest runRequest = singleMicroInstanceBuilder(amiId).userData(userData).build();

        RunInstancesResponse response = ec2.runInstances(runRequest);
        String instanceId = response.instances().get(0).instanceId();
        System.out.println("created instanceof amid=" + amiId + " returned Id=" + instanceId);
        System.out.println("end createInstance");
        return instanceId;
    }

    private RunInstancesRequest.Builder singleMicroInstanceBuilder(String amiId){
        return RunInstancesRequest.builder()
                .imageId(amiId)
                .instanceType(InstanceType.T2_MICRO)
                .maxCount(1)
                .minCount(1);
    }

    public String findRunningEC2InstancePublicDnsName(String instanceId) {
        System.out.println("------------------------end findRunningEC2InstancePublicDnsName");
        findRunningEC2Instance(instanceId,(r)->extractPublicDnsName(r));
        System.out.println("------------------------end findRunningEC2InstancePublicDnsName public DNS is " +
                this.savedPublicDns);
        return this.savedPublicDns;
    }

    private void extractPublicDnsName(DescribeInstancesResponse describeInstancesResponse) {
        System.out.println("this is the describeInstancesResponse " + describeInstancesResponse);
        this.savedPublicDns = describeInstancesResponse.reservations().get(0).instances().get(0).publicIpAddress();
    }

    public void terminateInstanceIfExists(String instanceId){
        System.out.println("start terminateInstanceIfExists");
        findRunningEC2Instance(instanceId,(r)->terminateInstance(instanceId));
    }
    public void printIfExists(String instanceId){
        System.out.println("start printIfExists");
        findRunningEC2Instance(instanceId,(r)->System.out.println(instanceId));
    }

    private void findRunningEC2Instance(String instanceId, Consumer<DescribeInstancesResponse> action) {
        System.out.println("start findRunningEC2Instance (action)");
        Filter filter = Filter.builder()
                .name("instance-state-name")
                .values("running")
                .name("instance-id")
                .values(instanceId)
                .build();

        Ec2Waiter ec2Waiter = ec2.waiter();
        WaiterResponse<DescribeInstancesResponse> waiterResponse =
                ec2Waiter.waitUntilInstanceRunning(d -> d.filters(filter));

        waiterResponse.matched().response().ifPresent(t->action.accept(t));
        System.out.println("------------------------end findRunningEC2Instance (action)");
    }

    public void terminateInstance(String instanceID) {
        System.out.println("------------------------start terminateInstance");
        TerminateInstancesRequest terminateInstancesRequest = TerminateInstancesRequest.builder()
                .instanceIds(instanceID)
                .build();

        TerminateInstancesResponse response = ec2.terminateInstances(terminateInstancesRequest);
        List<InstanceStateChange> terminatingInstances = response.terminatingInstances();
        terminatingInstances.stream().forEach(System.out::println);
        System.out.println("------------------------end terminateInstance");

    }

    public void close() {
        ec2.close();
        System.out.println("EC2 client closed");
    }

}
