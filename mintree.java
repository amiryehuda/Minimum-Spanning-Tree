
//Amir Yehuda    307966499
//Tamir Levi         308413178

package prim;
import java.util.*;

public class mintree {
	static final int N = 100000;
	 

	static class Edge {
		int source;
		int destination;
		int weight;

     public Edge(int source, int destination, int weight) {
         this.source = source;
         this.destination = destination;
         this.weight = weight;
     }
 }

 static class HeapNode{
     int vertex;
     int key;
 }

 static class ResultSet {
     int parent;
     int weight;
 }

 static class Graph {
     int vertices;
     LinkedList<Edge>[] adjacencylist;
     static Vector<Integer>[] cycles = new Vector[N];
     static int cyclenumber;
  

     Graph(int vertices) {
         this.vertices = vertices;
         adjacencylist = new LinkedList[vertices];
         //initialize adjacency lists for all the vertices
         for (int i = 0; i <vertices ; i++) {
             adjacencylist[i] = new LinkedList<>();
         }
     }

     // Function to mark the vertex with
     // different colors for different cycles
     public void dfs_cycle(int u, int p, int[] color, 
                        int[] mark, int[] par)
     {
         if (color[u] == 2)
         {
             return;
         }
         if (color[u] == 1)
         {
             int cur = p;
             mark[cur] = 1;
             while (cur != u)
             {
                 cur = par[cur];
                 mark[cur] = 1;
             }
             return;
         }
         par[u] = p;
         color[u] = 1;
         // simple dfs on graph
         for(int i=0; i<this.adjacencylist[u].size();i++)
         {
             // if it has not been visited previously
             if (this.adjacencylist[u].get(i).destination == par[u])
             {
                 continue;
             }
             dfs_cycle(this.adjacencylist[u].get(i).destination, u, color, mark, par);
         }
  
         // completely visited.
         color[u] = 2;
     }
  
     // Function to remove the cycles in the graph
     public void removeCircle(int edges, int mark[],Edge newEdge)
     {
     	int maxWeight = 0;
     	int counter =0;
     	Edge maxEdge= newEdge;
     	for(int i=0;i<N;i++)
     	{
     		if(mark[i]==1)
     			counter++;        			
     	} 
     	
     	int[] arrvert=new int [counter+1];
     	int arrvertindex=0;
     	for(int i=0;i<N;i++)
     	{
     		if(mark[i]==1) {
     			arrvert[arrvertindex]=i;
     			arrvertindex++;
     		}
     	} 
     	for(int k=0; k < arrvertindex; k++)
     	{
     		for(int i=0; i< this.adjacencylist[arrvert[k]].size(); i++)
     		{
     			for(int index=0;index <arrvertindex; index++)
     			{
     				if(this.adjacencylist[arrvert[k]].get(i).destination == arrvert[index])
     				{
     					if(this.adjacencylist[arrvert[k]].get(i).weight > maxWeight)
     					{
     						maxEdge = this.adjacencylist[arrvert[k]].get(i);
     						maxWeight = this.adjacencylist[arrvert[k]].get(i).weight;
     					}
     				}
     					
     			}
     		}
     	}
     	for(int i = 0; i < this.adjacencylist[maxEdge.destination].size();i++)
     		if(this.adjacencylist[maxEdge.destination].get(i).destination == maxEdge.source)
     			this.adjacencylist[maxEdge.destination].remove(i);
 
     	this.adjacencylist[maxEdge.source].remove(maxEdge);
     }

     public void addEdge(int source, int destination, int weight) 
     {

         Edge edge1 = new Edge(source, destination, weight);
         adjacencylist[source].add(edge1);

         Edge edge2 = new Edge(destination, source, weight);
         adjacencylist[destination].add(edge2); //for undirected graph
     }

