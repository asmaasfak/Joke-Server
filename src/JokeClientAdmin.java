/*--------------------------------------------------------

1. Tim Abilzade / 04/19/2015
2. Java version: build 1.8.0

3. Instructions:
 
> java JoketClientAdmin

then (assuming the server is running)

You will be prompted to enter a Mode, after which the Admin Client will request the server to change modes.
Example(s):

JokeMode

ProverbMode

MaintenanceMode

Also by typing in Quit it will exit the Admin Client


4. 
In separate shell windows:

> java JokeServer
> java JokeClient
> java JokeClientAdmin

All acceptable commands are displayed on the various consoles.

5. Files needed for running the program:
 a. JokeServer.java
 b. JokeClient.java
 c. JokeClientAdmin.java


5. Notes:

The server will be listening to this Admin client at a different port (2565)

N/A
----------------------------------------------------------*/
import java.io.*; // Get the Input Output libraries
import java.net.*; // Get the Java networking libraries
public class JokeClientAdmin {

	
	public static void main (String args[]){
		String serverName;// define the serv name
		if(args.length<1) serverName = "localhost";//if any command line argument was not entered (simply pressed enter) then the name is "home" - 127.0.0.1
		else serverName = args[0];// otherwise an argument is whatever is typed in
		
		System.out.println("This is an Admin Client \n");
		System.out.println("Using server: " + serverName + ", Port: 2565");
	
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));//Get an input from a user
		String input;
		try{
			do{
			
			System.out.println("Please enter servers mode, or type 'Quit' \n");
			System.out.println("Possible Modes are: JokeMode || ProverbMode || MaintenanceMode \n");
			System.out.flush();//clear
			input = in.readLine();
			
			getMode(input, serverName);//requests the server to change mode by sending Admins input
	
			}while(input.indexOf("Quit")<0);
			
			System.out.println ("Exited by superuser request.");
		}catch (IOException x ){
            x.printStackTrace();
	
	}
	}

static void getMode (String input, String serverName){
	Socket sock;
	PrintStream toServer;
	BufferedReader fromServer;

	
	try//Open our connection to server port
	{
		sock = new Socket(serverName, 2565);
		
		//Create filter I/O streams for the socket:
		toServer = new PrintStream(sock.getOutputStream());//input to server to process
		fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));// output from server, This is only for when Admin mistyped a command
																					// Not actually needed
		//Send Mode to server
		toServer.println(input); //send mode state to server
		
		toServer.flush();
		
		System.out.println(fromServer.readLine());
	
		}catch (IOException x) { System.out.println ("socket error."); x.printStackTrace ();}
	}	

}
