import java.util.Map;

// Blake Haddad & Andy Nguyen
// CS-570
// Link State Routing Assignment

import java.util.*;

public class linkStateRouting{

    public static void main(String[] args){

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