/**
 * Created by Jamie on 08/11/2015.
 */
import java.lang.reflect.Array;
import java.util.*;
public class Permutations {

    public static int factorial(int n) {
        int fact = 1; // this  will be the result
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }

        public static void main(String args[]) {
            Permutations g = new Permutations();
            String[] student_list = {"Student1","Student2","Student3"};
            ArrayList<String> permutations = g.generatePermutations(student_list);

            //ArrayList<ArrayList<String>> permutations_list = new ArrayList<ArrayList<String>>();
            String[][] permutations_list = new String[factorial(student_list.length)][student_list.length];
            System.out.println(permutations.toString());
            int i = 0;
            while(i < permutations.size()){
                String[] new_string = permutations.get(i).split("(?<=[0-9])(?=[A-Z])");
                //System.out.println(Arrays.toString(new_string));
                permutations_list[i] = new_string;
                i++;
            }
            //System.out.println(permutations.get(999999));
            for (String[] perm : permutations_list){
                System.out.println(Arrays.toString(perm));
            }
        }

        public ArrayList<String> generatePermutations( String[] student_list ) {
            ArrayList<String> permutations = new ArrayList<String>();
            if ( student_list.length == 2 ) {

                String x1 = student_list[0]  + student_list[1];
                String x2 = student_list[1]  + student_list[0];
                permutations.add(x1);
                permutations.add(x2);

            }
            else {
                for (  int i = 0 ; i < student_list.length  ; i++) {
                    String[] student_list2 = new String[student_list.length -1];
                    int k = 0;
                    for( int j =0 ; j< student_list2.length ; j++ ) {
                        if( i == j) {
                            k = 1;
                        }
                        student_list2[j] = student_list[j+k];
                    }
                    ArrayList<String> k2 = generatePermutations(student_list2);
                    for( String x : k2 ) {
                        String s = student_list[i]+x;
                        permutations.add(s);
                        // format s into a list -
                        // then run rsd for that list
                    }
                }
            }

            return permutations;
        }
    }

