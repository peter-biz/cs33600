/*


*/

import java.util.Scanner;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
   Three parameters:
      1. How many input number get group together (ie get pass 5, num would be 12345 67890)
      2. The number of output columns, default 3, so 3 columns per line, then new line
      3. Percision of decimal point (ie pass 3, num = 1.123)

   These parameters have defaults(order of precedence):
      1. Command line
      2. In the environment variles, CS336_COLUMNS, CS336_PRECISION, CS336_GROUPS
      3. in the filter.properties file
      4. deafult values

   should use the getenv() method to see if there are environment variables named CS336_COLUMNS or CS336_PRECISION or CS336_GROUPS.
   If any of these environment variables exists, then its string value should be converted to an integer value by using the parseInt() method.
   
   Input numbers should be "right justified" in each output column 


*/
public class Filter
{
   public static void main(String[] args)
   {
      //columns, percision, groups vars (deafult, overwritten if env var or cmd line or properties)
      int columns = 3;
      int percision = 13;
      int groups = 0;

      //get env vars and check if they exist, if they do, they overwrite default vars
      try {
         String columnsEnv = System.getenv("CS336_COLUMNS");
         String percisionEnv = System.getenv("CS336_PRECISION");
         String groupsEnv = System.getenv("CS336_GROUPS");

         if (columnsEnv != null) {
            columns = Integer.parseInt(columnsEnv);
         }
         if (percisionEnv != null) {
            percision = Integer.parseInt(percisionEnv);
         }
         if (groupsEnv != null) {
            groups = Integer.parseInt(groupsEnv);
         }
      } catch (NumberFormatException e) {
         System.err.println("Error: Environment variable not an integer");
      }

      //get properties file and check if it exists, if they do, they overwrite the env/curr vars
      try{
         Properties prop = new Properties();
         prop.load(new FileInputStream("filter.properties"));
         
         if(prop.getProperty("percision") != null) {
            percision = Integer.parseInt(prop.getProperty("percision"));
         }
         if(prop.getProperty("columns") != null) {
            columns = Integer.parseInt(prop.getProperty("columns"));
         }
         if(prop.getProperty("groups") != null) {
            groups = Integer.parseInt(prop.getProperty("groups"));
         }
      }
      catch (IOException e) {
         System.err.println("Error: Properties file not found");
      }

      //get command line args and check if they exist, if they do, they overwrite the all/curr vars
      // after commands, the arguments are as follows: int columns, int percision, int groups
      if (args.length > 0) { //TODO: ill set this up later :)
         System.out.println("args");
      }

      //get input from stdin
      Scanner in = new Scanner(System.in);

      while(in.hasNextDouble()) {
         double input = in.nextDouble();

         
      }
         
   }
}
