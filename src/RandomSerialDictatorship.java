/**
 * Created by Jamie on 30/11/2015.
 */

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionConversionException;

import java.lang.reflect.Array;
import java.util.*;

public class RandomSerialDictatorship {

    private static UtilityMethods utilityMethods = new UtilityMethods();


    public String[][] randomSerialDictatorship(ArrayList<String> student_list, HashMap<String, ArrayList<String>> student_preferences, ArrayList<String> project_list, int num_permutations){

        String[][] matrix = utilityMethods.setUpMatrix(student_preferences.keySet(), project_list);

        for (String[] row :  matrix ){
            System.out.println(Arrays.toString(row));
        }

        // Initialise projects_allocated array
        ArrayList<String> projects_allocated = new ArrayList<>();


        Collection<ImmutableList<String>> permutations;

        // If not all permutations are required
        if (num_permutations != utilityMethods.factorial(student_list.size())){
            permutations = utilityMethods.generatePermutations(student_list, num_permutations);
        }else{
            Collection student_col = student_list;
            permutations = Collections2.permutations(student_col);
        }

        for (ImmutableList<String> permutation: permutations) {


            System.out.println(permutation);

            // run rsd code here for permutation

            int i = 0;
            // Start here - need to format string into list first
            projects_allocated.clear();
            while (i < permutation.size()) {
                // If the current student in the priority list is in student_preferences

                String name = permutation.get(i);
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

        // divide all values in the matrix by factorial of the size of the  student list or the num of permutations given
        int divisor;
        int factorial_student_list = utilityMethods.factorial(student_list.size());
        if (num_permutations != factorial_student_list){
            divisor = num_permutations;
            System.out.println("divisor: " + divisor);
            matrix = utilityMethods.divideMatrixByFactorial(matrix, divisor);
        }else{
            divisor = factorial_student_list;
            System.out.println("divisor: " + divisor);

            matrix = utilityMethods.divideMatrixByFactorial(matrix, divisor);

        }



        for (String[] row : matrix){
            System.out.println(Arrays.toString(row));
        }

        return matrix;
    }


    public static void main(String[] args){


        ArrayList<String> project_list = utilityMethods.generateprojects(200);
        System.out.println("Project List:" + project_list.toString());

        HashMap<String, ArrayList<String>> student_preferences = utilityMethods.generateStudents(200, project_list, 6);

        System.out.println("Student Preferences: " + student_preferences.toString());

        String[][] matrix = utilityMethods.setUpMatrix(student_preferences.keySet(), project_list);


        ArrayList<String> student_list = new ArrayList<>();
        for (String name : student_preferences.keySet()){
            student_list.add(name);
        }
        System.out.println("Student list:" + student_list);

        RandomSerialDictatorship rsd = new RandomSerialDictatorship();

        matrix = rsd.randomSerialDictatorship(student_list, student_preferences, project_list, 100);


    }
}
