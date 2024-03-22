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
     1) Either send the server a negative integer and go to step 6,
        or go to step 2.
     2) Send the server a positive integer indicating
        the length of a sequence of integer values.
     3) Send the server a sequence of integers with the specified length.
     4) Receive back from the server the sum of the sequence.
     5) Go to step 1.
     6) Close the connection to the server.
*/
public class AdditionClient_Hw3_v3
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
      //using a sequence sentinel and integer counters
      Scanner scanner = new Scanner(System.in);
      boolean continueLoop = true;
      try {
         while (continueLoop) {
            int length = scanner.nextInt();

            if (length < 0) {
               // Send a negative number to the server to indicate termination
               out.println(length);
               out.flush();
               continueLoop = false;
            } else {
               // Send the length of the sequence to the server
               out.println(length);
               out.flush();

               // Send the sequence of integers to the server
               int[] sequence = new int[length];
               for (int i = 0; i < length; i++) {
                     sequence[i] = scanner.nextInt();
                     out.println(sequence[i]);
                     out.flush();
               }

               // Receive the sum of the sequence from the server
               String sumString = in.readLine();
               int sum = Integer.parseInt(sumString);
               System.out.println("CLIENT: Server response is: sum = " + sum);
            }
         }
      }
      catch (IOException e)
      {
         System.out.println("CLIENT: Error reading from server.");
         e.printStackTrace();
         System.exit(-1);
      }
         

      
      // Close the connection to the server.            

      try
      {
         socket.close();
      }
      catch (IOException e)
      {
         System.out.println("CLIENT: Error closing connection.");
         //System.out.println( e );
         e.printStackTrace();
         System.exit(-1);
      }

   }
   
      

      




}

