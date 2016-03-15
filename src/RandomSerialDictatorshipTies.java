

import org.apache.commons.math3.fraction.Fraction;

import java.util.*;

/**
 * Created by Jamie on 13/12/2015.
 */
public class RandomSerialDictatorshipTies {

    private static UtilityMethods utilityMethods = new UtilityMethods();


    public static String[][] calculate_values_of_matrix(String[][] matrix, BipartiteGraph bG){
        for (Vertex v : bG.vertexList){
            if (v.mate != null){
                if (v.isStudent) {
                    System.out.println("Incremenitng value between: " + v.name + " and " + v.mate.name);
                    utilityMethods.incrementValue(matrix, v.name, v.mate.name, new Fraction(1));
                }else{
                    utilityMethods.incrementValueProject(matrix, v.name, v.mate.name, new Fraction(1));
                }
            }
        }
        return matrix;
    }

    public static String[][] calculate_initial_values_of_matrix(String[][] matrix, BipartiteGraph bG){
        for (Vertex v : bG.vertexList){
            if (v.mate != null && v.isStudent) {
                System.out.println("Incremenitng value between: " + v.name + " and " + v.mate.name);
                utilityMethods.incrementValue(matrix, v.name, v.mate.name, new Fraction(1));
            }
        }
        return matrix;
    }


    public static BipartiteGraph undirectedToDirected(BipartiteGraph bG){

        for (Vertex v: bG.vertexList) {
            if (v.mate != null && v.isStudent) {
                Vertex u = v.mate;
                u.mate = null;
            }
            // if v is a student make its adjacent v null, i.e remove adjacent edges pointing from students to projets
            if (v.adjacentV != null && v.isStudent){
                v.adjacentV = new ArrayList<Vertex>();
            }

        }
        return bG;
    }


    public static void enum_perfect_matchings_iter(BipartiteGraph G, String[][] M){
        // Step 1: If G has no edge, stop
        if (!(G.hasEdge())){
            return;
        }
        // 2. Try to find a cycle in G by DFS therefore confirming if there is another perfect matching
        HashSet<Vertex> verticesInCycle = new HashSet<>();
        int i = 0;
        while (i < G.vertexList.size()){// - Cycle could start from any vertex?
            verticesInCycle = G.find_cycle(G.vertexList.get(i));
            if (!verticesInCycle.isEmpty()){
                // Step 4: Find a perfect matching M' by exchanging edges along the cycle. Output M'
                //System.out.println("Current graph...");
                //for (Vertex v : G.vertexList){
                //    System.out.println(v);
                //}

                System.out.println("Current Matching....");
                for (String[] row : M){
                    System.out.println(Arrays.toString(row));
                }
                System.out.println("Exchanging edges......");
                G.exchange_edges(verticesInCycle);
                // output M'
                System.out.println("New matching......");
                String[][] matching_to_output = calculate_values_of_matrix(M, G);
                for (String[] row : matching_to_output){
                    System.out.println(Arrays.toString(row));
                }
                System.out.println("Graph after exchaning edges...");
                for (Vertex v : G.vertexList){
                    System.out.println(v);
                }
                return;

            }
            i++;
            // 3. If there is no cycle found stop algorithm
            if ((i == G.vertexList.size()) && verticesInCycle.isEmpty()){
                System.out.println("Algorithm Stopped no cycle found!");
                return;
            }
        }

        System.out.println("Index of edge from: " + i);
        // use vertex G.vertexList.get(i-1) as edge, e when .mate is not null then the vertex is also in the matching
        Vertex edgeFrom = G.vertexList.get(i);
        /**
        int j = 1;
        while (edgeFrom.mate == null){
            edgeFrom = G.vertexList.get(i-j);
            j++;
        }
        System.out.println("Edge From: " + edgeFrom.name);
         **/

        try {

            BipartiteGraph g_plus = G.clone();
            // Generate G+(e)
            /**
            System.out.println("Printing g_plus...");
            for (Vertex v : g_plus.vertexList){
                System.out.println(v);
            }
             **/
            g_plus.remove_associated_edges(edgeFrom);
            // Call enum_perfect_matchings_iter(G+(e), M)
            enum_perfect_matchings_iter(g_plus, M);

        }catch (CloneNotSupportedException e){
            e.printStackTrace();
        }

        try {

            BipartiteGraph g_minus = G.clone();
            // Generate G-(e)
            g_minus.remove_matching_edge(edgeFrom.name, edgeFrom.mate.name);
            // Call enum_perfect_matchings_iter(G-(e), M')
            enum_perfect_matchings_iter(g_minus, M);

        } catch (CloneNotSupportedException e){
            e.printStackTrace();
        }

    }

