import java.io.Serializable;
import java.util.*;

/**
 * Created by Jamie on 06/12/2015.
 */
public class BipartiteGraph implements Cloneable, Serializable {

    public ArrayList<Vertex> vertexList;

    public Vertex getExposedUnvisited(List<Vertex> vL){
        for (Vertex x : vL) {
            if(x.mate==null && !x.visited) {
                for (Vertex w : x.adjacentV)
                    if (!w.visited) {
                        return x;
                    }
            }
        }
        return null;
    }


    public Vertex searchAP() {
        for (Vertex u : this.vertexList) {
            u.visited = false;
            u.startVertex = false;
            for (Vertex w : u.adjacentV)
                w.visited = false;
        }
        List<Vertex> queue = new LinkedList<Vertex>();
        Vertex u;
        while ((u = getExposedUnvisited(this.vertexList)) != null) { // find an exposed and unvisited vertex u
            queue.add(u);
            u.startVertex = true; // first vertex in alternating path
            while (queue.size() > 0) {
                Vertex v = queue.remove(0); // from front of queue
                v.visited = true;
                for (Vertex w : v.adjacentV)
                    if (!w.visited) {
                        w.visited = true;
                        w.predecessor = v;
                        if (w.mate == null) { // w is exposed
                            System.out.println("Augmenting path found!");
                            return w;
                        } else
                            queue.add(w.mate);
                    }
            }
        }
        System.out.println("No Augmenting path found!");
        return null;
    }

    public void augment(Vertex endVertex) {
        Vertex u, w, temp;
        w = endVertex;
        u = w.predecessor;
        while (!u.startVertex) {
            System.out.println(u.name);
            temp = u.mate;
            u.mate = w;
            w.mate = u;
            w = temp;
            u = w.predecessor;
        }
        u.mate = w;
        w.mate = u;
    }


    BipartiteGraph(ArrayList<String> student_list,  ArrayList<String> project_list){

        this.vertexList = new ArrayList<>();
        // Create Vertices for students
        for (String student : student_list){
            Vertex v = new Vertex(student, true);
            this.vertexList.add(v);
        }

        // Create Vertices for projects
        for (String project : project_list){
            Vertex v = new Vertex(project, false);
            this.vertexList.add(v);
        }

    }

    BipartiteGraph (ArrayList<Vertex> vertexList){
        this.vertexList = vertexList;
    }

    BipartiteGraph(BipartiteGraph bipartiteGraph){
        this.vertexList = bipartiteGraph.vertexList;
    }

    BipartiteGraph(){}

    public void new_matching_edge(String student, String project){
        for(Vertex v : vertexList){
            if(v.name.equals(student)){
                for (Vertex u : vertexList){
                    if (u.name.equals(project)){
                        v.mate = u;
                    }
                }
            }
        }
        for(Vertex v : vertexList){
            if(v.name.equals(project)){
                for (Vertex u : vertexList){
                    if (u.name.equals(student)){
                        v.mate = u;
                    }
                }
            }
        }

    }


    public void new_provisional_edge(String student, String project) {
        for (Vertex v : vertexList) {
            if (v.name.equals(student)) {
                for (Vertex u : vertexList) {
                    if (u.name.equals(project)) {
                        v.adjacentV.add(u);
                    }
                }
            }

        }

        for (Vertex v : vertexList) {
            if (v.name.equals(project)) {
                for (Vertex u : vertexList) {
                    if (u.name.equals(student)) {
                        v.adjacentV.add(u);
                    }
                }
            }
        }

    }

    public void remove_provisional_edge(String student, String project){

        //List<Vertex> toRemove = new ArrayList<>();

        for (Vertex v : vertexList) {
            if (v.name.equals(student)) {
                for (Vertex u : vertexList) {
                    if (u.name.equals(project)) {
                        v.adjacentV.remove(u);
                    }
                }
            }
            if (v.name.equals(project)) {
                for (Vertex u : vertexList) {
                    if (u.name.equals(student)) {
                        v.adjacentV.remove(u);
                    }
                }

            }
        }
    }


