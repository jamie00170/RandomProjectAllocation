import org.apache.commons.math3.fraction.Fraction;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Jamie on 09/03/2016.
 */
public class EvaluationExperiments {

    private static UtilityMethods utilityMethods = new UtilityMethods();


    public static double calculateSizeOfMatching(String[][] matrix){

        // To calculate the size of a matching - add up each value in the matrix
        double size = 0.0;

        int i = 1;
        while(i < matrix.length){
            int j = 1;
            while (j < matrix[i].length){
                size += utilityMethods.stringToFraction(matrix[i][j]).doubleValue();
                j++;
            }
            i++;
        }

        return size;
    }

    public static double calculateAvgChanceOfBeingUnmatched(String[][] matrix, int num_students){

        double avg_chance = 0.0;

        ArrayList<Double> value_unmatched_each_student = new ArrayList<>();

        // calculate value for each row then subtract it from one
        // gives the percentage chance of being un matched for a student
        // add it to the array of values
        int i = 1; // skip project labels in first row
        while (i < matrix.length){
            int j = 1; // skip student label's in first column of each row
            double row_total = 0.0;
            while (j < matrix[i].length){
                // if the value in the matrix at i, j isn't a 0
                if (!matrix[i][j].equals("0")){

                    double current_value = utilityMethods.stringToFraction(matrix[i][j]).doubleValue();
                    row_total += current_value;

                }
                j++;
            }
            // account for double precision error
            double value_unmatched = 1.0 - row_total;
            double diff = Math.abs(row_total - 1.0);
            if (diff < 0.0001)
                value_unmatched = 0.0;

            value_unmatched_each_student.add(value_unmatched);

            i++;
        }

        System.out.println("Values unmatched: " + value_unmatched_each_student);

        // for each row total
        for (double value_unmatched : value_unmatched_each_student){
            // add it to avg_chance
            avg_chance += value_unmatched;
        }

        // divide avg_chance by num_students
        avg_chance  = avg_chance / num_students;

        System.out.println("Average chance of being unmatched: " + avg_chance);
        return avg_chance;
    }


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

    public static int[] calculateProfileOfMatching(String[][] matching,  HashMap<String, ArrayList<String>> student_preferences, int size_of_preference_list){

        // find out the size of preference list
        int[] profile = new int[size_of_preference_list]; // need an index for each position in the preference list

        // for each student / row in the matrix
        // find the highest value in the row

        // Start with the students first choice ??
        for (String student: student_preferences.keySet()){

            int current_position_in_preference_list = 0;
            int index_biggest_value = 0;
            Fraction biggestValueAllocated = new Fraction(0); // create a variable to store the biggest fraction allocated
            ArrayList<String> preference_list = student_preferences.get(student);
            for (String project : preference_list){

                int[] coordinates = utilityMethods.getCoordinates(matching, student, project);
                String matrix_value = matching[coordinates[0]][coordinates[1]];
                Fraction frac_matrix_value = utilityMethods.stringToFraction(matrix_value);

                if (frac_matrix_value.compareTo(biggestValueAllocated) > 0){
                    biggestValueAllocated = frac_matrix_value;
                    index_biggest_value = current_position_in_preference_list;
                }
                current_position_in_preference_list++;
            }
            profile[index_biggest_value] = profile[index_biggest_value] + 1;
        }

        System.out.println("Profile: " + Arrays.toString(profile));
        return profile;
    }

    public static double runValueOfMatchingRSD(String[][] matching, HashMap<String, ArrayList<String>> student_preferences){

        Double cost = calculateValueOfMatching(matching, student_preferences);
        System.out.println("Value of matching for RSD: " + cost);

        return cost;
    }

    public static double runValueOfMatchingPS(String[][] matching, HashMap<String, ArrayList<String>> student_preferences){

        Double cost = calculateValueOfMatching(matching, student_preferences);
        System.out.println("Value of matching for PS: " + cost);

        return cost;
    }

