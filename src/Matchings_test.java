import java.lang.reflect.Array;
import java.util.*;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionConversionException;

/**
 * Created by Jamie on 31/01/2016.
 */
public class Matchings_test {

    private static UtilityMethods utilityMethods = new UtilityMethods();

    public static ArrayList<String> generateprojects(int num_of_projects) {

        ArrayList<String> project_list = new ArrayList<>();
        int i = 1;
        while (i <= num_of_projects) {
            project_list.add("Project" + i);
            i++;
        }
        return project_list;

    }

    public static HashMap<String, ArrayList<String>> generateStudents(int num_of_students, ArrayList<String> project_list) {


        HashMap<String, ArrayList<String>> student_preferences = new HashMap<>();
        int j = 1;
        while (j <= num_of_students) {
            Collections.shuffle(project_list);
            student_preferences.put("Student" + j, new ArrayList<>(project_list));
            j++;
        }
        return student_preferences;
    }


    public static HashMap<String, String> randomSerialDictatorship(HashMap<String, ArrayList<String>> student_preferences, ArrayList<String> project_list, ImmutableList<String> priority_list){

        HashMap<String, String> matching = new HashMap<>();
        // Initialise projects_allocated array
        ArrayList<String> projects_allocated = new ArrayList<>();
        int i = 0;
        // Start here - need to format string into list first
        projects_allocated.clear();
        while (i < priority_list.size()) {
            // If the current student in the priority list is in student_preferences
            if (student_preferences.containsKey(priority_list.get(i))) {
                String name = priority_list.get(i);
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
                        // Add the project to the projects_allocated list
                        projects_allocated.add(preferences.get(j));
                        matching.put(name, preferences.get(j));
                        break;
                    }
                    j++;
                }
            } else {
                System.out.println("Name not in student list!");
            }
            i++;
        }
        return matching;
    }


    public boolean enum_all_matchings(ArrayList<String> student_list, List<String> project_list, HashMap<String, ArrayList<String>> student_preferences , HashMap<String, String> matching){

        // generate all permutations of project list and match project_list[i] in current permutation to student_list[i]
        // to create a matching also unmatched project to be added to project list

        // To test for pareto optimality in M we need to check if there is no other matching in the set of
        // all possible matchings where for some matching M' a student is worse off in the current
        // matching than in M whilst some other student is better off in the current matching M'

        //A matching M is Pareto optimal if there is no other matching M0
        // such that no agent is worse off in M0 than in M, whilst some agent is better
        // off in M0 than in M

        Collection project_col = project_list;

        Collection<ImmutableList<String>> project_permutations = Collections2.permutations(project_col);

        // for each possible matching
        for (Object project : project_permutations){
            ImmutableList<String> project_permutation = (ImmutableList<String>) project;
            //System.out.println("Current permutation: " + project_permutation);
            int i = 0;
            boolean student_worse_off_than_M = false;
            boolean student_better_off_than_M = false;

            while (i < student_list.size()){

                String students_project_in_current_permutation = project_permutation.get(i);
                // TODO if student not in matching make their ith_choice in M integer.MaxValue
                String students_project_in_matching = matching.get(student_list.get(i));

                //System.out.println("Current_student: " + student_list.get(i));
                //System.out.println("Students project in M: " + students_project_in_matching );
                //System.out.println("Students project is M'" + students_project_in_current_permutation);
                ArrayList<String> students_preferences = student_preferences.get(student_list.get(i));
                //System.out.println("Current students preference list: " + students_preferences);
                // Check if the current student is worse off than in M
                int ith_choice_in_M = students_preferences.indexOf(students_project_in_matching);
                //System.out.println("ith choice in M: " + ith_choice_in_M);
                // get the project the student is matched to in M
                int ith_choice_in_current_permutation = project_permutation.indexOf(students_project_in_current_permutation);
                //System.out.println("ith choice in M': " + ith_choice_in_current_permutation);
                // compare the porjects based on the index value in studentpreferences
                if (ith_choice_in_M < ith_choice_in_current_permutation){
                    student_worse_off_than_M = true;
                } else if (ith_choice_in_current_permutation < ith_choice_in_M){
                    student_better_off_than_M = true;
                }

                i++;
            }
            if (student_better_off_than_M && student_worse_off_than_M){
                System.out.println("Matching is not Pareto optimal!");
                return false;
            }

        }

        System.out.println("Matching is pareto optimal!");
        return true;
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

    public static boolean checkRowsColumns(String[][] matrix){


        //Check columns
        int i = 1; // ignore first row
        while (i < matrix.length) {
            int j = 1; // ignore first column in each row
            Fraction currentColumnTotal = new Fraction(0);
            while (j < matrix[i].length) {// TODO : fix this will loop for number of projects instead of students
                currentColumnTotal = currentColumnTotal.add(stringToFraction(matrix[j][i]));
                //System.out.println(stringToFraction(matrix[j][i]));
                j++;
            }
            System.out.println("Current column total: " + currentColumnTotal);

            if (!currentColumnTotal.equals(new Fraction(1))) {
                return false;
            }
            i++;
        }



        // Check rows
        i = 1; // ignore first row
        while (i < matrix.length){
            int j = 1; // ignore first column in each row
            Fraction currentRowTotal = new Fraction(0);
            while (j < matrix[i].length){
                currentRowTotal = currentRowTotal.add(stringToFraction(matrix[i][j]));
                //System.out.println(stringToFraction(matrix[i][j]));
                //System.out.println("Current Row Total: " + currentRowTotal);

                j++;
            }

            System.out.println("Current row total: " + currentRowTotal);

            if (!currentRowTotal.equals(new Fraction(1))){
                return false;
            }
            i++;
        }
        return true;
    }

    public static void runCheckRowsColumnBostonSerial(HashMap<String, ArrayList<String>> student_preferences,  ArrayList<String> project_list){
        BostonSerial bs = new BostonSerial();
        String[][] matrix = bs.bostonSerial(student_preferences, project_list);


        if (checkRowsColumns(matrix)){
            System.out.println("All rows and columns add up to 1");
        }else{
            System.out.println("There is a row or column that doesn't add up to 1");
        }
    }

    public static void runCheckRowsColumnProalisticSerial(HashMap<String, ArrayList<String>> student_preferences,  ArrayList<String> project_list){

        Main main = new Main();

        String[][] matrix = main.probabilisticSerialDictatorship(student_preferences, project_list);

        if(checkRowsColumns(matrix)){
            System.out.println("All rows and columns add up to 1");
        }else{
            System.out.println("There is a row or column that doesn't add up to 1");
        }


    }


    public static void main(String[] args) {


        ArrayList<String> project_list = generateprojects(3);

        HashMap<String, ArrayList<String>> student_preferences = generateStudents(3, project_list);

        ArrayList<String> student_list = new ArrayList<>();

        for(String student : student_preferences.keySet()){
            student_list.add(student);
        }

        System.out.println("Student List: " + student_list.toString());
        System.out.println("Project List:" + project_list.toString());
        System.out.println("Student Preferences: " + student_preferences.toString());



        System.out.println("Running columns and rows check on Boston Serial....");
        runCheckRowsColumnBostonSerial(student_preferences, project_list);

        //System.out.println("Running columns and rows check on probabilistic serial.....");
        //runCheckRowsColumnProalisticSerial(student_preferences, project_list);

        System.exit(0);


        //A matching where the student is the key and the project they are matched to in the matching is the value
        HashMap<String, String> matching = new HashMap<>();

        Collection student_col = student_list;
        Collection<ImmutableList<String>> student_permutations = Collections2.permutations(student_col);

        int i = 0;
        for (Object student : student_permutations) {
            ImmutableList<String> student_permutation = (ImmutableList<String>) student;
            matching = randomSerialDictatorship(student_preferences, project_list, student_permutation);

            System.out.println("Matching: " + matching);

            System.out.println("------------------ enum all matchings running on permutation number: " + i + " --------------------");
            i++;
            Matchings_test mt = new Matchings_test();
            mt.enum_all_matchings(student_list, project_list, student_preferences, matching);

        }






    }
}
