/**
 * Created by Jamie on 03/03/2016.
 */
import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.FractionConversionException;

public class stringToFractionTest {

    public static Fraction stringToFraction(String fraction_string){

        Fraction f;

        if (fraction_string.length() == 5){
            Double numerator = Double.parseDouble(fraction_string.substring(0,1));
            Double denominator = Double.parseDouble(fraction_string.substring(4));

            Double fraction_value = numerator/denominator;

            try {
                f = new Fraction(fraction_value);
                return f;
            } catch (FractionConversionException e ){
                e.printStackTrace();
            }

        }else if (fraction_string.length() == 1){

            try {
                Double fraction_value = Double.parseDouble(fraction_string);

                f = new Fraction(fraction_value);
                return f;
            } catch (FractionConversionException e) {
                e.printStackTrace();
            }
        }
        // Need to add clauses for different length numerators and denominators i.e. 19/20
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
