import org.apache.commons.math3.fraction.Fraction;

import java.util.*;

/**
 * Created by Jamie on 12/03/2016.
 */
public class ProbalisticSerial {

    private static UtilityMethods utilityMethods = new UtilityMethods();

    public String[][] probabilisticSerialDictatorship(HashMap<String, ArrayList<String>> student_preferences, ArrayList<String> project_list){


        String[][] matrix = utilityMethods.setUpMatrix(student_preferences.keySet(),project_list);

        // Stores the amount left of each project to be consumed
        Map<String, Fraction> project_allocation = new HashMap<>();

        // Stores the amount of each student left to be consumed
        Map<String, Fraction> student_allocation = new HashMap<>();

        int i = 0;
        while (i < project_list.size()){
            project_allocation.put( project_list.get(i), new Fraction(0));
            i++;
        }
        System.out.println(project_allocation.toString());

        for(Object name : student_preferences.keySet()){
            student_allocation.put((String) name, new Fraction(0));
        }
        System.out.println(student_allocation.toString());
        System.out.println(student_preferences.toString());


        HashMap<String, Fraction> current_projects = new HashMap<>();
        int k = 0;
        while (k < project_list.size()){
            current_projects.put(project_list.get(k), new Fraction(0));
            k++;
        }

        // while projects are still to be allocated or students are fully matched i.e have a combined total of 1
        while(!student_preferences.isEmpty()){
            // Get current projects being consumed by students
            utilityMethods.getCurrentProjects(student_preferences, current_projects);

            System.out.println("Current projects:" + current_projects);
            String max_project = "";

            HashMap<String, Fraction> max_increment_for_projects = new HashMap<>();
            HashMap<String, Fraction> max_increment_for_students = new HashMap<>();

            for (String project: current_projects.keySet()){
                if (!(current_projects.get(project).equals(new Fraction(0)))){
                    Fraction max_increment_for_current_project = new Fraction(1).subtract(project_allocation.get(project)).divide(current_projects.get(project));
                    max_increment_for_projects.put(project, max_increment_for_current_project );
                }
            }

            // Store the max each student can be incremented each round
            for (String student: student_preferences.keySet()){

                Fraction max_increment_for_student = new Fraction(1).subtract(student_allocation.get(student));
                max_increment_for_students.put(student, max_increment_for_student);

            }


            Fraction increment_for_round;
            if (!max_increment_for_projects.isEmpty()) {
                increment_for_round = Collections.min(max_increment_for_projects.values());

                System.out.println("Max increments for projects: " + max_increment_for_projects);
                System.out.println("Max increments for students: " + max_increment_for_students);

                for (Map.Entry<String, Fraction> entry : max_increment_for_projects.entrySet()) {
                    if (entry.getValue().equals(increment_for_round)) {
                        max_project = entry.getKey();
                        break;
                    }

                }

                Fraction max_student_increment = Collections.min(max_increment_for_students.values());
                //If increment for round would cause a student to be over allocated
                if (increment_for_round.compareTo(max_student_increment) > 0 ){
                    increment_for_round = max_student_increment;
                }

                System.out.println("Max project:" + max_project);

                System.out.println("Increment: " + increment_for_round);


                // for each student that hasn't yet been matched
                for (String name : student_preferences.keySet()) {
                    // get the students list of preferences
                    ArrayList<String> preferences = student_preferences.get(name);
                    // get their first available choice - matched choices have been removed to always index 0
                    if (preferences.isEmpty())
                        continue;
                    String current_project = preferences.get(0);

                    student_allocation.put(name, student_allocation.get(name).add(increment_for_round));
                    project_allocation.put(current_project, project_allocation.get(current_project).add(increment_for_round));
                    // Increment values in the matrix
                    int[] coordinates = utilityMethods.getCoordinates(matrix, name, current_project);
                    String matrix_value = matrix[coordinates[0]][coordinates[1]];
                    System.out.println("Current matrix value: " + matrix_value);
                    Fraction f = utilityMethods.stringToFraction(matrix_value);

                    Fraction new_matrix_value = f.add(increment_for_round);
                    System.out.println("Current matrix value transformed into a fraction: " + f);

                    System.out.println("incrementing: " + increment_for_round + " to " + name + " and " + current_project);
                    System.out.println("New matrix value: " + new_matrix_value);
                    System.out.println();
                    matrix[coordinates[0]][coordinates[1]] = new_matrix_value.toString();

                }
            }

            System.out.println("Student Allocation: " + student_allocation.toString());
            System.out.println("Project Allocation: " + project_allocation.toString());
            System.out.println("Student preferences before removal: " + student_preferences.toString());
            // Removed matched Students and Projects
            utilityMethods.removeMatched(student_preferences, project_allocation, student_allocation);
            System.out.println("Student preferences: " + student_preferences.toString());
        }

        System.out.println("Student Allocation: " + student_allocation.toString());
        System.out.println("Project Allocation: " + project_allocation.toString());

        for (String[] row : matrix){
            System.out.println(Arrays.toString(row));
        }
        return matrix;
    }

}
