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

    public static void main(String[] args) {

        // Set up Vertices
        Vertex student1 = new Vertex("Student1");
        Vertex student2 = new Vertex("Student2");
        Vertex student3 = new Vertex("Student3");
        Vertex student4 = new Vertex("Student4");

        Vertex project1 = new Vertex("Project1");
        Vertex project2 = new Vertex("Project2");
        Vertex project3 = new Vertex("Project3");
        Vertex project4 = new Vertex("Project4");

        student1.mate = project2;
        student2.mate = project3;
        student3.mate = project4;

        project2.mate = student1;
        project3.mate = student2;
        project4.mate = student3;

        student1.adjacentV.add(project1);
        student1.adjacentV.add(project2);
        student1.adjacentV.add(project3);
        student1.adjacentV.add(project4);

        student2.adjacentV.add(project1);
        student2.adjacentV.add(project2);
        student2.adjacentV.add(project3);
        student2.adjacentV.add(project4);

        student3.adjacentV.add(project1);
        student3.adjacentV.add(project2);
        student3.adjacentV.add(project3);
        student3.adjacentV.add(project4);

        student4.adjacentV.add(project1);
        student4.adjacentV.add(project2);
        student4.adjacentV.add(project3);
        student4.adjacentV.add(project4);

        project1.adjacentV.add(student1);
        project1.adjacentV.add(student2);
        project1.adjacentV.add(student3);
        project1.adjacentV.add(student4);

        project2.adjacentV.add(student1);
        project2.adjacentV.add(student2);
        project2.adjacentV.add(student3);
        project2.adjacentV.add(student4);

        project3.adjacentV.add(student1);
        project3.adjacentV.add(student2);
        project3.adjacentV.add(student3);
        project3.adjacentV.add(student4);

        project4.adjacentV.add(student1);
        project4.adjacentV.add(student2);
        project4.adjacentV.add(student3);
        project4.adjacentV.add(student4);

        // Create a vertex list and add it to an Instance of Bipartite graph
        List<Vertex> vL = new LinkedList<>();

        vL.add(student1);
        vL.add(project1);

        vL.add(student2);
        vL.add(project2);

        vL.add(student3);
        vL.add(project3);

        vL.add(student4);
        vL.add(project4);

        BipartiteGraph bG = new BipartiteGraph(vL);

        bG.searchAP(vL);

    }

}
