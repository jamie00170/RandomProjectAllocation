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

    public static void writeCsvFile(String fileName) {

        FileWriter fileWriter = null;



        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADER);

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            //Write a new student object list to the CSV file

            fileWriter.append("56.7");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append("53.5");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append("58.0");
            fileWriter.append(NEW_LINE_SEPARATOR);


            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args){

        writeCsvFile("csv_file.csv");

    }

}
