/*
   Course: CS33600
   Name: Peter Bizoukas
   Email: pbizouka@pnw.edu
   Assignment: 3


*/

import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
   This version of the client will,
     1) Send the server a positive integer indicating the
        number of integer sequences that will follow.
     2) Send the server a sequence of positive integers.
     3) Send the server a negative integer to end the sequence.
     4) Receive back from the server the sum of the sequence.
     5) If not the last sequence, then go back to step 2.
     6) Close the connection to the server.
*/
public class AdditionClient_Hw3_v2
{
   public static final int SERVER_PORT = 5000; // Should be above 1023.

   public static void main (String[] args)
   {
      Socket          socket = null;
      BufferedReader  in = null;
      PrintWriter     out = null;

      final String hostName;
      if (args.length > 0)
      {
         hostName = args[0];
      }
      else
      {
         hostName = "localhost";
      }

      final int portNumber;
      if (args.length > 1)
      {
         portNumber = Integer.parseInt(args[1]);
      }
      else
      {
         portNumber = SERVER_PORT;
      }

      // Get this client's process id number (PID). This helps
      // to identify the client in the server's transcrip.
      final ProcessHandle handle = ProcessHandle.current();
      final long pid = handle.pid();
      System.out.println("CLIENT: Process ID number (PID): " + pid );

      // Make a connection to the server
      try
      {
         System.out.println("CLIENT: connecting to server: " + hostName + " on port " + portNumber );
         socket = new Socket(InetAddress.getByName(hostName), portNumber);

         in = new BufferedReader(
                  new InputStreamReader(
                       socket.getInputStream()));
         out = new PrintWriter(socket.getOutputStream());
      }
      catch (IOException e)
      {
         System.out.println("CLIENT: Cannot connect to server.");
         //System.out.println( e );
         e.printStackTrace();
         System.exit(-1);
      }

      // Implement the appropriate client/server protocol.
      // client/server v2 should use a sequence counter and integer sentinels
      final Scanner scanner = new Scanner(System.in);
      int numSequences = scanner.nextInt(); // Read the number of sequences
      scanner.nextLine(); // Discard the rest of the line

      while (numSequences > 0) {
         //System.out.println("CLIENT: Number of sequences: " + numSequences);
         int num;
         while (scanner.hasNextInt() &&  (num = scanner.nextInt()) >= 0) {
            
           // System.out.println("num = " + num);
            out.println(num);

            if (num < 0) {
               break;
            }
         }
         out.println(-1);
         out.flush();

         try {
            int sum = Integer.parseInt(in.readLine());
            System.out.println("CLIENT: Server response is: sum = " + sum);
         } catch (IOException e) {
            System.out.println("CLIENT: Error reading from server.");
            e.printStackTrace();
            System.exit(-1);
         }

         numSequences--;
      }
      // Close the connection to the server
      try
      {
         socket.close();
      }
      catch (IOException e)
      {
         System.out.println("CLIENT: Error closing the connection to the server.");
         //System.out.println( e );
         e.printStackTrace();
         System.exit(-1);
      }

      

   }


}
