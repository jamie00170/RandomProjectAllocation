

import java.util.*;

/**
 * Created by Jamie on 13/12/2015.
 */
public class RandomSerialDictatorshipTies {

    public static void calculate_values_of_matrix(String[][] matrix, BipartiteGraph bG){
        for (Vertex v : bG.vertexList){
            if (v.mate != null){
                incrementValue(matrix, v.name, v.mate.name, 1.0);
            }
        }

    }


    public static void incrementValue(String[][] matrix, String student, String project, Double calculated_value){
        int i = 0;
        int j;

        int[] coordinates = new int[2];

        while( i < matrix.length){
            j = 0;
            while (j < matrix[i].length){
                // only have to search first column and row
                if(matrix[i][j].equals(student))
                    coordinates[0] = i;
                if(matrix[i][j].equals(project))
                    coordinates[1] = j;
                j++;
            }
            i++;
        }

        if (!(coordinates[0] == 0 && coordinates[1] ==0)) {
            Double value = Double.parseDouble(matrix[coordinates[0]][coordinates[1]]);
            value += calculated_value;
            matrix[coordinates[0]][coordinates[1]] = Double.toString(value);
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
        boolean isCycle = false;
        int i = 0;
        while (!isCycle) { // - Cycle could start from any vertex?
            isCycle = G.depth_first_search(G.vertexList.get(i));
            i++;
        }

        // 3. If there is no cycle found stop alogrithm
        if (!isCycle){
            System.out.println("Algorithm Stopped no cycle found!");
            return;
        }

        if (isCycle){
            G.exchange_edges();
        }
        // use vertex G.vertexList.get(i-1) as edge, e when .mate is not null then the vertex is also in the matching
        Vertex edgeFrom = G.vertexList.get(i-1);
        int j = 1;
        while (edgeFrom.mate == null){
            edgeFrom = G.vertexList.get(i-j);
            j++;
        }


        try {
            BipartiteGraph g_plus = G.clone();

            // Generate G+(e)
            g_plus.remove_associated_edges(edgeFrom);

            enum_perfect_matchings_iter(g_plus, M);
        }catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        // Call enum_perfect_matchings_iter(G+(e), M)


        try {
            BipartiteGraph g_minus = G.clone();

            // Generate G-(e)
            g_minus.remove_matching_edge(edgeFrom);
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
        for (Vertex v : G.vertexList){
            System.out.println(v.toString());
        }
        // Step 2: Step 2: Trim unnecessary edges from G by a strongly connected component
        // decomposition algorithm with D(G, M)
        // TODO - Trim unnecessary edges from G by a strongly connected component decomposition algorithm
        // Step 3: Call Enum Perfect Matchings Iter (G, M)
        enum_perfect_matchings_iter(G, M);
    }


    public static void RandomSerialWithTies(Map<String, ArrayList<String[]>> student_preferences, ArrayList<String> project_list){

        ArrayList<String> student_list = new ArrayList<>();
        for (String student: student_preferences.keySet()){
            student_list.add(student);
        }

        // Create matrix
        String[][] matrix = new String[(student_preferences.size() + 1)][(project_list.size() + 1)];

        matrix[0][0] = "-";
        int i = 1;
        for (String student : student_preferences.keySet()) {
            matrix[i][0] = student;
            i++;
        }


        int j = 1;
        for (String project : project_list){

            matrix[0][j] = project;
            j++;
        }

        i = 1;
        while( i < matrix.length){
            j = 1;
            while (j < matrix[i].length){
                matrix[i][j] = "0";
                j++;
            }
            i++;
        }

        // 1. Construct undirected bipartite graph where V = (N U A) and E = empty
        BipartiteGraph bG = new BipartiteGraph(student_list, project_list);

        // 2. for each agent in order of current permutation
        String[] permutation = {"Student1", "Student2", "Student3"};

        //ArrayList<String[]> permutations = new ArrayList<>();

        for (String student : permutation) {
            // Until end of student's indifference classes is reached or student is matched
            j = 0;
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
            i++;
            // increment appropriate values in matrix at end of permutation i.e. where a vertex has a mate (matching edge)

        }

        System.out.println("Printing Graph.....");
        for (Vertex v : bG.vertexList){
            System.out.println(v.toString());
        }

        // Calculate values for matrix from graph
        calculate_values_of_matrix(matrix, bG);

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

        student_preferences.put("Student1", ar1 );

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
