/*
   Course: CS33600
   Name: Peter Bizoukas
   Email: pbizouka@pnw.edu
   Assignment: 2
*/

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;


/*
 *  Message headers:
 *  0x80 denotes end-of-transmission (EOT)
 *  msb is 1 denotes a text message (ASCII)
 *  msb is 0 denotes a numberic message, remaing 7 bits are a bit field, a 0 tells it to expect an int, a 1 tells it to expect a double
 *       for example: if we recieve the hex 0x39 (00111001), we read it from lsb to msb,
 *        so, we know it's numeric once we read it all, the client should then expected a double, two ints, three doubles, and then one int
 *    
 *    The eight bytes of a double are sent in little-endian byte order. ie: lsb first, msb last. 
 *    NOTE: java uses big-endian, so we need to reverse the byte order
 * 
 *    The four bytes of an int are sent in "weird-endian" byte order. this means the server will send the 3 msb, then the second msb, followed by the lsb
 *    
 *    Will need to use ByteBuffer to convert the bytes to the correct type
 *    putInt(): allows us to put an int into the buffer
 *    putDouble(): allows us to put a double into the buffer
 *    
 *    byte[] b = ByteBuffer.allocate(Integer.BYTES).putInt(12345).array();
 * 
 *    General requirements:
 *       The client should keep track of how many bytes it is receiving from the server. 
 *    After receiving an end-of-transmision message, the client should print out its count of the total number of bytes it received from the server. 
 *    If the client should detect an end-of-file condition before receiving an end-of-transmission message, 
 *    the client should print an error message that includes the number of bytes it has received so far from the server. 
 *    The client should also print to standard output the contents of each text message and each numeric message.
 *    Client should read the std input stream one byte at a time, using read() from InputStream.
 *    Client should check for end-of-file condition after every byte. 
 *    Client should assume that the server is not sending reliable data. ie: have error checking, try-catch's etc.
 *    Should not crash on bad input, should print an error message and continue.
 */
class Client
{
   public static void main(String[] args) throws IOException
   {
      final BufferedInputStream in = new BufferedInputStream(System.in);
      int byteCounter = 0;

      while(true) {
         
         byte header = (byte) in.read();  
         System.out.println("***HEADER***: " + header);   
         
         if(header == (byte) 0x80)  { //Expected end of file
            byteCounter++;
            break;
         }

         if(header == -1) { //Unexpected end of file
            System.out.printf("Unexpected EOF at byte %d.\n", byteCounter);
            break;
         }
         
         byteCounter++;

         if(((header & 0xff) >> 7) == 1) { 
            //Text message, hexadecimal 0x20 through 0x7E
            int numChars =  header & 0x7F; //this gets the last 7 bits
            byte[] txtBytes = new byte[numChars];
            for(int i = 7; i > 0; i--) {
              System.out.printf("text @byte: %d\n", byteCounter); 
            }
         }
         else if(((header & 0xff) >> 7) == 0) { 
            //Numeric message
            int numChars = header & 0x7F; //last 7 bits
            byte[] numBytes = new byte[numChars];
            //check if next byte is a double or int, then send to appropriate method
           System.out.printf("numeric, who knows the type, not me @byte: %d\n", byteCounter);          

         }
         
         //java -jar client_demo.jar < testdata


      }
     
      System.out.printf("\nRead %d bytes from standard input.\n", byteCounter);
   }

   private static byte[] readWeirdEndianInt() throws IOException {
      byte[] bytes = new byte[4];
      bytes[2] = (byte) System.in.read();
      bytes[1] = (byte) System.in.read();
      bytes[0] = (byte) System.in.read();
      bytes[3] = (byte) System.in.read();

      return bytes;
   }

   private static void readDoubleBuffer(byte[] b) throws IOException { //little endian
      double d = 1.0;
      System.out.printf("Numeric message: %f\n", d);
   }

   private static void readInt(byte[] b) { //weird endian
      int i = 0;
      System.out.printf("Numeric message: %d\n", i);
   }
}