    public static void enum_perfect_matchings(BipartiteGraph G, String[][] M){
        // Step 1: Find a perfect matching M of G and output M. If M is not found, stop.
        // Already found M in with RSDT algorithm

        // Transform undirected graph into a directed graph
        G = undirectedToDirected(G);

        System.out.println("-------------------------------------------");
        System.out.println("Graph input to emun_perfect_matchings_iter");
        for (Vertex v : G.vertexList){
            System.out.println(v.toString());
        }
        // Step 2: Step 2: Trim unnecessary edges from G by a strongly connected component
        // decomposition algorithm with D(G, M)
        // TODO - Trim unnecessary edges from G by a strongly connected component decomposition algorithm
        // Step 3: Call Enum Perfect Matchings Iter (G, M)
        enum_perfect_matchings_iter(G, M);
    }


    public static void RandomSerialWithTies(HashMap<String, ArrayList<String[]>> student_preferences, ArrayList<String> project_list){

        ArrayList<String> student_list = new ArrayList<>();
        for (String student: student_preferences.keySet()){
            student_list.add(student);
        }

        String[][] matrix = utilityMethods.setUpMatrix(student_preferences.keySet(), project_list);

        // 1. Construct undirected bipartite graph where V = (N U A) and E = empty
        BipartiteGraph bG = new BipartiteGraph(student_list, project_list);

        // 2. for each agent in order of current permutation
        String[] permutation = {"Student2", "Student1", "Student3"};

        //ArrayList<String[]> permutations = new ArrayList<>();

        for (String student : permutation) {
            // Until end of student's indifference classes is reached or student is matched
            int j = 0;
            while (j < student_preferences.get(student).size()) {
                // 3. Look at the agents ith indifference class
                String[] indifference_class = student_preferences.get(student).get(j);

                //4. provisionally add (i, a) to E for all a in agent i's current indifference class
                for (String project : indifference_class) {
                    //4.1 bG.newEdge(i, a) - for all a in i's indifference class
                    bG.new_provisional_edge(student, project);
                }
                Vertex end_v;
                if ((end_v = bG.searchAP()) != null) {
                    // 5.1. augment along path and modify E accordingly - augment()
                    bG.augment(end_v);
                    /**
                     System.out.println("Printing Graph.....");
                     for (Vertex v : bG.vertexList){
                     System.out.println(v.toString());
                     }
                     **/
                } else {
                    // 5.2 provisionally added edges are removed
                    for (String project : indifference_class) {
                        //4.1 bG.newEdge(i, a) - for all a in i's indifference class
                        bG.remove_provisional_edge(student, project);
                    }
                }
                // 5.3 move onto agent i's next indifference class until reach end of choices/classes
                j++;
            }

            // increment appropriate values in matrix at end of permutation i.e. where a vertex has a mate (matching edge)

        }

        System.out.println("Printing Graph.....");
        for (Vertex v : bG.vertexList){
            System.out.println(v.toString());
        }

        System.out.println("Printing matrix.....");
        for (String[] row: matrix){
            System.out.println(Arrays.toString(row));
        }

        // Calculate values for matrix from graph
        calculate_initial_values_of_matrix(matrix, bG);

        // Clean up graph, i.e. remove adjacent edges where there is a matching one
        bG.cleanUp();

        System.out.println("Printing graph after clean up....");
        for (Vertex v : bG.vertexList){
            System.out.println(v.toString());
        }
        // call perfect emum here?
        enum_perfect_matchings(bG, matrix);

        for (String[] row : matrix){
            System.out.println(Arrays.toString(row));
        }

    }

    public static int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static HashMap<String, ArrayList<String[]>> generateStudents(int num_of_students, ArrayList<String> project_list) {


        HashMap<String, ArrayList<String[]>> student_preferences = new HashMap<>();
        int j = 1;

        while (j <= num_of_students) {
            //int rand_index = randInt(0, (project_list.size()-1));
            Collections.shuffle(project_list);
            ArrayList<String[]> preferences = new ArrayList<>();
            for (String project : project_list){
                String[] project_Arr = {project};

                preferences.add(project_Arr);
            }

            student_preferences.put("Student" + j, preferences );
            j++;
        }
        return student_preferences;
    }

    public static void main(String[] args){

        // Create a sample student_preferences and project list

        ArrayList<String> project_list = new ArrayList<>();
        project_list.add("Project1");
        project_list.add("Project2");
        project_list.add("Project3");

        HashMap<String, ArrayList<String[]>> student_preferences = generateStudents(3, project_list);

        ArrayList<String[]> ar1 = new ArrayList<>();
        String[] pref1 = {"Project1", "Project2"};
        ar1.add(pref1);

        ArrayList<String[]> ar2 = new ArrayList<>();
        String[] pref2 = {"Project1", "Project2"};
        ar2.add(pref2);


        student_preferences.put("Student1", ar1 );
        student_preferences.put("Student2", ar2);

        for (Map.Entry<String, ArrayList<String[]>> entry : student_preferences.entrySet()){
            System.out.println(entry.getKey());
            ArrayList<String[]> aL1 = entry.getValue();
            for (String[] indiffer_class : aL1){
                System.out.println(Arrays.toString(indiffer_class));
            }
        }

        RandomSerialWithTies(student_preferences, project_list);

    }
}
