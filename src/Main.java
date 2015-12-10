import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.FractionConversionException;

import java.util.*;

// Specification + problem description +
public class Main {

    public static ArrayList<String> generateprojects(int num_of_projects) {

        ArrayList<String> project_list = new ArrayList<>();
        int i = 1;
        while (i <= num_of_projects) {
            project_list.add("Project" + i);
            i++;
        }
        return project_list;

    }

    public static Map<String, ArrayList<String>> generateStudents(int num_of_students, ArrayList<String> project_list) {


        Map<String, ArrayList<String>> student_preferences = new HashMap<>();
        int j = 1;
        while (j <= num_of_students) {
            Collections.shuffle(project_list);
            student_preferences.put("Student" + j, new ArrayList<>(project_list));
            j++;
        }
        return student_preferences;
    }

    public static Fraction stringToFraction(String fraction_string){

        Fraction f;

        if (fraction_string.length() == 5){
            Double numerator = Double.parseDouble(fraction_string.substring(0,1));
            Double denominator = Double.parseDouble(fraction_string.substring(4));

            Double fraction_value = numerator/denominator;

            try {
                f = new Fraction(fraction_value);
                return f;
            } catch (FractionConversionException e ){
                e.printStackTrace();
            }

        }else if (fraction_string.length() == 1){

            try {
                Double fraction_value = Double.parseDouble(fraction_string);

                f = new Fraction(fraction_value);
                return f;
            } catch (FractionConversionException e) {
                e.printStackTrace();
            }
        }
        // Need to add clauses for different length numerators and denominators i.e. 19/20

        return new Fraction(0);
    }



    public static void incrementValue(String[][] matrix, String student, String project, Fraction calculated_value){
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

        //Double value = Double.parseDouble(matrix[coordinates[0]][coordinates[1]]);
        String matrix_value = matrix[coordinates[0]][coordinates[1]];
        Fraction f = stringToFraction(matrix_value);

        f = f.add(calculated_value);
        String frac_string = f.toString();
        matrix[coordinates[0]][coordinates[1]] = frac_string;

    }

    public static void main(String[] args) {

        ArrayList<String> project_list = generateprojects(2);
        System.out.println("Project List:" + project_list.toString());

        Map<String, ArrayList<String>> student_preferences = generateStudents(2 , project_list);

        System.out.println("Student Preferences: " + student_preferences.toString());
        ArrayList<String> priority_list = new ArrayList<>();

        priority_list.add("Student1");
        priority_list.add("Student2");
        priority_list.add("Student3");

        // Create a random order of Students
        Collections.shuffle(priority_list);
        System.out.println("Priority List: " + priority_list.toString());

        probabilisticSerialDictatorship(student_preferences, project_list);
        //BostonSerial bs = new BostonSerial();
        //bs.bostonSerial(student_preferences, project_list);

    }

    public static int factorial(int n) {
        int fact = 1; // this  will be the result
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }

    public static boolean check_sizes(Map<String, Fraction> resource_allocation){
        Collection<Fraction> scores = resource_allocation.values();
        int num_of_projects_used = 0;
        for (Fraction score : scores){
           if (score.equals(new Fraction(1)))
               num_of_projects_used++;
        }
        return (num_of_projects_used == scores.size()) || resource_allocation.isEmpty();
    }


    public static Map<String, Fraction> getCurrentProjects(Map<String, ArrayList<String>> student_preferences, Map<String, Fraction> current_projects){

        // reset current projects
        for(String project : current_projects.keySet()){
            current_projects.put(project, current_projects.get(project).subtract(current_projects.get(project)));
        }

        for (String student : student_preferences.keySet()){
            // Get the students preferences
            ArrayList<String> preferences =  student_preferences.get(student);
            System.out.println(student + " " + preferences.get(0));
            current_projects.put(preferences.get(0), current_projects.get(preferences.get(0)).add(new Fraction(1)));
        }
        return current_projects;
    }

    public static void removeMatched(Map<String, ArrayList<String>> student_preferences, Map<String, Fraction> project_allocation, Map<String, Fraction> student_allocation){

        //System.out.println("Attempting to remove matched students and projects .... ");
        ArrayList<String> student_list = new ArrayList<>();
        for (String name : student_preferences.keySet()) {
            student_list.add(name);

        }

        for (String name : student_list) {
            if (student_allocation.get(name).equals(new Fraction(1))) {
                student_preferences.remove(name);
            }

        }

        ArrayList<String> items_to_remove = new ArrayList<>();

        for (String name : student_preferences.keySet()) {
            // Get students list of ranked choices
            ArrayList<String> preferences = student_preferences.get(name);
            // Loop through their choices
            for (String project : preferences) {
                // If the choice has been fully matched remove it from the preferences list
                if (project_allocation.get(project).equals(new Fraction(1))) {
                    // remove project from preferences list
                    //System.out.println("Removing " + project);
                    items_to_remove.add(project);
                }
            }
            preferences.removeAll(items_to_remove);
        }
        //System.out.println("Removed matched students and projects");
        }


