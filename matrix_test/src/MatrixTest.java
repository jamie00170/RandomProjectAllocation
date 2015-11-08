import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Jamie on 07/11/2015.
 */
public class MatrixTest {

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

    public static void incrementValue(String[][] matrix, String student, String project){
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
        int value = Integer.parseInt(matrix[coordinates[0]][coordinates[1]]);
        value++;
        matrix[coordinates[0]][coordinates[1]] = Integer.toString(value);
    }


    public static void main(String[] args) {

        ArrayList<String> project_list = generateprojects(3);
        System.out.println("Project List:" + project_list.toString());

        Map<String, ArrayList<String>> student_preferences = generateStudents(3, project_list);

        System.out.println("Student Preferences: " + student_preferences.toString());


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
        j = 1;
        while( i < matrix.length){
            j = 1;
            while (j < matrix[i].length){
                matrix[i][j] = "0";
                j++;
            }
            i++;
        }

        for(String[] row: matrix)
            System.out.println(Arrays.toString(row));

        int value = Integer.parseInt(matrix[1][1]);
        value++;
        System.out.println(value);

        // increment student 3 and project 3's value
        incrementValue(matrix, "Student1", "Project1");

        incrementValue(matrix, "Student2", "Project2");

        incrementValue(matrix, "Student3", "Project3");
        for(String[] row: matrix)
            System.out.println(Arrays.toString(row));


    }
}
