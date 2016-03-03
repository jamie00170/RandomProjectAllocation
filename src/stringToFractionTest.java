/**
 * Created by Jamie on 03/03/2016.
 */
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionConversionException;

import java.util.Arrays;

public class stringToFractionTest {

    public static Fraction stringToFraction(String fraction_string){

        Fraction f;

        fraction_string = fraction_string.replaceAll("\\s","");
        String[] data = fraction_string.split("/");
        System.out.println("string split:" + Arrays.toString(data));

        Double numerator = Double.parseDouble(data[0]);
        Double denominator = Double.parseDouble(data[1]);

        Double fraction_value = numerator/denominator;

        try {
            f = new Fraction(fraction_value);
            return f;
        } catch (FractionConversionException e ){
            e.printStackTrace();
        }

        System.out.println("FRACTION NOT TRANSFORMED TO STRING");
        return new Fraction(0);
    }

    public static void main(String[] args){

        //Fraction fraction = new Fraction(32/384);
        String frac_string = "32 / 384";

        Fraction fraction = stringToFraction(frac_string);

        System.out.println(fraction);

        frac_string = "1 / 10";

        Fraction fraction1 = stringToFraction(frac_string);

        System.out.println(fraction1);

        frac_string = "1 / 2";

        Fraction fraction2 = stringToFraction(frac_string);

        System.out.println(fraction2);


    }

}
