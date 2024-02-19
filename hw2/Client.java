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
   public static void main(String[] args)
   {
      final BufferedInputStream in = new BufferedInputStream(System.in);
      int byteCounter = 0;

      try {
         while (true) {
            int b = in.read();
            if (b == -1) {
               System.out.printf("\nError: End of file condition detected after reading %d bytes from standard input.\n", byteCounter);
               break;
            }
            byteCounter++;
            if ((b & 0x80) == 0x80) {
               System.out.printf("\nRead %d bytes from standard input.\n", byteCounter);
               break;
            }
            if ((b & 0x80) == 0) {
               if ((b & 0x40) == 0) {
                  byte[] intBytes = new byte[4];
                  for (int i = 0; i < 4; i++) {
                     intBytes[i] = (byte) in.read();
                     byteCounter++;
                  }
                  ByteBuffer bb = ByteBuffer.wrap(intBytes);
                  System.out.printf("%d\n", bb.getInt());
               }
               else {
                  byte[] doubleBytes = new byte[8];
                  for (int i = 0; i < 8; i++) {
                     doubleBytes[i] = (byte) in.read();
                     byteCounter++;
                  }
                  ByteBuffer bb = ByteBuffer.wrap(doubleBytes);
                  System.out.printf("%f\n", bb.getDouble());
               }
            }
            else {
               byte[] textBytes = new byte[b & 0x7F];
               for (int i = 0; i < (b & 0x7F); i++) {
                  textBytes[i] = (byte) in.read();
                  byteCounter++;
               }
               System.out.printf("%s\n", new String(textBytes));
            }
         }
      }
      catch (IOException e) {
         System.err.println("Error: IOException");
      }


      System.out.printf("\nRead %d bytes from standard input.\n", byteCounter);
   }
}
