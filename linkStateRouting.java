import java.util.Map;
import java.util.Vector;
import java.io.*;
import java.util.Scanner;
import java.util.*;
import java.text.*;

// Blake Haddad & Andy Nguyen
// CS-570
// Link State Routing Assignment

import java.util.*;

public class linkStateRouting{
    public ArrayList<router> routerList = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> linkedIDs = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> linkCosts = new ArrayList<>();
    HashMap<Integer, String> IDandNetName = new HashMap<>();
    ArrayList<Integer> IDindex = new ArrayList<>();

    public static void main(String[] args){
        linkStateRouting test = new linkStateRouting();
        test.loadFile();
        test.printRoutersLinksAndCosts();
        test.createRouters();
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

    public void printRoutersLinksAndCosts()
    {
        for (int k = 0; k < linkedIDs.size(); k++){
            System.out.println("ID: " + IDindex.get(k));
            System.out.println("Linked IDs: " + linkedIDs.get(k));
            System.out.println("Linked costs: " + linkCosts.get(k));
        }
    }

    public void createRouters(){
        int i = 0;
        for(Integer id : IDandNetName.keySet()){
            HashMap<Integer,String> connectionMap = formConnectedRoutersMap(i);
            HashMap<Integer,Integer> connectionCostMap = formConnectedRouterCostMap(i);
            routerList.add(new router(id,IDandNetName.get(id), connectionMap, connectionCostMap, true));
            i++;
        }
    }

    public HashMap<Integer,String> formConnectedRoutersMap(Integer index){
        HashMap<Integer,String> connections = new HashMap<>();
        ArrayList<Integer> connectedIDs = linkedIDs.get(index);
        for(int i = 0; i<connectedIDs.size(); i++){
            connections.put(connectedIDs.get(i), IDandNetName.get(connectedIDs.get(i)));
        }
        return connections;
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
    public HashMap<Integer,String> connectedRouters = new HashMap<Integer,String>();
    public HashMap<Integer,Integer> connectedRouterCost = new HashMap<Integer,Integer>();
    public String routerName;
    public Integer ID;
    public graph g;
    boolean onOffStatus;

    router(Integer ID, String name, HashMap<Integer,String> routerMap, HashMap<Integer,Integer> costMap, boolean onOff){
        this.ID = ID;
        this.routerName = name;
        this.connectedRouters = routerMap;
        this.connectedRouterCost = costMap;
        this.onOffStatus = onOff;
    }

    public String getRouterName(){
        return this.routerName;
    }

    public void setRouterName(String name){
        this.routerName = name;
    }

    public void recievePacket(LSP packet, Integer forwarderId){

    }

    public void originatePacket(){
        
    }
}

class LSP{
    public String originRouter;
    public Integer seqNum;
    public Integer timeToLive;
    //list of network names
    //list of each directly connected router, network behind it, and cost

    public void decrementTTL(){
        this.timeToLive = this.timeToLive - 1;
    }

    public Integer getTTL(){
        return this.timeToLive;
    }
}

class graph{
    public int[][] adjacencyMtrx;
    public int totalElements;
    public List<node> nodeList = new ArrayList<>();

    graph(){

    }

    graph(int numElements){
        adjacencyMtrx = new int[numElements][numElements];
        this.totalElements = numElements;
    } 

    public void addEdge(int start, int end){
        adjacencyMtrx[start][end] = 1;
        adjacencyMtrx[end][start] = 1;
    }

    public void addNode(int value){
        nodeList.add(new node(value));
    }

    public void printNodes(){
        for(int i = 0; i<nodeList.size(); i++){
            System.out.println(nodeList.get(i).getValue());
        }
    }

    public void printAdjMatrix(){
        int a = adjacencyMtrx.length;
        int b = adjacencyMtrx[0].length;
        System.out.println(a + " " + b);
        for(int i = 0; i<adjacencyMtrx.length; i++){
            for(int j = 0; j<adjacencyMtrx[0].length; j++){
                System.out.print(adjacencyMtrx[i][j] + " ");
            }
            System.out.print("\n");
        }
    }
}

class node{
    int value;

    node(int val){
        this.value = val;
    }

    int getValue(){
        return this.value;
    }

    void setValue(int val){
        this.value = val;
    }
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