    public static double runValueOfMatchingBS(String[][] matching, HashMap<String, ArrayList<String>> student_preferences){

        Double cost = calculateValueOfMatching(matching, student_preferences);
        System.out.println("Value of matching for BS: " + cost);

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

        System.out.println("Enter the number of permutations to run rsd for: ");
        System.out.println("Note for experiments 1000 - 10000 is a reasonable number for large instances, however the number of permutations cannot exceed ");
        System.out.println("the factorial of the number of students.");
        int num_permutations = scanner.nextInt();

        System.out.println("What experiment do you want to run? ");
        System.out.println("Enter value, depth, profile, unmatched, size");
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

                HashMap<String, ArrayList<String>> student_preferences_copy = (HashMap) objectCloner.deepCopy(student_preferences);

                HashMap<String, ArrayList<String>> student_preferences_copy2 = (HashMap) objectCloner.deepCopy(student_preferences);

                HashMap<String, ArrayList<String>> student_preferences_copy3 = (HashMap) objectCloner.deepCopy(student_preferences);


                ProbabilisticSerial ps = new ProbabilisticSerial();

                String[][] ps_matching = ps.probabilisticSerialDictatorship(student_preferences_copy2, project_list);


                ArrayList<String> student_list = new ArrayList<>();
                for (String name : student_preferences_copy3.keySet()) {
                    student_list.add(name);
                }
                System.out.println("Student list:" + student_list);


                RandomSerialDictatorship rsd = new RandomSerialDictatorship();

                String[][] rsd_matching = rsd.randomSerialDictatorship(student_list, student_preferences_copy3, project_list, num_permutations);

                BostonSerial bs = new BostonSerial();
                String[][] bs_matching = bs.bostonSerial(student_preferences, project_list);


                if (experiment.equals("value")) {
                    // Need to create a deep copy or running boston serial removes elements from student_preferences

                    // need to pass a copy because we need the original state of the the preference lists to calculate cost
                    double cost_bs = runValueOfMatchingBS(bs_matching, student_preferences_copy);

                    double cost_ps = runValueOfMatchingPS(ps_matching, student_preferences_copy);

                    double cost_rsd = runValueOfMatchingRSD(rsd_matching, student_preferences_copy);

                    csvWriter.csvWriteLineDouble(cost_rsd, cost_ps, cost_bs);

                }else if (experiment.equals("depth")){

                    int rsd_depth = calculateDepthOfMatching(rsd_matching, student_preferences_copy);
                    int ps_depth = calculateDepthOfMatching(ps_matching, student_preferences_copy);
                    int bs_depth = calculateDepthOfMatching(bs_matching, student_preferences_copy);

                    csvWriter.csvWriteLineInt(rsd_depth, ps_depth, bs_depth);

                }else if (experiment.equals("unmatched")){

                    double bs_chance = calculateAvgChanceOfBeingUnmatched(bs_matching, student_list.size());
                    double ps_chance = calculateAvgChanceOfBeingUnmatched(ps_matching, student_list.size());
                    double rsd_chance = calculateAvgChanceOfBeingUnmatched(rsd_matching, student_list.size());

                    csvWriter.csvWriteLineDouble(rsd_chance, ps_chance, bs_chance);

                }else if (experiment.equals("size")){


                    double rsd_size = calculateSizeOfMatching(rsd_matching);
                    double ps_size = calculateSizeOfMatching(ps_matching);
                    double bs_size = calculateSizeOfMatching(bs_matching);

                    csvWriter.csvWriteLineDouble(rsd_size, ps_size, bs_size);

                } else if (experiment.equals("profile")){

                    int[] rsd_profile = calculateProfileOfMatching(rsd_matching, student_preferences_copy, size_of_preference_lists);
                    int[] ps_profile = calculateProfileOfMatching(ps_matching, student_preferences_copy, size_of_preference_lists);
                    int[] bs_profile = calculateProfileOfMatching(bs_matching, student_preferences_copy, size_of_preference_lists);

                    csvWriter.csvWriteProfile(rsd_profile, ps_profile, bs_profile);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        csvWriter.closeCsv();

    }

}
