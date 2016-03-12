import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionConversionException;

import java.util.*;

// Specification + problem description +
public class Main {

    private static UtilityMethods utilityMethods = new UtilityMethods();



    public static void main(String[] args) {

        ArrayList<String> project_list = utilityMethods.generateprojects(4);

        System.out.println("Project List:" + project_list.toString());

        int size_of_preference_list = 4;

        HashMap<String, ArrayList<String>> student_preferences = utilityMethods.generateStudents(4 , project_list, size_of_preference_list);

        System.out.println("Student Preferences: " + student_preferences.toString());

        ProbalisticSerial ps = new ProbalisticSerial();
        ps.probabilisticSerialDictatorship(student_preferences, project_list);

        //BostonSerial bs = new BostonSerial();
        //bs.bostonSerial(student_preferences, project_list);




    }


    }
