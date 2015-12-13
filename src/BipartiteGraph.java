import javafx.scene.effect.PerspectiveTransform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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


    public Vertex searchAP(List<Vertex> vL) {
        for (Vertex u : vL) {
            u.visited = false;
            u.startVertex = false;
            for (Vertex w : u.adjacentV)
                w.visited = false;
        }
        List<Vertex> queue = new LinkedList<Vertex>();
        Vertex u;
        while ((u = getExposedUnvisited(vL)) != null) { // find an exposed and unvisited vertex u
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

    BipartiteGraph(ArrayList<String> student_list,  ArrayList<String[]> project_list){

        // Create Vertices for students
        for (String student : student_list){
            Vertex v = new Vertex(student);
            this.vertexList.add(v);
        }

        // Create Vertices for projects
        for (String[] project : project_list){
            Vertex v = new Vertex(Arrays.toString(project));
            this.vertexList.add(v);
        }

    }


    public void newEdge(Vertex i, Vertex a) {

        // Create a non-matching edge between i and a
        i.adjacentV.add(a);
        a.adjacentV.add(i);

        // Do below in initialisation
        // this.vertexList.add(i);
        // this.vertexList.add(a);

    }

    public void removeEdge(Vertex i, Vertex a){

        i.mate = null;
        i.adjacentV = null;

        a.mate = null;
        a.adjacentV = null;

        vertexList.remove(i);
        vertexList.remove(a);

    }

    public static void main(String[] args) {

        // Set up Vertices
        Vertex student1 = new Vertex("Student1");
        Vertex student2 = new Vertex("Student2");
        Vertex student3 = new Vertex("Student3");
        //Vertex student4 = new Vertex("Student4");

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


        //////////////////////////////////////////////////////////////////////////////////////////////////

        // Create student_list and project list to use as initial values for the bipartite graph


        ArrayList<String> student_list = new ArrayList<>();
        student_list.add("Student1");
        student_list.add("Student2");
        student_list.add("Student3");

        ArrayList<String[]> project_list = new ArrayList<>();

        String[] project_1 = {"Project1"};
        String[] project_2 = {"Project2"};
        String[] project_3 = {"Project3"};

        project_list.add(project_1);
        project_list.add(project_2);
        project_list.add(project_3);

        BipartiteGraph bG2 = new BipartiteGraph(student_list, project_list);

        // new_matching_edge
        // new_provisioanl_edge
        // remove_matching edge
        // remove_provisional_edge

    }

}
