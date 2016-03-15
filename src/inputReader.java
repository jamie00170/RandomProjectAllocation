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

    private static UtilityMethods utilityMethods = new UtilityMethods();

    public static void main(String[] args) {
        BufferedReader br = null;


        try {

            String filename;
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the name of the file to use as input: ");

            filename = sc.nextLine();

            System.out.println("Enter the name of the algortihm to run : RSD, PS or BS ");

            String alg;
            alg = sc.nextLine();


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


            if (alg.equals("PS")) {
                ProbalisticSerial ps = new ProbalisticSerial();
                ps.probabilisticSerialDictatorship(student_preferences, project_list);
            }else if (alg.equals("BS")){
                BostonSerial bs = new BostonSerial();
                bs.bostonSerial(student_preferences, project_list);
            }else if (alg.equals("RSD")){

                String[][] matrix = utilityMethods.setUpMatrix(student_preferences.keySet(), project_list);

                RandomSerialDictatorship randomSerialDictatorship = new RandomSerialDictatorship();
                matrix = randomSerialDictatorship.permute(student_list, 0, student_preferences, project_list, matrix);

                matrix = utilityMethods.divideMatrixByFactorial(matrix, utilityMethods.factorial(student_list.size()));

                for (String[] row : matrix){
                    System.out.println(Arrays.toString(row));
                }
            }


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
