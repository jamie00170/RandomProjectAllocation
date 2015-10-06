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
            student_preferences.put("Student" + j , new ArrayList<String>(project_list));
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

        randomSerialDictatorship(student_preferences, project_list, priority_list);
        //probabilisticSerialDictatorship(student_preferences, project_list, priority_list);

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
        Collection<Integer> scores = resource_allocation.values();
        int num_of_projects_used = 0;
        for (int score : scores){
           if (score == 0)
               num_of_projects_used++;
        }
        return (num_of_projects_used == scores.size());
    }


    public static void probabilisticSerialDictatorship(Map student_preferences, ArrayList project_list, ArrayList priority_list){

        Map<String, Integer> resource_allocation = new HashMap<>();
        Map<String, Arrays> student_probabilities = new HashMap<>();

        int i = 0;
        while (i < project_list.size()){
            resource_allocation.put((String) project_list.get(i), 0);
            i++;
        }
        System.out.println(resource_allocation.toString());
        // while resources i.e projects are still to be allocated
        while(check_sizes(resource_allocation)){ // or students are fully matched i.e have a combinted total of 1
            // loop until one project has been used
                // get each students first choice run clock, stop when one is exhausted
                // move onto next choice unless students choice was the one which was exhausted

                // Divide amount of each resource left by each agent consuming it
                // decrement resource by above number
        }
    }
}
