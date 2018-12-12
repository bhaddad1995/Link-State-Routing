// Blake Haddad & Andy Nguyen
// CS-570
// Link State Routing Assignment

import java.util.Map;
import java.util.Vector;

import javafx.scene.Node;

import java.io.*;
import java.util.Scanner;
import java.util.*;
import java.text.*;
import java.util.*;

public class linkStateRouting{
    Scanner reader = new Scanner(System.in);  // Reading from System.in
    public HashMap<Integer,router> routerList = new HashMap<>();
    public ArrayList<ArrayList<Integer>> linkedIDs = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> linkCosts = new ArrayList<>();
    HashMap<Integer, String> IDandNetName = new HashMap<>();
    ArrayList<Integer> IDindex = new ArrayList<>();
    graph initialGraph;

    public static void main(String[] args){
        linkStateRouting test = new linkStateRouting();
        test.loadFile();
        // test.printRoutersLinksAndCosts();
        test.createRouters();
        test.addConnections();
        while(true){
            test.handleInput(test.promptUserInput());
        }
    }

    public void loadFile(){
        String[] splitID;
        String[] splitLink;
        String splitnetName;

        ArrayList<Integer> temp = new ArrayList<>();
        ArrayList<Integer> temp2 = new ArrayList<>();
        int cost;
        int routerID;
        int i = 0;
        int count =0;



        try(BufferedReader br = new BufferedReader(new FileReader("infile.dat"))) {
            for(String line; (line = br.readLine()) != null; ) {
                if(line.equals("")){
                    break;
                }

                if (line.charAt(0) != ' ') {
                    splitID = line.split(" ");
                    routerID= Integer.parseInt(splitID[0]);
                    IDindex.add(routerID);
                    splitnetName = splitID[1];
                    IDandNetName.put(routerID, splitnetName);
                    if (count > 0) {
                        ArrayList<Integer> copiedTemp = new ArrayList<Integer>();
                        ArrayList<Integer> copiedTemp2 = new ArrayList<Integer>();
                        for(Integer nt : temp){
                            copiedTemp.add(new Integer(nt));
                        }
                        for(Integer nt : temp2){
                            copiedTemp2.add(new Integer(nt));
                        }
                        System.out.println("temp: " + temp);
                        System.out.println("temp2: " + temp2);
                        linkedIDs.add(copiedTemp);
                        linkCosts.add(copiedTemp2);
                        System.out.println("Being cleared");
                        temp.clear();
                        temp2.clear();
                    }
                    count++;

                }

                if(line.charAt(0) == ' '){
                    splitLink = line.split(" ");
                    temp.add(Integer.parseInt(splitLink[1]));
                    if(splitLink.length>2){
                        temp2.add(Integer.parseInt(splitLink[2]));
                        System.out.println("Being added to temp2: " + splitLink[2]);
                    }else{
                        temp2.add(1);
                        System.out.println("Being added to temp2: " + splitLink[1]);
                    }
                    System.out.println("Being added to temp: " + Integer.parseInt(splitLink[1]));
                }



                i++;


            }

            ArrayList<Integer> copiedTemp = new ArrayList<Integer>();
            ArrayList<Integer> copiedTemp2 = new ArrayList<Integer>();
            for(Integer nt : temp){
                copiedTemp.add(new Integer(nt));
            }
            for(Integer nt : temp2){
                copiedTemp2.add(new Integer(nt));
            }
            System.out.println("temp: " + temp);
            System.out.println("temp2: " + temp2);
            linkedIDs.add(copiedTemp);
            linkCosts.add(copiedTemp2);
            System.out.println("Being cleared");
            temp.clear();
            temp2.clear();

        } catch (IOException j) {
            System.err.print("Error loading file with exception:\n"+j);
            j.printStackTrace();
            System.exit(0);
        }

        for (Integer ID : IDandNetName.keySet())
        {
            Integer IDKey = ID;
            String networkName = IDandNetName.get(IDKey);

            System.out.println("ID: " + IDKey);
            System.out.println("Network Name: " + networkName);
        }
    }