     public void primMST()
     {

         boolean[] inHeap = new boolean[vertices];
         ResultSet[] resultSet = new ResultSet[vertices];
         //keys[] used to store the key to know whether min hea update is required
         int [] key = new int[vertices];
         //create heapNode for all the vertices
         HeapNode [] heapNodes = new HeapNode[vertices];
         for (int i = 0; i <vertices ; i++) {
             heapNodes[i] = new HeapNode();
             heapNodes[i].vertex = i;
             heapNodes[i].key = Integer.MAX_VALUE;
             resultSet[i] = new ResultSet();
             resultSet[i].parent = -1;
             inHeap[i] = true;
             key[i] = Integer.MAX_VALUE;
         }

         //decrease the key for the first index
         heapNodes[0].key = 0;

         //add all the vertices to the MinHeap
         MinHeap minHeap = new MinHeap(vertices);
         //add all the vertices to priority queue
         for (int i = 0; i <vertices ; i++) {
             minHeap.insert(heapNodes[i]);
         }

         //while minHeap is not empty
         while(!minHeap.isEmpty()){
             //extract the min
             HeapNode extractedNode = minHeap.extractMin();

             //extracted vertex
             int extractedVertex = extractedNode.vertex;
             inHeap[extractedVertex] = false;

             //iterate through all the adjacent vertices
             LinkedList<Edge> list = adjacencylist[extractedVertex];
             for (int i = 0; i <list.size() ; i++) {
                 Edge edge = list.get(i);
                 //only if edge destination is present in heap
                 if(inHeap[edge.destination]) {
                     int destination = edge.destination;
                     int newKey = edge.weight;
                     //check if updated key < existing key, if yes, update if
                     if(key[destination]>newKey) {
                         decreaseKey(minHeap, newKey, destination);
                         //update the parent node for destination
                         resultSet[destination].parent = extractedVertex;
                         resultSet[destination].weight = newKey;
                         key[destination] = newKey;
                     }
                 }
             }
         }
         Graph newGraph =  createGraph(resultSet);
         this.adjacencylist = newGraph.adjacencylist;

     }

     public void decreaseKey(MinHeap minHeap, int newKey, int vertex){

         //get the index which key's needs a decrease;
         int index = minHeap.indexes[vertex];

         //get the node and update its value
         HeapNode node = minHeap.mH[index];
         node.key= newKey;
         minHeap.bubbleUp(index);
     }
 }
 static class MinHeap{
      int capacity;
      int currentSize;
      HeapNode[] mH;
      int [] indexes; //will be used to decrease the key


     public MinHeap(int capacity) {
         this.capacity = capacity;
         mH = new HeapNode[capacity + 1];
         indexes = new int[capacity];
         mH[0] = new HeapNode();
         mH[0].key = Integer.MIN_VALUE;
         mH[0].vertex=-1;
         currentSize = 0;
     }

     public void insert(HeapNode node) {
         currentSize++;
         int idx = currentSize;
         mH[idx] = node;
         indexes[node.vertex] = idx;
         bubbleUp(idx);
     }

     public void bubbleUp(int position) {
         int parentIdx = position/2;
         int currentIdx = position;
         while (currentIdx > 0 && mH[parentIdx].key > mH[currentIdx].key) {
             HeapNode currentNode = mH[currentIdx];
             HeapNode parentNode = mH[parentIdx];

             //swap the positions
             indexes[currentNode.vertex] = parentIdx;
             indexes[parentNode.vertex] = currentIdx;
             swap(currentIdx,parentIdx);
             currentIdx = parentIdx;
             parentIdx = parentIdx/2;
         }
     }

     public HeapNode extractMin() {
         HeapNode min = mH[1];
         HeapNode lastNode = mH[currentSize];
//         update the indexes[] and move the last node to the top
         indexes[lastNode.vertex] = 1;
         mH[1] = lastNode;
         mH[currentSize] = null;
         sinkDown(1);
         currentSize--;
         return min;
     }

     public void sinkDown(int k) {
         int smallest = k;
         int leftChildIdx = 2 * k;
         int rightChildIdx = 2 * k+1;
         if (leftChildIdx < heapSize() && mH[smallest].key > mH[leftChildIdx].key) {
             smallest = leftChildIdx;
         }
         if (rightChildIdx < heapSize() && mH[smallest].key > mH[rightChildIdx].key) {
             smallest = rightChildIdx;
         }
         if (smallest != k) {

             HeapNode smallestNode = mH[smallest];
             HeapNode kNode = mH[k];

             //swap the positions
             indexes[smallestNode.vertex] = k;
             indexes[kNode.vertex] = smallest;
             swap(k, smallest);
             sinkDown(smallest);
         }
     }

     public void swap(int x, int y) {
         HeapNode temp = mH[x];
         mH[x] = mH[y];
         mH[y] = temp;
     }

     public boolean isEmpty() {return currentSize == 0; }

     public int heapSize() {return currentSize;}
 }
 
