/*
   https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Properties.html
*/

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

/**

*/
class PropertiesExample
{
   public static void main(String[] args)
   {
      final Properties properties = new Properties();
      try
      {
         // Read all the property key/value pairs.
         properties.load(
            new FileInputStream(
               new File("example.properties")));

         properties.list(System.out); // Print all the properties.
         System.out.println();

         // You can read the properties in any order.
         final String p5 = properties.getProperty("p5");
         final String p4 = properties.getProperty("p4");
         final String p1 = properties.getProperty("p1");
         final String p3 = properties.getProperty("p3");
         final String p2 = properties.getProperty("p2");
         System.out.println("p1 = " + p1);
         System.out.println("p2 = " + p2);
         System.out.println("p3 = " + p3);
         System.out.println("p4 = " + p4);
         System.out.println("p5 = " + p5);
      }
      catch (FileNotFoundException e)
      {
         e.printStackTrace(System.err);
         System.exit(-1);
      }
      catch (IOException e)
      {
         e.printStackTrace(System.err);
         System.exit(-1);
      }

   }
}
