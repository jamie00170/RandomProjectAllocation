import java.util.*;

public class Main {

    private static UtilityMethods utilityMethods = new UtilityMethods();


    public static void main(String[] args) {

        // if the user wants to load from file
        System.out.println("Load instance from a file or Generate Random Instance?");
        System.out.println("Enter GR or LF");

        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();

        if (command.equals("LF")) {
            //     ask for filename
            System.out.println("Enter the name of the file to load the instance from: ");
            String filename = scanner.nextLine();
            //     ask for algortihm to run
            System.out.println("Which algorithm do you want to run?");
            System.out.println("Enter RSD, PS or BS");
            String alg = scanner.nextLine();

            InputReader inputReader = new InputReader();
            inputReader.read_file(filename, alg);
        } else {
            //     ask for num students
            System.out.println("Enter the number of students: ");
            int num_students = Integer.parseInt(scanner.nextLine());
            //     num projects
            System.out.println("Enter the number of projects: ");
            int num_projects = Integer.parseInt(scanner.nextLine());
            //     size of preference lists
            System.out.println("Enter the size of preference lists: ");
            int size_of_preference_lists = Integer.parseInt(scanner.nextLine());
            //     alg
            System.out.println("Which algorithm do you want to run?");
            System.out.println("Enter RSD, PS or BS");
            String alg = scanner.nextLine();

            ArrayList<String> project_list = utilityMethods.generateprojects(num_projects);

            System.out.println("Project List:" + project_list.toString());

            HashMap<String, ArrayList<String>> student_preferences = utilityMethods.generateStudents(num_students, project_list, size_of_preference_lists);

            System.out.println("Student Preferences: " + student_preferences.toString());

            ArrayList<String> student_list = new ArrayList<>();
            for (String name : student_preferences.keySet()) {
                student_list.add(name);
            }
            System.out.println("Student list:" + student_list);

            //     run given alg with randomly generated instance with given parameters
            if (alg.equals("PS")) {
                ProbalisticSerial ps = new ProbalisticSerial();
                ps.probabilisticSerialDictatorship(student_preferences, project_list);
            } else if (alg.equals("BS")) {
                BostonSerial bs = new BostonSerial();
                bs.bostonSerial(student_preferences, project_list);
            } else if (alg.equals("RSD")) {

                String[][] matrix = utilityMethods.setUpMatrix(student_preferences.keySet(), project_list);

                RandomSerialDictatorship randomSerialDictatorship = new RandomSerialDictatorship();
                matrix = randomSerialDictatorship.permute(student_list, 0, student_preferences, project_list, matrix);

                matrix = utilityMethods.divideMatrixByFactorial(matrix, utilityMethods.factorial(student_list.size()));

                for (String[] row : matrix) {
                    System.out.println(Arrays.toString(row));
                }

            }

        }


    }
}
