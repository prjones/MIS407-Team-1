import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

@SuppressWarnings("unused")
public class UserInterface {

	@SuppressWarnings("resource")
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
			// code from Troy starts
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
			if (checkUsername.equalsIgnoreCase("noone")) {
			} else {
				login = false;
			}
			ArrayList<String> availableScooters = new ArrayList<String>();
			boolean runMain = false;
			boolean isRide = false;
			boolean isReserve = false;
			boolean isPickup = false;
			boolean isPrint = false;
			// boolean login = true;
			String formatLine = "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
			String result;
			String destStation;
			String password = "";
			String paymentMethod = "xxxxxxxxxxxxxxxx";
			String noReservation = "0000000000";
			String newUserTitle = "user";
			String newUserStatus = "out";
			int numberOfRides = 0;
			Double totalPayment = 0.00;
			Scanner logon = new Scanner(System.in);
			// Start login screen
			System.out.println(formatLine + "\n \n \n \n");
			System.out.printf("%100s", "Welcome to Ryder! \n \n \n \n"); // format later
			System.out.println(formatLine);
			Thread.sleep(2000);
			boolean runCheck = true;
			String username = "noone";
			ResultSet getUsername = stat.executeQuery("SELECT * FROM Login");
			while (getUsername.next()) {
				username = getUsername.getString(1);
			}

			while (login) {
				System.out.printf("%90s", "Enter Username: ");
				username = logon.next();
				boolean newUser = false;
				ResultSet rsCheckUser = stat.executeQuery(checkForUser(username)); // checks if user has an account
				try {
					if (rsCheckUser.next()) {
						System.out.print("");
					} else { // creates new user
						newUser = true;
						System.out.printf("%93s", "Welcome New User!");
						System.out.println();
						System.out.printf("%94s", "Enter New Password: ");
						password = logon.next();
						System.out.println();

						System.out.printf("%115s", "Would you like to enter a payment method now or later?");
						System.out.println();
						System.out.printf("%87s", "(now)");
						System.out.println();
						System.out.printf("%88s", "(later)");
						System.out.println();
						System.out.printf("%84s", " ");
						String response = logon.next();
						System.out.println();
						if (response.equalsIgnoreCase("later")) {
							System.out.printf("%105s", "Sounds good! We will ask for it before your first ride.");
							System.out.println();
							stat.execute(
									"INSERT INTO Users(Username, Password, NumberOfRides, PaymentMethod, TotalPayment, Reservation, Title, Status) VALUES ('"
											+ username + "', '" + password + "', " + numberOfRides + ", '"
											+ paymentMethod + "', " + totalPayment + ", '" + noReservation + "', '"
											+ newUserTitle + "', '" + newUserStatus + "')");
							login = false;
						} else if (response.equalsIgnoreCase("now")) {
							System.out.printf("%107s",
									"Please enter your 16 digit Card Number (formatted xxxxxxxxxxxxxxxx): ");
							paymentMethod = logon.next();
							System.out.println();
							stat.execute(
									"INSERT INTO Users(Username, Password, NumberOfRides, PaymentMethod, TotalPayment, Reservation, Title, Status) VALUES ('"
											+ username + "', '" + password + "', " + numberOfRides + ", '"
											+ paymentMethod + "', " + totalPayment + ", '" + noReservation + "', '"
											+ newUserTitle + "', '" + newUserStatus + "')");
							System.out.printf("%131s",
									"Thank you! We've added it to your account. Don't worry, we'll keep it safe!");
							login = false;
							
						} else {
							System.out.printf("%85s", "Oops! Please retype in your answer"); // come back maybe???
						}
					}
				} catch (IndexOutOfBoundsException e) {
					System.out.println("Error");
					System.out.println(e.getMessage());
					System.exit(0);
				}
				while (!newUser) { // sign user in
					System.out.printf("%90s", "Enter password: ");
					password = logon.next();
					System.out.println();
					String passwordCheck = "";
					String query = checkPassword(password, username);
					ResultSet checkPassword = stat.executeQuery(query);
					while (checkPassword.next()) {
						passwordCheck = checkPassword.getString(2);
					}
					if (password.equals(passwordCheck)) {
						System.out.printf("%90s", "Welcome Back!");
						stat.execute("UPDATE Users SET Status = 'in' WHERE Username = '" + username + "'"); // marks
																											// signed in
						login = false;
						newUser = true;
					} else {
						System.out.printf("%95s", "Incorrect password.");
						System.out.println();
						System.out.println();
					}
				} // end if!newUser
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
				System.out.println();
				writeUser.close();
				rewriteFile("UpdatedUserDatabase.txt", "UserDatabase.txt");
			} // end login screen