 public static Graph createGraph(ResultSet[] resultSet) 
 {
 		Graph newGraph=new Graph(40);
         for (int i = 1; i <newGraph.vertices ; i++) 
         {
         	Edge tempEdge1= new Edge(i, resultSet[i].parent, resultSet[i].weight);
         	newGraph.adjacencylist[i].add(tempEdge1);
         	Edge tempEdge2= new Edge( resultSet[i].parent,i, resultSet[i].weight);
         	newGraph.adjacencylist[resultSet[i].parent].add(tempEdge2);
         }
		return newGraph;   	
 }
 public static void printGraph(Graph graph){
 int count = 0;
 System.out.println("  \nEdges        weight\n");
 for (int i = 0; i< graph.vertices;i++)
 {
 	for (int j=0;j<graph.adjacencylist[i].size();j++)
 	{
 		if(i < graph.adjacencylist[i].get(j).destination)
 		{
   		  System.out.println("[ "+ i + " - "+ graph.adjacencylist[i].get(j).destination+ " ]" +"          "+ graph.adjacencylist[i].get(j).weight);
   		  count++;
 		}
 	}
 }
 System.out.println("-------------------------------------------------------------");
 }
 
 public static void main(String[] args) {
 	int vertices = 40;
 	 Graph Newgraph = new Graph(vertices);

     // arrays required to color the
     int[] color = new int[N];
     
     // graph, store the parent of node
     int[] par = new int[N];

     // mark with unique numbers
     Newgraph.cyclenumber = 0;
     int[] mark = new int[N];
     
     // store the numbers of cycle
     int edges = Newgraph.adjacencylist.length;
     for (int i = 0; i < N; i++)
    	 Newgraph.cycles[i] = new Vector<>();
     
    
     Newgraph.addEdge(0, 1, 9);
     Newgraph.addEdge(0, 2, 15);
     Newgraph.addEdge(0, 3, 6);
     Newgraph.addEdge(1, 5, 5);
     Newgraph.addEdge(2, 4, 9);
     Newgraph.addEdge(3, 6, 6);
     Newgraph.addEdge(3, 5, 9);
 	Newgraph.addEdge(4, 8, 8);
 	Newgraph.addEdge(4, 7, 10);
 	Newgraph.addEdge(6, 7, 10);
 	Newgraph.addEdge(6, 12, 8);
 	Newgraph.addEdge(5, 12, 11);
 	Newgraph.addEdge(5, 20, 7);
 	Newgraph.addEdge(7, 9, 5);
 	Newgraph.addEdge(8, 9, 7);
 	Newgraph.addEdge(8, 10, 16);
 	Newgraph.addEdge(10, 11, 11);
 	Newgraph.addEdge(10, 19, 25);
 	Newgraph.addEdge(11, 12, 17);
 	Newgraph.addEdge(12, 13, 14);
 	Newgraph.addEdge(12, 14, 5);
 	Newgraph.addEdge(13, 15, 18);
 	Newgraph.addEdge(14, 18, 10);
 	Newgraph.addEdge(14, 19, 7);
 	Newgraph.addEdge(15, 16, 11);
 	Newgraph.addEdge(16, 17, 13);
 	Newgraph.addEdge(14, 17, 8);
 	Newgraph.addEdge(20, 21, 6);
 	Newgraph.addEdge(20, 22, 5);
 	Newgraph.addEdge(19, 23, 9);
 	Newgraph.addEdge(23, 24, 12);
 	Newgraph.addEdge(24, 27, 18);
 	Newgraph.addEdge(27, 26, 13);
 	Newgraph.addEdge(27, 30, 12);
 	Newgraph.addEdge(26, 25, 12);
 	Newgraph.addEdge(25, 28, 7);
 	Newgraph.addEdge(28, 29, 8);
 	Newgraph.addEdge(28, 32, 5);
 	Newgraph.addEdge(29, 34, 10);
 	Newgraph.addEdge(32, 33, 9);
 	Newgraph.addEdge(30, 31, 14);
 	Newgraph.addEdge(34, 35, 12);
 	Newgraph.addEdge(34, 36, 7);
 	Newgraph.addEdge(35, 37, 9);
 	Newgraph.addEdge(36, 38, 11);
 	Newgraph.addEdge(36, 39, 6);

 	System.out.println("========== Initial Graph ==========");
 	printGraph(Newgraph);
 	System.out.println("========== after Prim algorithm: ==========");
 	Newgraph.primMST();
 	printGraph(Newgraph);
 	Edge newEdge = new Edge(22,15,40);
 	 System.out.println("New edge : ( 22, 15, 40) ");
 	System.out.println("========== after adding adge : ==========");
 	Newgraph.addEdge(22, 15, 40);
 	edges = Newgraph.adjacencylist.length;
     // call DFS to mark the cycles
 	Newgraph.dfs_cycle(0, 0, color, mark, par);
 	edges = Newgraph.adjacencylist.length;
     // function to update the graph without cycles
 	Newgraph.removeCircle(edges, mark,newEdge);
 	printGraph(Newgraph);
     }

}