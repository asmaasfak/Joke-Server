/*--------------------------------------------------------

1. Tim Abilzade / 04/19/2015
2. Java version: build 1.8.0

3. Precise command-line compilation examples / instructions:
 
> javac JokeServer.java


4. 
In separate shell windows:

> java JokeServer
> java JokeClient
> java JokeClientAdmin

All acceptable commands are displayed on the various consoles.

5. How to use

For this server you only need to run it, and it will be listening to any potention clients

6. Files needed for running the program:
 a. JokeServer.java
 b. JokeClient.java
 c. JokeClientAdmin.java


7. Notes:

N/A
*/
import java.io.*; // Get the Input Output libraries
import java.net.*; // Get the Java networking libraries
import java.util.*;

class AdminLooper implements Runnable {


	public void run() { // RUNning the Admin listen loop
		System.out.println("In the admin looper thread");

		int q_len = 6; /* Number of requests for OpSys to queue */
		int port = 2565; // We are listening at a different port for Admin
							// clients
		Socket sock;

		try {
			ServerSocket servsock = new ServerSocket(port, q_len);
			while (true) {
				// wait for the next ADMIN client connection:
				sock = servsock.accept();
				new AdminWorker(sock).start();
			}
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}

}

class Worker extends Thread { // Define Class
	Socket sock; // Class member, socket, local to Worker
	String useridJ; 
	String useridP;

	// static ArrayList<String> users;

	Worker(Socket s) {
		sock = s;
	}// Constructor, assign arg s to local socket

	public void run() {
		// Get I/O streams in/out from the socket:
		PrintStream out = null;
		BufferedReader in = null;

		try {
			in = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));// Getting an
																	// input
																	// from a
																	// connection
																	// with
																	// client
																	// (user)
			out = new PrintStream(sock.getOutputStream());// out put from the
															// connection
			
			
			
			
			try {
				
				String username = in.readLine();// receives and reads clients name
				useridJ = in.readLine();// receives the state of Jokes
				useridP = in.readLine();// receives the state of Proverbs
				String input = in.readLine(); // receives clients choice
				
				if((JokeServer.JPmode == true && useridJ.equals("11111")))//this resets the counter (if all jokes have been sent then start over)
				{
					useridJ = "00000";
				}
				
				if((JokeServer.JPmode == false && useridP.equals("11111")))//this resets the counter (if all Proverbs have been sent then start over)
				{
					useridP = "00000";
				}

				 
			
				if (JokeServer.inMaintenance == true) {//If the server is in maintenance mode it will only display a message (and also sent the joke/proverb state)
					
					out.println("Sorry the server is currently under the maintenance");
					out.println(useridJ);
					out.println(useridP);
				
				}else{// if the server is available then gen an input from client
					
					
					if ((input.indexOf("Yes") > -1 || (input.length() < 1)) && JokeServer.JPmode == true) {// You can either say Yes or hit enter - also checks what state the server is in

						out.println(giveJoke(username));//sends username to the function - gets and randomized joke - sends it to a client
						out.println(useridJ);// sends an updated cookie to client - joke state
						out.println(useridP);// sends an updated cookie to client - Proverb state
						System.out.println("looking up Jokes for "+ username);

					} else if ((input.indexOf("Yes") > -1 || (input.length() < 1)) && JokeServer.JPmode == false) {// You can either say Yes or hit enter - also checks what state the server is in
						out.println(giveProverb(username));//sends username to the function - gets and randomized joke - sends it to a client
						
						out.println(useridJ);// sends an updated cookie to client - joke state
						out.println(useridP);// sends an updated cookie to client - Proverb state
						
						System.out.println("looking up Proverbs for "+ username);//Displays what the server is doing and for who
					} else if (input.indexOf("Quit") < 0) {
						out.println("I did not understand what you meant by ' "
								+ input + " '. Please see the instructions!");//just in case an in put is wrong 
						out.println(useridJ);// still keep the old cookies even if the client didnt request anything
						out.println(useridP);// -^
					}
				} 	
				
				

			} catch (IOException x) {
				System.out.println("Server read error");// debugging if
														// something wrong with
														// input
				x.printStackTrace();
			}
			
			/*PrintStream output_to_Text = new PrintStream(new FileOutputStream("JokeOutput.txt"));
			System.setOut(output_to_Text);*/
			
			sock.close(); // close the connection, but not the server
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}


	public String giveJoke(String username) {// Send a joke to server (randomized and according to the current state
		String joke = null;
		char[] test = useridJ.toCharArray();// Breaks doing the cookie string into an array of characters
		
		while(true){
		
			//int random = (int) (Math.random() * 5) + 1;//generates a random number from 1 to 5
			
			Random rm = new Random();
			int random = rm.nextInt(5)+1; 
			
			if (random == 1 && test[0] != '1') { // if it generated 1 and the first character of a cookie is 0 then return this joke. Otherwise continue searching
				test[0] = '1';
				useridJ = String.valueOf(test);//convert an array back to string
				joke = "Joke A:'If "
						+ username //insert clients username into this Joke
						+ " had a dollar for every girl that found him unattractive, they would eventually find him attractive.'";
				break;// if found - then send
	
			}else // the logic below is identical to the first joke
				if (random == 2 && test[1] != '1') {
				test[1] = '1';
				useridJ = String.valueOf(test);
				joke = "Joke B:'"
						+ username
						+ " bought shoes from a drug dealer. I don't know what he laced them with but I've been tripping all day.' ";
				break;
			}else 
				if (random == 3 && test[2] != '1') {
				test[2] = '1';
				useridJ = String.valueOf(test);
				joke = "Joke C: 'Three men walk into a bar, the fourth one ducks ";
				break;
			}else 
				if (random == 4 && test[3] != '1') {
				test[3] = '1';
				useridJ = String.valueOf(test);
				joke = "Joke D: 'I never make mistakes...I thought I did once; but I was wrong.' ";
				break;
			}else 
				if (random == 5 && test[4] != '1') {
				test[4] = '1';
				useridJ = String.valueOf(test);
				joke = "Joke E: 'Until 1961 it was illegal to attempt suicide in the UK. The punishment was death.' ";
				break;
				} 
		
		}
		
		return joke;// send joke to Client

	}

