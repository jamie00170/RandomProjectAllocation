import java.util.*;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionConversionException;


/**
 * Created by Jamie on 15/11/2015.
 */


public class BostonSerial {

    /**
    public static Map<String, Fraction> getCurrentProjects(Map<String, ArrayList<String>> student_preferences, Map<String, Fraction> current_projects, Integer p){

        // reset current projects
        for(String project : current_projects.keySet()){
            current_projects.put(project, current_projects.get(project).subtract(current_projects.get(project)));
        }

        for (String student : student_preferences.keySet()){
            // Get the students preferences
            ArrayList<String> preferences =  student_preferences.get(student);
            System.out.println(student + " " + preferences.get(p));
            current_projects.put(preferences.get(p), current_projects.get(preferences.get(p)).add(new Fraction(1)));
        }
        return current_projects;
    }
     **/


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

    public static boolean check_sizes(Map<String, Fraction> resource_allocation){
        Collection<Fraction> scores = resource_allocation.values();
        int num_of_projects_used = 0;
        for (Fraction score : scores){
            if (score.equals(new Fraction(1)))
                num_of_projects_used++;
        }
        return (num_of_projects_used == scores.size()) || resource_allocation.isEmpty();
    }


    public static HashMap<String, Fraction> getCurrentProjects(HashMap<String, ArrayList<String>> student_preferences, HashMap<String, Fraction> current_projects){

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

    public static Fraction stringToFraction(String fraction_string){

        Fraction f;

        fraction_string = fraction_string.replaceAll("\\s","");
        String[] data = fraction_string.split("/");
        //System.out.println("string split:" + Arrays.toString(data));


        if (data.length > 1) {
            Double numerator = Double.parseDouble(data[0]);
            Double denominator = Double.parseDouble(data[1]);

            Double fraction_value = numerator / denominator;


            try {
                f = new Fraction(fraction_value);
                return f;
            } catch (FractionConversionException e) {
                e.printStackTrace();
            }
        }else{

            Double fraction_value = Double.parseDouble(data[0]);
            try {
                f = new Fraction(fraction_value);
                return f;
            } catch (FractionConversionException e) {
                e.printStackTrace();
            }
        }


        System.out.println("FRACTION NOT TRANSFORMED TO STRING");
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



    public String[][] bostonSerial(HashMap<String, ArrayList<String>> student_preferences, ArrayList<String> project_list){

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
        HashMap<String, Fraction> project_allocation = new HashMap<>();

        // Stores the amount of each student left to be consumed
        HashMap<String, Fraction> student_allocation = new HashMap<>();


        i = 0;
        while (i < project_list.size()){
            project_allocation.put(project_list.get(i), new Fraction(0));
            i++;
        }
        System.out.println(project_allocation.toString());

        for(Object name : student_preferences.keySet()){
            student_allocation.put((String) name, new Fraction(0));
        }
        System.out.println(student_allocation.toString());
        //System.out.println(student_preferences.toString());


        HashMap<String, Fraction> current_projects = new HashMap<>();
        int k = 0;
        while (k < project_list.size()){
            current_projects.put((String) project_list.get(k), new Fraction(0));
            k++;
        }

        // Store the max any project and the any student can be incremented in a given round
        HashMap<String, Fraction> max_project_increments = new HashMap<>();
        HashMap<String, Fraction> max_student_increments = new HashMap<>();


        while(!(check_sizes(project_allocation) || check_sizes(student_allocation) || student_preferences.isEmpty())){

            //Clear max student and max project increment hash maps
            max_project_increments.clear();
            max_student_increments.clear();


            // Get current projects being consumed by students
            HashMap<String, Fraction> currentProjects = getCurrentProjects(student_preferences, current_projects);
            System.out.println("Current Projects: " + currentProjects.toString());

            // Store the max each project can be incremented by for the current round
            for (String project : current_projects.keySet()){
                // Only add projects that are being consumed in the current round
                if (current_projects.get(project).compareTo(new Fraction(0)) == 0)
                    continue;

                Fraction max_increment_for_project = new Fraction(1).subtract(project_allocation.get(project));
                if (current_projects.get(project).compareTo(new Fraction(1)) > 0){
                    Fraction new_increment = max_increment_for_project.divide(current_projects.get(project));
                    //System.out.println("increment: " + max_increment_for_project + " divided by: " + current_projects.get(project) + " = " +  max_increment_for_project.divide(current_projects.get(project)));
                    max_project_increments.put(project, new_increment);
                }else{
                    max_project_increments.put(project, max_increment_for_project);
                }
            }

            // Store the max each student can be incremented each round
            for (String student: student_preferences.keySet()){

                Fraction max_increment_for_student = new Fraction(1).subtract(student_allocation.get(student));
                max_student_increments.put(student, max_increment_for_student);

            }


            System.out.println("Max increments for projects: " + max_project_increments);
            System.out.println("Max increments for students: " + max_student_increments);


            // for each student that hasn't yet been matched
            for (String name : student_preferences.keySet()) {

                // if the student has picked a project that is already matched then skip the student
                // get the students list of preferences
                ArrayList<String> preferences = student_preferences.get(name);
                // get their first available choice - matched choices have been removed to always index 0
                if (preferences.isEmpty())
                    continue;
                String current_project = preferences.get(0);
                // if the student has picked a project that is already matched then skip the student
                if (project_allocation.get(current_project).compareTo(new Fraction(1)) == 0)
                    continue;

                // Increment value is equal to the minimum of max_student_increments.get(student) and
                // max_project_increments.get(project)
                Fraction incrementValue;
                Fraction max_student_increment = max_student_increments.get(name);
                Fraction max_project_increment = max_project_increments.get(current_project);

                if (max_student_increment.compareTo(max_project_increment) < 0){
                    incrementValue = max_student_increment;
                }else{
                    incrementValue = max_project_increment;
                }


                System.out.println("Student: " + name + " Project: " + current_project);
                System.out.println("Increment: " + incrementValue + "\n");

                student_allocation.put(name, student_allocation.get(name).add(incrementValue));
                project_allocation.put(current_project, project_allocation.get(current_project).add(incrementValue));
                // Increment values in the matrix
                incrementValue(matrix, name, current_project, incrementValue);

            }
            //p++;
            System.out.println("Student Allocation: " + student_allocation.toString());
            System.out.println("Project Allocation: " + project_allocation.toString());
            System.out.println("Student preferences before removal: " + student_preferences.toString());
            // Removed matched Students and Projects
            removeMatched(student_preferences, project_allocation, student_allocation);
            System.out.println("Student preferences: " + student_preferences.toString());
        }
        //System.out.println("Student Allocation: " + student_allocation.toString());
        //System.out.println("Project Allocation: " + project_allocation.toString());

        for (String[] row : matrix){
            System.out.println(Arrays.toString(row));
        }

        return matrix;
    }

    public static void main(String[] args){

        /**
        Fraction f1 = new Fraction(0.33333333333333);
        Fraction f2 = new Fraction(0);

        Fraction f3 = new Fraction(0.33333333333);
        Fraction f4 = new Fraction(0);

        Double f3_value = f3.doubleValue();
        Double f4_value = f4.doubleValue();

        System.out.println("f3_value: " + f3_value);
        System.out.println("f4_value: " + f4_value);


        Fraction smallest_space;
        if (f3_value.compareTo(f4_value) > 0){
            smallest_space = new Fraction(f3_value);
        }else{
            smallest_space = new Fraction(f4_value);
        }
        System.out.println("Smallest space: " + smallest_space);


        if (new Fraction(1/3).compareTo(new Fraction(0)) > 0){
            smallest_space = new Fraction(1/3);
        }else{
            smallest_space = new Fraction(0);
        }

        System.out.println(smallest_space);
         **/
    }
}
