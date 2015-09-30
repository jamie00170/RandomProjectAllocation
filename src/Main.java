import java.util.*;

public class Main {

    public static void main(String[] args) {



        Map<String, ArrayList> student_preferences = new HashMap<>();

        student_preferences.put("Jamie", new ArrayList<>(Arrays.asList("project1", "project2")));
        student_preferences.put("Jack", new ArrayList<>(Arrays.asList("project2", "project1")));
        student_preferences.put("Ben", new ArrayList<>(Arrays.asList("project1", "project3")));

        ArrayList<String> project_list = new ArrayList<>();

        project_list.add("project1");
        project_list.add("project2");
        project_list.add("project3");

        //System.out.println(project_list.toString());
        //System.out.println(student_preferences.toString());

        ArrayList<String> priority_list = new ArrayList<>();

        priority_list.add("Jamie");
        priority_list.add("Jack");
        priority_list.add("Ben");

        //Collections.shuffle(priority_list);

        randomSerialDictatorship(student_preferences, project_list, priority_list);
        probabilisticSerialDictatorship(student_preferences, project_list, priority_list);

    }

    public static void randomSerialDictatorship(Map student_preferences, ArrayList project_list, ArrayList priority_list){

        ArrayList<String> projects_allocated = new ArrayList<>();
        int i = 0;
        int j =0;
        while (i < student_preferences.size()){
            while (j < priority_list.size()){
                if (student_preferences.containsKey(priority_list.get(j))){
                    String name = priority_list.get(j).toString();
                    @SuppressWarnings("unchecked")
                    ArrayList<String> preferences = (ArrayList<String>) student_preferences.get(name);
                    for(String choice : preferences){
                        if(projects_allocated.contains(choice)){
                            if (preferences.get(preferences.size() - 1).equals(choice)){
                                System.out.println(name + " not matched because none of their choices are available!");
                            }
                        }else{
                            System.out.println(name + " " + choice);
                            projects_allocated.add(choice);
                            break;
                        }
                    }
                }else{
                    System.out.println("Name not in student list!");
                }
                j++;
            }
            i++;
        }
    }


    public static boolean check_sizes(Map resource_Allocation){
        Collection<Integer> sizes = resource_Allocation.values();
        int num_of_zeros = 0;
        for (int size : sizes){
           if (size == 0)
               num_of_zeros++;
        }
        return (num_of_zeros == sizes.size());
    }

    public static void probabilisticSerialDictatorship(Map student_preferences, ArrayList project_list, ArrayList priority_list){

        Map<String, Integer> resource_allocation = new HashMap<>();
        int i = 0;
        while (i < project_list.size()){
            resource_allocation.put((String) project_list.get(i), 1 );
            i++;
        }
        System.out.println(resource_allocation.toString());
        // while resources i.e projects are still to be allocated
        while(check_sizes(resource_allocation)){
            
        }

    }
}