			for (int i = 0; i < 5; i++) {
				System.out.println("\n");
			}
			runMain = true;
			while (runMain) {
				Scanner inMain = new Scanner(System.in);
				System.out.printf("%92s", "Do you want to: ");
				System.out.println();
				System.out.printf("%87s", "(Ride)");
				System.out.println();
				System.out.printf("%89s", "(Reserve)");
				System.out.println();
				System.out.printf("%89s", "(Pick Up)");
				System.out.println();
				System.out.printf("%88s", "(Print)");
				System.out.println();
				System.out.printf("%80s", " ");
				result = inMain.nextLine();
				if (result.equalsIgnoreCase("Ride")) {
					isRide = true;
				}
				if (result.equalsIgnoreCase("Reserve")) {
					isReserve = true;
				}
				if (result.equalsIgnoreCase("Pick Up")) {
					isPickup = true;
				}
				if (result.equalsIgnoreCase("Print")) {
					isPrint = true;
				}

				if (isRide) { // if Ride

					for (int i = 0; i < 5; i++) {
						System.out.println("\n");
					}
					String location = LocationSelection.findStartingStation(); // selects nearest location
					System.out.printf("%99s", "--------------------------");
					System.out.println();
					System.out.printf("%97s", "Starting Station is: " + location);
					System.out.println();
					System.out.printf("%99s", "--------------------------");
					System.out.println();
					ResultSet rsCheckAv = stat.executeQuery(checkAvailability(location));
					try {
						if (rsCheckAv.next()) {
							availableScooters.add(rsCheckAv.getString(1));
						} else {
							System.out.print("");
						}
						String scooterID = availableScooters.get(0);
					} catch (IndexOutOfBoundsException e) {
						System.out.printf("%80s", "Sorry, there are no available scooters at " + location + ".");
						System.exit(0);
					}
					if (rsCheckAv.next()) {
						availableScooters.add(rsCheckAv.getString(1));
					} else {
						System.out.print("");
					}
					ResultSetMetaData rsmdCheckAvRide = rsCheckAv.getMetaData();
					int columnsNumberRide = rsmdCheckAvRide.getColumnCount();
					while (rsCheckAv.next()) {
						for (int i = 1; i < columnsNumberRide - 1; i++) {
							availableScooters.add(rsCheckAv.getString(1));
						}
					}
					System.out.printf("%99s", "Where is your destination?");
					System.out.println();
					System.out.printf("%87s", "(MWL)");
					System.out.println();
					System.out.printf("%88s", "(Parks)");
					System.out.println();
					System.out.printf("%88s", "(State)");
					System.out.println();
					System.out.printf("%88s", "(Kildee)");
					System.out.println();
					System.out.printf("%86s", "(MU)");
					System.out.println();
					System.out.printf("%88s", "(Freddy)");
					System.out.println();
					System.out.printf("%86s", " ");
					destStation = inMain.nextLine();
					String scooterID = availableScooters.get(0);
					Double chargeDecrease = 0.00;
					Double distance = 0.0;
					Double price = 0.00;
					ResultSet rsTripEst = stat.executeQuery(getTripEstimation(location, destStation));
					if (rsTripEst.next()) {
						distance = rsTripEst.getDouble(1);
						price = distance * 2;
					}
					ResultSetMetaData rsmdTripEst = rsTripEst.getMetaData();
					int columnsNumber2 = rsmdTripEst.getColumnCount();
					while (rsTripEst.next()) {
						for (int i = 1; i < columnsNumber2 - 1; i++)
							System.out.print(rsTripEst.getString(1));
						System.out.println();
					}
					String cardNumber = "";
					ResultSet checkPayment = stat
							.executeQuery("SELECT * FROM Users WHERE Username = '" + username + "'");
					while (checkPayment.next()) {
						cardNumber = checkPayment.getString(4);
					}
					if (cardNumber.equals("xxxxxxxxxxxxxxxx")) {
						System.out.printf("%107s",
								"Please enter your 16 digit Card Number (formatted xxxxxxxxxxxxxxxx): ");
						cardNumber = inMain.next();
						System.out.println();
						stat.execute("UPDATE Users SET PaymentMethod = '" + cardNumber + "' WHERE Username = '"
								+ username + "'");
					}
					System.out.printf("%108s",
							"Your trip from " + location + " to " + destStation + " will cost: $" + price + "0");
					System.out.println();
					System.out.printf("%100s", "Your ScooterID is: " + availableScooters.get(0));
					System.out.println();
					System.out.printf("%95s", "Have a safe ride!");
					boolean hasResultSet = stat.execute(updateLocation(destStation, scooterID)); // updateLocation
					if (distance > 1.5) {
						chargeDecrease = 0.21;
					} else if (distance >= 1.0) {
						chargeDecrease = 0.16;
					} else if (distance >= 0.7) {
						chargeDecrease = 0.11;
					} else {
						chargeDecrease = 0.08;
					}
					stat.execute(
							"UPDATE Users SET NumberOfRides = NumberOfRides + 1 WHERE Username = '" + username + "'");
					stat.execute("UPDATE Users SET TotalPayment = TotalPayment + " + price + " WHERE Username = '"
							+ username + "'");
					stat.execute("UPDATE Scooters Set CurrBattCharge = CurrBattCharge - " + chargeDecrease
							+ " WHERE ScooterID = '" + scooterID + "'");
					Scanner loginCheck = new Scanner(System.in);
					System.out.println();
					System.out.printf("%98s", "Do you want to sign out? Y/N");
					System.out.println();
					System.out.printf("%84s", " ");
					String loginVar = loginCheck.next();
					if (loginVar.equalsIgnoreCase("Y")) { // log out
						stat.execute("UPDATE Login SET LoginUsername = 'noone'");
						stat.execute("UPDATE Users SET Status = 'out'");
					} else if (loginVar.equalsIgnoreCase("N")) { // stay logged in
						stat.execute("Update Login SET LoginUsername = '" + username + "'");
					}
					loginCheck.close();
					runMain = false;
				} // end if Ride
				if (isReserve) { // if reserve
					System.out.printf("%108s", "Where would you like to pick up a scooter?");
					System.out.println();
					System.out.printf("%87s", "(MWL)");
					System.out.println();
					System.out.printf("%88s", "(Parks)");
					System.out.println();
					System.out.printf("%88s", "(State)");
					System.out.println();
					System.out.printf("%88s", "(Kildee)");
					System.out.println();
					System.out.printf("%86s", "(MU)");
					System.out.println();
					System.out.printf("%88s", "(Freddy)");
					System.out.println();
					System.out.printf("%86s", " ");
					String resStart = inMain.next();
					String reserved = "Reserved";
					ResultSet rsCheckAvRes = stat.executeQuery(checkAvailability(resStart));
					try {
						if (rsCheckAvRes.next()) {
							availableScooters.add(rsCheckAvRes.getString(1));
						} else {
							System.out.print("");
						}
						String scooterID = availableScooters.get(0);
					} catch (IndexOutOfBoundsException e) {
						System.out.printf("%80s",
								"Sorry, there are no available scooters at " + resStart + ". Try reserving later!");
						System.exit(0);
					}
					if (rsCheckAvRes.next()) {
						availableScooters.add(rsCheckAvRes.getString(1));
					} else {
						System.out.print("");
					}
					ResultSetMetaData rsmdCheckAvRes = rsCheckAvRes.getMetaData();
					int columnsNumberRes = rsmdCheckAvRes.getColumnCount();
					while (rsCheckAvRes.next()) {
						for (int i = 1; i < columnsNumberRes - 1; i++) {
							availableScooters.add(rsCheckAvRes.getString(1));
						}
					}
					String resScooterID = availableScooters.get(0);
					boolean hasResultSet = stat.execute(markReserved(resScooterID)); // Mark ScooterID as Reserved
					if (hasResultSet) {
						ResultSet result2 = stat.getResultSet();
					}
					PrintWriter write = new PrintWriter("UpdatedScooterDatabase.txt");
					ResultSet rsPrint = stat.executeQuery("SELECT * FROM Scooters");
					while (rsPrint.next()) {
						int i = 1;
						write.println(rsPrint.getString(i) + " " + rsPrint.getString(i + 1) + " "
								+ rsPrint.getString(i + 2) + " " + rsPrint.getString(i + 3));
						i++;
					}
					System.out.println();
					write.close();
					System.out.printf("%143s", "Your reserved ScooterID is " + resScooterID + " at the " + resStart
							+ " station. The Scooter is reserved on your account! See you then!");
					stat.execute("UPDATE Users SET Reservation = '" + resScooterID + "' WHERE Username = '" + username
							+ "'");
					Scanner loginCheck = new Scanner(System.in);
					System.out.println();
					System.out.printf("%98s", "Do you want to sign out? Y/N");
					System.out.println();
					System.out.printf("%84s", " ");
					String loginVar = loginCheck.next();
					if (loginVar.equalsIgnoreCase("Y")) { // log out
						stat.execute("UPDATE Login SET LoginUsername = 'noone'");
						stat.execute("UPDATE Users SET Status = 'out'");
					} else if (loginVar.equalsIgnoreCase("N")) { // stay logged in
						stat.execute("Update Login SET LoginUsername = '" + username + "'");
					}
					loginCheck.close();
					runMain = false;
				} // end if reserve

				if (isPickup) { // needs to ask for credit card info
					Scanner pickup = new Scanner(System.in);
					String scooterID = "";
					String startLocation = "";
					ResultSet rsCheckUserRes = stat
							.executeQuery("SELECT * FROM Users WHERE Username = '" + username + "'");
					while (rsCheckUserRes.next()) {
						scooterID = rsCheckUserRes.getString(6);
					}
					ResultSet rsCheckResStartLocation = stat
							.executeQuery("SELECT * FROM Scooters WHERE ScooterID = '" + scooterID + "'");
					while (rsCheckResStartLocation.next()) {
						startLocation = rsCheckResStartLocation.getString(3);
					}
					if (scooterID.equals("0000000000")) {
						System.out.printf("%102s", "You do not have a reserved scooter.");
						isPickup = false;
						System.exit(0); // come back to later
					}
					System.out.println();
					System.out.printf("%103s", "Your Reserved Scooter is at " + startLocation);
					System.out.println();
					System.out.printf("%99s", "Where is your destination?");
					System.out.println();
					System.out.printf("%87s", "(MWL)");
					System.out.println();
					System.out.printf("%88s", "(Parks)");
					System.out.println();
					System.out.printf("%88s", "(State)");
					System.out.println();
					System.out.printf("%88s", "(Kildee)");
					System.out.println();
					System.out.printf("%86s", "(MU)");
					System.out.println();
					System.out.printf("%88s", "(Freddy)");
					System.out.println();
					System.out.printf("%86s", " ");
					String destination = pickup.next();
					ResultSet rsFindResLocation = stat
							.executeQuery("SELECT * FROM Scooters WHERE ScooterID = '" + scooterID + "'");
					boolean whileFindRes = rsFindResLocation.next();
					while (whileFindRes) {
						String location = rsFindResLocation.getString(3);
						ResultSet rsCheckResLocation = stat.executeQuery(findReservationLocation(scooterID));
						Double chargeDecrease = 0.0;
						Double distance = 0.0; // gets price
						Double price = 0.00;
						ResultSet rsTripEst = stat.executeQuery(getTripEstimation(location, destination));
						if (rsTripEst.next()) {
							distance = rsTripEst.getDouble(1);
							price = distance * 2; // 4min per mile
						}
						if (distance > 1.5) {
							chargeDecrease = 0.23;
						} else if (distance >= 1.0) {
							chargeDecrease = 0.18;
						} else if (distance >= 0.7) {
							chargeDecrease = 0.13;
						} else {
							chargeDecrease = 0.08;
						}
						ResultSetMetaData rsmdTripEst = rsTripEst.getMetaData();
						int columnsNumber2 = rsmdTripEst.getColumnCount();
						while (rsTripEst.next()) {
							for (int i = 1; i < columnsNumber2 - 1; i++) {
								System.out.print(rsTripEst.getString(1));
							}
						}
						String cardNumber = "";
						ResultSet checkPayment = stat
								.executeQuery("SELECT * FROM Users WHERE Username = '" + username + "'");
						while (checkPayment.next()) {
							cardNumber = checkPayment.getString(4);
						}
						if (cardNumber.equals("xxxxxxxxxxxxxxxx")) {
							System.out.printf("%107s",
									"Please enter your 16 digit Card Number (formatted xxxxxxxxxxxxxxxx): ");
							cardNumber = inMain.next();
							System.out.println();
							stat.execute("UPDATE Users SET PaymentMethod = '" + cardNumber + "' WHERE Username = '"
									+ username + "'");
						}
						System.out.printf("%130s", "Your trip from " + location + " to " + destination + " will cost: $"
								+ price + "0" + ". It will be chararged to: " + cardNumber);
						System.out.println();
						System.out.printf("%93s", "Have a safe ride!");
						boolean hasResultSet = stat.execute(markUnreserved(scooterID, destination));
						if (hasResultSet) {
							System.out.println("line 454");
						}
						PrintWriter write = new PrintWriter("UpdatedScooterDatabase.txt");
						ResultSet rsPrint = stat.executeQuery("SELECT * FROM Scooters");
						while (rsPrint.next()) {
							write.println(rsPrint.getString(1) + " " + rsPrint.getString(2) + " " + rsPrint.getString(3)
									+ " " + rsPrint.getString(4));
						}
						System.out.println();
						stat.execute("UPDATE Users SET NumberOfRides = NumberOfRides + 1 WHERE Username = '" + username
								+ "'");
						stat.execute("UPDATE Users SET TotalPayment = TotalPayment + " + price + " WHERE Username = '"
								+ username + "'");
						stat.execute("UPDATE Users SET Reservation = '0000000000' WHERE Username = '" + username + "'");
						stat.execute("UPDATE Scooters Set CurrBattCharge = CurrBattCharge - " + chargeDecrease
								+ " WHERE ScooterID = '" + scooterID + "'");
						Scanner loginCheck = new Scanner(System.in);
						System.out.println();
						System.out.printf("%98s", "Do you want to sign out? Y/N");
						System.out.println();
						System.out.printf("%84s", " ");
						String loginVar = loginCheck.next();
						if (loginVar.equalsIgnoreCase("Y")) { // log out
							stat.execute("UPDATE Login SET LoginUsername = 'noone'");
							stat.execute("UPDATE Users SET Status = 'out'");
						} else if (loginVar.equalsIgnoreCase("N")) { // stay logged in
							stat.execute("Update Login SET LoginUsername = '" + username + "'");
						}
						loginCheck.close();
						write.close();
						pickup.close();
						runMain = false;
						whileFindRes = false;
					}
				} // end ifPickup
				if (isPrint) {
					String title = "";
					formatLine = "-------------------------------------------------------------------------------";
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
						write.print(rsPrint);
						write.close();
					}
					Scanner loginCheck = new Scanner(System.in);
					System.out.println();
					System.out.printf("%98s", "Do you want to sign out? Y/N");
					System.out.println();
					System.out.printf("%84s", " ");
					String loginVar = loginCheck.next();
					if (loginVar.equalsIgnoreCase("Y")) { // log out
						stat.execute("UPDATE Login SET LoginUsername = 'noone'");
						stat.execute("UPDATE Users SET Status = 'out'");
					} else if (loginVar.equalsIgnoreCase("N")) { // stay logged in
						stat.execute("Update Login SET LoginUsername = '" + username + "'");
					}
					loginCheck.close();
					runMain = false;
				} // end ifPrint
				stat.execute("UPDATE Scooters Set CurrBattCharge = CurrBattCharge + 0.01 WHERE CurrBattCharge < 1.00");
			} // end while runMain
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
		} // end Main try
		System.exit(0);
	}// end main method

	public static String checkForUser(String u) {
		String username = u; // username to check
		String blank = "SELECT Username FROM Users WHERE Username = ''"; // change Users to UserDatabase name
		String query = blank.substring(0, 45) + username + blank.substring(45);
		return query;
	}

	public static String checkPassword(String p, String u) {
		String password = p; // password to check
		String username = u; // username to check
		String blank = "SELECT * FROM Users WHERE Username = '' AND Password = ''"; // change Users to UserDatabase name
		String query = blank.substring(0, 38) + username + blank.substring(38, 56) + password + "'";
		return query;
	}

	public static String findReservationLocation(String scooterID) {
		String blank = "SELECT ScooterLocation FROM Scooters WHERE ScooterID = ''";
		String query = blank.substring(0, 56) + scooterID + blank.substring(56);
		return query;
	}

	public static String checkAvailability(String scooterLocation) {
		String location = scooterLocation; // location to check
		String blank = "SELECT * FROM Scooters WHERE ScooterLocation = '' AND IsReserved = 'no' AND CurrBattCharge > .33";
		String query = blank.substring(0, 48) + location + blank.substring(48);
		return query;
	}

	public static String updateLocation(String location, String scooterID) {
		String blank = "UPDATE Scooters SET ScooterLocation = '' WHERE ScooterID = ''";
		String query = blank.substring(0, 39) + location + blank.substring(39, 60) + scooterID + blank.substring(60);
		return query;
	}

	public static String markReserved(String scooterID) {
		String query = "UPDATE Scooters SET IsReserved = 'yes' WHERE ScooterID = '" + scooterID + "'";
		return query;
	}

	public static String markUnreserved(String scooterID, String destination) {
		String query = "UPDATE Scooters SET IsReserved = 'no', ScooterLocation = '" + destination
				+ "' WHERE ScooterID = '" + scooterID + "'";
		return query;
	}

	public static String getTripEstimation(String startLocation, String endLocation) {

		String blank = "SELECT Distance FROM Distances WHERE StartLocation = '' AND EndLocation = '";
		String query = blank.substring(0, 54) + startLocation + blank.substring(54, 75) + endLocation + "'";
		return query;
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
