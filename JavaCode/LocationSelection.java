import java.util.Scanner;
public class LocationSelection {

	
	
	
	
	
	public static Double locateUserLat() {
		Scanner scanLat = new Scanner(System.in); //scans for latitude from GPS
		System.out.printf("%94s", "Enter User Latitude: ");
		double latCoord = scanLat.nextDouble();
		
		return latCoord;
	}
	
	public static Double locateUserLong() {
		Scanner scanLong = new Scanner(System.in); //scans for longitude from GPS
		System.out.printf("%95s", "Enter User Longitude: ");
		double longCoord = scanLong.nextDouble();
		
		return longCoord;
	}
	
	@SuppressWarnings("unused")
	public static String findStartingStation() {
		
		double userLat = LocationSelection.locateUserLat();
		double userLong = LocationSelection.locateUserLong();
		String startingLocation;
		
			double parksUpperBoundLat = 42.0314;
			double parksLowerBoundLat = 42.0257;
			double parksLeftBoundLong = -93.6562;
			double parksRightBoundLong = -93.6452;
			
			double stateUpperBoundLat = 42.0257; //parksLowerBoundLat & kildeeLowerBoundLat & UmwlLowerBoundLat & LmwlUpperBoundLat & muUpperBoundLat
			double stateLowerBoundLat = 42.0205;
			double stateLeftBoundLong = -93.6562;
			double stateRightBoundLong = -93.6479;
			
			double kildeeUpperBoundLat = 42.0314;
			double kildeeLowerBoundLat = 42.0257;
			double kildeeLeftBoundLong = -93.6452;
			double kildeeRightBoundLong = -93.6418;
			
			double UmwlUpperBoundLat = 42.0271;
			double UmwlLowerBoundLat = 42.0257;
			double mwlLeftBoundLong = -93.6418;
			double mwlRightBoundLong = -93.6360;
			
			double LmwlUpperBoundLat = 42.0257;
			double LmwlLowerBoundLat = 42.0189;
			
			double muUpperBoundLat = 42.0257;
			double muLowerBoundLat = 42.0189;
			double muLeftBoundLong = -93.6479;
			double muRightBoundLong = -93.6418;
			
			double freddyUpperBoundLat = 42.0436;
			double freddyLowerBoundLat = 42.0314;
			double freddyRightBoundLong = -93.6375;
			double freddyLeftBoundLong = -93.6499;
	
		
		 if (userLat > LmwlLowerBoundLat & userLat < parksUpperBoundLat) { //if 1
			 if(userLong < mwlRightBoundLong & userLong > stateLeftBoundLong) { //if 1.1
				 if(userLat > parksLowerBoundLat & userLat < parksUpperBoundLat) { //if 1.1.1
					 if(userLong < parksRightBoundLong & userLong > parksLeftBoundLong) { startingLocation = "Parks";} //end if 1.1.1.1
					 
					 else if (userLong < kildeeRightBoundLong & userLong > kildeeLeftBoundLong) { startingLocation = "Kildee";} //end if 1.1.1.2
					 else if (userLong < mwlRightBoundLong & userLong > mwlLeftBoundLong) { //if 1.1.1.3 
						 if (userLat > LmwlLowerBoundLat & userLat < UmwlUpperBoundLat) { startingLocation = "MWL";} //end if 1.1.1.3.1 
						 else { startingLocation = "Out of Range. Walk to the West (to Kildee) or to the South (to MWL)";} //end else
					 } //end if 1.1.1.3
					 else { startingLocation = "No Location Near You";} //end else
				 } //end if 1.1.1
				 else if(userLat > muLowerBoundLat & userLat < muUpperBoundLat) { //if 1.1.2
					 if(userLong < muRightBoundLong & userLong > muLeftBoundLong) { startingLocation = "MU";} //end if 1.1.2.1
					 else if (userLong < stateRightBoundLong & userLong > stateLeftBoundLong) { startingLocation = "State";} //end if 1.1.2.2
					 else if (userLong < mwlRightBoundLong & userLong > mwlLeftBoundLong) { startingLocation = "MWL";} //end if 1.1.2.3
					 else { startingLocation = "No Location Near You";}
				 } //end if 1.1.2
				 else { startingLocation = "error1";}
				
			 } //end if 1.1
			 else { startingLocation = "error2"; }		
	} //end if 1
		 else if (userLat > freddyLowerBoundLat & userLat < freddyUpperBoundLat) {
			 if (userLong < freddyRightBoundLong & userLong > freddyLeftBoundLong) { startingLocation = "Freddy";}
			 else { startingLocation = "No Station Near You";}
		 }
	else { startingLocation = "error3"; }
	
		 return startingLocation;
	}	
} //end class