    public void cleanUp(){

        ArrayList<String> edges_to_remove = new ArrayList<>();

        for (Vertex v : this.vertexList){
            if (v.isStudent) {
                if (v.mate != null) {
                    if (v.adjacentV.size() > 0) {
                        for (Vertex u : v.adjacentV) {
                            if (u.name.equals(v.mate.name)) {
                                edges_to_remove.add(v.name);
                                edges_to_remove.add(u.name);
                            }
                        }
                    }
                }
            }
        }

        int i = 0;
        while (i + 1 < edges_to_remove.size()){
            System.out.println("Removing provisional edge between: " + edges_to_remove.get(i) + " " + edges_to_remove.get(i+1));
            this.remove_provisional_edge(edges_to_remove.get(i), edges_to_remove.get(i + 1));
            i = i + 2;

        }


    }

    public boolean hasEdge(){

        for (Vertex v : this.vertexList){
            if (v.mate != null){
                return true;
            }
        }
        return false;
    }

    public LinkedList<Vertex> verticesInCycle(HashMap<Vertex, ArrayList<Vertex>> visitedBy, Vertex last_vertex, Vertex start_vertex){



        System.out.println("Entered vertices in cycle!");
        /**
        for (Vertex v : visitedBy.keySet()){
            System.out.print(v.name + ": ");
            for (Vertex u : visitedBy.get(v))
                System.out.print(u.name + " ");
            System.out.println();
        }
        **/
        LinkedList<Vertex> path = new LinkedList<>();

        //System.out.println("Last vertex: " + last_vertex.name);
        // Look for last_vertex in values

        Vertex current_vertex = new Vertex();
        for (Vertex v : visitedBy.keySet()){
            if (visitedBy.get(v).contains(last_vertex)){
                path.add(last_vertex);
                //System.out.println("Adding " + last_vertex.name + " to the path!");
                current_vertex = v;
            }
        }

        // While the path doesn't contain the start vertex
        while (!path.contains(start_vertex)) {

            for (Vertex v : visitedBy.keySet()) {
                if (visitedBy.get(v).contains(current_vertex)) {
                    path.add(current_vertex);
                    //System.out.println("Adding " + current_vertex.name + " to the path!");
                    if (current_vertex.equals(start_vertex)){
                        break;
                    }
                    current_vertex = v;
                }
            }
        }

        System.out.println("final path...");
        for (Vertex vertex : path){
            System.out.println(vertex.name);
        }
        System.out.println("Exited Vertices in cycle!");
        return path;
    }


    public Queue<Vertex> find_cycle(Vertex startVertex) {

        System.out.println("\n\nBeginning Depth First Search to find cycle .......\n");
        System.out.println("Starting from vertex: " + startVertex.name);

        Stack<Vertex> stack = new Stack<>();

        // keeps track of vertex visited another vertex - used to reconstruct cycle by back tracking
        HashMap<Vertex, ArrayList<Vertex>> visitedBy = new HashMap<>();

        // Set up vertices and push the start vertex onto the stack
        for (Vertex v : this.vertexList) {
            // Initialise visited By
            visitedBy.put(v, new ArrayList<>());
            v.visited = false;
            v.startVertex = false;
            if (v.name.equals(startVertex.name)) {
                v.startVertex = true;
                stack.push(v);
            }
        }


        /**
        for (Vertex v : visitedBy.keySet()){
            System.out.print(v.name + ": ");
            for (Vertex u : visitedBy.get(v))
                System.out.print(u.name + " ");
            System.out.println();
         }
         **/



        boolean after_first = false;

        while (!(stack.empty())) {
            // Pop the top vertex on the stack and store it in u
            Vertex u = stack.pop();
            // Print that u has been visited
            System.out.println("Visited: " + u.name);

            //visited_vertices.push(u);
            // If u hasn't already been visited
            if (!(u.visited = false)) {
                u.visited = true;
                // If u has an adjacent vertex
                if (u.adjacentV != null) {
                    // For each of u's adjacent vertices
                    for (Vertex vertex : u.adjacentV) {
                        // if vertex (one of u's adjacent vertices) has not been visited or is the startVertex and the algorithm
                        // has already looked at the first vertex
                        if (!(vertex.visited) || (vertex.startVertex && after_first)) {
                            stack.push(vertex);
                            visitedBy.get(u).add(vertex);
                            // If the vertex is th start vertex then we have come back to the start and therefore there
                            // is a cycle starting at the startVertex
                            if (vertex.startVertex) {
                                System.out.println("Cycle found starting at Vertex: " + vertex.name);
                                System.out.println("\n\n");

                                // Use visited by to backtrack and return the vertices in the cycle
                                return verticesInCycle(visitedBy, u, startVertex) ;
                            }
                        }
                    }
                }
                // if the vertex has a mate
                if (u.mate != null) {
                    if (!(u.mate.visited) || (u.startVertex && after_first)) {
                        stack.push(u.mate);
                        visitedBy.get(u).add(u.mate);
                    }
                    // If back to the start vertex - cycle found
                    if (u.mate.startVertex) {
                        System.out.println("Cycle found starting at Vertex: " + u.mate.name);

                        System.out.println("\n");
                        // Use visited by to backtrack and return the vertices in the cycle
                        return verticesInCycle(visitedBy, u, startVertex);


                        // Back track through verticies in cycle starting from last node in cycle
                    }
                }
            }
            after_first = true;
        }
        System.out.println("No cycle Found!");
        return new PriorityQueue<>();
    }


