/*--------------------------------------------------------

1. Tim Abilzade / 04/19/2015
2. Java version: build 1.8.0

3. Precise command-line compilation examples / instructions:
 
> javac JokeClient.java


4. 
In separate shell windows:

> java JokeServer
> java JokeClient
> java JokeClientAdmin

All acceptable commands are displayed on the various consoles.

5. How to use

For this client, assuming the server is running,

Enter your user name, note that you have to enter at least one character (This is going to happen once)
Example(s):
Tim, John, Katy, Client1

Then you will be prompted to receive either a Joke or a Proverb depending on which state the server is currently in.
You can either say 'Yes' or press Enter in order to receive a joke or a proverb
You you want to exit - say 'Quit' 

Examples: 'Yes' 'Quit'

6. Files needed for running the program:
 a. JokeServer.java
 b. JokeClient.java
 c. JokeClientAdmin.java


7. Notes:

The server sends and displays the state of Jokes and proverbs for debugging and grading reasons :)


N/A
----------------------------------------------------------*/
import java.io.*; // Get the Input Output libraries
import java.net.*; // Get the Java networking libraries


public class JokeClient {
	
	

	static String useridJ; //to keep track of the state of Jokes
	static String useridP;//to keep track of the state of Proverbs
	
	public static void main (String args[]){

		
		String serverName;// define the serv name
		
		
		if(args.length<1) serverName = "localhost";//if any command line argument was not entered (simply pressed enter) then the name is "home" - 127.0.0.1
		else serverName = args[0];// otherwise an argument is whatever is typed in
		
		System.out.println("Tim Abil's Joke Client, 1.8.\n");
		System.out.println("Using server: " + serverName + ", Port: 2565");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));//Get an input from a user
		
	
		
        String username = null;
        
        try {
        	do{
        	System.out.print("Please enter your name: ");
			username = in.readLine();
			if(username.length() >= 1)
			{
				break;
			}else System.out.println("You have not entered anything");
			
			
        	}while(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
      
       // userid = UUID.randomUUID();
       
        useridJ = "00000"; //Cookies for Jokes
        useridP = "00000"; //Cookies for Proverbs
       
        
        
		try{
			String input;
			do{ 						//Requests an input from user until user says quit
				System.out.print("Would you like me to give you a joke or a proverb, (Quit) to end: ");
				System.out.flush();//clear
				input = in.readLine();// send users choice to server
				getter(input, serverName, username); //send this information to server (input servername and username)
				
			}while (input.indexOf("Quit") < 0); // quitsmoking.com will cause it to quit also 
			System.out.println ("Cancelled by user request.");//if typed quit - EXITs
			
			
			

		
			
            
		}catch (IOException x) {x.printStackTrace();}//debug
	}
	
	static String toText (byte ip[]) { /* Make portable for 128 bit format */
		 StringBuffer result = new StringBuffer ();
		 for (int i = 0; i < ip.length; ++i) {
		 if (i > 0) result.append (".");
			result.append (0xff & ip[i]);
		 }
		 return result.toString ();
		 }
	
	static void getter (String input, String serverName, String username ){//
		
		Socket sock = null;
		BufferedReader fromServer;
		PrintStream toServer;
		String textFromServer;
		
		
		
		try{//Open our connection to server port

			//PrintWriter outFile = new PrintWriter(new FileWriter("JokeOutput.txt")); // was here for output to .txt
			sock = new Socket(serverName, 1565);
			
			
			//Create filter I/O streams for the socket:
			fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));// output from server
			toServer = new PrintStream(sock.getOutputStream());//input to server to process
			
			
			
			//Send machine name or IP address to server
		
			
			toServer.println(username);// send username of a client to server
			toServer.println(useridJ);//send the state of Jokes of a client to server
			toServer.println(useridP);//send the state of Proverbs of a client to server
			toServer.println(input); //send weather the client want anything from server
			
			
			
		toServer.flush();

		//read two or three lines of response from the server and block while synchronously waiting:
			
				textFromServer = fromServer.readLine(); // Receive any jokes proverbs or other info from server
				useridJ = fromServer.readLine(); // Receive the state of jokes proverbs - cookie
				useridP = fromServer.readLine(); // Receive the state of jokes proverbs - cookie
				
				

				//outFile.println(textFromServer);
				if(textFromServer != null)System.out.println("\n " + textFromServer + "\n"); //Print out to screen jokes, proverbs or any other info form server
				if(useridJ != null) System.out.println("Joke state of "+ username + " is now: "+ useridJ); //Prints out the state of Jokes - for debugging reasons
				if(useridP != null)System.out.println("Proverbs state "+ username + " is now: "+ useridP);//Prints out the state of Proverbs - for debugging reasons
			
				

			
			sock.close();
			}catch (IOException x) { System.out.println ("socket error."); x.printStackTrace ();
		}
	}
}
