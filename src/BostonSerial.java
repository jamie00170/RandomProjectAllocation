import java.util.*;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionConversionException;


/**
 * Created by Jamie on 15/11/2015.
 */


public class BostonSerial {

    private static UtilityMethods utilityMethods = new UtilityMethods();

    public HashMap<String, Fraction> getCurrentProjects(HashMap<String, ArrayList<String>> student_preferences, HashMap<String, Fraction> current_projects, HashMap<String, Integer> current_position_in_prefernce_list){

        // reset current projects
        for(String project : current_projects.keySet()){
            current_projects.put(project, current_projects.get(project).subtract(current_projects.get(project)));
        }

        for (String student : student_preferences.keySet()){
            // Get the students preferences
            ArrayList<String> preferences =  student_preferences.get(student);
            if (!preferences.isEmpty()) {
                // current project
                String current_project = preferences.get(current_position_in_prefernce_list.get(student));
                //System.out.println(student + " " + current_project);
                current_projects.put(current_project, current_projects.get(current_project).add(new Fraction(1)));
            }
        }
        return current_projects;
    }

    public void removeMatched(Map<String, ArrayList<String>> student_preferences, Map<String, Fraction> student_allocation, HashMap<String, Integer> current_position_in_preference_list){

        ArrayList<String> student_list = new ArrayList<>();
        for (String student : student_preferences.keySet()) {
            student_list.add(student);
        }

        ArrayList<String> names_to_remove = new ArrayList<>();
        for (String student : student_list) {
            if (student_allocation.get(student).equals(new Fraction(1))) {
                names_to_remove.add(student);
            }
            int size_preference_list = student_preferences.get(student).size();

            // If reached the end of the students prefernce list - remove student
            if (current_position_in_preference_list.get(student) > size_preference_list -1) {
                names_to_remove.add(student);
            }

        }

        for (String name : names_to_remove){
            student_preferences.remove(name);
        }

    }


    public String[][] bostonSerial(HashMap<String, ArrayList<String>> student_preferences, ArrayList<String> project_list){

        String[][] matrix = utilityMethods.setUpMatrix(student_preferences.keySet(), project_list);

        // Stores the amount left of each project to be consumed
        HashMap<String, Fraction> project_allocation = new HashMap<>();

        // Stores the amount of each student left to be consumed
        HashMap<String, Fraction> student_allocation = new HashMap<>();


        int i = 0;
        while (i < project_list.size()){
            project_allocation.put(project_list.get(i), new Fraction(0));
            i++;
        }
        System.out.println(project_allocation.toString());

        for(String name : student_preferences.keySet()){
            student_allocation.put(name, new Fraction(0));
        }
        System.out.println(student_allocation.toString());
        System.out.println(student_preferences.toString());


        HashMap<String, Fraction> current_projects = new HashMap<>();
        int k = 0;
        while (k < project_list.size()){
            current_projects.put((String) project_list.get(k), new Fraction(0));
            k++;
        }

        // Store the max any project and the any student can be incremented in a given round
        HashMap<String, Fraction> max_project_increments = new HashMap<>();
        HashMap<String, Fraction> max_student_increments = new HashMap<>();

        HashMap<String, Integer> current_position_in_preference_list = new HashMap<>();
        for (String student: student_preferences.keySet()){
            current_position_in_preference_list.put(student, 0);
        }


        while(!student_preferences.isEmpty()){

            //Clear max student and max project increment hash maps
            max_project_increments.clear();
            max_student_increments.clear();


            // Get current projects being consumed by students
            current_projects = getCurrentProjects(student_preferences, current_projects, current_position_in_preference_list);
            //System.out.println("Current Projects: " + currentProjects.toString());

            // Store the max each project can be incremented by for the current round
            for (String project : current_projects.keySet()){
                // Only add projects that are being consumed in the current round
                //if (current_projects.get(project).compareTo(new Fraction(0)) == 0)
                //    continue;

                Fraction max_increment_for_project = new Fraction(1).subtract(project_allocation.get(project));
                if (current_projects.get(project).compareTo(new Fraction(1)) > 0){
                    Fraction new_increment = max_increment_for_project.divide(current_projects.get(project));
                    //System.out.println("increment: " + max_increment_for_project + " divided by: " + current_projects.get(project) + " = " +  max_increment_for_project.divide(current_projects.get(project)));
                    max_project_increments.put(project, new_increment);
                }else{
                    max_project_increments.put(project, max_increment_for_project);
                }
            }

            // Store the max each student can be incremented each round
            for (String student: student_preferences.keySet()){

                Fraction max_increment_for_student = new Fraction(1).subtract(student_allocation.get(student));
                max_student_increments.put(student, max_increment_for_student);

            }

            //System.out.println("Max increments for projects: " + max_project_increments);
            //System.out.println("Max increments for students: " + max_student_increments);


            // for each student that hasn't yet been matched
            for (String student : student_preferences.keySet()) {

                // if the student has picked a project that is already matched then skip the student
                // get the students list of preferences
                ArrayList<String> preferences = student_preferences.get(student);
                // get their first available choice - matched choices have been removed to always index 0
                if (preferences.isEmpty())
                    continue;
                String current_project = preferences.get(current_position_in_preference_list.get(student));
                // if the student has picked a project that is already matched then skip the student
                if (project_allocation.get(current_project).compareTo(new Fraction(1)) == 0){
                    System.out.println("Student: " + student + " Project: " + current_project + " Skipped for this round");
                    System.out.println();
                    continue;

                }
                // Increment value is equal to the minimum of max_student_increments.get(student) and
                // max_project_increments.get(project)
                Fraction incrementValue;
                Fraction max_student_increment = max_student_increments.get(student);
                Fraction max_project_increment = max_project_increments.get(current_project);

                if (max_student_increment.compareTo(max_project_increment) < 0){
                    incrementValue = max_student_increment;
                }else{
                    incrementValue = max_project_increment;
                }


                System.out.println("Student: " + student + " Project: " + current_project);
                System.out.println("Increment: " + incrementValue + "\n");

                student_allocation.put(student, student_allocation.get(student).add(incrementValue));
                project_allocation.put(current_project, project_allocation.get(current_project).add(incrementValue));
                // Increment values in the matrix
                utilityMethods.incrementValue(matrix, student, current_project, incrementValue);

            }

            // If the project the student is consumed fully during a round, or was skipped
            // increment the current position in preference list students consuming

            for (String student : student_preferences.keySet()) {

                ArrayList<String> preference_list = student_preferences.get(student);
                String current_project = preference_list.get(current_position_in_preference_list.get(student));

                if (project_allocation.get(current_project).compareTo(new Fraction(1)) == 0) {
                    // increment position in students preference list if not on last project
                    current_position_in_preference_list.put(student, current_position_in_preference_list.get(student) + 1);
                }

            }

            //System.out.println("Current position in preference lists: " + current_position_in_preference_list);

            //System.out.println("Student Allocation: " + student_allocation.toString());
            //System.out.println("Project Allocation: " + project_allocation.toString());
            System.out.println("Student preferences before removal: " + student_preferences.toString());
            // Removed matched Students and Projects
            removeMatched(student_preferences, student_allocation, current_position_in_preference_list);
            System.out.println("Student preferences: " + student_preferences.toString());


        }

        for (String[] row : matrix){
            System.out.println(Arrays.toString(row));
        }

        return matrix;
    }

}
