import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Jamie on 11/03/2016.
 */
public class CsvWriter {

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    //CSV file header
    private static final String FILE_HEADER = "rsd, ps, bs";

    private static FileWriter fileWriter;


    public CsvWriter(String fileName){

        try {

            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADER);

        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void csvWriteLineValue(double rsd_value, double ps_value, double bs_value){

        try {
            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            //Write a new student object list to the CSV file

            fileWriter.append(Double.toString(rsd_value));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(Double.toString(ps_value));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(Double.toString(bs_value));
            fileWriter.append(NEW_LINE_SEPARATOR);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void csvWriteLineDepth(int rsd_depth, int ps_depth, int bs_depth){

        try {
            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            //Write a new student object list to the CSV file

            fileWriter.append(Integer.toString(rsd_depth));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(Integer.toString(ps_depth));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(Integer.toString(bs_depth));
            fileWriter.append(NEW_LINE_SEPARATOR);
        }catch (IOException e){
            e.printStackTrace();
        }


    }


    public void closeCsv(){

        try {
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error while flushing/closing fileWriter !!!");
            e.printStackTrace();
        }

    }



    public static void main(String[] args){

        CsvWriter csvWriter = new CsvWriter("test_csv.csv");

        csvWriter.csvWriteLineValue(58.5, 57.0, 54.4);

        csvWriter.closeCsv();

    }

}
