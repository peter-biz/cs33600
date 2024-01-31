/*


*/

import java.util.Scanner;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.FileNotFoundException;

/**
   Three parameters:
      1. How many input number get group together (ie get pass 5, num would be 12345 67890)
      2. The number of output columns, default 3, so 3 columns per line, then new line
      3. precision of decimal point (ie pass 3, num = 1.123)

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
      //columns, precision, groups vars (deafult, overwritten if env var or cmd line or properties)
      int columns = 3;
      int precision = 13;
      int groups = 0;

      //get env vars and check if they exist, if they do, they overwrite default vars
      try {
         String columnsEnv = System.getenv("CS336_COLUMNS");
         String precisionEnv = System.getenv("CS336_PRECISION");
         String groupsEnv = System.getenv("CS336_GROUPS");

         if (columnsEnv != null) {
            columns = Integer.parseInt(columnsEnv);
         }
         if (precisionEnv != null) {
            precision = Integer.parseInt(precisionEnv);
         }
         if (groupsEnv != null) {
            groups = Integer.parseInt(groupsEnv);
         }
      } catch (NumberFormatException e) {
         System.err.println("Error: Environment variable not an integer");
      }

      //get properties file and check if it exists, if they do, they overwrite the env/curr vars
      try {
         Properties prop = new Properties();
         File propertiesFile = new File("filter.properties");
         prop.load(new FileInputStream(propertiesFile));

         // create a filter-save.properties file if it doesn't exist, copy filter.properties to it
         File saveFile = new File("filter-save.properties");
         Files.copy(propertiesFile.toPath(), saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

         if (prop.getProperty("precision") != null) {
            precision = Integer.parseInt(prop.getProperty("precision"));
         }
         if (prop.getProperty("columns") != null) {
            columns = Integer.parseInt(prop.getProperty("columns"));
         }
         if (prop.getProperty("groups") != null) {
            groups = Integer.parseInt(prop.getProperty("groups"));
         }
      }
      catch (FileNotFoundException e) {
         System.err.println("Error: Properties file not found");
      }
      catch (IOException e) {
         System.err.println("Error: Properties file not found");
      }
      catch (NumberFormatException e) {
         System.err.println("Error: Properties file does not contain an integer");
      }
      

      //get command line args and check if they exist, if they do, they overwrite the all/curr vars
      // after commands, the arguments are as follows: int columns, int precision, int groups
      //Does not have to be all 3 arguments, can be 1, 2, or 3 and use current vars for the rest
      if (args.length > 0) {
         try {
            if (args.length == 1) {
               columns = Integer.parseInt(args[0]);
            }
            if (args.length == 2) {
               columns = Integer.parseInt(args[0]);
               precision = Integer.parseInt(args[1]);
            }
            if (args.length == 3) {
               columns = Integer.parseInt(args[0]);
               precision = Integer.parseInt(args[1]);
               groups = Integer.parseInt(args[2]);
            }
         } catch (NumberFormatException e) {
            System.err.println("Error: Command line argument not an integer");
         }
      }

      //get input from stdin
      Scanner in = new Scanner(System.in);
      int count = 0;

      while(in.hasNextDouble()) {
         double input = in.nextDouble();
         //Columns determine amount of columns per row(2 spaces between columns), ie(col=3:  1  2  3\n), 
         //precision is decimal pt(ie precision=3: 1.123), 
         //groups is how many numbers per group(ie groups=4: 1 2 \n 1 2 \n\n)
         //ALL output is right justified

         //if groups is 0, then no groups, just print out in the colums with precision (groups is only 0 if cmd line arg is 0)
         if (groups == 0) {
            // If groups is 0, print each number in a column with specified precision
            System.out.printf("%" + columns + "." + precision + "f", input);
            count++;

            if(count % columns == 0)
            {   
               System.out.print("\n");
            }
            if (count % columns != 0)
            {
               System.out.print("  ");
            }
        } else {
            // If groups is greater than 0, print numbers in groups
            System.out.printf("%" + columns + "." + precision + "f", input);
            count++;

            //check if we have reached the desired amount of columns
            if(count % columns == 0)
            {   
               System.out.print("\n");
            }

            //check if we have reached the desired amount of groups
            if(count % groups == 0)
            {
               System.out.print("\n\n");
               count = 0;
            }
            if (count % groups != 0 && count % columns != 0)
            {
               System.out.print("  ");
            }
        }
         

      }

       
      
      in.close();
   }
}
