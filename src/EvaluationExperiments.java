import org.apache.commons.math3.fraction.Fraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Jamie on 09/03/2016.
 */
public class EvaluationExperiments {

    private static UtilityMethods utilityMethods = new UtilityMethods();

    public static Double calculateCostOfMatching(String[][] matrix, HashMap<String, ArrayList<String>> student_preferences){

        System.out.println("Student Preferences in Calculate cost: " + student_preferences);


        Double costOfMatching = 0.0;

        int counter = 0;
        for (String[] row : matrix){
            int j = 0;
            if (counter >= 1) { // skip the first row - contains students
                int i = 1; // skip the project labels
                while (i < row.length) {
                    //System.out.print(row[i] + " ");
                    Fraction fraction = utilityMethods.stringToFraction(row[i]);
                    if (fraction.compareTo(new Fraction(0)) > 0){ // if the value is more than 0
                        String project = matrix[0][i];
                        String student = matrix[counter][0];
                        System.out.println("Student: " + student + " Project: " + project);
                        // Get what choice the current project is of the student
                        int position_in_preference_list = student_preferences.get(student).indexOf(project);
                        System.out.println("Position in preference list: " + position_in_preference_list);
                        //System.out.println("Adding : " + (position_in_preference_list + 1) * fraction.doubleValue() + " to the cost of the matching");
                        Fraction increment_fraction = new Fraction((position_in_preference_list +1) * fraction.doubleValue());
                        costOfMatching += increment_fraction.doubleValue();
                        System.out.println();
                    }
                    i++;
                }
                //System.out.println();
            }
            counter++;
        }

        return costOfMatching;
    }

    public static void runCostOfMatchingRSD(){


    }

    public static void runCostOfMatchingPS(String[][] matrix, HashMap<String, ArrayList<String>> student_preferences){

        Double cost = calculateCostOfMatching(matrix, student_preferences);
        System.out.println("Cost of matching for PS: " + cost);

    }

    public static void runCostOfMatchingBS(String[][] matching, HashMap<String, ArrayList<String>> student_preferences){

        Double cost = calculateCostOfMatching(matching, student_preferences);
        System.out.println("Cost of matching for BS: " + cost);

    }

    public static void main(String[] args){

        ArrayList<String> project_list = utilityMethods.generateprojects(20);

        HashMap<String, ArrayList<String>> student_preferences = utilityMethods.generateStudents(10, project_list, 3);



        ObjectCloner objectCloner = new ObjectCloner();
        //ArrayList<String> project_list_copy = new ArrayList<>(project_list);
        try {
            // Need to create a deep copy or running boston serial removes elements from student_preferences
            HashMap<String, ArrayList<String>> student_preferences_copy = (HashMap) objectCloner.deepCopy(student_preferences);

            BostonSerial bs = new BostonSerial();
            String[][] matching =  bs.bostonSerial(student_preferences, project_list );

            // need to pass a copy because we need the original state of the the preference lists to calculate cost
            runCostOfMatchingBS(matching, student_preferences_copy);

            /**
            Main main = new Main();
            matching = main.probabilisticSerialDictatorship(student_preferences_copy, project_list_copy);

            runCostOfMatchingPS(matching);
             **/
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
