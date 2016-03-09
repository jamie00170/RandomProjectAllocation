/**
 * Created by Jamie on 30/11/2015.
 */

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionConversionException;

import java.lang.reflect.Array;
import java.util.*;

public class RandomSerialDictatorship {

    private static UtilityMethods utilityMethods = new UtilityMethods();


    static String[][] permute(ArrayList<String> student_list, int k, HashMap<String, ArrayList<String>> student_preferences, ArrayList<String> project_list, String[][] matrix){
        String[] permutation = new String[student_list.size()];
        // Initialise projects_allocated array
        ArrayList<String> projects_allocated = new ArrayList<>();

        for(int n = k; n < student_list.size(); n++){
            java.util.Collections.swap(student_list, n, k);
            permute(student_list, k+1, student_preferences, project_list, matrix);
            java.util.Collections.swap(student_list, k, n);
        }
        if (k == student_list.size() -1) {
            permutation = student_list.toArray(permutation);
            System.out.println(Arrays.toString(permutation));
            // run rsd code here for permutation

            int i = 0;
            // Start here - need to format string into list first
            projects_allocated.clear();
            while (i < permutation.length) {
                // If the current student in the priority list is in student_preferences
                if (student_preferences.containsKey(permutation[i])) {
                    String name = permutation[i];
                    // Get the students preferences
                    ArrayList<String> preferences = student_preferences.get(name);
                    // For each choice in the list of preferences
                    //System.out.println("Preferences: " + preferences);
                    int j = 0;
                    while (j < preferences.size()) {
                        // If the project has already been taken
                        if (projects_allocated.contains(preferences.get(j))) {
                            // If the choice is the students last
                            if (preferences.get(preferences.size() - 1).equals(preferences.get(j))) {
                                // Unable to match student
                                System.out.println(name + " not matched because none of their choices are available!");
                            }
                        } else {
                            // Match the student to their choice
                            System.out.println(name + " " + preferences.get(j));
                            utilityMethods.incrementValue(matrix, name, preferences.get(j), new Fraction(1));
                            // Add the project to the projects_allocated list
                            projects_allocated.add(preferences.get(j));
                            break;
                        }
                        j++;
                    }
                } else {
                    System.out.println("Name not in student list!");
                }
                i++;
            }

        }
        /**
        HashMap<String, String> matching = new HashMap<>();

        // Run enum all matchings here?
        int i = 1;
        while(i < matrix.length){
            int j = 1;
            while (j < matrix[i].length) {
                matching.put(matrix[i][0], matrix[0][j]);
                j++;
                }
            i++;
        }

        boolean isParetoOptimal;
        Matchings_test mt = new Matchings_test();
        isParetoOptimal =  mt.enum_all_matchings(student_list, project_list, student_preferences, matching);
        if(!isParetoOptimal){
            System.out.println("A matching was found that is not Pareto Optimal");
            System.exit(1);
        }
         **/

        return matrix;
    }


    public static void main(String[] args){


        ArrayList<String> project_list = utilityMethods.generateprojects(5);
        System.out.println("Project List:" + project_list.toString());

        HashMap<String, ArrayList<String>> student_preferences = utilityMethods.generateStudents(5, project_list, 3);

        System.out.println("Student Preferences: " + student_preferences.toString());
        ArrayList<String> priority_list = new ArrayList<>();

        // Set up
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


        //String[] student_list = new String[student_preferences.size()];
        ArrayList<String> student_list = new ArrayList<>();
        for (String name : student_preferences.keySet()){
            student_list.add(name);
        }
        System.out.println("Student list:" + student_list);


        matrix = RandomSerialDictatorship.permute(student_list, 0, student_preferences, project_list, matrix);

        int divisor = utilityMethods.factorial(student_list.size());

        int p = 1;
        while( p < matrix.length){
            int f = 1;
            while (f < matrix[p].length){
                try {
                    Double double_value = Double.parseDouble(matrix[p][f]);

                    Fraction fraction = new Fraction(double_value / divisor);

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

        for (String[] row : matrix){
            System.out.println(Arrays.toString(row));
        }
    }
}
