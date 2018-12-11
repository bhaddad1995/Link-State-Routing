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

    public void loadFile(){
        HashMap<Integer, String> IDandNetName = new HashMap<>();
        ArrayList<Integer> IDindex = new ArrayList<>();
        String[] splitID;
        String[] splitLink;
        String splitnetName;

        ArrayList<ArrayList<Integer>> linkedIDs = new ArrayList<>();
        LinkedList<Integer> linkCosts = new LinkedList<Integer>();
        ArrayList<Integer> temp = new ArrayList<>();
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
                        for(Integer nt : temp){
                            copiedTemp.add(new Integer(nt));
                        }
                        System.out.println("temp: " + temp);
                        linkedIDs.add(copiedTemp);
                        System.out.println("Being cleared");
                        temp.clear();
                    }
                    count++;

                }

                if(line.charAt(0) == ' '){
                    splitLink = line.split(" ", 2);
                    temp.add(Integer.parseInt(splitLink[1]));
                    System.out.println("Being added to temp: " + Integer.parseInt(splitLink[1]));
                }

                for (int o = 0; o < temp.size(); o++){
//                    System.out.println(temp.get(o));

                }



                i++;


            }
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

        for (int k = 0; k < linkedIDs.size(); k++){
            System.out.println("ID: " + k);
            System.out.println(linkedIDs.get(k));
        }
    }

    public void printAll()
    {

    }

    public static void main(String[] args){
        linkStateRouting test = new linkStateRouting();
        test.loadFile();
    }

}

class router{
    public HashMap<String,Integer> connectedRouters = new HashMap<String,Integer>();
    public String routerName;
    public graph g;

    public String getRouterName(){
        return this.routerName;
    }

    public void setRouterName(String name){
        this.routerName = name;
    }

    public void recievePacket(LSP packet, String forwarderId){

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