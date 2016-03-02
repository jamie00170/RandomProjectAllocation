import org.apache.commons.math3.fraction.Fraction;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Jamie on 11/01/2016.
 */
public class inputReader {


    public static void main(String[] args) {
        BufferedReader br = null;


        try {

            String filename;
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the name of the file to use as input: ");

            filename = sc.next();

            br = new BufferedReader(new FileReader(filename));

            String currentLine;

            int number_students = 0;
            int number_projects = 0;

            int i = 0;
            while (i < 2) {
                currentLine = br.readLine();
                if (i == 0)
                    number_students = Integer.parseInt(currentLine);
                if (i == 1)
                    number_projects = Integer.parseInt(currentLine);
                i++;
            }
            int number_supervisors = Integer.parseInt(br.readLine());

            i = 0;
            //String[] student_list = new String[number_students];
            ArrayList<String> student_list = new ArrayList<>();
            while (i < (number_students)){
                student_list.add(br.readLine());
                i++;
            }

            i = 0;
            HashMap<String, ArrayList<String>> student_preferences = new HashMap<>();
            while (i < (number_students)){
                String[] project_rankings = br.readLine().split("\\s+");
                int j = 0;
                while (j < project_rankings.length){
                    project_rankings[j] = "Project-" + project_rankings[j];
                    j++;
                }

                student_preferences.put(student_list.get(i), new ArrayList<>(Arrays.asList(project_rankings)));
                i++;
            }

            i = 0;
            ArrayList<String> project_list = new ArrayList<>();
            while (i < (number_projects-1)){
                project_list.add("Project-" + br.readLine());
                i++;
            }

            System.out.println("Number of projects: " + number_projects);
            System.out.println("Number of students: " + number_students);
            System.out.println("Number of supervisors: " + number_supervisors);

            System.out.println("Student list: " + student_list.toString());
            System.out.println("Project list: " + project_list.toString());

            System.out.println("Student Preferences: " + student_preferences.toString());

            // Set up
            // Create matrix
            String[][] matrix = new String[(student_preferences.size() + 1)][(project_list.size() + 1)];

            matrix[0][0] = "-";
            i = 1;
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
            while( i < matrix.length){
                j = 1;
                while (j < matrix[i].length){
                    matrix[i][j] = "0";
                    j++;
                }
                i++;
            }


            matrix = RandomSerialDictatorship.permute(student_list, 0, student_preferences, project_list, matrix);

            int p = 1;
            while( p < matrix.length){
                int f = 1;
                while (f < matrix[p].length){
                    try {
                        Double double_value = Double.parseDouble(matrix[p][f]);

                        matrix[p][f] = Double.toString(double_value / RandomSerialDictatorship.factorial(student_list.size()));
                    } catch (NumberFormatException e){
                        e.printStackTrace();
                    }
                    f++;
                }
                p++;
            }

            for (String[] row : matrix){
                System.out.println(Arrays.toString(row));
            }



            Main.probabilisticSerialDictatorship(student_preferences, project_list);
            //BostonSerial bs = new BostonSerial();
            //bs.bostonSerial(student_preferences, project_list);

        }catch (FileNotFoundException e){
            System.out.println("Specified file could not be found!");
        }
        catch (IOException e) {
            e.printStackTrace();

        }finally {

            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
