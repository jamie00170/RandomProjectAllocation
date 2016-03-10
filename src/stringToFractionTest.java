/**
 * Created by Jamie on 03/03/2016.
 */
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionConversionException;

import java.io.FileReader;
import java.math.BigDecimal;
import java.util.Arrays;

public class stringToFractionTest {

   private static UtilityMethods utilityMethods = new UtilityMethods();

    public static void main(String[] args){

        //Fraction fraction = new Fraction(32/384);
        String frac_string = "32 / 384";

        Fraction fraction = utilityMethods.stringToFraction(frac_string);

        System.out.println(fraction);

        frac_string = "1 / 10";

        Fraction fraction1 = utilityMethods.stringToFraction(frac_string);

        System.out.println(fraction1);

        frac_string = "1 / 2";

        Fraction fraction2 = utilityMethods.stringToFraction(frac_string);

        System.out.println(fraction2);

        frac_string = "65555/ 655556";

        Fraction fraction3 = utilityMethods.stringToFraction(frac_string);


        BigDecimal bd = new BigDecimal(0.982789447);

        Double d = bd.doubleValue();

        System.out.println("Double value:" + d);

        double epsilon;
        epsilon = 5 * (Math.pow(10.0, -9.0));

        Fraction fraction4 = new Fraction(d, epsilon, 10 );

        System.out.println(fraction4);


    }

}
