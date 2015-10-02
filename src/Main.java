import java.util.*;

public class Main {

    static int num_of_projects_used = 0;

    public static void main(String[] args) {

        Map<String, ArrayList> student_preferences = new HashMap<>();

        student_preferences.put("Student1", new ArrayList<>(Arrays.asList("project1", "project2", "project3", "project4")));
        student_preferences.put("Student2", new ArrayList<>(Arrays.asList("project2", "project4", "project3", "project1")));
        student_preferences.put("Student3", new ArrayList<>(Arrays.asList("project1", "project3", "project2", "project4")));
        student_preferences.put("Student4", new ArrayList<>(Arrays.asList("project3", "project4", "project2", "project1")));

        ArrayList<String> project_list = new ArrayList<>();

        project_list.add("project1");
        project_list.add("project2");
        project_list.add("project3");
        project_list.add("project4");

        //System.out.println(project_list.toString());
        //System.out.println(student_preferences.toString());

        ArrayList<String> priority_list = new ArrayList<>();

        priority_list.add("Student1");
        priority_list.add("Student2");
        priority_list.add("Student3");
        priority_list.add("Student4");

        Collections.shuffle(priority_list);

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
                @SuppressWarnings("unchecked")
                // Get the students preferences
                        ArrayList<String> preferences = (ArrayList<String>) student_preferences.get(name);
                // For each choice in the list of preferences
                for (String choice : preferences) {
                    // If the project has already been taken
                    if (projects_allocated.contains(choice)) {
                        // If the choice is the students last
                        if (preferences.get(preferences.size() - 1).equals(choice)) {
                            // Unable to match student
                            System.out.println(name + " not matched because none of their choices are available!");
                        }
                    } else {
                        // Match the student to their choice
                        System.out.println(name + " " + choice);
                        // Add the project to the projects_allocated list
                        projects_allocated.add(choice);
                        break;
                    }
                }
            } else {
                System.out.println("Name not in student list!");
            }
            i++;
        }
    }



    public static boolean check_sizes(Map resource_allocation){
        Collection<Integer> sizes = resource_allocation.values();
        int num_of_zeros = 0;
        for (int size : sizes){
           if (size == 0)
               num_of_zeros++;
        }
        return (num_of_zeros == sizes.size());
    }


    public static void probabilisticSerialDictatorship(Map student_preferences, ArrayList project_list, ArrayList priority_list){

        Map<String, Integer> resource_allocation = new HashMap<>();
        Map<String, Arrays> student_probabilities = new HashMap<>();

        int i = 0;
        while (i < project_list.size()){
            resource_allocation.put((String) project_list.get(i), 1);
            i++;
        }
        System.out.println(resource_allocation.toString());
        // while resources i.e projects are still to be allocated
        while(check_sizes(resource_allocation)){
            // loop until one project has been used
                // get each students first choice run clock, stop when one is exhausted
                // move onto next choice unless students choice was the one which was exhausted

                // Divide amount of each resource left by each agent consuming it
                // decrement resource by above number
        }

    }
}
