

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

    public static BipartiteGraph remove_associated_edges_rsdt(BipartiteGraph bipartiteGraph, Vertex v){


        // Set v.mates adjacent list to empty
        System.out.println("Removing  all of " + v.mate.name + "'s adjacent verticies");
        // Remove all non matching edges coming in and out of v and v.mate
        if (v.mate.adjacentV.removeAll(v.mate.adjacentV)){
            System.out.println("All of " + v.mate.name + "'s adjacent edges removed");
        }

        for (Vertex u : bipartiteGraph.vertexList) {
            // if u has any adjacent edges - check to see if v is an adjacent edge
            if (u.adjacentV != null && u.adjacentV.size() > 0) {
                //System.out.println("Looking at " + u.name + "'s adjacent list");
                ArrayList<Integer> index_edges_to_remove = new ArrayList<>();
                int index = 0;
                for (Vertex vertex : u.adjacentV) {
                    // if v is in any adjacency lists remove it
                    if (vertex.name.equals(v.name)) {
                        System.out.println("Removing: " + v.name + " from " + u.name + "'s adjacent list");
                        index_edges_to_remove.add(index);
                    }
                    index++;
                }
                if (index_edges_to_remove.size() > 0) {
                    int index_to_remove = index_edges_to_remove.get(0);
                    u.adjacentV.remove(index_to_remove);
                }

            }
        }

        return bipartiteGraph;
    }


    public static void enum_perfect_matchings_iter(BipartiteGraph G, String[][] M) {
        // Step 1: If G has no edge, stop

        try {

            if (!(G.hasEdge())) {
                return;
            }
            // 2. Try to find a cycle in G by DFS therefore confirming if there is another perfect matching
            Queue<Vertex> verticesInCycle = new PriorityQueue<>();
            int i = 0;
            Vertex e = new Vertex();
            ObjectCloner objectCloner = new ObjectCloner();
            //BipartiteGraph g_plus = (BipartiteGraph) objectCloner.deepCopy(G); // create a deep copy of G

            BipartiteGraph g_minus = new BipartiteGraph();

            BipartiteGraph g_plus = (BipartiteGraph) objectCloner.deepClone(G);
            //BipartiteGraph g_plus = new BipartiteGraph(G); // copy G
            while (i < G.vertexList.size()) {// - Cycle could start from any vertex?
                verticesInCycle = G.find_cycle(G.vertexList.get(i));

                if (!verticesInCycle.isEmpty()) {
                    // Step 4: Find a perfect matching M' by exchanging edges along the cycle. Output M'
                    System.out.println("Current Matching....");
                    for (String[] row : M) {
                        System.out.println(Arrays.toString(row));
                    }

                    // Choose an edge e here, that is both in the matching and in the cycle
                    //System.out.println("Verticies in cycle in the matching: ");
                    for (Vertex v : verticesInCycle) {
                        if (v.mate != null) {
                            e = v.clone();
                            System.out.println("e: " + e.name + " - " + e.mate.name);
                            break;
                        }
                    }

                    System.out.println("Exchanging edges......");
                    G.exchange_edges(verticesInCycle);
                    // output M'
                    System.out.println("New matching......");
                    String[][] matching_to_output = calculate_values_of_matrix(M, G);
                    for (String[] row : matching_to_output) {
                        System.out.println(Arrays.toString(row));
                    }
                    System.out.println("Graph after exchaning edges...");
                    for (Vertex v : G.vertexList) {
                        System.out.println(v);
                    }

                    g_minus = (BipartiteGraph) objectCloner.deepClone(G);

                    break; // cycle found
                }
                i++;
                // 3. If there is no cycle found stop algorithm
                if ((i == G.vertexList.size()) && verticesInCycle.isEmpty()) {
                    System.out.println("Algorithm Stopped no cycle found!");

                    System.out.println("Final Matching...");
                    for (String[] row : M){
                        System.out.println(Arrays.toString(row));
                    }
                    // TODO = still need to calculate final matching values
                    return;
                }

            }

            // Generate G+(e)
            //System.out.println("Printing g_plus before removing edges...");
            //for (Vertex v : g_plus.vertexList) {
            //    System.out.println(v);
            //}

            if (e.mate != null) {
                System.out.println("e: " + e.name + " - " + e.mate.name);
                g_plus = remove_associated_edges_rsdt(g_plus, e);
            } else {
                System.out.println("e is not in the matching!");
                System.exit(1);
            }


            System.out.println("Printing g_plus after removing edges...");
            for (Vertex v : g_plus.vertexList) {
                System.out.println(v);
            }

            // Call enum_perfect_matchings_iter(G+(e), M)

            enum_perfect_matchings_iter(g_plus, M);

            // Generate G-(e)
            //ObjectCloner objectCloner = new ObjectCloner();
            //BipartiteGraph g_minus = (BipartiteGraph) objectCloner.cloneObject(G);

            System.out.println("G....");
            for (Vertex v : G.vertexList){
                System.out.println(v.toString());
            }

            if (e.mate != null) {
                g_minus.remove_matching_edge_directed(e.name, e.mate.name);
            } else {
                System.out.println("Printing g_minus...");

                for (Vertex v : g_minus.vertexList){
                    System.out.println(v);
                }

                System.out.println("e is not in the matching!");
                System.exit(1);
            }

            System.out.println("Printing g_minus...");
            for (Vertex v : g_minus.vertexList) {
                System.out.println(v);
            }


            // Call enum_perfect_matchings_iter(G-(e), M')
            // G_minus represents M'
            enum_perfect_matchings_iter(g_minus, M);


        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }catch(Exception ex){
            ex.printStackTrace();
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


    public void RandomSerialWithTies(HashMap<String, ArrayList<String[]>> student_preferences, ArrayList<String> project_list){

        ArrayList<String> student_list = new ArrayList<>();
        for (String student: student_preferences.keySet()){
            student_list.add(student);
        }

        String[][] matrix = utilityMethods.setUpMatrix(student_preferences.keySet(), project_list);

        // 1. Construct undirected bipartite graph where V = (N U A) and E = empty
        BipartiteGraph bG = new BipartiteGraph(student_list, project_list);

        // 2. for each agent in order of current permutation
        String[] permutation = new String[student_list.size()];

        System.out.println("Student list: " + student_list);

        int k = 0;
        while (k < student_list.size()){
            permutation[k] = student_list.get(k);
            k++;
        }

        System.out.println("Permutation: " + Arrays.toString(permutation));
        //ArrayList<String[]> permutations = new ArrayList<>();

        for (String student : permutation) {
            // Until end of student's indifference classes is reached or student is matched
            int j = 0;
            System.out.println("Student: " + student);

            while (j < student_preferences.get(student).size()) {
                // 3. Look at the agents ith indifference class
                System.out.println("Student indifference class: " + Arrays.toString(student_preferences.get(student).get(j)));
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

        }

        // Clean up graph, i.e. remove adjacent edges where there is a matching one
        bG.cleanUp();

        System.out.println("Printing Graph.....");
        for (Vertex v : bG.vertexList){
            System.out.println(v.toString());
        }

        // Calculate values for matrix from graph
        calculate_initial_values_of_matrix(matrix, bG);

        for (String[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }

        // call perfect emum here?
        enum_perfect_matchings(bG, matrix);


    }




    public static int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }


    public static void main(String[] args){


        ArrayList<String> project_list = utilityMethods.generateprojects(3);

        GenerateRandomInstance generateRandomInstance = new GenerateRandomInstance();

        HashMap<String, ArrayList<String[]>> student_preferences = new HashMap<>();

        student_preferences = generateRandomInstance.generateStudents(3, project_list, 3);

        student_preferences = generateRandomInstance.generateRandomInstanceWithTies(student_preferences, 1);

        for (Map.Entry<String, ArrayList<String[]>> entry: student_preferences.entrySet()){
            System.out.println(entry.getKey());
            for (String[] indifference_class : entry.getValue()){
                System.out.print(Arrays.toString(indifference_class) + " ");
            }
            System.out.println();
        }

        RandomSerialDictatorshipTies rsdt = new RandomSerialDictatorshipTies();

        rsdt.RandomSerialWithTies(student_preferences, project_list);

    }

}
