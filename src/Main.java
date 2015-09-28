import java.lang.reflect.Array;
import java.util.*;

public class Main {

    public static void main(String[] args) {



        Map<String, ArrayList> student_preferences = new HashMap<>();

        student_preferences.put("Jamie", new ArrayList<>(Arrays.asList("project1", "project2")));
        student_preferences.put("Jack", new ArrayList<>(Arrays.asList("project2", "project1")));
        student_preferences.put("Ben", new ArrayList<>(Arrays.asList("project3", "project2")));

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

        randomSerialDictatorship(student_preferences, project_list, priority_list);
    }

    public static void randomSerialDictatorship(Map student_preferences, ArrayList project_list, ArrayList priority_list){

        ArrayList<String> projects_allocated = new ArrayList<>();
        int i = 0;
        int j =0;
        while (i < student_preferences.size()){
            while (j < priority_list.size()){
                if (student_preferences.containsKey(priority_list.get(j))){
                    // try and match priority_list.get(j) to their first available choice
                    String name = priority_list.get(j).toString();
                    Object preferences = student_preferences.get(name);
                    ArrayList<String> array_preferences = (ArrayList<String>) preferences;

                    for(String choice : array_preferences){
                        if(projects_allocated.contains(choice)){
                            if (array_preferences.get(array_preferences.size() - 1).equals(choice)){
                                System.out.println(name + " not matched because none their choices are available");
                            }
                        }else{
                            System.out.println(name + " " + choice);
                            projects_allocated.add(choice);
                            break;
                        }
                    }

                }else{
                    System.out.println("Name not in student list");
                }
                j++;
            }
            i++;
        }

    }
}
