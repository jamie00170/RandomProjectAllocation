import java.io.*;
import java.util.*;
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
            while (i < (number_projects)) {
                project_list.add("Project-" + br.readLine());
                i++;
            }

            System.out.println("Number of projects: " + number_projects);
            System.out.println("Number of students: " + number_students);

            System.out.println("Student list: " + student_list.toString());
            System.out.println("Project list: " + project_list.toString());

            System.out.println("Student Preferences: " + student_preferences.toString());


            if (alg.equals("PS")) {
                ProbabilisticSerial ps = new ProbabilisticSerial();
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

    public void read_file_with_ties(String filename) {

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
            ArrayList<String> student_list = new ArrayList<>();
            while (i < (number_students)) {
                student_list.add(br.readLine());
                i++;
            }

            i = 0;
            HashMap<String, ArrayList<String[]>> student_preferences = new HashMap<>();
            while (i < (number_students)) {
                ArrayList<String[]> preference_list = new ArrayList<>();
                String[] project_rankings = br.readLine().split(" | ");
                //System.out.println("project rankings: " + Arrays.toString(project_rankings));
                int j = 0;
                int num_ties_for_current_student = 0;
                while (j < project_rankings.length) {
                    // Each student is in its own indifference class until a |
                    // is reached then all the students are in a tie until the next | is reached
                    //String[] current_indifference_class;
                    if (project_rankings[j].equals("|")){
                        ArrayList<String> projects_in_current_indifference_class = new ArrayList<>();

                        j++; // move onto first project in the indifference class
                        while(j < project_rankings.length -1 && !project_rankings[j].equals("|")){
                            // add project rankings j to the current indifference class
                            projects_in_current_indifference_class.add("Project-" + project_rankings[j]);
                            j++;
                        }
                        //System.out.println("Projects in current indifference class" + projects_in_current_indifference_class);
                        String[] current_indifference_class = new String[projects_in_current_indifference_class.size()];
                        int k = 0;
                        while (k < current_indifference_class.length){
                            current_indifference_class[k] = projects_in_current_indifference_class.get(k);
                            k++;
                        }
                        num_ties_for_current_student = num_ties_for_current_student + current_indifference_class.length;
                        if (!project_rankings[j-1].equals("|")) {
                            preference_list.add(current_indifference_class);
                        }
                        //preference_list.add(current_indifference_class);
                    }else{
                        String[] current_indifference_class = {"Project-" + project_rankings[j]};
                        preference_list.add(current_indifference_class);
                        j++;
                    }
                }


                student_preferences.put(student_list.get(i), new ArrayList<>(preference_list));
                i++;
            }

            i = 0;
            ArrayList<String> project_list = new ArrayList<>();
            while (i < (number_projects)) {
                project_list.add("Project-" + br.readLine());
                i++;
            }

            System.out.println("Number of projects: " + number_projects);
            System.out.println("Number of students: " + number_students);

            System.out.println("Student list: " + student_list.toString());
            System.out.println("Project list: " + project_list.toString());

            System.out.println("Student Preferences: ");
            for (Map.Entry<String, ArrayList<String[]>> entry : student_preferences.entrySet()){
                System.out.println(entry.getKey());
                for (String[] indifference_class : entry.getValue()){
                    System.out.println(Arrays.toString(indifference_class));
                }
            }

            RandomSerialDictatorshipTies rsdt = new RandomSerialDictatorshipTies();
            rsdt.RandomSerialWithTies(student_preferences, project_list);

        } catch (FileNotFoundException e) {
            System.out.println("Specified file could not be found!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
