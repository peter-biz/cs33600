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
      1. User given values
      2. In the environment variles, CS336_COLUMNS, CS336_PRECISION, CS336_GROUPS
      3. in the filter.properties file

   should use the getenv() method to see if there are environment variables named CS336_COLUMNS or CS336_PRECISION or CS336_GROUPS.
   If any of these environment variables exists, then its string value should be converted to an integer value by using the parseInt() method.
   
   Input numbers should be "right justified" in each output column 


*/
public class Filter
{
   public static void main(String[] args)
   {
      Properties prop = new Properties();
      File file = new File("filter.properties");
      FileInputStream fileInput = null;
      

   }
}