    public String promptUserInput(){
        String input = "";
        while(true){
            System.out.println("-----------------------------------");
            System.out.println("--Enter C to continue.");
            System.out.println("--Enter Q to quit.");
            System.out.println("--Enter P to print routing table.");
            System.out.println("--Enter S followed by an id number to shut down a router. Ex: \"S 1\"");
            System.out.println("--Enter T followed by an id number to start up a router. Ex: \"T 1\"");
            System.out.print("Enter action: ");
            input = reader.nextLine(); // Scans the next token of the input as an int.
            if (input.equalsIgnoreCase("C") || input.equalsIgnoreCase("Q") || input.equalsIgnoreCase("P")) {
                return input;
            }
            if (input.length() >= 3) {
                String[] splitStr = input.split(" ");
                if ((splitStr[0].equalsIgnoreCase("S") || splitStr[0].equalsIgnoreCase("T"))
                        && isInteger(splitStr[1])) {
                    return input;
                }
            }
        }
    }
    
    public void handleInput(String input){
        if(input.equalsIgnoreCase("Q")){
            System.out.println("Quitting program...");
            System.exit(0);
        }else if(input.equalsIgnoreCase("P")){
            System.out.println("-----Routing Table-----");
            printRoutingTable();
        }else if(input.equalsIgnoreCase("C")){
            //Continue
        }else if(input.length()>=3){
            String[] splitStr = input.split(" ");
            Integer routerId = Integer.parseInt(splitStr[1]);
            if(routerList.containsKey(routerId)){
                if(splitStr[0].equalsIgnoreCase("S")){
                    System.out.println("Shutting down router " + routerId + "...");
                    routerList.get(routerId).turnOffRouter();
                }else if(splitStr[0].equalsIgnoreCase("T")){
                    System.out.println("Starting up router " + routerId + "...");
                    routerList.get(routerId).turnOnRouter();
                }
            }else{
                System.out.println("Router ID does not exist in the current configuration.");
            }
        }
    }

    public void printRoutingTable(){
        for(Integer ids : routerList.keySet()){
            routerList.get(ids).printRoutingTable();
        }
    }
    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }
    
    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    public void printRoutersLinksAndCosts()
    {
        for (int k = 0; k < linkedIDs.size(); k++){
            System.out.println("ID: " + IDindex.get(k));
            System.out.println("Linked IDs: " + linkedIDs.get(k));
            System.out.println("Linked costs: " + linkCosts.get(k));
        }
    }

    // public void createInitialGraph(){
    //     initialGraph = new graph(routerList.size());
        
    // }

    public void createRouters(){
        int i = 0;
        for(Integer id : IDandNetName.keySet()){
            //HashMap<Integer,String> connectionMap = formConnectedRoutersMap(i);
            HashMap<Integer,Integer> connectionCostMap = formConnectedRouterCostMap(i);
            routerList.put(id,new router(id,IDandNetName.get(id), connectionCostMap, true));
            i++;
        }
    }
    
    public void addConnections(){
        for(int i=0; i<linkedIDs.size(); i++){
            router currentRouter = routerList.get(IDindex.get(i));
            for(int j=0;j<linkedIDs.get(i).size();j++){
                System.out.println("Adding connection to router " + linkedIDs.get(i).get(j) + " from router: " + IDindex.get(i));
                currentRouter.addConnectedRouter(linkedIDs.get(i).get(j),routerList.get(linkedIDs.get(i).get(j)));
            }
        }
    }

    public HashMap<Integer,Integer> formConnectedRouterCostMap(Integer index){
        HashMap<Integer,Integer> connections = new HashMap<>();
        ArrayList<Integer> connectedIDs = linkedIDs.get(index);
        for(int i = 0; i<connectedIDs.size(); i++){
            connections.put(connectedIDs.get(i), linkCosts.get(index).get(i));
        }
        return connections;
    }

}

class router{
    public HashMap<Integer,router> connectedRouters = new HashMap<Integer,router>();
    public HashMap<Integer,Integer> connectedRouterCost = new HashMap<Integer,Integer>();
    public HashMap<Integer,Integer> routerIdToRecievedSequenceNum = new HashMap<Integer,Integer>();
    public String routerName;
    public Integer ID;
    public graph g;
    boolean onStatus;

