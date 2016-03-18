import java.util.*;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionConversionException;


/**
 * Created by Jamie on 15/11/2015.
 */


public class BostonSerial {

    private static UtilityMethods utilityMethods = new UtilityMethods();


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


        while(!(utilityMethods.check_sizes(project_allocation) || utilityMethods.check_sizes(student_allocation) || student_preferences.isEmpty())){

            //Clear max student and max project increment hash maps
            max_project_increments.clear();
            max_student_increments.clear();


            // Get current projects being consumed by students
            HashMap<String, Fraction> currentProjects = utilityMethods.getCurrentProjects(student_preferences, current_projects);
            System.out.println("Current Projects: " + currentProjects.toString());

            // Store the max each project can be incremented by for the current round
            for (String project : current_projects.keySet()){
                // Only add projects that are being consumed in the current round
                if (current_projects.get(project).compareTo(new Fraction(0)) == 0)
                    continue;

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

            System.out.println("Max increments for projects: " + max_project_increments);
            System.out.println("Max increments for students: " + max_student_increments);

            // for each student that hasn't yet been matched
            for (String name : student_preferences.keySet()) {

                // if the student has picked a project that is already matched then skip the student
                // get the students list of preferences
                ArrayList<String> preferences = student_preferences.get(name);
                // get their first available choice - matched choices have been removed to always index 0
                if (preferences.isEmpty())
                    continue;
                String current_project = preferences.get(0);
                // if the student has picked a project that is already matched then skip the student
                if (project_allocation.get(current_project).compareTo(new Fraction(1)) == 0)
                    continue;

                // Increment value is equal to the minimum of max_student_increments.get(student) and
                // max_project_increments.get(project)
                Fraction incrementValue;
                Fraction max_student_increment = max_student_increments.get(name);
                Fraction max_project_increment = max_project_increments.get(current_project);

                if (max_student_increment.compareTo(max_project_increment) < 0){
                    incrementValue = max_student_increment;
                }else{
                    incrementValue = max_project_increment;
                }


                System.out.println("Student: " + name + " Project: " + current_project);
                System.out.println("Increment: " + incrementValue + "\n");

                student_allocation.put(name, student_allocation.get(name).add(incrementValue));
                project_allocation.put(current_project, project_allocation.get(current_project).add(incrementValue));
                // Increment values in the matrix
                utilityMethods.incrementValue(matrix, name, current_project, incrementValue);

            }

            System.out.println("Student Allocation: " + student_allocation.toString());
            System.out.println("Project Allocation: " + project_allocation.toString());
            System.out.println("Student preferences before removal: " + student_preferences.toString());
            // Removed matched Students and Projects
            utilityMethods.removeMatched(student_preferences, project_allocation, student_allocation);
            System.out.println("Student preferences: " + student_preferences.toString());
        }

        for (String[] row : matrix){
            System.out.println(Arrays.toString(row));
        }

        return matrix;
    }

}
