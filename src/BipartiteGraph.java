import javafx.scene.effect.PerspectiveTransform;

import java.util.*;

/**
 * Created by Jamie on 06/12/2015.
 */
public class BipartiteGraph {

    public List<Vertex> vertexList;

    public Vertex getExposedUnvisited(List<Vertex> vL){
        for (Vertex x : vL) {
            if(x.mate==null && !x.visited) {
                System.out.println("Exposed Vertex: " + x.name);
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

    // Create a constructor that creates a bipartite graph
    BipartiteGraph(List<Vertex> vL){
        this.vertexList = vL;

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

        List<Vertex> toRemove = new ArrayList<>();

        for (Vertex v : vertexList) {
            if (v.name.equals(student) || v.name.equals(project)){
                v.adjacentV = new ArrayList<Vertex>();
            }
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

    public boolean depth_first_search(Vertex startVertex) {

        System.out.println("\n\nBeginning Depth First Search.......\n");

        Stack<Vertex> stack = new Stack<>();

        for (Vertex v : this.vertexList) {
            v.visited = false;
            v.startVertex = false;
            if (v.name.equals(startVertex.name)) {
                v.startVertex = true;
                stack.push(v);
            }
        }


        boolean after_first = false;
        while (!(stack.empty())) {
            Vertex u = stack.pop();
            System.out.println("Visited: " + u.name);
            if (!(u.visited = false)) {
                u.visited = true;
                if (u.adjacentV != null) {
                    for (Vertex vertex : u.adjacentV) {
                        if (!(vertex.visited) || (vertex.startVertex && after_first)) {
                            stack.push(vertex);
                            if (vertex.startVertex) {
                                System.out.println("Cycle found starting at Vertex: " + vertex.name);
                                return true;
                            }
                        }
                    }
                }
                if (u.mate != null) {
                    if (!(u.mate.visited) || (u.startVertex && after_first)) {
                        stack.push(u.mate);
                    }
                }
            }
            after_first = true;
        }
        return false;
    }



    public void remove_matching_edge(Vertex v){

        v.mate = null;
    }

    public BipartiteGraph clone() throws CloneNotSupportedException{

        return (BipartiteGraph) super.clone();

    }

    public void remove_associated_edges(Vertex v){

        v.mate.adjacentV = null;
        v.mate = null;
        v.adjacentV = null;

        vertexList.remove(v);
        vertexList.remove(v.mate);
    }

    public void exchange_edges(){
        // **Needs to be used in depth first search method so right edges are changed**

        for (Vertex v: vertexList){
            // if vertex/edge is currently in the matching reverse it and remove it from matching
            if (v.mate != null){
                v.mate.adjacentV.add(v);
                v.mate = null;
            }
            if (v.adjacentV != null){
                // Needs fixed
                v.mate = v.adjacentV.get(0);

                v.adjacentV.remove(0);
            }


        }
    }


    public static void main(String[] args) {

        /**
        Set up Vertices
        Vertex student1 = new Vertex("Student1");
        Vertex student2 = new Vertex("Student2");
        Vertex student3 = new Vertex("Student3");

        Vertex project1 = new Vertex("Project1");
        Vertex project2 = new Vertex("Project2");
        Vertex project3 = new Vertex("Project3");

        student1.mate = project2;
        project2.mate = student1;

        student1.adjacentV.add(project1);
         student1.adjacentV.add(project2);
         student2.adjacentV.add(project2);
         project1.adjacentV.add(student1);

        project2.adjacentV.add(student1);
        project2.adjacentV.add(student2);

        // Create a vertex list and add it to an Instance of Bipartite graph
        List<Vertex> vL = new LinkedList<>();

        vL.add(student1);
        vL.add(project1);

        vL.add(student2);
        vL.add(project2);

        vL.add(student3);
        vL.add(project3);

        //vL.add(student4);
        //vL.add(project4);

        BipartiteGraph bG = new BipartiteGraph(vL);

        for (Vertex v : bG.vertexList){
            System.out.println(v.toString());
        }

        bG.searchAP(vL);


        bG.augment(student2);

        for (Vertex v : bG.vertexList){
            System.out.println(v.toString());
        }
        **/

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

        bG2.new_provisional_edge("Student1", "Project1");
        bG2.new_matching_edge("Student1", "Project2");
        bG2.new_provisional_edge("Student2", "Project2");
        bG2.new_provisional_edge("Student1", "Project2");

        for (Vertex v : bG2.vertexList){
            System.out.println(v.toString());
        }

        Vertex end_v = bG2.searchAP();

        bG2.augment(end_v);

        for (Vertex v : bG2.vertexList){
            System.out.println(v.toString());
        }

    }

}
