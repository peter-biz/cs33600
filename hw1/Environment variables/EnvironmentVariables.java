/**
   A simple demonstration of using environment variables
   (and command line arguments).

   See
   https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/System.html#getenv(java.lang.String)
*/
public class EnvironmentVariables
{
   public static void main(String[] args)
   {
      // Walk the args array and look for environment variables.
      for (final String cmdLineArg : args)
      {
         final String value = System.getenv( cmdLineArg );
         if (null == value)
         {
            System.out.println( cmdLineArg + " is not an environment variable." );
         }
         else
         {
            System.out.println( "The value of " + cmdLineArg + " is " + value );
         }
      }
   }
}