// /*
//     Code written for cmpsc441 class
//     by Professor. Mohan using the Amazon EC2 SDK.
//     This class provides the methods such as create instances, display running
//     instances, terminate instances, start instances, and stop instances.
//     The code expects you to copy the credentials to ~/.aws/credentials file
//     for correct working.
//
// */
// package cloud;
// import java.util.*;
//
// import java.lang.ProcessBuilder;
// import com.amazonaws.AmazonClientException;
// import com.amazonaws.AmazonServiceException;
// import com.amazonaws.auth.AWSCredentials;
// import com.amazonaws.auth.AWSStaticCredentialsProvider;
// import com.amazonaws.auth.profile.ProfileCredentialsProvider;
// import com.amazonaws.regions.Region;
// import com.amazonaws.regions.Regions;
// import com.amazonaws.services.ec2.AmazonEC2;
// import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
// import com.amazonaws.services.ec2.model.RunInstancesRequest;
// import com.amazonaws.services.ec2.model.RunInstancesResult;
// import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
// import com.amazonaws.services.ec2.model.StartInstancesRequest;
// import com.amazonaws.services.ec2.model.StopInstancesRequest;
// import com.amazonaws.services.ec2.model.Instance;
// import com.amazonaws.services.ec2.model.DescribeInstancesResult;
// import com.amazonaws.services.ec2.model.Reservation;
// import com.amazonaws.services.ec2.model.InstanceState;
//
// public class AWSPro {
//     private AmazonEC2 ec2;
//     private Set<Instance> instances = new HashSet<Instance>();
//     /**
//      * Public constructor.
//      * @throws Exception
//      */
//     public AWSPro () throws Exception {
//         init();
//     }
//     private void init() throws Exception {
//         AWSCredentials credentials = null;
//         try {
//             credentials = new ProfileCredentialsProvider().getCredentials();
//         } catch (Exception e) {
//             throw new AmazonClientException(
//                     "Cannot load the credentials from the credential profiles file. " +
//                     "Please make sure that your credentials file is at the correct " +
//                     "location (~/.aws/credentials), and is in valid format.",
//                     e);
//         }
//
//         ec2 = AmazonEC2ClientBuilder.standard()
//             .withCredentials(new AWSStaticCredentialsProvider(credentials))
//             .withRegion(Regions.US_EAST_2 )
//             .build();
//     }
//
//
//     public void createInstances(int noOfInstances) throws InterruptedException {
//         RunInstancesRequest runInstancesRequest = new RunInstancesRequest()
//         .withInstanceType("t2.micro")
//         .withImageId("ami-f96b4e9c")
//         .withMinCount(noOfInstances)
//         .withMaxCount(noOfInstances)
//         .withKeyName("toccin-key2018")//change to the name of the pem file you want to use
//         .withSecurityGroupIds("sg-9d67bef6");//change to your own security group that has AmazonEC2FullAccess enabled
//
//         RunInstancesResult runInstances = ec2.runInstances(runInstancesRequest);
//         System.out.println("#provisioning machines in the Cloud...");
//         Thread.sleep(20000);
//     }
//
//     public void describeInstances(){
//         System.out.println("#describe current instances");
//         DescribeInstancesResult describeInstancesRequest = ec2.describeInstances();
//         List<Reservation> reservations = describeInstancesRequest.getReservations();
//         for (Reservation reservation : reservations)
//             instances.addAll(reservation.getInstances());
//
//     }
//
//     public ArrayList<String> displayRunningInstances(){
//         ArrayList<String> ips = new  ArrayList<String>();
//         describeInstances();
//         System.out.println("#display running instances");
//         for (Instance ins : instances){
//              String instanceId = ins.getInstanceId();
//              String instanceIPs = ins.getPublicIpAddress();
//              InstanceState is = ins.getState();
//              if (is.getName().equalsIgnoreCase("running")){
//                 System.out.println(instanceId + " " + instanceIPs + " " + is.getName());
//                 ips.add(instanceIPs);
//             }
//
//         }
//         return ips;
//
//     }
//     public static void getMachinesReady(String pemFileLocation, ArrayList<String> ips) throws Exception{
//         for (String ipAddress : ips) {
//             System.out.println("preparing the machine:" + ipAddress);
//             ArrayList<String> commands = new ArrayList<String>();
//             commands.add("yes");
//             commands.add("sudo su -");
//             commands.add("echo -e \"cmpsc441\ncmpsc441\" | (passwd ubuntu)");
//             commands.add("exit");
//             commands.add("sudo sed -i \"s/PasswordAuthentication no/PasswordAuthentication yes/g\" /etc/ssh/sshd_config");
//             commands.add("sudo service ssh restart");
//           //  commands.add("sudo apt-get update");
//             // commands.add("sudo apt-get install unzip\n");
//             // commands.add("unzip DogsData.zip");
//             commands.add("exit");
//             String logs = AWSShell.executeCommandsInEC2(pemFileLocation, ipAddress.trim(), "ubuntu", commands);
//             System.out.println(logs);
//         }
//         System.out.println("All set to go...");
//     }
//     public void terminateInstances(){
//         describeInstances();
//         System.out.println("#terminating running instances");
//         ArrayList<String> instanceIdsToTerminate = new ArrayList<String>();
//         for (Instance ins : instances){
//             String instanceId = ins.getInstanceId();
//             InstanceState is = ins.getState();
//             if (is.getName().equalsIgnoreCase("running"))
//                 instanceIdsToTerminate.add(instanceId);
//         }
//         TerminateInstancesRequest terminateRequest = new TerminateInstancesRequest(instanceIdsToTerminate);
//         ec2.terminateInstances(terminateRequest);
//     }
//
//     public void startInstances(){
//         describeInstances();
//         System.out.println("#starting stopped instances");
//         ArrayList<String> instanceIdsToStart = new ArrayList<String>();
//         for (Instance ins : instances){
//             String instanceId = ins.getInstanceId();
//             InstanceState is = ins.getState();
//             if (is.getName().equalsIgnoreCase("stopped"))
//                 instanceIdsToStart.add(instanceId);
//         }
//         StartInstancesRequest startRequest = new StartInstancesRequest(instanceIdsToStart);
//         ec2.startInstances(startRequest);
//
//     }
//
//     public void stopInstances(){
//         describeInstances();
//         System.out.println("#stopping running instances");
//         ArrayList<String> instanceIdsToStop = new ArrayList<String>();
//         for (Instance ins : instances){
//             String instanceId = ins.getInstanceId();
//             InstanceState is = ins.getState();
//             if (is.getName().equalsIgnoreCase("running"))
//                 instanceIdsToStop.add(instanceId);
//         }
//         StopInstancesRequest stopRequest = new StopInstancesRequest(instanceIdsToStop);
//         ec2.stopInstances(stopRequest);
//
//     }
//
//     public static void placeJavaFiles(String pemFileLocation, ArrayList<String> ips){
//       for(String ipAddress : ips){
//         AWSShell.placeFiles(pemFileLocation, ipAddress.trim(), "ubuntu");
//       }
//     }
//
//     public static void main(String[] args) throws Exception, InterruptedException{
//         AWSPro obj = new AWSPro();
//         obj.createInstances(1);
//         System.out.println("#waiting for machines to be ssh friendly...");
//         Thread.sleep(20000);
//         ArrayList<String> ips = obj.displayRunningInstances();
//         String pemFileLocation = "/Users/nick/Computer_Science/441/toccin-key2018.pem";//enter in local path to pem file
//         getMachinesReady(pemFileLocation, ips);
//         placeJavaFiles(pemFileLocation, ips);
//
//         /*
//         obj.terminateInstances();
//         obj.startInstances();
//         obj.stopInstances();
//         */
//     }
// }
