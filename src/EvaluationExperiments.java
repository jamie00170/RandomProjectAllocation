import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionConversionException;

import java.util.*;

/**
 * Created by Jamie on 09/03/2016.
 */
public class EvaluationExperiments {

    private static UtilityMethods utilityMethods = new UtilityMethods();


    public static int calculateDepthOfMatching(String[][] matrix, HashMap<String, ArrayList<String>> student_preferences){

        int depth = 0;
        // Depth of a matching is equal largest index of a project in a students preference list that has a non-zero value
        // Look at all last choices - check if non-zero value if the matrix that corresponds to these, then check next last choice
        //  can stop when a non-zero value is found

        // Assumes all preference lists are the same size
        String arbitrary_student = "";
        for (String student : student_preferences.keySet()) {
            arbitrary_student = student;
            break;
        }

        // set the current index of projects to be the last
        int current_index_of_projects = student_preferences.get(arbitrary_student).size() -1;

        while (current_index_of_projects >= 0) {

            for (String student : student_preferences.keySet()) {

                // look at current_index_of_projects choice
                String current_index_choice_project = student_preferences.get(student).get(current_index_of_projects);

                // check if no non-zero value at student - current_index_choice_project

                // get the coordinates in the martrix
                int[] coordinates = utilityMethods.getCoordinates(matrix, student, current_index_choice_project);

                // convert the matrix value at coordinates to a fraction
                String string_matrix_value = matrix[coordinates[0]][coordinates[1]];
                Fraction matrix_value = utilityMethods.stringToFraction(string_matrix_value);

                // if the value if non-zero depth of matching is equal to current index of the projects
                if (matrix_value.compareTo(new Fraction(0)) != 0) {
                    // current index is the depth of the matching
                    return current_index_of_projects + 1; // depth starts at 1, index starts at 0
                }

            }
            current_index_of_projects--;
        }

        return depth + 1; // depth starts at 1, index starts at 0
    }


    public static Double calculateValueOfMatching(String[][] matrix, HashMap<String, ArrayList<String>> student_preferences){

        // A better value =  a better matching

        Double valueOfMatching = 0.0;

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

                        int multiplier = student_preferences.get(student).size() - position_in_preference_list;

                        Fraction increment_fraction = new Fraction(multiplier * fraction.doubleValue());
                        valueOfMatching += increment_fraction.doubleValue();
                        //System.out.println();
                    }
                    i++;
                }
                //System.out.println();
            }
            counter++;
        }

        return valueOfMatching;
    }

    public static double runValueOfMatchingRSD(String[][] matching, HashMap<String, ArrayList<String>> student_preferences){

        Double cost = calculateValueOfMatching(matching, student_preferences);
        System.out.println("Cost of matching for RSD: " + cost);

        return cost;
    }

    public static double runValueOfMatchingPS(String[][] matching, HashMap<String, ArrayList<String>> student_preferences){

        Double cost = calculateValueOfMatching(matching, student_preferences);
        System.out.println("Cost of matching for PS: " + cost);

        return cost;
    }

    public static double runValueOfMatchingBS(String[][] matching, HashMap<String, ArrayList<String>> student_preferences){

        Double cost = calculateValueOfMatching(matching, student_preferences);
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
        System.out.println("Enter value, depth, % of position ");
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

                if (experiment.equals("value")) {
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

                    String[][] rsd_matching = rsd.randomSerialDictatorship(student_list, student_preferences_copy3, project_list, 1000);


                    System.out.println("Running Evaluation on matchings generated......\n\n");

                    //for (String[] row : bs_matching){
                    //System.out.println(Arrays.toString(row));
                    //}
                    System.out.println();
                    // need to pass a copy because we need the original state of the the preference lists to calculate cost
                    double cost_bs = runValueOfMatchingBS(bs_matching, student_preferences_copy);
                    System.out.println();
                    //for (String[] row : ps_matching){
                    //    System.out.println(Arrays.toString(row));
                    //}
                    System.out.println();
                    double cost_ps = runValueOfMatchingPS(ps_matching, student_preferences_copy);
                    System.out.println();
                    //for (String[] row : rsd_matching){
                    //    System.out.println(Arrays.toString(row));
                    //}
                    System.out.println();
                    double cost_rsd = runValueOfMatchingRSD(rsd_matching, student_preferences_copy);

                    csvWriter.csvWriteLineValue(cost_rsd, cost_ps, cost_bs);

                }else if (experiment.equals("depth")){

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

                    String[][] rsd_matching = rsd.randomSerialDictatorship(student_list, student_preferences_copy3, project_list, 1000);


                    System.out.println("Running Evaluation on matchings generated......\n\n");


                    int rsd_depth = calculateDepthOfMatching(rsd_matching, student_preferences_copy);
                    int ps_depth = calculateDepthOfMatching(ps_matching, student_preferences_copy);
                    int bs_depth = calculateDepthOfMatching(bs_matching, student_preferences_copy);

                    csvWriter.csvWriteLineDepth(rsd_depth, ps_depth, bs_depth);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        csvWriter.closeCsv();

    }

}
