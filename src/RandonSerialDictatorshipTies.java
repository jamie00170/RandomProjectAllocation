import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Jamie on 13/12/2015.
 */
public class RandonSerialDictatorshipTies {


    public static void RandomSerialWithTies(Map<String, ArrayList<String>> student_preferences, ArrayList<String> project_list){
        // 1. Construct undirected bipartite graph where V = (N U A) and E = empty
        // 1.1 BipartieGrpah bG = new BipartieGraph(student_list, project_list)
        // 2. for each agent in order of current permutation
        //    3. start at first indifference class
        //    4. provisionally add (i, a) to E for all a in agent i's current indifference class
        //    4.1 bG.newEdge(i, a) - for all a in i's indifference class
        //    5. if augmenting path starting from agent i - SearchAP(bG.getVertexList)
        //        5.1. augment along path and modify E accordingly - augment()
        //    else
        //        5.2 provisionally added edges are removed
        //          5.2.1 bG.removeEdge(i, a)
        //        5.3 move onto agent i's next indifference class until reach end of choices/classes
    }


    public static void main(String[] args){

    }
}
