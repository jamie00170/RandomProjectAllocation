import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionConversionException;

import java.util.*;

/**
 * Created by Jamie on 09/03/2016.
 */
public class UtilityMethods {

    public void incrementValue(String[][] matrix, String student, String project, Fraction calculated_value){
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

        Fraction current_value = stringToFraction(matrix[coordinates[0]][coordinates[1]]);
        Fraction new_value = current_value.add(calculated_value);
        matrix[coordinates[0]][coordinates[1]] = new_value.toString();
    }

    public int factorial(int n) {
        int fact = 1; // this  will be the result
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }

    public ArrayList<String> generateprojects(int num_of_projects) {

        ArrayList<String> project_list = new ArrayList<>();
        int i = 1;
        while (i <= num_of_projects) {
            project_list.add("Project" + i);
            i++;
        }
        return project_list;

    }

    public HashMap<String, ArrayList<String>> generateStudents(int num_of_students, ArrayList<String> project_list, int size_of_preference_list) {


        HashMap<String, ArrayList<String>> student_preferences = new HashMap<>();

        int j = 1;
        while (j <= num_of_students) {
            Collections.shuffle(project_list);
            student_preferences.put("Student" + j, new ArrayList<>(project_list.subList(0, size_of_preference_list)));
            j++;
        }
        return student_preferences;
    }

    public Fraction stringToFraction(String fraction_string){

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

    public HashMap<String, Fraction> getCurrentProjects(HashMap<String, ArrayList<String>> student_preferences, HashMap<String, Fraction> current_projects){

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

    public boolean check_sizes(Map<String, Fraction> resource_allocation){
        Collection<Fraction> scores = resource_allocation.values();
        int num_of_projects_used = 0;
        for (Fraction score : scores){
            if (score.equals(new Fraction(1)))
                num_of_projects_used++;
        }
        return (num_of_projects_used == scores.size()) || resource_allocation.isEmpty();
    }

    public void removeMatched(Map<String, ArrayList<String>> student_preferences, Map<String, Fraction> project_allocation, Map<String, Fraction> student_allocation){

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

    public int[] getCoordinates(String[][] matrix, String student, String project){
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

        return coordinates;

    }

    public String[][] setUpMatrix(Set<String> student_list, ArrayList<String> project_list ){

        // Set up
        // Create matrix
        String[][] matrix = new String[(student_list.size() + 1)][(project_list.size() + 1)];

        matrix[0][0] = "-";
        int i = 1;
        for (String student : student_list) {
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

        return matrix;
    }

    public String[][] divideMatrixByFactorial(String[][] matrix, int factorial){

        int p = 1;
        while( p < matrix.length){
            int f = 1;
            while (f < matrix[p].length){
                try {
                    Double double_value = Double.parseDouble(matrix[p][f]);

                    Fraction fraction = new Fraction(double_value / factorial);

                    matrix[p][f] = fraction.toString();
                } catch (NumberFormatException e){
                    e.printStackTrace();
                }catch (FractionConversionException e){
                    e.printStackTrace();
                }
                f++;
            }
            p++;
        }

        return matrix;

    }


}
