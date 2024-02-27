/*
   Course: CS33600
   Name: Peter Bizoukas
   Email: pbizouka@pnw.edu
   Assignment: 2
*/

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
* This class reads the message sent from the server and prints the translated message.
*/
class Client
{
   private static int byteCounter = 0;

   /**
    * This is the main method that reads the message from the server and prints the translated message.
    * @param args
    * @throws IOException
    */
   public static void main(String[] args) throws IOException
   {
      final BufferedInputStream in = new BufferedInputStream(System.in);

      while(true) {
         byte header;
         try {
            header = (byte) in.read();
            byteCounter++;
            if(header == (byte) 0x80)  { //Expected end of file
               break;
            }
   
            if(header == -1) { //Unexpected end of file
               System.out.printf("Unexpected EOF at byte %d.\n", byteCounter);
               break;
            }
         } catch(IOException e) {
               System.out.println("Unexpected EOF in header.");
               break;
         }

         int isTextOrNumber = (header & 0xff) >> 7; //msb
         if(isTextOrNumber == 0) { //number
            checkType(in, header);
         } else { //text
            printCharacters(in, header);
         }
      }
   System.out.printf("\nRead %d bytes from standard input.\n", byteCounter); //Prints the number of bytes read from standard input.
}

   /**
    * This checks the type of the message and calls the appropriate method to print the message.
    * @param in
    * @param header
    * @throws IOException
    */
   private static void checkType(BufferedInputStream in, byte header) throws IOException {
         for (int i = 8; i > 1; i--) {
            byte b = (byte) (header & 1);
            if((int) b == 1) {
               printDouble(in);
            } else {
               printInt(in);
            }
            header = (byte) (header >> 1);
         }
      }


   /**
    * This reads the input stream in "ASCII" style and stores the bytes in a byte array and then prints the characters.
    * @param in
    * @param header
    * @throws IOException
    */
   private static void printCharacters(BufferedInputStream in, byte header) throws IOException{ //ASCII
      int length = (header & 0x7f); //last 7 bits
      for(int i = 0; i < length; i++) {
         byte b = (byte) in.read();
         byteCounter++;
         System.out.print((char) b);
      }
      System.out.println();

   }


   /**
    * This reads the input stream in "little endian" style and prints the double at 12 decimal places.
    * @param in
    * @throws IOException
    */
   private static void printDouble(BufferedInputStream in) throws IOException { //little endian
      ByteBuffer doubleByteBuffer = ByteBuffer.allocate(Double.BYTES);

      if (in.available() >= Double.BYTES) {
         for (int i = 7; i >= 0; i--) {
            byte doubleByte = (byte) in.read();
            doubleByteBuffer.put(i, doubleByte);
            byteCounter++;
         }

      } else {
         System.out.println("Insufficient data for double message.");
      }

      System.out.printf("%.12f\n", doubleByteBuffer.getDouble());      
   }

   /**
    * This reads the input stream in "weird endian" style and then prints the integer.
    * @param in
    * @throws IOException
    */
   private static void printInt(BufferedInputStream in) throws IOException{ //weird endian (3rd, 2nd, 1st, 4th)
      ByteBuffer intByteBuffer = ByteBuffer.allocate(Integer.BYTES);
      byte intByte;

      intByte = (byte) in.read(); //3rd
      intByteBuffer.put(2, intByte);

      intByte = (byte) in.read(); //2nd
      intByteBuffer.put(1, intByte);

      intByte = (byte) in.read(); //1st
      intByteBuffer.put(0, intByte);

      intByte = (byte) in.read(); //4th
      intByteBuffer.put(3, intByte);

      byteCounter += 4;

      System.out.println(intByteBuffer.getInt());
   }
}