    public void remove_matching_edge(String v, String u){

        for (Vertex vertex : vertexList){
            if (vertex.name.equals(v)){
                vertex.mate = null;
            }
            if (vertex.name.equals(u)){
                vertex.mate = null;
            }
        }

    }

    public void remove_matching_edge_directed(String v, String u){

        for (Vertex vertex : vertexList){
            if (vertex.name.equals(v)){
                vertex.mate = null;
            }

        }

    }

    public BipartiteGraph clone() throws CloneNotSupportedException{

        ArrayList<Vertex> new_vertexList = new ArrayList<>();

        for (Vertex v : vertexList){
            Vertex clone_vertex = v.clone();
            new_vertexList.add(clone_vertex);
        }

        BipartiteGraph clone = new BipartiteGraph(new_vertexList);
        return clone;

    }

    public void setVertexList(ArrayList<Vertex> vertexList){
        this.vertexList = vertexList;
    }

    public ArrayList<Vertex> getVertexList(){
        return this.vertexList ;
    }

    /**
     * Remove all of the associated edges of both end points of v. Edge is from v - v.mate
     * @param v
     */
    public void remove_associated_edges(Vertex v) {

        System.out.println("Vertex list entered to remove associated edges...");
        for (Vertex ver : vertexList){
           System.out.println(ver.toString());
        }
        System.out.println();

        // Remove all non matching edges coming in and out of v and v.mate

        for (Vertex u : vertexList) {
            // if u has any adjacent edges - check to see if v is an adjacent edge
            if (u.adjacentV != null && u.adjacentV.size() > 0) {
                //System.out.println("Looking at " + u.name + "'s adjacent list");
                for (Vertex vertex : u.adjacentV) {
                    // if v is in any adjacency lists remove it
                    if (vertex.equals(v)) {
                        System.out.println("Removing: " + v.name + " from " + u.name + "'s adjacent list");
                        u.adjacentV.remove(v);
                    }
                }
            }
        }

        // Set v.mates adjacent list to empty
        System.out.println("Removing  all of " + v.mate.name + "'s adjacent verticies");
        v.mate.adjacentV.clear();

    }

