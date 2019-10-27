import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.border.Border;

@SuppressWarnings("unused")
public class UserInterface2 {
	static String username = "";
	static String password = "";
	static String checkedPassword = "";
	static UserInterface2 sender;
	static boolean tryPassCheck = false;
	static boolean trySignUp = false;
	static String startLocation = "";
	static String endLocation = "";
	static double distance = 0.0;
	static double price = 0.0;
	static boolean isRide = false;
	static boolean isReserve = false;
	static boolean isPickup = false;
	static String scooterID = "";
	static String reservedScooterID = "";
	static String paymentMethod = "xxxxxxxxxxxxxxxx";
	
	@SuppressWarnings({ "resource" })
	public static void main(String[] args)
			throws IOException, SQLException, ClassNotFoundException, InterruptedException {
		if (args.length == 0) {
			System.out.println("Usage: java CarDB propertiesFile");
			System.exit(0);
		}
		SimpleDataSource.init(args[0]);
		try (Connection conn = SimpleDataSource.getConnection(); Statement stat = conn.createStatement()) // start try
		{
			try {
				stat.execute("DROP TABLE Scooters");
				stat.execute("DROP TABLE Distances");
				stat.execute("DROP TABLE Users");
				stat.execute("DROP TABLE Login");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

			stat.execute(
					"CREATE TABLE Scooters (ScooterID VARCHAR(20), CurrBattCharge DECIMAL(6,2), ScooterLocation VARCHAR(20), IsReserved VARCHAR(5))");
			String inputFileName = "ScooterDatabase.txt";
			File inputFile = new File(inputFileName);
			Scanner in = new Scanner(inputFile);

			while (in.hasNext()) {
				PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO Scooters(ScooterID, CurrBattCharge, ScooterLocation, IsReserved) VALUES (?, ?, ?, ?)");
				stmt.setString(1, in.next());
				stmt.setDouble(2, in.nextDouble());
				stmt.setString(3, in.next());
				stmt.setString(4, in.next());
				stmt.executeUpdate();
			} // end while

			stat.execute(
					"CREATE TABLE Distances (StartLocation VARCHAR(20), EndLocation VARCHAR(20), Distance DECIMAL(6,2))");
			String inputFileNameDist = "DistanceDatabase.txt";
			File inputFileDist = new File(inputFileNameDist);
			Scanner inDist = new Scanner(inputFileDist);
			while (inDist.hasNext()) {
				PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO Distances(StartLocation, EndLocation, Distance) VALUES (?, ?, ?)");
				stmt.setString(1, inDist.next());
				stmt.setString(2, inDist.next());
				stmt.setDouble(3, inDist.nextDouble());
				stmt.executeUpdate();
			}
			stat.execute(
					"CREATE TABLE Users (Username VARCHAR(20), Password VARCHAR(20), NumberOfRides INT, PaymentMethod CHAR(16), TotalPayment DECIMAL(6,2), Reservation VARCHAR (20), Title VARCHAR(10), Status VARCHAR(3))");
			String inputFileNameUser = "UserDatabase.txt";
			File inputFileUser = new File(inputFileNameUser);
			Scanner inUser = new Scanner(inputFileUser);
			while (inUser.hasNext()) {
				PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO Users(Username, Password, NumberOfRides, PaymentMethod, TotalPayment, Reservation, Title, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
				stmt.setString(1, inUser.next());
				stmt.setString(2, inUser.next());
				stmt.setString(3, inUser.next());
				stmt.setString(4, inUser.next());
				stmt.setDouble(5, inUser.nextDouble());
				stmt.setString(6, inUser.next());
				stmt.setString(7, inUser.next());
				stmt.setString(8, inUser.next());
				stmt.executeUpdate();
			}
			inUser.close();
			stat.execute("CREATE TABLE Login (LoginUsername VARCHAR(20))");
			String checkUsername = "";
			boolean login = true;
			String inputFileNameLogin = "LoginDatabase.txt";
			File inputFileLogin = new File(inputFileNameLogin);
			Scanner inLogin = new Scanner(inputFileLogin);
			while (inLogin.hasNext()) {
				checkUsername = inLogin.next();
				stat.execute("INSERT INTO Login (LoginUsername) VALUES ('" + checkUsername + "')");
			}
			
			
			 //layout
			 
			 
			 //Frame Construction
			 JFrame startFrame = new JFrame("Start Frame");
			 startFrame.setLocation(600,200);
			 startFrame.setSize(750,600);
			 
			 JFrame loginFrame = new JFrame("Login Frame");
			 loginFrame.setLocation(700,200);
			 loginFrame.setLayout(new GridLayout(3,2));
			 JFrame signUpFrame = new JFrame("Sign Up Frame");
			 signUpFrame.setLocation(700,200);
			 signUpFrame.setLayout(new GridLayout(3,2));
			 JFrame mainFrame = new JFrame("Main Frame");
			 mainFrame.setLocation(700,200);
			 mainFrame.setLayout(new GridLayout(3,2));
			 JFrame rideFrame = new JFrame("Ride Frame");
			 rideFrame.setLocation(700,200);
			 rideFrame.setLayout(new GridLayout(3,2));
			 JFrame reserveFrame = new JFrame("Reserve Frame");
			 reserveFrame.setLocation(700,200);
			 reserveFrame.setLayout(new GridLayout(3,2));
			 JFrame pickupFrame = new JFrame("Pickup Frame");
			 pickupFrame.setLocation(700,200);
			 pickupFrame.setLayout(new GridLayout(3,2));
			 JFrame printFrame = new JFrame("Print Frame");
			 printFrame.setLocation(700,200);
			 printFrame.setLayout(new GridLayout(3,2));
			 //images
			 ImageIcon img = new ImageIcon("logo.jpg");
			 JLabel imgLabel = new JLabel(img);
			 
			 
			 //panels
			 JPanel startPanel = new JPanel();
			 startPanel.add(imgLabel);
			 startPanel.setBorder(BorderFactory.createLineBorder(Color.black));
			 startPanel.setSize(750,600);
			 JPanel loginPanel = new JPanel();
			 JPanel signUpPanel = new JPanel();
			 JPanel mainPanel = new JPanel(new GridLayout (5,5));
			 JPanel ridePanelStart = new JPanel();
			 JPanel ridePanelEnd = new JPanel();
			 JPanel reservePanel = new JPanel();
			 JPanel pickupPanel = new JPanel();
			 JPanel printPanel = new JPanel();
			 
			 //labels
			 ridePanelStart.add(new JLabel("Select where you'd like to pick up a scooter: "));
			 ridePanelEnd.add(new JLabel("Select where you'd like to ride to: "));
			 JLabel rideStartSelection = new JLabel("Select where you would like to start your ride");
			 JLabel rideEndSelection = new JLabel("Select where you would like to ride to");
			 JLabel reserveSelection = new JLabel("Select where you want to reserve a scooter");
			 JLabel pickupDestSelection = new JLabel("Select where you would like to ride to");
			 JLabel loginDescription = new JLabel("Enter username and password. Hit Enter after typing in each box so it saves.");
			 JLabel signUpDescription = new JLabel("Enter username, password and Credit Card. Hit Enter after typing in each box so it saves.");
			 
			 //buttons
			 JButton loginButton = new JButton("Login");
			 //loginButton.setPreferredSize(new Dimension(10,100));
			 JButton signUpButton = new JButton("Sign Up");
			 //signUpButton.setPreferredSize(new Dimension(10,50));
			 JButton loginEnter = new JButton("Enter");
			 JButton signUpEnter = new JButton("Enter");
			 JButton rideButton = new JButton("Ride Now");
			 JButton reserveButton = new JButton("Reserve");
			 JButton pickupButton = new JButton("Pick Up");
			 JButton printButton = new JButton("Print");
			 JButton mwlRideStart = new JButton("MWL");
			 JButton muRideStart = new JButton("Memorial Union");
			 JButton stateRideStart = new JButton("State Gym");
			 JButton parksRideStart = new JButton("Parks Library");
			 JButton kildeeRideStart = new JButton("Kildee Hall");
			 JButton freddyRideStart = new JButton("Freddy Court");
			 JButton mwlRes = new JButton("MWL");
			 JButton muRes = new JButton("Memorial Union");
			 JButton stateRes = new JButton("State Gym");
			 JButton parksRes = new JButton("Parks Library");
			 JButton kildeeRes = new JButton("Kildee Hall");
			 JButton freddyRes = new JButton("Freddy Court");
			 JButton mwlPick = new JButton("MWL");
			 JButton muPick = new JButton("Memorial Union");
			 JButton statePick = new JButton("State Gym");
			 JButton parksPick = new JButton("Parks Library");
			 JButton kildeePick = new JButton("Kildee Hall");
			 JButton freddyPick = new JButton("Freddy Court");
			 JButton mwlRideEnd = new JButton("MWL");
			 JButton muRideEnd = new JButton("Memorial Union");
			 JButton stateRideEnd = new JButton("State Gym");
			 JButton parksRideEnd = new JButton("Parks Library");
			 JButton kildeeRideEnd = new JButton("Kildee Hall");
			 JButton freddyRideEnd = new JButton("Freddy Court");
			 JButton printPrint = new JButton("Print");
			 
			 
			 //labels
			 
			 
			 //textfields
			 JTextField loginUsername = new JTextField("Username",20);
			 JTextField loginPassword = new JTextField("Password",20);
			 JTextField signUpUsername = new JTextField("New Username",20);
			 JTextField signUpPassword = new JTextField("New Password",20);
			 JTextField enterPayment = new JTextField("Enter 16 Digit Credit Cart Number", 40);
			 
			 
			 //layout
			 //startFrame
			 GroupLayout startLayout = new GroupLayout(startPanel);
			 	startLayout.setHorizontalGroup(
			 		startLayout.createSequentialGroup()
			 		.addGroup(startLayout.createParallelGroup(GroupLayout.Alignment.LEADING))
			 		.addComponent(imgLabel, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			 		.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			 		.addComponent(loginButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		 			.addComponent(signUpButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			 			
			 		);
			 	startLayout.setVerticalGroup(
			 		startLayout.createSequentialGroup()	
			 			.addGroup(startLayout.createParallelGroup(GroupLayout.Alignment.CENTER))
			 				.addComponent(imgLabel)
			 				.addComponent(loginButton)
			 				.addComponent(signUpButton)
			 				
			 			);
			 	startLayout.setAutoCreateContainerGaps(true);
			 	startPanel.setLayout(startLayout); 
			 	
			 	
			 
			 
			 
			 
			 
			 
			 //start
			 startPanel.add(loginButton);
			 startPanel.add(signUpButton);
			 startFrame.add(startPanel);
			 startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 startFrame.setVisible(true);
			 loginButton.addActionListener(new ActionListener() {

				 
				@Override
				public void actionPerformed(ActionEvent e) {
					if(e.getSource() == loginButton) {
						System.out.println("login");
						loginFrame.setVisible(true);
						startFrame.setVisible(false);
					}
				}
				 
			 });
			 signUpButton.addActionListener(new ActionListener() {
				 
				 @Override
				 public void actionPerformed(ActionEvent e) {
					 if(e.getSource() == signUpButton ) {
						 System.out.println("Sign up");
						 trySignUp = true;
						 signUpFrame.setVisible(true);
						 startFrame.setVisible(false);
					 }
				 }
			 });
			 //login
			 loginPanel.add(loginDescription);
			 loginPanel.add(loginUsername);
			 loginPanel.add(loginPassword);
			 loginPanel.add(loginEnter);
			 
			 loginEnter.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					
					if(e.getSource() == loginEnter) {
						checkedPassword = loginPassword.getText();
						username = loginUsername.getText();
						try {
							if(checkLogin(checkedPassword, username, conn)) {
								mainFrame.setVisible(true);
							} else {}
						} catch (SQLException e1) {e1.printStackTrace();}
						loginFrame.setVisible(false);
					}
				}
				 
			 });
			 loginFrame.add(loginPanel);
			 loginFrame.setSize(600,400);
			 loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 //sign up
			 signUpPanel.add(signUpDescription);
			 signUpPanel.add(signUpUsername);
			 signUpPanel.add(signUpPassword);
			 signUpPanel.add(signUpEnter);
			 signUpPanel.add(enterPayment);
			
			 signUpEnter.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(e.getSource() == signUpEnter) {
						password = signUpPassword.getText();
						paymentMethod = enterPayment.getText();
						username = signUpUsername.getText();
						try {
							if(signup(password, username, paymentMethod, conn)) {
								mainFrame.setVisible(true);
							} else {}
						} catch (SQLException e1) {e1.printStackTrace();}
						signUpFrame.setVisible(false);
					}
					
				}
				 
			 });
			 
			 signUpFrame.add(signUpPanel);
			 signUpFrame.setSize(600,400);
			 signUpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 //main
			 mainPanel.add(rideButton);
			 mainPanel.add(reserveButton);
			 mainPanel.add(pickupButton);	
			 mainPanel.add(printButton);
			 rideButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					rideFrame.setVisible(true);
					mainFrame.setVisible(false);
					
				}
				 
			 });
			 reserveButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						reserveFrame.setVisible(true);
						mainFrame.setVisible(false);
					}
					 
				 });
			 pickupButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						pickupFrame.setVisible(true);
						mainFrame.setVisible(false);
					}
					
				 });
			 printButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						printFrame.setVisible(true);
						mainFrame.setVisible(false);
					}
					 
				 });
			 mainFrame.add(mainPanel);
			 mainFrame.setSize(600,400);
			 mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 //ride 
			 //ride start
			 mwlRideStart.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					startLocation = "MWL";
					rideFrame.add(ridePanelEnd);
					ridePanelStart.setVisible(false);
				}
				 
			 }); 
			 muRideStart.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						startLocation = "MU";
						rideFrame.add(ridePanelEnd);
						ridePanelStart.setVisible(false);
					}
					 
				 }); 
			 stateRideStart.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						startLocation = "State";
						rideFrame.add(ridePanelEnd);
						ridePanelStart.setVisible(false);
					}
					 
				 });
			 parksRideStart.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						startLocation = "Parks";
						rideFrame.add(ridePanelEnd);
						ridePanelStart.setVisible(false);
					}
					 
				 });
			 kildeeRideStart.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						startLocation = "Kildee";
						rideFrame.add(ridePanelEnd);
						ridePanelStart.setVisible(false);
					}
					 
				 });
			 freddyRideStart.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						startLocation = "Freddy";
						rideFrame.add(ridePanelEnd);
						ridePanelStart.setVisible(false);
					}
					 
				 });
			 ridePanelStart.add(mwlRideStart);
			 ridePanelStart.add(muRideStart);
			 ridePanelStart.add(stateRideStart);
			 ridePanelStart.add(parksRideStart);
			 ridePanelStart.add(kildeeRideStart);
			 ridePanelStart.add(freddyRideStart);
			 ridePanelStart.setVisible(true);
			 //ride end
			 mwlRideEnd.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						endLocation = "MWL";
						try {
							isRide = true;
							distance = getTripEstimation(startLocation, endLocation, conn);
							paymentMethod = getPaymentMethod(username, conn);
							price = distance *2;
							scooterID = getScooterID(isRide, username, startLocation, conn);
							updateUser("isRide", price, username, scooterID, startLocation, endLocation, conn);
							System.out.println("The trip from "+startLocation+" to "+endLocation+" will cost $"+price+"0 and will be charged to "+paymentMethod);
							System.out.println("Press any letter and press enter to end the program. When it says Ending, the program has finished.");
						} catch (SQLException e) {
							
							e.printStackTrace();
						}
						rideFrame.setVisible(false);
					}
					 
				 }); 
			 muRideEnd.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						endLocation = "MU";
						try {
							isRide = true;
							distance = getTripEstimation(startLocation, endLocation, conn);
							paymentMethod = getPaymentMethod(username, conn);							
							price = distance *2;
							scooterID = getScooterID(isRide, username, startLocation, conn);
							updateUser("isRide", price, username, scooterID, startLocation, endLocation, conn);
							System.out.println("The trip from "+startLocation+" to "+endLocation+" will cost $"+price+"0 and will be charged to "+paymentMethod);
							System.out.println("Press any letter and press enter to end the program. When it says Ending, the program has finished.");
						} catch (SQLException e) {
							
							e.printStackTrace();
						}
						rideFrame.setVisible(false);
					}
					 
				 }); 
			 stateRideEnd.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						endLocation = "State";
						try {
							isRide = true;
							distance = getTripEstimation(startLocation, endLocation, conn);
							price = distance *2;
							paymentMethod = getPaymentMethod(username, conn);
							scooterID = getScooterID(isRide, username, startLocation, conn);
							updateUser("isRide", price, username, scooterID, startLocation, endLocation, conn);
							System.out.println("The trip from "+startLocation+" to "+endLocation+" will cost $"+price+"0 and will be charged to "+paymentMethod);
							System.out.println("Press any letter and press enter to end the program. When it says Ending, the program has finished.");
						} catch (SQLException e) {
							
							e.printStackTrace();
						}
						rideFrame.setVisible(false);
					}
					 
				 }); 
			 parksRideEnd.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						endLocation = "Parks";
						try {
							isRide = true;
							distance = getTripEstimation(startLocation, endLocation, conn);
							price = distance *2;
							paymentMethod = getPaymentMethod(username, conn);
							scooterID = getScooterID(isRide, username, startLocation, conn);
							updateUser("isRide", price, username, scooterID, startLocation, endLocation, conn);
							System.out.println("The trip from "+startLocation+" to "+endLocation+" will cost $"+price+"0 and will be charged to "+paymentMethod);
							System.out.println("Press any letter and press enter to end the program. When it says Ending, the program has finished.");
						} catch (SQLException e) {
							
							e.printStackTrace();
						}
						rideFrame.setVisible(false);
					}
					 
				 }); 
			 kildeeRideEnd.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						endLocation = "Kildee";
						try {
							isRide = true;
							distance = getTripEstimation(startLocation, endLocation, conn);
							price = distance *2;
							paymentMethod = getPaymentMethod(username, conn);
							scooterID = getScooterID(isRide, username, startLocation, conn);
							updateUser("isRide", price, username, scooterID, startLocation, endLocation, conn);
							System.out.println("The trip from "+startLocation+" to "+endLocation+" will cost $"+price+"0 and will be charged to "+paymentMethod);
							System.out.println("Press any letter and press enter to end the program. When it says Ending, the program has finished.");
						} catch (SQLException e) {
							
							e.printStackTrace();
						}
						rideFrame.setVisible(false);
					}
					 
				 }); 
			 freddyRideEnd.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						endLocation = "Freddy";
						try {
							isRide = true;
							distance = getTripEstimation(startLocation, endLocation, conn);
							price = distance *2;
							paymentMethod = getPaymentMethod(username, conn);
							scooterID = getScooterID(isRide, username, startLocation, conn);
							updateUser("isRide", price, username, scooterID, startLocation, endLocation, conn);
							System.out.println("The trip from "+startLocation+" to "+endLocation+" will cost $"+price+"0 and will be charged to "+paymentMethod);
							System.out.println("Press any letter and press enter to end the program. When it says Ending, the program has finished.");
						} catch (SQLException e) {
							
							e.printStackTrace();
						}
						rideFrame.setVisible(false);
					}
					 
				 }); 
			 ridePanelEnd.add(mwlRideEnd);
			 ridePanelEnd.add(muRideEnd);
			 ridePanelEnd.add(stateRideEnd);
			 ridePanelEnd.add(kildeeRideEnd);
			 ridePanelEnd.add(parksRideEnd);
			 ridePanelEnd.add(freddyRideEnd);
			 rideFrame.add(ridePanelStart);
			 rideFrame.setSize(600,400);
			 rideFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 //reserve
			 mwlRes.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						try {
							isRide = true;
							startLocation = "MWL";
							scooterID = getScooterID(isRide, username, startLocation, conn);
							System.out.println("Your reserved Scooter "+scooterID+" is at "+startLocation);
							updateUser("isReserve", 0.0, username, scooterID, startLocation, endLocation, conn);	
							System.out.println("Press any letter and press enter to end the program. When it says Ending, the program has finished.");
						} catch (SQLException e) {
							e.printStackTrace();
						}
						reserveFrame.setVisible(false);				
					}
					 
				 }); 
			 muRes.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						try {
							isRide = true;
							startLocation = "MWL";
							scooterID = getScooterID(isRide, username, startLocation, conn);
							System.out.println("Your reserved Scooter "+scooterID+" is at "+startLocation);
							updateUser("isReserve", 0.0, username, scooterID, startLocation, endLocation, conn);	
							System.out.println("Press any letter and press enter to end the program. When it says Ending, the program has finished.");
						} catch (SQLException e) {
							e.printStackTrace();
						}	
						reserveFrame.setVisible(false);			
					}
					 
				 }); 
			 stateRes.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						try {
							isRide = true;
							startLocation = "MWL";
							scooterID = getScooterID(isRide, username, startLocation, conn);
							System.out.println("Your reserved Scooter "+scooterID+" is at "+startLocation);
							updateUser("isReserve", 0.0, username, scooterID, startLocation, endLocation, conn);	
							System.out.println("Press any letter and press enter to end the program. When it says Ending, the program has finished.");
						} catch (SQLException e) {
							e.printStackTrace();
						}
						reserveFrame.setVisible(false);			
					}
					 
				 }); 
			 parksRes.addActionListener(new ActionListener() {


					@Override
					public void actionPerformed(ActionEvent arg0) {
						try {
							isRide = true;
							startLocation = "MWL";
							scooterID = getScooterID(isRide, username, startLocation, conn);
							System.out.println("Your reserved Scooter "+scooterID+" is at "+startLocation);
							updateUser("isReserve", 0.0, username, scooterID, startLocation, endLocation, conn);	
							System.out.println("Press any letter and press enter to end the program. When it says Ending, the program has finished.");
						} catch (SQLException e) {
							e.printStackTrace();
						}
						reserveFrame.setVisible(false);			
					}
					 
				 }); 
			 kildeeRes.addActionListener(new ActionListener() {


					@Override
					public void actionPerformed(ActionEvent arg0) {
						try {
							isRide = true;
							startLocation = "MWL";
							scooterID = getScooterID(isRide, username, startLocation, conn);
							System.out.println("Your reserved Scooter "+scooterID+" is at "+startLocation);
							updateUser("isReserve", 0.0, username, scooterID, startLocation, endLocation, conn);	
							System.out.println("Press any letter and press enter to end the program. When it says Ending, the program has finished.");
						} catch (SQLException e) {
							e.printStackTrace();
						}
						reserveFrame.setVisible(false);			
					}
					 
				 }); 
			 freddyRes.addActionListener(new ActionListener() {


					@Override
					public void actionPerformed(ActionEvent arg0) {
						try {
							isRide = true;
							startLocation = "MWL";
							scooterID = getScooterID(isRide, username, startLocation, conn);
							System.out.println("Your reserved Scooter "+scooterID+" is at "+startLocation);
							updateUser("isReserve", 0.0, username, scooterID, startLocation, endLocation, conn);	
							System.out.println("Press any letter and press enter to end the program. When it says Ending, the program has finished.");
						} catch (SQLException e) {
							e.printStackTrace();
						}
						reserveFrame.setVisible(false);			
					}
					 
				 }); 
			 reservePanel.add(reserveSelection);
			 reservePanel.add(mwlRes);
			 reservePanel.add(muRes);
			 reservePanel.add(stateRes);
			 reservePanel.add(parksRes);
			 reservePanel.add(kildeeRes);
			 reservePanel.add(freddyRes);
			 reserveFrame.add(reservePanel);
			 reserveFrame.setSize(600,400);
			 reserveFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 //pickup
			 mwlPick.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						endLocation = "MWL";
						try {
							isPickup = true;
							startLocation = getStartLocation(username, conn);
							scooterID = getScooterID(isPickup, username, startLocation, conn);
							distance = getTripEstimation(startLocation, endLocation, conn);
							paymentMethod = getPaymentMethod(username, conn);
							price = distance *2;
							System.out.println("Your reserved Scooter "+scooterID+" is at "+startLocation);
							updateUser("isPickup", price, username, scooterID, startLocation, endLocation, conn);	
							System.out.println("The trip from "+startLocation+" to "+endLocation+" will cost $"+price+"0 and will be charged to "+paymentMethod);
							System.out.println("Press any letter and press enter to end the program. When it says Ending, the program has finished.");
						} catch (SQLException e) {
							
							e.printStackTrace();
						}
						pickupFrame.setVisible(false);
						
						
					}
					 
				 }); 
			 muPick.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						endLocation = "MU";
						try {
							isPickup = true;
							startLocation = getStartLocation(username, conn);
							scooterID = getScooterID(isPickup, username, startLocation, conn);
							distance = getTripEstimation(startLocation, endLocation, conn);
							paymentMethod = getPaymentMethod(username, conn);
							price = distance *2;
							System.out.println("Your reserved Scooter "+scooterID+" is at "+startLocation);
							updateUser("isPickup", price, username, scooterID, startLocation, endLocation, conn);	
							System.out.println("The trip from "+startLocation+" to "+endLocation+" will cost $"+price+"0 and will be charged to "+paymentMethod);
							System.out.println("Press any letter and press enter to end the program. When it says Ending, the program has finished.");
						} catch (SQLException e) {
							
							e.printStackTrace();
						}
						pickupFrame.setVisible(false);
					}
					 
				 }); 
			 statePick.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						endLocation = "State";
						try {
							isPickup = true;
							startLocation = getStartLocation(username, conn);
							scooterID = getScooterID(isPickup, username, startLocation, conn);
							distance = getTripEstimation(startLocation, endLocation, conn);
							paymentMethod = getPaymentMethod(username, conn);
							price = distance *2;
							System.out.println("Your reserved Scooter "+scooterID+" is at "+startLocation);
							updateUser("isPickup", price, username, scooterID, startLocation, endLocation, conn);	
							System.out.println("The trip from "+startLocation+" to "+endLocation+" will cost $"+price+"0 and will be charged to "+paymentMethod);
							System.out.println("Press any letter and press enter to end the program. When it says Ending, the program has finished.");
						} catch (SQLException e) {
							
							e.printStackTrace();
						}
						pickupFrame.setVisible(false);
					}
					 
				 }); 
			 parksPick.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						endLocation = "Parks";
						try {
							isPickup = true;
							startLocation = getStartLocation(username, conn);
							scooterID = getScooterID(isPickup, username, startLocation, conn);
							distance = getTripEstimation(startLocation, endLocation, conn);
							paymentMethod = getPaymentMethod(username, conn);
							price = distance *2;
							System.out.println("Your reserved Scooter "+scooterID+" is at "+startLocation);
							updateUser("isPickup", price, username, scooterID, startLocation, endLocation, conn);
							System.out.println("The trip from "+startLocation+" to "+endLocation+" will cost $"+price+"0 and will be charged to "+paymentMethod);
							System.out.println("Press any letter and press enter to end the program. When it says Ending, the program has finished.");
						} catch (SQLException e) {
							
							e.printStackTrace();
						}
						pickupFrame.setVisible(false);
					}
					 
				 }); 
			 kildeePick.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						endLocation = "Kildee";
						try {
							isPickup = true;
							startLocation = getStartLocation(username, conn);
							scooterID = getScooterID(isPickup, username, startLocation, conn);
							distance = getTripEstimation(startLocation, endLocation, conn);
							paymentMethod = getPaymentMethod(username, conn);
							price = distance *2;
							System.out.println("Your reserved Scooter "+scooterID+" is at "+startLocation);
							updateUser("isPickup", price, username, scooterID, startLocation, endLocation, conn);	
							System.out.println("The trip from "+startLocation+" to "+endLocation+" will cost $"+price+"0 and will be charged to "+paymentMethod);
							System.out.println("Press any letter and press enter to end the program. When it says Ending, the program has finished.");
						} catch (SQLException e) {
							
							e.printStackTrace();
						}
						pickupFrame.setVisible(false);
					}
					 
				 }); 
			 freddyPick.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						endLocation = "Freddy";
						try {
							isPickup = true;
							startLocation = getStartLocation(username, conn);
							scooterID = getScooterID(isPickup, username, startLocation, conn);
							distance = getTripEstimation(startLocation, endLocation, conn);
							paymentMethod = getPaymentMethod(username, conn);
							price = distance *2;
							System.out.println("Your reserved Scooter "+scooterID+" is at "+startLocation);
							updateUser("isPickup", price, username, scooterID, startLocation, endLocation, conn);	
							System.out.println("The trip from "+startLocation+" to "+endLocation+" will cost $"+price+"0 and will be charged to "+paymentMethod);
							System.out.println("");
							System.out.println("Press any letter and press enter to end the program. When it says Ending, the program has finished.");
						} catch (SQLException e) {
							
							e.printStackTrace();
						}
						pickupFrame.setVisible(false);
					}
					 
				 }); 
			 pickupPanel.add(pickupDestSelection);
			 pickupPanel.add(mwlPick);
			 pickupPanel.add(muPick);
			 pickupPanel.add(statePick);
			 pickupPanel.add(kildeePick);
			 pickupPanel.add(statePick);
			 pickupPanel.add(freddyPick);
			 pickupFrame.add(pickupPanel);
			 pickupFrame.setSize(600,400);
			 pickupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 //print
			 printPanel.add(printPrint);
			 printPrint.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						try {
							print(username, conn);
						} catch (FileNotFoundException | SQLException e) {
							
							e.printStackTrace();
						}
						printFrame.setVisible(false);
					}
					 
				 }); 
			 printFrame.add(printPanel);
			 printFrame.setSize(600,400);
			 printFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 Scanner sc = new Scanner(System.in);
			
			
			String z = sc.next();
			System.out.println("Ending");
			updateBatteries(scooterID, distance, conn);
			// end while runMain
			in.close();
			inDist.close();
			PrintWriter write = new PrintWriter("UpdatedScooterDatabase.txt");
			ResultSet rsPrint = stat.executeQuery("SELECT * FROM Scooters");
			while (rsPrint.next()) {
				int i = 1;
				write.println(rsPrint.getString(i) + " " + rsPrint.getString(i + 1) + " " + rsPrint.getString(i + 2)
						+ " " + rsPrint.getString(i + 3));
				i++;
			}
			write.close();
			PrintWriter writeUser = new PrintWriter("UpdatedUserDatabase.txt");
			ResultSet rsPrintUser = stat.executeQuery("SELECT * FROM Users");
			while (rsPrintUser.next()) {
				int i = 1;
				writeUser.println(rsPrintUser.getString(i) + " " + rsPrintUser.getString(i + 1) + " "
						+ rsPrintUser.getString(i + 2) + " " + rsPrintUser.getString(i + 3) + " "
						+ rsPrintUser.getString(i + 4) + " " + rsPrintUser.getString(i + 5) + " "
						+ rsPrintUser.getString(i + 6) + " " + rsPrintUser.getString(i + 7));
				i++;
			}
			writeUser.close();

			PrintWriter writeLogin = new PrintWriter("UpdatedLoginDatabase.txt");
			ResultSet rsPrintLogin = stat.executeQuery("SELECT * FROM Login");
			while (rsPrintLogin.next()) {
				username = rsPrintLogin.getString(1);
				writeLogin.println(username);
			}
			writeLogin.close();
			rewriteFile("UpdatedScooterDatabase.txt", "ScooterDatabase.txt");
			rewriteFile("UpdatedUserDatabase.txt", "UserDatabase.txt");
			rewriteFile("UpdatedLoginDatabase.txt", "LoginDatabase.txt");
		System.exit(0);
		} // end Main try
	
	}// end main method

	

	
	public static String getPaymentMethod(String username, Connection conn) throws SQLException {
		Statement stat = conn.createStatement();
		String paymentMethod = "";
		ResultSet getCardNumber = stat.executeQuery("SELECT * FROM Users WHERE Username = '"+username+"'");
		while(getCardNumber.next()) {
			paymentMethod = getCardNumber.getString(4);
		}
		return paymentMethod;
	}
	
	public static boolean checkLogin(String password, String username, Connection conn) throws SQLException{
		Statement stat = conn.createStatement();
		String checkedUsername = ".";
			 ResultSet checkPassword = stat.executeQuery("SELECT * FROM Users WHERE Username = '"+username+"'");
				while (checkPassword.next()) {
					checkedUsername = checkPassword.getString(1);
					password = checkPassword.getString(2);
					//System.out.println(password + " 1");
				}
			 if(password.equals(checkedPassword) && checkedUsername.equals(username)) {
				 System.out.println(username+" is Logged In.");
				 
				 return true;
				
			 } else {
				 System.out.println("Incorrect Username or Password. Try Again.");
				 System.exit(0);
				 return false;
			 }
			
			 
		
	}
	public static boolean signup(String password, String username, String paymentMethod, Connection conn) throws SQLException{
		Statement stat = conn.createStatement();
		
		String checkedUsername = ".";
		ResultSet checkNewUsername = stat.executeQuery("SELECT * FROM Users");
		while(checkNewUsername.next()) {
			checkedUsername = checkNewUsername.getString(1);
			if(checkedUsername.equals(username)) { //username already exists
				System.out.println(username+" already exists. Please try again.");
				System.exit(0);
				return false;
				
			}
		}
		
			stat.execute(
						"INSERT INTO Users(Username, Password, NumberOfRides, PaymentMethod, TotalPayment, Reservation, Title, Status) VALUES ('"
								+ username + "', '" + password + "', " + 0 + ", '"
								+ paymentMethod + "', " + 0.0 + ", '" + "0000000000" + "', '"
								+ "user" + "', '" + "out" + "')");
			return true;
	}
	
	public static void updateBatteries(String scooterID, Double distance, Connection conn) throws SQLException {
		Double chargeDecrease = 0.0;
		Statement stat = conn.createStatement();
		if (distance > 1.5) {
			chargeDecrease = 0.23;
		} else if (distance >= 1.0) {
			chargeDecrease = 0.18;
		} else if (distance >= 0.7) {
			chargeDecrease = 0.13;
		} else {
			chargeDecrease = 0.08;
		}
		stat.execute("UPDATE Scooters SET CurrBattCharge = CurrBattCharge - '"+chargeDecrease+"' WHERE ScooterID = '"+scooterID+"'");
		stat.execute("UPDATE Scooters SET CurrBattCharge = CurrBattCharge + 0.01 WHERE CurrBattCharge <= 0.99");
	}

	public static String getScooterID(boolean task, String username, String startLocation, Connection conn) throws SQLException {
		Statement stat = conn.createStatement();
		String scooterID = "";
		if(task = isRide) {
			ResultSet rsGetScooterID = stat.executeQuery("SELECT * FROM Scooters WHERE ScooterLocation = '"+startLocation+"' AND IsReserved = 'no' AND CurrBattCharge > .33");
			if(rsGetScooterID.next()) {
				scooterID = rsGetScooterID.getString(1);
			}
		} else if(task = isPickup) {
			ResultSet rsGetScooterID = stat.executeQuery("SELECT * FROM Users WHERE username = '"+username+"'");
			if(rsGetScooterID.next()) {
				scooterID = rsGetScooterID.getString(6);
			}
		}
		return scooterID;
	}

	
	public static void print(String username, Connection conn) throws SQLException, FileNotFoundException {
		Statement stat = conn.createStatement();
		String title = "";
		String formatLine = "-------------------------------------------------------------------------------";
		ResultSet rsCheckTitle = stat
				.executeQuery("SELECT * FROM Users WHERE Username = '" + username + "'");
		while (rsCheckTitle.next()) {
			title = rsCheckTitle.getString(7);
		}
		System.out.printf("%97s", "Printing " + title + " report");
		System.out.println();
		if (title.equals("manager")) { // run print as manager
			PrintWriter write = new PrintWriter("ManagerReport.txt");
			ResultSet rsPrint = stat.executeQuery("SELECT * FROM Users");
			System.out.printf("%121s", formatLine);
			System.out.println();
			System.out.printf("%44s", "|");
			System.out.printf("%16s |", "Username");
			System.out.printf("%25s |", "Total Rides");
			System.out.printf("%30s |", "Total Payment");
			System.out.println();
			System.out.printf("%121s", formatLine);
			System.out.println();
			while (rsPrint.next()) {
				System.out.printf("%44s", "|");
				System.out.printf("%16s |", rsPrint.getString(1));
				System.out.printf("%25s |", rsPrint.getString(3));
				System.out.printf("%30s |", rsPrint.getString(5));
				System.out.println();
				System.out.printf("%121s", formatLine);
				System.out.println();
			}
			System.out.println();
			System.out.println("Press any letter and press enter to end the program. When it says Ending, the program has finished.");
			write.print(rsPrint);
			write.close();
		} // end if manager
		if (title.equals("user")) {
			PrintWriter write = new PrintWriter("ManagerReport.txt");
			ResultSet rsPrint = stat.executeQuery("SELECT * FROM Users WHERE Username = '" + username + "'");
			System.out.printf("%121s", formatLine);
			System.out.println();
			System.out.printf("%44s", "|");
			System.out.printf("%16s |", "Username");
			System.out.printf("%25s |", "Total Rides");
			System.out.printf("%30s |", "Total Payment");
			System.out.println();
			System.out.printf("%121s", formatLine);
			System.out.println();
			while (rsPrint.next()) {
				System.out.printf("%44s", "|");
				System.out.printf("%16s |", rsPrint.getString(1));
				System.out.printf("%25s |", rsPrint.getString(3));
				System.out.printf("%30s |", rsPrint.getString(5));
				System.out.println();
				System.out.printf("%121s", formatLine);
				System.out.println();
			}
			System.out.println();
			System.out.println("Press any letter and press enter to end the program. When it says Ending, the program has finished.");
			write.print(rsPrint);
			write.close();
			
		}
		
		
	}
	
	
	public static String updateUser(String task, Double price, String username, String scooterID, String startLocation, String endLocation, Connection conn) throws SQLException {
		
		Statement stat = conn.createStatement();
		if(task.equals("isRide")) {
			stat.execute("UPDATE Users SET NumberOfRides = NumberOfRides + 1 WHERE Username = '"+username+"'");
			stat.execute("UPDATE Users SET TotalPayment = TotalPayment + "+price+" WHERE Username = '"+username+"'");
			stat.execute("UPDATE Scooters SET ScooterLocation = '"+endLocation+"' WHERE ScooterID = '"+scooterID+"'");
		} else if(task.equals("isReserve")) {
			stat.execute("UPDATE Users SET Reservation = '"+scooterID+"' WHERE Username = '"+username+"'");
			stat.execute("UPDATE Scooters SET IsReserved = 'yes' WHERE ScooterID = '"+scooterID+"'");
		} else if(task.equals("isPickup")) {
			stat.execute("UPDATE Users SET Reservation = '0000000000' WHERE Username = '"+username+"'");
			stat.execute("UPDATE Users SET NumberOfRides = NumberOfRides + 1 WHERE Username = '"+username+"'");
			stat.execute("UPDATE Users SET TotalPayment = TotalPayment + "+price+" WHERE Username = '"+username+"'");
			stat.execute("UPDATE Scooters SET ScooterLocation = '"+endLocation+"' WHERE ScooterID = '"+scooterID+"'");
			stat.execute("UPDATE Scooters SET IsReserved = 'no' WHERE ScooterID = '"+scooterID+"'");
		} else {
			System.out.println("MASSIVE ERROR");
		}
		return reservedScooterID;
	}

	public static String getStartLocation(String username, Connection conn) throws SQLException {
		Statement stat = conn.createStatement();
		String startLocation = "";
		String reservedScooterID = "";
		ResultSet getScooterID = stat.executeQuery("SELECT * FROM Users WHERE Username = '"+username+"'");
		while(getScooterID.next()) {
			reservedScooterID = getScooterID.getString(6);
		}
		if(reservedScooterID.equals("0000000000")) {
			System.out.println("You have no Scooter Reservation. To reserve a Scooter, login and select Reserve.");
			System.exit(0);
		}
		ResultSet getLocation = stat.executeQuery("SELECT * FROM Scooters WHERE ScooterID = '"+reservedScooterID+"'");
		while(getLocation.next()) {
			startLocation = getLocation.getString(3);
		}
		
		return startLocation;
	}
	
	public static String checkAvailability(String scooterLocation, Connection conn) throws SQLException {
		Statement stat = conn.createStatement();
		String scooterID = "";
		ResultSet checkAvailability = stat.executeQuery("SELECT * FROM Scooters WHERE ScooterLocation = '"+scooterLocation+"' AND IsReserved = 'no' AND CurrBattCharge > .33");
		if (checkAvailability.next()) {
			scooterID = checkAvailability.getString(1);
		} else {
			System.out.println("Sorry, there are no scooters available at "+scooterLocation+". Please try again later.");
			System.exit(0);
		}
		
		return scooterID;
	}


	public static double getTripEstimation(String startLocation, String endLocation, Connection conn) throws SQLException {
		Statement stat = conn.createStatement();
		 ResultSet getTripLength = stat.executeQuery("SELECT Distance FROM Distances WHERE StartLocation = '"+startLocation+"' AND EndLocation = '"+endLocation+"'");
		 while(getTripLength.next()) {
			 distance = getTripLength.getDouble(1);
		 }
		 price = distance * 2;
		
		 return distance;
	}

	public static void rewriteFile(String fileToRead, String fileToWrite) throws IOException {
		String output = "no";
		File inFile = new File(fileToRead); // original
		Scanner fileIn = new Scanner(inFile);
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileToWrite)); // writes to fileToWrite
		while (fileIn.hasNextLine()) {
			output = fileIn.nextLine(); // reads file into output
			writer.write(output);
			writer.newLine();
		}
		writer.close();
		fileIn.close();
	} // close rewriteFile

}// close class
