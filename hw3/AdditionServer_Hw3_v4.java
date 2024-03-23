/*
   Course: CS33600
   Name: Peter Bizoukas
   Email: pbizouka@pnw.edu
   Assignment: 3


*/

import java.net.*;
import java.util.StringTokenizer;
import java.io.*;

/**
   For each client, this server will,
     1) Read an integer.
        If the integer is negative,
        then close the connection to the client.
     2) Read a sequence of positive integer values,
        one value per line of text,
        up until a negative value.
     3) Send back the sum of the sequence as a text string.
     4) Go back to step 1.
*/
public class AdditionServer_Hw3_v4
{
   public static final int SERVER_PORT = 5000; // Should be above 1023.

   public static void main (String[] args)
   {
      final int portNumber;
      if (args.length > 0)
      {
         portNumber = Integer.parseInt(args[0]);
      }
      else
      {
         portNumber = SERVER_PORT;
      }

      int clientCounter = 0;

      // Get this server's process id number (PID). This helps
      // to identify the server in TaskManager or TCPView.
      final ProcessHandle handle = ProcessHandle.current();
      final long pid = handle.pid();
      System.out.println("SERVER: Process ID number (PID): " + pid );

      // Get the name and IP address of the local host and
      // print them on the console for information purposes.
      try
      {
         final InetAddress address = InetAddress.getLocalHost();
         System.out.println("SERVER Hostname: " + address.getCanonicalHostName() );
         System.out.println("SERVER IP address: " +address.getHostAddress() );
         System.out.println("SERVER Using port no. " + portNumber);
      }
      catch (UnknownHostException e)
      {
         System.out.println("Unable to determine this host's address.");
         System.out.println( e );
      }

      // Create the server's listening socket.
      ServerSocket serverSocket = null;
      try
      {
         serverSocket = new ServerSocket(portNumber);
         System.out.println("SERVER online:");
      }
      catch (IOException e)
      {
         System.out.println("SERVER: Error creating network connection.");
         //System.out.println( e );
         e.printStackTrace();
         System.exit(-1);
      }

      while(true) // Serve multiple clients.
      {           // Each client can make one request.
         Socket socket = null;
         BufferedReader in = null;
         PrintWriter out = null;

         // Wait for an incoming client request.
         try
         {
            socket = serverSocket.accept();

            // At this point, a client connection has been made.
            in = new BufferedReader(
                     new InputStreamReader(
                          socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());
         }
         catch(IOException e)
         {
            System.out.println("SERVER: Error connecting to client");
            System.out.println( e );
         }

         ++clientCounter;
         // Get the IP address of the client and log it to the console.
         final InetAddress clientIP = socket.getInetAddress();
         System.out.println("SERVER: Client " + clientCounter + ": IP: " +  clientIP.getHostAddress());

         // Implement the appropriate client/server protocol.
         //client/server v4 should use a sequence sentinel and integer sentinels
         String line;
         try {
            while ((line = in.readLine()) != null) {
               int sum = 0;
               while(true) {
                  final int sentinel = Integer.parseInt(line.trim());
                  if(sentinel >= 0) {
                     sum += sentinel;
                    
                  } else {
                     break;
                  }
                  line = in.readLine();

               }
               System.out.println("SERVER: Client " + clientCounter + ": Message recieved: sum = " + sum);
               out.println(sum);
               out.flush();
               
            }
               //Close the connection to the client.
               try
               {
                  socket.close();
                  System.out.println("SERVER: Client " + clientCounter + ": Closed socket.");
               }
               catch (IOException e)
               {
                  System.out.println("SERVER: Error closing connection.");
                  //System.out.println( e );
                  e.printStackTrace();
               }
            
        } catch (IOException e) {
            System.out.println("SERVER: Error reading from or writing to the client.");
            e.printStackTrace();
        }




      }
   }
}