    router(Integer ID, String name, HashMap<Integer,Integer> costMap, boolean onOff){
        this.ID = ID;
        this.routerName = name;
        this.connectedRouterCost = costMap;
        this.onStatus = onOff;
    }

    public void turnOffRouter(){
        this.onStatus = false;
    }

    public void turnOnRouter(){
        this.onStatus = true;
    }

    public String getRouterName(){
        return this.routerName;
    }

    public void setRouterName(String name){
        this.routerName = name;
    }

    public void addConnectedRouter(Integer id, router r){
        this.connectedRouters.put(id, r);
    }
    
    public void printRoutingTable(){
        for(Integer ids : connectedRouters.keySet()){
            System.out.println(getRouterName() + ", " + connectedRouters.get(ids).getRouterName());
        }
    }

    public void recievePacket(LSP packet, Integer forwarderId){
        packet.decrementTTL();
        if(packet.getTTL()==0 || compareSequenceNum(forwarderId, packet) || onStatus == false){
            //do nothing with packet
            return;
        }else{
            // packets information should be compared to the receiving 
            // router’s connectivity graph and the LSP should be sent out (in the form of function calls) 
            // to all directly connected routers except the one on which it was received.
        }
    }

    //returns true if stored seq num is higher than new packets
    public boolean compareSequenceNum(Integer routerID, LSP packet){
        if(!routerIdToRecievedSequenceNum.containsKey(routerID)){
            return true;
        }else{
            Integer currentSeqValue = routerIdToRecievedSequenceNum.get(routerID);
            if(currentSeqValue > packet.getSeqNum()){
                return true;
            }
        }
        return false;
    }

    public void originatePacket(){
        if(onStatus == false){
            //do nothing
            return;
        }else{
            LSP packet = new LSP(getRouterName(),1,10);

        }
    }
}

class LSP{
    public String originRouter;
    public Integer seqNum;
    public Integer timeToLive;
    public ArrayList<Triplet<Integer,String,Integer>> idNetworkCost = new ArrayList<>();
    //list of network names
    //list of each directly connected router, network behind it, and cost

    LSP(String origin, Integer seq, Integer ttl){
        this.originRouter = origin;
        this.seqNum = seq;
        this.timeToLive = ttl;
    }

    public void decrementTTL(){
        this.timeToLive = this.timeToLive - 1;
    }

    public Integer getTTL(){
        return this.timeToLive;
    }

    public Integer getSeqNum(){
        return this.seqNum;
    }

    public void incrementSeqNum(){
        this.seqNum++;
    }
}

class graph{
    public int[][] adjacencyMtrx;
    public int totalElements;
    public List<Integer> routerIdList = new ArrayList<>();
    public HashMap<Integer,Integer> routerIdToIndex = new HashMap<>();

    graph(){

    }

    graph(int numElements){
        adjacencyMtrx = new int[numElements][numElements];
        this.totalElements = numElements;
    } 

    public void addEdge(Integer routerIdStart, Integer routerIdEnd){
        adjacencyMtrx[routerIdToIndex.get(routerIdStart)][routerIdToIndex.get(routerIdEnd)] = 1;
        adjacencyMtrx[routerIdToIndex.get(routerIdEnd)][routerIdToIndex.get(routerIdStart)] = 1;
    }

    public void addNode(int value){
        routerIdList.add(value);
        routerIdToIndex.put(routerIdList.size(), value);
    }

    // public void printNodes(){
    //     for(int i = 0; i<nodeList.size(); i++){
    //         System.out.println(nodeList.get(i).getValue());
    //     }
    // }

    // public void printAdjMatrix(){
    //     int a = adjacencyMtrx.length;
    //     int b = adjacencyMtrx[0].length;
    //     System.out.println(a + " " + b);
    //     for(int i = 0; i<adjacencyMtrx.length; i++){
    //         for(int j = 0; j<adjacencyMtrx[0].length; j++){
    //             System.out.print(adjacencyMtrx[i][j] + " ");
    //         }
    //         System.out.print("\n");
    //     }
    // }
}

class Triplet<T, U, V> {

    private final T first;
    private final U second;
    private final V third;

    public Triplet(T first, U second, V third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public T getFirst() { return first; }
    public U getSecond() { return second; }
    public V getThird() { return third; }
}