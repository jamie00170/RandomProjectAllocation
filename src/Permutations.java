/**
 * Created by Jamie on 08/11/2015.
 */
import java.lang.reflect.Array;
import java.util.*;
public class Permutations {

    /**
     * Write a description of class GeneratePermutations here.
     *
     * @author Kushtrim
     * @version 1.01
     */
        public static void main(String args[]) {
            Permutations g = new Permutations();
            String[] elements = {"Studenta","Studentb","Studentc"};
            ArrayList<String> permutations = g.generatePermutations(elements);

            //ArrayList<ArrayList<String>> permutations_list = new ArrayList<ArrayList<String>>();
            String[][] permutations_list = new String[6][3];
            System.out.println(permutations.toString());
            int i = 0;
            while(i < permutations.size()){
                String[] new_string = permutations.get(i).split("(?<=[a-z])(?=[A-Z])");
                //System.out.println(Arrays.toString(new_string));
                permutations_list[i] = new_string;
                i++;
            }
            System.out.println(Arrays.toString(permutations_list[0]));
            //System.out.println(permutations.get(999999));
            for (String[] perm : permutations_list){
                System.out.println(Arrays.toString(perm));
            }
        }

        private ArrayList<String> generatePermutations( String[] elements ) {
            ArrayList<String> permutations = new ArrayList<String>();
            if ( elements.length == 2 ) {

                String x1 = elements[0]  + elements[1];
                String x2 = elements[1]  + elements[0];
                permutations.add(x1);
                permutations.add(x2);

            }
            else {
                for (  int i = 0 ; i < elements.length  ; i++) {
                    String[] elements2 = new String[elements.length -1];
                    int kalo = 0;
                    for( int j =0 ; j< elements2.length ; j++ ) {
                        if( i == j)
                        {
                            kalo = 1;
                        }
                        elements2[j] = elements[j+kalo];
                    }
                    ArrayList<String> k2 = generatePermutations(elements2);
                    for( String x : k2 ) {
                        String s = elements[i]+x;
                        permutations.add(s);
                    }
                }
            }

            return permutations;
        }
    }

