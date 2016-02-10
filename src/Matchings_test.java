import java.lang.reflect.Array;
import java.util.*;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;

/**
 * Created by Jamie on 31/01/2016.
 */
public class Matchings_test {

    public static int factorial(int n) {
        int fact = 1; // this  will be the result
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }

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


    public static HashMap<String, String> randomSerialDictatorship(HashMap<String, ArrayList<String>> student_preferences, ArrayList<String> project_list, ArrayList<String> priority_list){

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


    public static void enum_all_matchings(ArrayList<String> student_list, List<String> project_list, HashMap<String, ArrayList<String>> student_preferences , HashMap<String, String> matching){

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
            System.out.println("Current permutation: " + project_permutation);
            int i = 0;
            boolean student_worse_off_than_M = false;
            boolean student_better_off_than_M = false;

            while (i < student_list.size()){

                String students_project_in_current_permutation = project_permutation.get(i);
                // TODO if student not in matching make their ith_choice in M integer.MaxValue
                String students_project_in_matching = matching.get(student_list.get(i));

                System.out.println("Current_student: " + student_list.get(i));
                System.out.println("Students project in M: " + students_project_in_matching );
                System.out.println("Students project is M'" + students_project_in_current_permutation);
                ArrayList<String> students_preferences = student_preferences.get(student_list.get(i));
                System.out.println("Current students preference list: " + students_preferences);
                // Check if the current student is worse off than in M
                int ith_choice_in_M = students_preferences.indexOf(students_project_in_matching);
                System.out.println("ith choice in M: " + ith_choice_in_M);
                // get the project the student is matched to in M
                int ith_choice_in_current_permutation = project_permutation.indexOf(students_project_in_current_permutation);
                System.out.println("ith choice in M': " + ith_choice_in_current_permutation);
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
                break;
            }

        }

        System.out.println("Matching is pareto optimal!");
    }

    public static void main(String[] args) {

        ArrayList<String> project_list = generateprojects(4);

        HashMap<String, ArrayList<String>> student_preferences = generateStudents(4, project_list);

        ArrayList<String> student_list = new ArrayList<>();

        for(String student : student_preferences.keySet()){
            student_list.add(student);
        }

        System.out.println("Student List: " + student_list.toString());
        System.out.println("Project List:" + project_list.toString());
        System.out.println("Student Preferences: " + student_preferences.toString());


        //A matching where the student is the key and the project they are matched to in the matching is the value
        HashMap<String, String> matching = new HashMap<>();
        matching = randomSerialDictatorship(student_preferences, project_list, student_list);

        System.out.println("Matching: " + matching);


        System.out.println("------------------enum all matchings running--------------------");
        enum_all_matchings(student_list, project_list, student_preferences, matching);

    }
}
