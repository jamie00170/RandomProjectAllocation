import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

        System.out.println(student_preferences.size());

    }
}
