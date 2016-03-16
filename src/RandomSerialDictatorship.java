/**
 * Created by Jamie on 30/11/2015.
 */

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionConversionException;

import java.lang.reflect.Array;
import java.util.*;

public class RandomSerialDictatorship {

    private static UtilityMethods utilityMethods = new UtilityMethods();


    public String[][] permute(ArrayList<String> student_list, int k, HashMap<String, ArrayList<String>> student_preferences, ArrayList<String> project_list, String[][] matrix){
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

                i++;
            }

        }
        

        return matrix;
    }


    public static void main(String[] args){


        ArrayList<String> project_list = utilityMethods.generateprojects(3);
        System.out.println("Project List:" + project_list.toString());

        HashMap<String, ArrayList<String>> student_preferences = utilityMethods.generateStudents(3, project_list, 3);

        System.out.println("Student Preferences: " + student_preferences.toString());

        String[][] matrix = utilityMethods.setUpMatrix(student_preferences.keySet(), project_list);


        //String[] student_list = new String[student_preferences.size()];
        ArrayList<String> student_list = new ArrayList<>();
        for (String name : student_preferences.keySet()){
            student_list.add(name);
        }
        System.out.println("Student list:" + student_list);

        RandomSerialDictatorship rsd = new RandomSerialDictatorship();

        matrix = rsd.permute(student_list, 0, student_preferences, project_list, matrix);

        int divisor = utilityMethods.factorial(student_list.size());

        // divide all values in the matrix by factorial of the size of the  student list
        matrix = utilityMethods.divideMatrixByFactorial(matrix, divisor);

        for (String[] row : matrix){
            System.out.println(Arrays.toString(row));
        }
    }
}
