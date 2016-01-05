import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Jamie on 23/11/2015.
 */

public class Vertex {

    public String name;
    public boolean visited, startVertex;
    public boolean isStudent;
    public Vertex predecessor, mate;
    public List<Vertex> adjacentV; // graph represented
    // by adjacency lists


    public Vertex(String name, boolean isStudent){

        if (name.startsWith("[")) {
            this.name = name.substring(1, (name.length()-1));
        } else {
            this.name = name;
        }

        this.mate = null;
        this.isStudent =isStudent;
        adjacentV = new ArrayList<Vertex>();

    }

    public String toString(){
        String return_string = "";
        return_string = return_string.concat(this.name + "\n");
        if (this.mate != null)
            return_string = return_string.concat("Mate: " + this.mate.name + "\n");
        /**
        if (this.adjacentV != null) {
            String adjacentString = this.adjacentV.toString();
            return_string = return_string.concat("Adjacent Vertices: " + adjacentString + "\n");
        }
         **/
        return return_string;
    }

}
