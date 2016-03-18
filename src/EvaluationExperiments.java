import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionConversionException;

import java.util.*;

/**
 * Created by Jamie on 09/03/2016.
 */
public class EvaluationExperiments {

    private static UtilityMethods utilityMethods = new UtilityMethods();


    public static int calculateDepthOfMatching(String[][] matrix,HashMap<String, ArrayList<String>> student_preferences){

        int depth = 0;
        // Depth of a matching is equal

        return depth;
    }




    public static Double calculateCostOfMatching(String[][] matrix, HashMap<String, ArrayList<String>> student_preferences){


        Double costOfMatching = 0.0;

        int counter = 0;
        for (String[] row : matrix){
            int j = 0;
            if (counter >= 1) { // skip the first row - contains students
                int i = 1; // skip the project labels
                while (i < row.length) {
                    Fraction fraction = utilityMethods.stringToFraction(row[i]);
                    if (fraction.compareTo(new Fraction(0)) > 0){ // if the value is more than 0
                        String project = matrix[0][i];
                        String student = matrix[counter][0];
                        // Get what choice the current project is of the student
                        int position_in_preference_list = student_preferences.get(student).indexOf(project);

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

        // TODO - take account of unmatched fraction?
        return costOfMatching;
    }

    public static double runCostOfMatchingRSD(String[][] matching, HashMap<String, ArrayList<String>> student_preferences){

        Double cost = calculateCostOfMatching(matching, student_preferences);
        System.out.println("Cost of matching for RSD: " + cost);

        return cost;
    }

    public static double runCostOfMatchingPS(String[][] matching, HashMap<String, ArrayList<String>> student_preferences){

        Double cost = calculateCostOfMatching(matching, student_preferences);
        System.out.println("Cost of matching for PS: " + cost);

        return cost;
    }

    public static double runCostOfMatchingBS(String[][] matching, HashMap<String, ArrayList<String>> student_preferences){

        Double cost = calculateCostOfMatching(matching, student_preferences);
        System.out.println("Cost of matching for BS: " + cost);

        return cost;
    }

    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        //     ask for num students
        System.out.println("Enter the number of students: ");
        int num_students = scanner.nextInt();
        //     num projects
        System.out.println("Enter the number of projects: ");
        int num_projects = scanner.nextInt();
        //     size of preference lists
        System.out.println("Enter the size of preference lists: ");
        int size_of_preference_lists = scanner.nextInt();

        System.out.println("What experiment do you want to run? ");
        System.out.println("Enter cost, depth, % of position ");
        String experiment = scanner.next();

        System.out.println("How many random instances should the experiment be run on? ");
        int num_instances = scanner.nextInt();

        System.out.println("What should the output file be named? - note don't include a file extension ");
        String output_filename = scanner.next();

        CsvWriter csvWriter = new CsvWriter(output_filename + ".csv");

        for (int i =0; i < num_instances; i++) {

            ArrayList<String> project_list = utilityMethods.generateprojects(num_projects);

            HashMap<String, ArrayList<String>> student_preferences = utilityMethods.generateStudents(num_students, project_list, size_of_preference_lists);


            ObjectCloner objectCloner = new ObjectCloner();
            //ArrayList<String> project_list_copy = new ArrayList<>(project_list);

            try {

                if (experiment.equals("cost")) {
                    // Need to create a deep copy or running boston serial removes elements from student_preferences
                    HashMap<String, ArrayList<String>> student_preferences_copy = (HashMap) objectCloner.deepCopy(student_preferences);

                    HashMap<String, ArrayList<String>> student_preferences_copy2 = (HashMap) objectCloner.deepCopy(student_preferences);

                    HashMap<String, ArrayList<String>> student_preferences_copy3 = (HashMap) objectCloner.deepCopy(student_preferences);


                    BostonSerial bs = new BostonSerial();
                    String[][] bs_matching = bs.bostonSerial(student_preferences, project_list);

                    ProbalisticSerial ps = new ProbalisticSerial();

                    String[][] ps_matching = ps.probabilisticSerialDictatorship(student_preferences_copy2, project_list);


                    ArrayList<String> student_list = new ArrayList<>();
                    for (String name : student_preferences_copy3.keySet()) {
                        student_list.add(name);
                    }
                    System.out.println("Student list:" + student_list);


                    RandomSerialDictatorship rsd = new RandomSerialDictatorship();

                    String[][] rsd_matching = rsd.randomSerialDictatorship(student_list, student_preferences_copy3, project_list, 100);


                    System.out.println("Running Evaluation on matchings generated......\n\n");

                    //for (String[] row : bs_matching){
                    //System.out.println(Arrays.toString(row));
                    //}
                    System.out.println();
                    // need to pass a copy because we need the original state of the the preference lists to calculate cost
                    double cost_bs = runCostOfMatchingBS(bs_matching, student_preferences_copy);
                    System.out.println();
                    //for (String[] row : ps_matching){
                    //    System.out.println(Arrays.toString(row));
                    //}
                    System.out.println();
                    double cost_ps = runCostOfMatchingPS(ps_matching, student_preferences_copy);
                    System.out.println();
                    //for (String[] row : rsd_matching){
                    //    System.out.println(Arrays.toString(row));
                    //}
                    System.out.println();
                    double cost_rsd = runCostOfMatchingRSD(rsd_matching, student_preferences_copy);

                    csvWriter.csvWriteLine(cost_rsd, cost_ps, cost_bs);

                }else if (experiment.equals("depth"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        csvWriter.closeCsv();

    }

}
