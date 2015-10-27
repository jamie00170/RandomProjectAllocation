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
           if (score == 1.0)
               num_of_projects_used++;
        }
        return (num_of_projects_used == scores.size());
    }


    public static Map<String, Double> getCurrentProjects(Map<String, ArrayList<String>> student_preferences, Map<String, Double> current_projects){

        // reset current projects
        for(String project : current_projects.keySet()){
            current_projects.put(project, current_projects.get(project) - current_projects.get(project));
        }

        for (String student : student_preferences.keySet()){
            // Get the students preferences
            ArrayList<String> preferences = (ArrayList<String>) (student_preferences.get(student));
            System.out.println(student + " " + preferences.get(0));
            current_projects.put(preferences.get(0), current_projects.get(preferences.get(0)) + 1.0);
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

        ArrayList<String> items_to_remove = new ArrayList<>();

        for (String name : student_preferences.keySet()) {
            // Get students list of ranked choices
            ArrayList<String> preferences = student_preferences.get(name);
            // Loop through their choices
            for (String project : preferences) {
                // If the choice has been fully matched remove it from the preferences list
                if (project_allocation.get(project) == 1.0) {
                    // remove project from preferences list
                    //System.out.println("Removing " + project);
                    items_to_remove.add(project);
                }
            }
            preferences.removeAll(items_to_remove);
        }
        System.out.println("Removed matched students and projects");
        }


    public static void probabilisticSerialDictatorship(Map<String, ArrayList<String>> student_preferences, ArrayList project_list){

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

        Map<String, Double> projects_remaining = new HashMap<>();
        int l = 0;
        while (l < project_list.size()){
            projects_remaining.put((String) project_list.get(l), 0.0);
            l++;
        }

        // while projects are still to be allocated or students are fully matched i.e have a combined total of 1
        while(!(check_sizes(project_allocation) || check_sizes(student_allocation))){
            // loop for number of students or projects - whatever there is more of
            // Get current projects being consumed by students
            Map<String, Double> currentProjects = getCurrentProjects(student_preferences, current_projects);
            System.out.println("Current Projects: " + currentProjects.toString());

            // for each student that hasn't yet been matched
            for (String name : student_preferences.keySet()){
                // get the students list of preferences
                ArrayList<String> preferences = (ArrayList<String>) (student_preferences.get(name));
                // get their first available choice - matched choices have been removed to always index 0
                String current_project = preferences.get(0);
                // Find out the number of student consuming this students current project
                Double num_of_students_consuming = current_projects.get(current_project);
                // Find the amount of the project yet to be assigned
                Double amount_of_project_remaining = (1.0 - (projects_remaining.get(current_project)));

                //System.out.println(name +" has: " + (amount_of_project_remaining/num_of_students_consuming) + "percenatge of being matched to" + current_project);
                // increment student allocation + project allocation
                student_allocation.put(name, student_allocation.get(name) + (amount_of_project_remaining / num_of_students_consuming));
                project_allocation.put(current_project, project_allocation.get(current_project) + (amount_of_project_remaining/num_of_students_consuming));

            }
                System.out.println("Student preferences before removal: " + student_preferences.toString());
                // Removed matched Students and Projects
                removeMatched(student_preferences, project_allocation, student_allocation);
                System.out.println("Student preferences: " + student_preferences.toString());


        }

        System.out.println("Student Allocation: " + student_allocation.toString());
        System.out.println("Project Allocation: " + project_allocation.toString());
        }

        //System.out.println(current_projects.toString());
    }


