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


    public static HashMap<String, ArrayList<String[]>> generateRandomInstanceWithTies(HashMap<String, ArrayList<String[]>> student_preferences, double t){

        for (String student: student_preferences.keySet()){

            ArrayList<String[]> preference_list = student_preferences.get(student);
            int i = 0;
            int index_of_current_indifference_class = 0;

            int initial_size_of_preference_list = preference_list.size() -1;

            while(i < initial_size_of_preference_list) {
                double rand = Math.random();
                String[] current_indifference_class = preference_list.get(index_of_current_indifference_class);
                if (rand <= t){
                    System.out.println("Current student: " + student);
                    System.out.println("current indifference class: " + Arrays.toString(current_indifference_class));

                    String[] next_indifference_class = preference_list.get(index_of_current_indifference_class + 1);
                    String[] new_indifference_class = concat(current_indifference_class, next_indifference_class);

                    System.out.println("new indifference class: " + Arrays.toString(new_indifference_class));

                    preference_list.remove(index_of_current_indifference_class + 1);
                    preference_list.remove(index_of_current_indifference_class);

                    preference_list.add(index_of_current_indifference_class, new_indifference_class);

                }else{
                    index_of_current_indifference_class++;
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


        for (Map.Entry<String, ArrayList<String[]>> entry: student_preferences.entrySet()){
            System.out.println(entry.getKey());
            for (String[] indifference_class : entry.getValue()){
                System.out.print(Arrays.toString(indifference_class) + " ");
            }
            System.out.println();
        }


        System.out.println("Generating random instance.....");
        generateRandomInstanceWithTies(student_preferences, 0.5);


        for (Map.Entry<String, ArrayList<String[]>> entry: student_preferences.entrySet()){
            System.out.println(entry.getKey());
            for (String[] indifference_class : entry.getValue()){
                System.out.print(Arrays.toString(indifference_class) + " ");
            }
            System.out.println();
        }
    }





}