    public void exchange_edges(Queue<Vertex> verticesInCycle){
        // **Needs to be used in depth first search method so right edges are changed**

        HashMap<Vertex, Vertex> edges_to_remove = new HashMap<>();
        HashMap<Vertex, Vertex> edges_to_add = new HashMap<>();


        for (Vertex v: verticesInCycle) {
            // Add edges to be added to the matching to the hash map
            if (v.adjacentV.size() > 0) {
                int i = 0;
                while(i < v.adjacentV.size()){
                    if (verticesInCycle.contains(v.adjacentV.get(i))){
                        edges_to_add.put(v.adjacentV.get(i), v);
                        break;
                    }
                    i++;
                }
            }
            // Add edges to be removed to hash map
            if (v.mate != null) {
                edges_to_remove.put(v, v.mate);
            }
        }


        for (Map.Entry<Vertex, Vertex> entry : edges_to_remove.entrySet()){
            // if vertex/edge is currently in the matching reverse it and remove it from matching
            System.out.println("Removing edge in matching between: " + entry.getKey().name + " and " + entry.getValue().name);
            entry.getKey().mate = null;
            entry.getValue().adjacentV.add(entry.getKey());
        }

        for (Map.Entry<Vertex, Vertex> entry : edges_to_add.entrySet()){
            // if edge is not currently in the matching reverse it therefore adding it to the matching
            System.out.println("Adding edge to the matching between: " + entry.getKey().name + " and " + entry.getValue().name);
            entry.getKey().mate = entry.getValue();
            entry.getValue().adjacentV.remove(entry.getKey());
            // Add edge to matching and remove non matching edge



        }

    }


    public static BipartiteGraph undirectedToDirected(BipartiteGraph bG){

        for (Vertex v: bG.vertexList) {
            if (v.mate != null && v.isStudent) {
                Vertex u = v.mate;
                u.mate = null;
            }
            // if v is a student make its adjacent v null, i.e remove adjacent edges pointing from students to projets
            if (v.adjacentV != null && v.isStudent){
                // Have to make adjacent edge point to student
                for (Vertex u :  v.adjacentV){
                    if (!u.adjacentV.contains(v)) {
                        u.adjacentV.add(v);
                    }
                }
                // then make student's adjacent edges null
                v.adjacentV = new ArrayList<Vertex>();
            }

        }
        return bG;
    }


    public static void main(String[] args) {


        //////////////////////////////////////////////////////////////////////////////////////////////////

        // Create student_list and project list to use as initial values for the bipartite graph

        ArrayList<String> student_list = new ArrayList<>();
        student_list.add("Student1");
        student_list.add("Student2");
        student_list.add("Student3");

        ArrayList<String> project_list = new ArrayList<>();

        String project_1 = "Project1";
        String project_2 = "Project2";
        String project_3 = "Project3";

        project_list.add(project_1);
        project_list.add(project_2);
        project_list.add(project_3);

        BipartiteGraph bG2 = new BipartiteGraph(student_list, project_list);

        bG2.new_matching_edge("Student1", "Project2");
        bG2.new_matching_edge("Student2", "Project3");
        bG2.new_matching_edge("Student3", "Project1");

        //bG2.new_provisional_edge("Project2", "Student1");
        bG2.new_provisional_edge("Project1", "Student2");
        bG2.new_provisional_edge("Project1", "Student1");
        bG2.new_provisional_edge("Project2", "Student2");
        bG2.new_provisional_edge("Project2", "Student3");
        bG2.new_provisional_edge("Project3", "Student1");
        bG2.new_provisional_edge("Project3", "Student3");


        bG2 = undirectedToDirected(bG2);

        Vertex e = new Vertex();

        System.out.println("Graph input.....");
        for (Vertex v : bG2.vertexList){
            if (v.name.equals("Student2")){
                e = v;
            }
            System.out.println(v.toString());
        }

        /**
        System.out.println("------------------------------");
        Queue<Vertex> verticesInCycle = new PriorityQueue<>();
        Vertex startVertex;
        for (Vertex v : bG2.vertexList){
            if (v.name.equals("Student1")){
                startVertex = v;
                bG2.find_cycle(startVertex);
                //if (!verticesInCycle.isEmpty()){
                //    bG2.exchange_edges(verticesInCycle);
                //}
            }
        }
         **/


        bG2.remove_associated_edges(e);

        System.out.println("Graph after removing associated edges.....");
        for (Vertex v : bG2.vertexList){
            System.out.println(v.toString());
        }

    }

}
