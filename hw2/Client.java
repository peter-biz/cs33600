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

class Client
{
   private static int byteCounter = 0;

   public static void main(String[] args) throws IOException
   {
      final BufferedInputStream in = new BufferedInputStream(System.in);

      while(true) {
         byte header;
         try {
            header = (byte) in.read();
            //System.out.println("***HEADER***: " + header);    //TODO: remove this

            if(header == (byte) 0x80)  { //Expected end of file
               // byteCounter++; //TODO: might need this
               break;
            }
   
            if(header == -1) { //Unexpected end of file
               System.out.printf("Unexpected EOF at byte %d.\n", byteCounter);
               break;
            }
   
            //header reads current bit. The full 8 bits are read from the input stream and then the messages are sent after 
            if((header & 0x80) == 0x80) { //Text message
               byte[] bytes = new byte[header & 0x7f];
               printCharacters(in, bytes);
               byteCounter += bytes.length;           
            } else if(((header & 0xff) >> 7) == 0) { //Numeric message
               byte intOrDouble = (byte) (header & 1);
               if(intOrDouble == 0) { //int
                  printInt(in);
               } else { //double
                  printDouble(in);
               }
               header = (byte) (header >> 1);
            }        
         } 
         catch(IOException e) {
            System.out.println("Unexpected EOF in header.");
            break;
      }
         
   }
   System.out.printf("\nRead %d bytes from standard input.\n", byteCounter); 

         
 
}


   /**
    * This reads the input stream in "ASCII" style and stores the bytes in a byte array and returns it.
    * @param b
    * @throws IOException
    * @return byte[]
    */
   private static void printCharacters(BufferedInputStream in, byte[] b) { //ASCII
      try{
         for(int i = 0; i < b.length; i++) {
            b[i] = (byte) in.read();
            
         }
      } catch(IOException e) {
         System.out.println("Unexpected EOF in text message.");
      }
      System.out.println(new String(b));
   }


   /**
    * This reads the input stream in "little endian" style and stores the bytes in a byte array --- @TODO:
    * @param b
    * @throws IOException
    */
   private static void printDouble(BufferedInputStream in) { //little endian
      ByteBuffer doubleByteBuffer = ByteBuffer.allocate(Double.BYTES);
     
      try{
         for(int i = 7; i >= 0; i--) {
            byte doubleByte = (byte) in.read();
            doubleByteBuffer.put(i, doubleByte);
            byteCounter++;
         }

         System.out.printf("double%.12f\n", doubleByteBuffer.getDouble());

      }
      catch(IOException e) {
         System.out.println("Unexpected EOF in double message.");
      }

      
   }

   /**
    * This reads the input stream in "weird endian" style and stores the bytes in a byte array --- @TODO:
    * @param b
    * @throws IOException
    */
   private static void printInt(BufferedInputStream in) { //weird endian (3rd, 2nd, 1st, 4th)
      ByteBuffer intByteBuffer = ByteBuffer.allocate(Integer.BYTES);
      try {
         //read in weird endian

         byte intByte;

         //read 3rd
         intByte = (byte) in.read();
         intByteBuffer.put(2, intByte);

         //read 2nd
         intByte = (byte) in.read();
         intByteBuffer.put(1, intByte);

         //read 1st
         intByte = (byte) in.read();
         intByteBuffer.put(0, intByte);

         //read 4th
         intByte = (byte) in.read();
         intByteBuffer.put(3, intByte);
         
         byteCounter += 4;
         System.out.println("INT"+intByteBuffer.getInt());

      }
      catch(IOException e) {
         System.out.println("Unexpected EOF in int message.");
      }

   }
}
