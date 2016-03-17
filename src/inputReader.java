import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
;

/**
 * Created by Jamie on 11/01/2016.
 */
public class InputReader {

    private static UtilityMethods utilityMethods = new UtilityMethods();

    public void read_file(String filename, String alg) {


        try (BufferedReader br = new BufferedReader((new FileReader(filename)))) {

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

            i = 0;
            //String[] student_list = new String[number_students];
            ArrayList<String> student_list = new ArrayList<>();
            while (i < (number_students)) {
                student_list.add(br.readLine());
                i++;
            }

            i = 0;
            HashMap<String, ArrayList<String>> student_preferences = new HashMap<>();
            while (i < (number_students)) {
                String[] project_rankings = br.readLine().split("\\s+");
                int j = 0;
                while (j < project_rankings.length) {
                    project_rankings[j] = "Project-" + project_rankings[j];
                    j++;
                }

                student_preferences.put(student_list.get(i), new ArrayList<>(Arrays.asList(project_rankings)));
                i++;
            }

            i = 0;
            ArrayList<String> project_list = new ArrayList<>();
            while (i < (number_projects - 1)) {
                project_list.add("Project-" + br.readLine());
                i++;
            }

            System.out.println("Number of projects: " + number_projects);
            System.out.println("Number of students: " + number_students);

            System.out.println("Student list: " + student_list.toString());
            System.out.println("Project list: " + project_list.toString());

            System.out.println("Student Preferences: " + student_preferences.toString());


            if (alg.equals("PS")) {
                ProbalisticSerial ps = new ProbalisticSerial();
                ps.probabilisticSerialDictatorship(student_preferences, project_list);
            } else if (alg.equals("BS")) {
                BostonSerial bs = new BostonSerial();
                bs.bostonSerial(student_preferences, project_list);
            } else if (alg.equals("RSD")) {

                Scanner scanner = new Scanner(System.in);

                System.out.println("How many permutations of student list do you want to run?");
                int num_permutations = scanner.nextInt();

                RandomSerialDictatorship randomSerialDictatorship = new RandomSerialDictatorship();
                randomSerialDictatorship.randomSerialDictatorship(student_list, student_preferences, project_list, num_permutations);

            }


        } catch (FileNotFoundException e) {
            System.out.println("Specified file could not be found!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {

    }
}