    public static void probabilisticSerialDictatorship(Map<String, ArrayList<String>> student_preferences, ArrayList<String> project_list){

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
                matrix[i][j] = "0.0";
                j++;
            }
            i++;
        }


        // Stores the amount left of each project to be consumed
        Map<String, Fraction> project_allocation = new HashMap<>();

        // Stores the amount of each student left to be consumed
        Map<String, Fraction> student_allocation = new HashMap<>();

        i = 0;
        while (i < project_list.size()){
            project_allocation.put( project_list.get(i), new Fraction(0));
            i++;
        }
        System.out.println(project_allocation.toString());

        for(Object name : student_preferences.keySet()){
            student_allocation.put((String) name, new Fraction(0));
        }
        System.out.println(student_allocation.toString());
        //System.out.println(student_preferences.toString());



        Map<String, Fraction> current_projects = new HashMap<>();
        int k = 0;
        while (k < project_list.size()){
            current_projects.put(project_list.get(k), new Fraction(0));
            k++;
        }

        // while projects are still to be allocated or students are fully matched i.e have a combined total of 1
        while(!(check_sizes(project_allocation) || check_sizes(student_allocation) || student_preferences.isEmpty())){
            // Get current projects being consumed by students
            Map<String, Fraction> currentProjects = getCurrentProjects(student_preferences, current_projects);
            System.out.println("Current Projects: " + currentProjects.toString());

            // Get the maximum amount each project can be incremented by
            // 1/highest number of students consuming a project - max(current_projects.values())

            //Double max_increment = (1/(Collections.max(current_projects.values())));
            Fraction max_increment = (new Fraction(1).divide(Collections.max(current_projects.values())));

            // Get the most project with most students consuming then
            // access the project_Allocation to get amount remaining
            // divide that number by number of students consuming

            // Iterate over entry set if value == max value
            // key == max_project
            String max_project = "";
            for (Map.Entry<String, Fraction> entry : current_projects.entrySet()){
                if (entry.getValue().equals(Collections.max(current_projects.values()))){
                    max_project = entry.getKey();
                }
            }
            //Double amount_remaing_of_max_project = 1.0 - project_allocation.get(max_project);
            Fraction amount_remaing_of_max_project = new Fraction(1).subtract(project_allocation.get(max_project));

            // for each student that hasn't yet been matched
            for (String name : student_preferences.keySet()){
                // get the students list of preferences
                ArrayList<String> preferences = student_preferences.get(name);
                // get their first available choice - matched choices have been removed to always index 0
                String current_project = preferences.get(0);
                // Find out the number of student consuming this students current project
                //Double num_of_students_consuming = current_projects.get(current_project);
                // Find the amount of the project yet to be assigned
                //Double amount_of_project_remaining = (1.0 - (project_allocation.get(current_project)));

                //System.out.println(name +" has: " + (amount_of_project_remaining/num_of_students_consuming) + "percentage of being matched to" + current_project);
                // increment student allocation + project allocation
                // TAKE ACCOUNT OF AMOUNT OF PROJECT REMAINING
                //System.out.println("Increment: " + max_increment * amount_remaing_of_max_project);
                System.out.println("Increment: " + max_increment.multiply(amount_remaing_of_max_project));
                student_allocation.put(name, student_allocation.get(name).add(max_increment.multiply(amount_remaing_of_max_project)));
                project_allocation.put(current_project, project_allocation.get(current_project).add((max_increment.multiply(amount_remaing_of_max_project))));
                // Increment values in the matrix
                incrementValue(matrix, name, current_project, (max_increment.multiply(amount_remaing_of_max_project)));
            }
            System.out.println("Student Allocation: " + student_allocation.toString());
            System.out.println("Project Allocation: " + project_allocation.toString());
            System.out.println("Student preferences before removal: " + student_preferences.toString());
            // Removed matched Students and Projects
            removeMatched(student_preferences, project_allocation, student_allocation);
            System.out.println("Student preferences: " + student_preferences.toString());
        }

        System.out.println("Student Allocation: " + student_allocation.toString());
        System.out.println("Project Allocation: " + project_allocation.toString());

        for (String[] row : matrix){
            System.out.println(Arrays.toString(row));
        }
    }

    public static void RandomSerialWithTies(Map<String, ArrayList<String>> student_preferences, ArrayList<String> project_list){
        // 1. Construct undirected bipartite graph where V = (N U A) and E = empty
        // 1.1 BipartieGrpah bG = new BipartieGraph(student_list, project_list)
        // 2. for each agent in order of current permutation
        //    3. start at first indifference class
        //    4. provisionally add (i, a) to E for all a in agent i's current indifference class
        //    4.1 bG.newEdge(i, a)
        //    5. if augmenting path starting from agent i - SearchAP(bG.getVertexList)
        //        5.1. augment along path and modify E accordingly - augment()
        //    else
        //        5.2 provisionally added edges are removed
        //          5.2.1 bG.removeEdge(i, a)
        //        5.3 move onto agent i's next indifference class until reach end of choices/classes
    }

        //System.out.println(current_projects.toString());
    }
