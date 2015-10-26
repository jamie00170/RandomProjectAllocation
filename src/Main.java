import com.sun.xml.internal.bind.v2.TODO;

import java.util.*;

// Specification + problem description +
public class Main {

    public static ArrayList<String> generateprojects(int num_of_projects) {

        ArrayList<String> project_list = new ArrayList<>();
        int i = 1;
        while (i <= num_of_projects) {
            project_list.add("Project" + i);
            i++;
        }
        return project_list;

    }

    public static Map<String, ArrayList<String>> generateStudents(int num_of_students, ArrayList project_list) {


        Map<String, ArrayList<String>> student_preferences = new HashMap<>();
        int j = 1;
        while (j <= num_of_students) {
            Collections.shuffle(project_list);
            student_preferences.put("Student" + j, new ArrayList<String>(project_list));
            j++;
        }
        return student_preferences;
    }

    public static void main(String[] args) {

        ArrayList<String> project_list = generateprojects(5);
        System.out.println("Project List:" + project_list.toString());

        Map<String, ArrayList<String>> student_preferences = generateStudents(5 , project_list);

        System.out.println("Student Preferences: " + student_preferences.toString());
        ArrayList<String> priority_list = new ArrayList<>();

        priority_list.add("Student1");
        priority_list.add("Student2");
        priority_list.add("Student3");
        priority_list.add("Student4");
        priority_list.add("Student5");

        // Create a random order of Students
        Collections.shuffle(priority_list);
        System.out.println("Priority List: " + priority_list.toString());

        //randomSerialDictatorship(student_preferences, project_list, priority_list);
        probabilisticSerialDictatorship(student_preferences, project_list);

    }

    public static void randomSerialDictatorship(Map student_preferences, ArrayList project_list, ArrayList priority_list){

        // Initialise projects_allocated array
        ArrayList<String> projects_allocated = new ArrayList<>();
        int i =0;
        // Loop for number of students
        while (i < priority_list.size()) {
            // If the current student in the priority list is in student_preferences
            if (student_preferences.containsKey(priority_list.get(i))) {
                String name = priority_list.get(i).toString();
                // Get the students preferences
                ArrayList<String> preferences = (ArrayList<String>) (student_preferences.get(name));
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
                        break;
                    }
                    j++;
                }
            } else {
                System.out.println("Name not in student list!");
            }
            i++;
        }
    }


    public static boolean check_sizes(Map resource_allocation){
        Collection<Double> scores = resource_allocation.values();
        int num_of_projects_used = 0;
        for (double score : scores){
           if (score == 0.0)
               num_of_projects_used++;
        }
        return (num_of_projects_used == scores.size());
    }

    public static Map<String, Double> getCurrentProjects(int j, Map<String, ArrayList<String>> student_preferences, Map<String, Double> current_projects, ArrayList<String> students
            , int num_of_iterations){
        while(j < num_of_iterations) {
            String name = students.get(j);
            // Get the students preferences
            ArrayList<String> preferences = (ArrayList<String>) (student_preferences.get(name));
            System.out.println(name + " " + preferences.get(0));
            current_projects.put(preferences.get(0), current_projects.get(preferences.get(0)) + 1.0);
            j++;
        }
        return current_projects;
    }

    public static void removeMatched(Map<String, ArrayList<String>> student_preferences, Map<String, Double> project_allocation, Map<String, Double> student_allocation){

        System.out.println("Attempting to remove matched students and projects .... ");
        ArrayList<String> student_list = new ArrayList<>();
        for (String name : student_preferences.keySet()) {
            student_list.add(name);

        }
        for (String name : student_list) {
            if (student_allocation.get(name) == 1.0) {
                student_preferences.remove(name);
            }

        }

        ArrayList<String> project_list = new ArrayList<>();
        for (String project : project_allocation.keySet()){
            project_list.add(project);
        }
        for (String name : student_list) {
            ArrayList<String> preferences = student_preferences.get(name);
            int i = 0;
            while (i < preferences.size()) {
                if (project_allocation.get(preferences.get(i)) == 1.0) {
                    // remove project from all preferences list
                    System.out.println("Removing " + preferences.get(i));
                    preferences.remove(preferences.get(i));

                    i--;
                }
            i++;
            }
        }
        System.out.println("Removed matched students and projects");
        }



    public static void probabilisticSerialDictatorship(Map student_preferences, ArrayList project_list){

        // Stores the amount left of each project to be consumed
        Map<String, Double> project_allocation = new HashMap<>();

        // Stores the amount of each student left to be consumed
        Map<String, Double> student_allocation = new HashMap<>();

        // store the final matrix of students probability of being matched to a project
        Map<String, Arrays> student_probabilities = new HashMap<>();

        int i = 0;
        while (i < project_list.size()){
            project_allocation.put((String) project_list.get(i), 0.0);
            i++;
        }
        System.out.println(project_allocation.toString());

        ArrayList<String> students = new ArrayList<>();
        for(Object name : student_preferences.keySet()){
            student_allocation.put((String) name, 0.0);
            students.add((String) name);
        }
        System.out.println(student_allocation.toString());
        //System.out.println(student_preferences.toString());
        // while projects are still to be allocated or students are fully matched i.e have a combined total of 1
        int j = 0;
        int num_of_iterations = Math.max(project_list.size(), student_preferences.size());

        Map<String, Double> current_projects = new HashMap<>();
        int k = 0;
        while (k < project_list.size()){
            current_projects.put((String) project_list.get(k), 0.0);
            k++;
        }

        Map<String, Double> projects_remaining = new HashMap<>();
        int l = 0;
        while (l < project_list.size()){
            projects_remaining.put((String) project_list.get(l), 0.0);
            l++;
        }

        while(check_sizes(project_allocation) || check_sizes(student_allocation)){

            while (j < num_of_iterations){

                // Get current projects being consumed by students
                Map<String, Double> currentProjects = getCurrentProjects(j, student_preferences, current_projects, students, num_of_iterations);
                System.out.println("Current Projects: " + currentProjects.toString());

                for (String name : students){
                    ArrayList<String> preferences = (ArrayList<String>) (student_preferences.get(name));
                    // increment student allocation + project allocation
                    String current_project = preferences.get(j);
                    Double num_of_students_consuming = current_projects.get(current_project);
                    // have a method that gets the amount of each project remaining before loop so it doesn't change during the iteration
                    Double amount_of_project_remaining = (1.0 - (projects_remaining.get(current_project)));

                    student_allocation.put(name, student_allocation.get(name) + (amount_of_project_remaining / num_of_students_consuming));
                    project_allocation.put(current_project, project_allocation.get(current_project) + (amount_of_project_remaining/num_of_students_consuming));

                    // Deal with fully matched students + projects
                    //if (student_allocation.get(name) == 1.0){
                       // student_preferences.remove(name);
                    //}
                    // Deal with moving onto next round
                }
                System.out.println("Student preferences before removal: " + student_preferences.toString());
                removeMatched(student_preferences, project_allocation, student_allocation);
                System.out.println("Student preferences: " + student_preferences.toString());

                // increment appropriate students and projects
                // remove matched students + projects from student preferences

                //j++;
                break;
                }
                break;
                // get each students first choice run clock, stop when one is exhausted
                // move onto next choice unless students choice was the one which was exhausted

                // Divide amount of each resource left by each agent consuming it
                // decrement resource by above number
        }
        System.out.println("Student Allocation: " + student_allocation.toString());
        System.out.println("Project Allocation: " + project_allocation.toString());
        //System.out.println(current_projects.toString());
    }

}
