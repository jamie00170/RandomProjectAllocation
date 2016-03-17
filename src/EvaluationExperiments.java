import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionConversionException;

import java.util.*;

/**
 * Created by Jamie on 09/03/2016.
 */
public class EvaluationExperiments {

    private static UtilityMethods utilityMethods = new UtilityMethods();

    public static Double calculateCostOfMatching(String[][] matrix, HashMap<String, ArrayList<String>> student_preferences){

        //System.out.println("Student Preferences in Calculate cost: " + student_preferences);


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
                        //System.out.println("Student: " + student + " Project: " + project);
                        // Get what choice the current project is of the student
                        int position_in_preference_list = student_preferences.get(student).indexOf(project);
                        //System.out.println("Position in preference list: " + position_in_preference_list);
                        //System.out.println("Adding : " + (position_in_preference_list + 1) * fraction.doubleValue() + " to the cost of the matching");
                        Fraction increment_fraction = new Fraction((position_in_preference_list +1) * fraction.doubleValue());
                        costOfMatching += increment_fraction.doubleValue();
                        //System.out.println();
                    }
                    i++;
                }
                //System.out.println();
            }
            counter++;
        }

        return costOfMatching;
    }

    public static void runCostOfMatchingRSD(String[][] matching, HashMap<String, ArrayList<String>> student_preferences){

        Double cost = calculateCostOfMatching(matching, student_preferences);
        System.out.println("Cost of matching for RSD: " + cost);
    }

    public static void runCostOfMatchingPS(String[][] matching, HashMap<String, ArrayList<String>> student_preferences){

        Double cost = calculateCostOfMatching(matching, student_preferences);
        System.out.println("Cost of matching for PS: " + cost);

    }

    public static void runCostOfMatchingBS(String[][] matching, HashMap<String, ArrayList<String>> student_preferences){

        Double cost = calculateCostOfMatching(matching, student_preferences);
        System.out.println("Cost of matching for BS: " + cost);

    }

    public static void main(String[] args){

        ArrayList<String> project_list = utilityMethods.generateprojects(10);

        HashMap<String, ArrayList<String>> student_preferences = utilityMethods.generateStudents(7, project_list, 5);

        ObjectCloner objectCloner = new ObjectCloner();
        //ArrayList<String> project_list_copy = new ArrayList<>(project_list);
        try {
            // Need to create a deep copy or running boston serial removes elements from student_preferences
            HashMap<String, ArrayList<String>> student_preferences_copy = (HashMap) objectCloner.deepCopy(student_preferences);

            HashMap<String, ArrayList<String>> student_preferences_copy2 = (HashMap) objectCloner.deepCopy(student_preferences);

            HashMap<String, ArrayList<String>> student_preferences_copy3 = (HashMap) objectCloner.deepCopy(student_preferences);


            BostonSerial bs = new BostonSerial();
            String[][] bs_matching =  bs.bostonSerial(student_preferences, project_list);

            ProbalisticSerial ps = new ProbalisticSerial();

            String[][] ps_matching = ps.probabilisticSerialDictatorship(student_preferences_copy2, project_list);

            /////////////////////////////////////RSD////////////////////////////////////////////////////////

            // Set up
            // Create matrix
            String[][] matrix = utilityMethods.setUpMatrix(student_preferences_copy.keySet(), project_list);


            //String[] student_list = new String[student_preferences.size()];
            ArrayList<String> student_list = new ArrayList<>();
            for (String name : student_preferences_copy3.keySet()){
                student_list.add(name);
            }
            System.out.println("Student list:" + student_list);


            RandomSerialDictatorship rsd = new RandomSerialDictatorship();

            for (String[] row : matrix){
                System.out.println(Arrays.toString(row));
            }

            String[][] rsd_matching = rsd.permute(student_list,0, student_preferences_copy3, project_list, matrix);

            int divisor = utilityMethods.factorial(student_list.size());

            int p = 1;
            while( p < rsd_matching.length) {
                int f = 1;
                while (f < rsd_matching[p].length) {
                    try {

                        Fraction fraction = utilityMethods.stringToFraction(rsd_matching[p][f]).divide(new Fraction(divisor));

                        rsd_matching[p][f] = fraction.toString();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    } catch (FractionConversionException e) {
                        e.printStackTrace();
                    }
                    f++;
                }
                p++;
            }



            System.out.println("Running Evaluation on matchings generated......\n\n");

            //for (String[] row : bs_matching){
                //System.out.println(Arrays.toString(row));
            //}
            System.out.println();
            // need to pass a copy because we need the original state of the the preference lists to calculate cost
            runCostOfMatchingBS(bs_matching, student_preferences_copy);
            System.out.println();
            //for (String[] row : ps_matching){
            //    System.out.println(Arrays.toString(row));
            //}
            System.out.println();
            runCostOfMatchingPS(ps_matching, student_preferences_copy);
            System.out.println();
            //for (String[] row : rsd_matching){
            //    System.out.println(Arrays.toString(row));
            //}
            System.out.println();
            runCostOfMatchingRSD(rsd_matching, student_preferences_copy);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
