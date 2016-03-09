import java.util.*;

/**
 * Created by Jamie on 07/03/2016.
 */
public class GenerateRandomInstance {

    public static HashMap<String, ArrayList<String[]>> generateStudents(int num_of_students, ArrayList<String> project_list) {


        HashMap<String, ArrayList<String[]>> student_preferences = new HashMap<>();
        int j = 1;

        while (j <= num_of_students) {
            //int rand_index = randInt(0, (project_list.size()-1));
            Collections.shuffle(project_list);
            ArrayList<String[]> preferences = new ArrayList<>();
            for (String project : project_list){
                String[] project_Arr = {project};

                preferences.add(project_Arr);
            }

            student_preferences.put("Student" + j, preferences );
            j++;
        }
        return student_preferences;
    }

    public static String[] concat(String[] first, String[] second) {
        String[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }


    public static HashMap<String, ArrayList<String[]>> generateRandomInstance(HashMap<String, ArrayList<String[]>> student_preferences, double t){

        for (String student: student_preferences.keySet()){

            ArrayList<String[]> preference_list = student_preferences.get(student);
            int i = 0;
            while(i < preference_list.size()-1) {
                double rand = Math.random();
                String[] current_indifference_class = preference_list.get(i);
                if (rand < t){
                    current_indifference_class = concat(current_indifference_class, preference_list.get(i+1));
                    System.out.println("new indifference class: " + Arrays.toString(current_indifference_class));
                    preference_list.remove(i);
                    preference_list.remove(i+1);
                    preference_list.add(i, current_indifference_class);
                }
                i++;
            }

        }


        return student_preferences;


    }


    public static void main(String[] args) {

        ArrayList<String> project_list = new ArrayList<>();
        project_list.add("Project1");
        project_list.add("Project2");
        project_list.add("Project3");
        project_list.add("Project4");
        project_list.add("Project5");

        HashMap<String, ArrayList<String[]>> student_preferences = generateStudents(3, project_list);

        /**
        ArrayList<String[]> ar1 = new ArrayList<>();
        String[] pref1 = {"Project1", "Project2"};
        ar1.add(pref1);

        ArrayList<String[]> ar2 = new ArrayList<>();
        String[] pref2 = {"Project1", "Project2"};
        ar2.add(pref2);


        student_preferences.put("Student1", ar1);
        student_preferences.put("Student2", ar2);
         **/

        for (Map.Entry<String, ArrayList<String[]>> entry: student_preferences.entrySet()){
            System.out.println(entry.getKey());
            for (String[] indifference_class : entry.getValue()){
                System.out.print(Arrays.toString(indifference_class) + " ");
            }
            System.out.println();
        }


        System.out.println("Generating random instance.....");
        generateRandomInstance(student_preferences, 1);


        for (Map.Entry<String, ArrayList<String[]>> entry: student_preferences.entrySet()){
            System.out.println(entry.getKey());
            for (String[] indifference_class : entry.getValue()){
                System.out.print(Arrays.toString(indifference_class) + " ");
            }
            System.out.println();
        }
    }





}