	private String giveProverb(String username) {// Send a Proverb to server (randomized and according to the current state)
		String proverb = null;                   // The logic of this function is identical to getJoke function. Please dont make me re-type everything :)
		char[] test = useridP.toCharArray();
		
		while(true){

			//int random = (int) (Math.random() * 5) + 1;
			
			Random rm = new Random();
			int random = rm.nextInt(5)+1; 
			
			if (random == 1 && test[0] != '1') {
				test[0] = '1';
				useridP = String.valueOf(test);
				proverb = "Proverb A:'"
						+ username
						+ " Don't bite the hand that feeds you.' ";
				break;
	
			}else 
				if (random == 2 && test[1] != '1') {
				test[1] = '1';
				useridP = String.valueOf(test);
				proverb = "Proverb B:'"
						+ username
						+ " remember - Good things come to those who wait..' ";
				break;
			}else 
				if (random == 3 && test[2] != '1') {
				test[2] = '1';
				useridP = String.valueOf(test);
				proverb = "Proverb C: 'Assistance to drowning persons is in the hands of those persons themselves.'";
				break;
			}else 
				if (random == 4 && test[3] != '1') {
				test[3] = '1';
				useridP = String.valueOf(test);
				proverb = "Proverb D: 'Whatever is being done, is being done for the sake of best' ";
				break;
			}else 
				if (random == 5 && test[4] != '1') {
				test[4] = '1';
				useridP = String.valueOf(test);
				proverb = "Proverb E 'Necessity is the mother of invention.' ";
				break;
				} 
		
		
		}
		
		return proverb;

	}

	static String toText(byte ip[])// formatting - not interesting :)
	{ // Make portable for 128 bit format
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < ip.length; ++i) {
			if (i > 0)
				result.append(".");
			result.append(0xff & ip[i]);
		}
		return result.toString();
	}

}

public class JokeServer {

	static boolean JPmode = true;
	static boolean inMaintenance = false;

	public static void main(String a[]) throws IOException {
		int q_len = 6;
		int port = 1565;
		Socket sock;

		AdminLooper AL = new AdminLooper(); // another thread
		Thread t = new Thread(AL);
		t.start();

		ServerSocket servsock = new ServerSocket(port, q_len);// Create socket
																// with the
																// following
																// arguments
																// (port number,
																// queue length)
		// created new object servsock, pass 2 arg to constructor

		System.out.println("Clark Elliot's Inet server 1.8 starting up, listening at port:"
						+ port);
		while (true) { // I could have made a switch that would turn the server OFF here         
			sock = servsock.accept();// New client connection
			new Worker(sock).start(); // Spawn worker to handle the connection
		}
	}
}

class AdminWorker extends Thread {

	Socket sock;

	AdminWorker(Socket s) {
		sock = s;
	}// Constructor, assign arg s to local socket

	public void run() {
		// Get I/O streams in/out from the socket:
		PrintStream out = null;
		BufferedReader in = null;

		try {

			in = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
			out = new PrintStream(sock.getOutputStream());

			try {
				String state = in.readLine(); //What is Admin Clients input - choice - mode - ...

				if (state.indexOf("JokeMode") > -1) // Joke - true; Proverb -
													// false
				{
					JokeServer.inMaintenance = false;//if this was on then turn it off
			
					JokeServer.JPmode = true; // Joke mode enabled
					
					out.println("Joke Mode Enabled");
					System.out.println("Joke Mode Enabled");//Make server print to screen what mode was enabled by the Admin Client
				} else if (state.indexOf("ProverbMode") > -1) {
					
					JokeServer.inMaintenance = false;
					JokeServer.JPmode = false;// Proverb mode enabled
					out.println("Proverb Mode Enabled");
					System.out.println("Proverb Mode Enabled");
				} else if (state.indexOf("MaintenanceMode") > -1) {
					JokeServer.inMaintenance = true;
					out.println("Maintenance Mode Enabled");
					System.out.println("Maintenance Mode Enabled");

				} 
				//maybe turn OFF server code here

				else if (state.indexOf("Quit") > -1) {
					out.println("Come again :)");//Exit if Admin client said Quit. This message is just because Admin client is waiting for the server to respond.
					System.out.println("Admin Exited");//Exit if Admin client said Quit
				}

				else {
					out.println("The server did not understand what you meant by "//Just in case the input did not match any of the specified Modes
							+ state + ". Please see the instructions!");
				}
			} catch (IOException ioe) {
				System.out.println(ioe);
			}

		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}
}
