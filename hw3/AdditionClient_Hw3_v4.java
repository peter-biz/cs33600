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
     2) Send the server a sequence of positive integers.
     3) Send the server a negative integer to end the sequence.
     4) Receive back from the server the sum of the sequence.
     5) Go to step 1.
     6) Close the connection to the server.
*/
public class AdditionClient_Hw3_v4
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
      //client/server v4 should use a sequence sentinel and integer sentinels
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

      try {
         String line; // The sequence of integers

         while ((line = reader.readLine()) != null){ 
            if (line.equals("-1")) { // If the client -1, terminate the client
                  // Send a negative integer to the server to indicate termination
                  out.println("-1");
                  out.flush();
                  break;
            } else { // Otherwise, send the sequence of integers to the server
                  // Send the sequence of integers to the server
                  out.println(line); 
                  out.flush(); 

                  // Wait for the server to send a negative integer to end the sequence
                  String endSequence = in.readLine(); // Read the server's response
                  if (endSequence.equals("-1")) { // If the server sends -1, the sequence is over
                     // Receive the sum of the sequence from the server
                     String sumString = in.readLine(); 
                     int sum = Integer.parseInt(sumString); 
                     System.out.println("CLIENT: Server respose is: sum = " + sum);
                  } else {
                     System.out.println("Error: Server did not end the sequence correctly.");
                     break;
                  }
            }
         }
      } catch (IOException e) { 
         System.out.println("CLIENT: Error reading input or communicating with server.");
         e.printStackTrace();
         System.exit(-1);
      }


      //Close the connection to the server.
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
