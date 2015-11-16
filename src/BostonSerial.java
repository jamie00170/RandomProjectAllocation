import java.util.*;

/**
 * Created by Jamie on 15/11/2015.
 */

public class BostonSerial {

    public static Map<String, Double> getCurrentProjects(Map<String, ArrayList<String>> student_preferences, Map<String, Double> current_projects){

        // reset current projects
        for(String project : current_projects.keySet()){
            current_projects.put(project, current_projects.get(project) - current_projects.get(project));
        }

        for (String student : student_preferences.keySet()){
            // Get the students preferences
            ArrayList<String> preferences =  student_preferences.get(student);
            System.out.println(student + " " + preferences.get(0));
            current_projects.put(preferences.get(0), current_projects.get(preferences.get(0)) + 1.0);
        }
        return current_projects;
    }


    public static void removeMatched(Map<String, ArrayList<String>> student_preferences, Map<String, Double> project_allocation, Map<String, Double> student_allocation){

        //System.out.println("Attempting to remove matched students and projects .... ");
        ArrayList<String> student_list = new ArrayList<>();
        for (String name : student_preferences.keySet()) {
            student_list.add(name);

        }

        for (String name : student_list) {
            if (student_allocation.get(name) >= 1.0) {
                student_preferences.remove(name);
            }
        }
    }

    public static boolean check_sizes(Map<String, Double> resource_allocation){
        Collection<Double> scores = resource_allocation.values();
        int num_of_projects_used = 0;
        for (double score : scores){
            if (score == 1.0)
                num_of_projects_used++;
        }
        return (num_of_projects_used == scores.size()) || resource_allocation.isEmpty();
    }

    public static void incrementValue(String[][] matrix, String student, String project, Double calculated_value){
        int i = 0;
        int j;

        int[] coordinates = new int[2];

        while( i < matrix.length){
            j = 0;
            while (j < matrix[i].length){
                // only have to search first column and row
                if(matrix[i][j].equals(student))
                    coordinates[0] = i;
                if(matrix[i][j].equals(project))
                    coordinates[1] = j;
                j++;
            }
            i++;
        }

        Double value = Double.parseDouble(matrix[coordinates[0]][coordinates[1]]);
        value += calculated_value;
        matrix[coordinates[0]][coordinates[1]] = Double.toString(value);
    }



    public void bostonSerial(Map<String, ArrayList<String>> student_preferences, ArrayList<String> project_list){

        // Create matrix
        String[][] matrix = new String[(student_preferences.size() + 1)][(project_list.size() + 1)];

        matrix[0][0] = "-";
        int i = 1;
        for (String student : student_preferences.keySet()) {
            matrix[i][0] = student;
            i++;
        }


        int j = 1;
        for (String project : project_list){

            matrix[0][j] = project;
            j++;
        }

        i = 1;
        while( i < matrix.length){
            j = 1;
            while (j < matrix[i].length){
                matrix[i][j] = "0.0";
                j++;
            }
            i++;
        }


        // Stores the amount left of each project to be consumed
        Map<String, Double> project_allocation = new HashMap<>();

        // Stores the amount of each student left to be consumed
        Map<String, Double> student_allocation = new HashMap<>();


        i = 0;
        while (i < project_list.size()){
            project_allocation.put(project_list.get(i), 0.0);
            i++;
        }
        System.out.println(project_allocation.toString());

        for(Object name : student_preferences.keySet()){
            student_allocation.put((String) name, 0.0);
        }
        System.out.println(student_allocation.toString());
        //System.out.println(student_preferences.toString());


        Map<String, Double> current_projects = new HashMap<>();
        int k = 0;
        while (k < project_list.size()){
            current_projects.put((String) project_list.get(k), 0.0);
            k++;
        }
        int p = 0;
        while(p < project_allocation.size()){
            // Get current projects being consumed by students
            Map<String, Double> currentProjects = getCurrentProjects(student_preferences, current_projects);
            System.out.println("Current Projects: " + currentProjects.toString());

            // Get the maximum amount each project can be incremented by
            // 1/highest number of students consuming a project - max(current_projects.values())


            // for each student that hasn't yet been matched
            for (String name : student_preferences.keySet()){
                // if the student has picked a project that is already matched then skip the student
                // get the students list of preferences
                ArrayList<String> preferences = student_preferences.get(name);
                // get their first available choice - matched choices have been removed to always index 0
                String current_project = preferences.get(p);
                // if the student has picked a project that is already matched then skip the student
                if (project_allocation.get(current_project) >= 1.0)
                    continue;

                // Find out the number of student consuming this students current project
                //Double num_of_students_consuming = current_projects.get(current_project);
                // Find the amount of the project yet to be assigned
                //Double amount_of_project_remaining = (1.0 - (project_allocation.get(current_project)));

                //System.out.println(name +" has: " + (amount_of_project_remaining/num_of_students_consuming) + "percentage of being matched to" + current_project);
                // increment student allocation + project allocation
                // TAKE ACCOUNT OF AMOUNT OF PROJECT REMAINING
                System.out.println("Increment: " + (1.0/current_projects.get(current_project)));
                student_allocation.put(name, student_allocation.get(name) + (1.0/current_projects.get(current_project)));
                project_allocation.put(current_project, project_allocation.get(current_project) + (1.0/current_projects.get(current_project)));
                // Increment values in the matrix
                incrementValue(matrix, name, current_project, (1.0/current_projects.get(current_project)));
            }
            p++;
            System.out.println("Student Allocation: " + student_allocation.toString());
            System.out.println("Project Allocation: " + project_allocation.toString());
            System.out.println("Student preferences before removal: " + student_preferences.toString());
            // Removed matched Students and Projects
            removeMatched(student_preferences, project_allocation, student_allocation);
            System.out.println("Student preferences: " + student_preferences.toString());
        }

        System.out.println("Student Allocation: " + student_allocation.toString());
        System.out.println("Project Allocation: " + project_allocation.toString());

        for (String[] row : matrix){
            System.out.println(Arrays.toString(row));
        }
    }
}
