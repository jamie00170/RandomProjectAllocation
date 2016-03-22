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
            //     ask for algorithm to run
            System.out.println("Which algorithm do you want to run?");
            System.out.println("Enter RSD, PS, BS or RSDT");
            String alg = scanner.nextLine();

            if (alg.equals("RSDT")){

                InputReader inputReader = new InputReader();
                inputReader.read_file_with_ties(filename);

            }else {
                InputReader inputReader = new InputReader();
                inputReader.read_file(filename, alg);
            }
        } else {
            //     ask for num students
            System.out.println("Enter the number of students: ");
            int num_students = scanner.nextInt();
            //     num projects
            System.out.println("Enter the number of projects: ");
            int num_projects = scanner.nextInt();
            //     size of preference lists
            System.out.println("Enter the size of preference lists: ");
            int size_of_preference_lists = scanner.nextInt();
            //     alg
            System.out.println("Which algorithm do you want to run?");
            System.out.println("Enter RSD, PS, BS or RSDT");
            String alg = scanner.next();

            ArrayList<String> project_list = utilityMethods.generateprojects(num_projects);

            HashMap<String, ArrayList<String>> student_preferences = utilityMethods.generateStudents(num_students, project_list, size_of_preference_lists);

            ArrayList<String> student_list = new ArrayList<>();
            for (String name : student_preferences.keySet()) {
                student_list.add(name);
            }

            if (!alg.equals("RSDT")){
                System.out.println("Project List:" + project_list.toString());
                System.out.println("Student Preferences: " + student_preferences.toString());
                System.out.println("Student list:" + student_list);


            }

            //     run given alg with randomly generated instance with given parameters
            if (alg.equals("PS")) {

                ProbabilisticSerial ps = new ProbabilisticSerial();
                ps.probabilisticSerialDictatorship(student_preferences, project_list);

            } else if (alg.equals("BS")) {

                BostonSerial bs = new BostonSerial();
                bs.bostonSerial(student_preferences, project_list);

            } else if (alg.equals("RSD")) {

                System.out.println("How many permutations of student list do you want to run?");
                int num_permutations = scanner.nextInt();

                RandomSerialDictatorship randomSerialDictatorship = new RandomSerialDictatorship();
                randomSerialDictatorship.randomSerialDictatorship(student_list, student_preferences, project_list, 100);

            }else if (alg.equals("RSDT")){

                System.out.println("What probability of ties?");
                System.out.println("Enter a real number between 0 and 1");

                double probability_of_ties = scanner.nextDouble();

                GenerateRandomInstance generateRandomInstance = new GenerateRandomInstance();

                HashMap<String, ArrayList<String[]>> student_pref_ties = new HashMap<>();
                student_pref_ties = generateRandomInstance.generateStudents(3, project_list);

                student_pref_ties = generateRandomInstance.generateRandomInstanceWithTies(student_pref_ties, 0.7);

                System.out.println("Student Preferences: ");
                for (Map.Entry<String, ArrayList<String[]>> entry: student_pref_ties.entrySet()){
                    System.out.println(entry.getKey());
                    for (String[] indifference_class : entry.getValue()){
                        System.out.print(Arrays.toString(indifference_class) + " ");
                    }
                    System.out.println();
                }

                RandomSerialDictatorshipTies rsdt = new RandomSerialDictatorshipTies();

                rsdt.RandomSerialWithTies(student_pref_ties, project_list);

            }

        }


    }
}
