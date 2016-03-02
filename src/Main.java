import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionConversionException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

    public static HashMap<String, ArrayList<String>> generateStudents(int num_of_students, ArrayList<String> project_list) {


        HashMap<String, ArrayList<String>> student_preferences = new HashMap<>();
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

        ArrayList<String> project_list = generateprojects(10);
        System.out.println("Project List:" + project_list.toString());

        HashMap<String, ArrayList<String>> student_preferences = generateStudents(10 , project_list);

        System.out.println("Student Preferences: " + student_preferences.toString());

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
            if (!preferences.isEmpty()) {
                System.out.println(student + " " + preferences.get(0));
                current_projects.put(preferences.get(0), current_projects.get(preferences.get(0)).add(new Fraction(1)));
            }
        }
        return current_projects;
    }

    public static void removeMatched(Map<String, ArrayList<String>> student_preferences, Map<String, Fraction> project_allocation, Map<String, Fraction> student_allocation){

        //System.out.println("Attempting to remove matched students and projects .... ");
        ArrayList<String> student_list = new ArrayList<>();
        for (String name : student_preferences.keySet()) {
            student_list.add(name);
        }

        ArrayList<String> names_to_remove = new ArrayList<>();
        for (String name : student_list) {
            if (student_preferences.get(name).isEmpty()){
                names_to_remove.add(name);
            }
            if (student_allocation.get(name).equals(new Fraction(1))) {
                student_preferences.remove(name);
            }

        }

        for (String name : names_to_remove){
            student_preferences.remove(name);
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

    }


    public static String[][] probabilisticSerialDictatorship(Map<String, ArrayList<String>> student_preferences, ArrayList<String> project_list){

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
            getCurrentProjects(student_preferences, current_projects);

            System.out.println("Current projects:" + current_projects);
            String max_project = "";
            ArrayList<String> projects_in_tie = new ArrayList<>();
            for (Map.Entry<String, Fraction> entry : current_projects.entrySet()){
                // Stores, in fraction form, the highest number of students consuming a project in the current round
                Fraction max_num_students_consuming = Collections.max(current_projects.values());
                if (entry.getValue().equals(max_num_students_consuming)){
                    max_project = entry.getKey();
                    projects_in_tie.add(max_project);
                }
            }
            Fraction amount_remaining_of_max_project;
            // Need to check there is not a tie in the max project
            System.out.println("Max project ties: " + projects_in_tie);
            if (projects_in_tie.size() > 1){ // There is a tie
                Fraction smallest_space_avaiable = new Fraction(1);
                for (String project : projects_in_tie){
                    if (project_allocation.get(project).compareTo(smallest_space_avaiable) < 0){
                        // Stores the value of smallest space available for the current round
                        smallest_space_avaiable = new Fraction(1).subtract(project_allocation.get(project));
                    }
                }
                amount_remaining_of_max_project = smallest_space_avaiable;
            }else{
                amount_remaining_of_max_project = new Fraction(1).subtract(project_allocation.get(max_project)).abs();
            }

            System.out.println("amomp: " +  amount_remaining_of_max_project);
            System.out.println("Max project:" + max_project);


            Fraction increment_for_round = amount_remaining_of_max_project.divide(current_projects.get(max_project));
            System.out.println("Increment for round before check: " + increment_for_round);
            // Or
            // for project in current projects
            //    if project_allocation.get(project) + increment > 1
            //       use 1 - proj_allocation.get(project) / num students consuming
            /**
            Fraction increment_before_check = increment_for_round;
            for (Map.Entry<String, Fraction> entry : current_projects.entrySet()){
                // If there is a student consuming the project in the current round
                if (entry.getValue().compareTo(new Fraction(1)) >= 0) {
                    Double value_of_entry = project_allocation.get(entry.getKey()).add(increment_before_check).doubleValue();
                    System.out.println("Value of entry: " + value_of_entry + " for project: " + entry.getKey());
                    if (value_of_entry.compareTo(1.0) > 0) {
                        increment_for_round = new Fraction(1).subtract(project_allocation.get(entry.getKey())).divide(current_projects.get(entry.getKey()));
                    }
                }
            }
             **/

            System.out.println("Increment: " + increment_for_round);
            // for each student that hasn't yet been matched
            for (String name : student_preferences.keySet()){
                // get the students list of preferences
                ArrayList<String> preferences = student_preferences.get(name);
                // get their first available choice - matched choices have been removed to always index 0
                if (preferences.isEmpty())
                        continue;
                String current_project = preferences.get(0);

                student_allocation.put(name, student_allocation.get(name).add(increment_for_round));
                project_allocation.put(current_project, project_allocation.get(current_project).add(increment_for_round));
                // Increment values in the matrix
                incrementValue(matrix, name, current_project, increment_for_round);
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
        return matrix;
    }



        //System.out.println(current_projects.toString());
    }
