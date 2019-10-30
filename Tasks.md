# Tasks

#### If anyone has any questions, feel free to reach out. Some things might not have enough detail. If you think anything should be added, add it and put what you added in the Wiki.

## History
### _Joseph, Peter, Bailey_
>* History of the scooters, the users, and the stations
>* Use of Pandas
>* Useful for analyzing data about popular stations and times they are popular
>
> ### Scooter History - _Peter_
>* Store all rides and scooter information into a .csv file
>* Include:
>   * Scooter ID
>   * Trip Distance
>   * Starting and Ending Battery Charge
>   * Starting and Ending Location
>   * User
>   * Time
>   * If reserved or not
>
> ### User History - _Bailey_
>* Store all ride history information into a .csv file
>* Include:
>   * Trip Distance
>   * Scooter ID used
>   * Starting and Ending Location
>   * Time
>   * If a reservation was made or not (boolean variable)
>
> ### Station History - _Joseph_
>* Store all information about what scooters are at a specific location in .csv file(s)
>* Either 7 different .csv or all in 1, up to you
>* Include: 
>   * Scooter ID
>   * Time scooter was placed or removed from that station
>   * Scooter Battery Charge at arrival
>   * Scooter Battery Charge at departure
>   * Any other necessary information you can think of
## Login/Signup
### _Charlie_
>* Logs in current users and sets up accounts for new users
>* Requirements:
>   * Asks user if they have an account or will sign up for one
>   * If current user, prompt them to enter username and password
>       * Use a database (SQLite) to check and verify if password is correct, if not, have them enter password again
>   * If new user, ask for:
>       * Username (make sure to check the database and verify the username does not currently exist)
>       * Password (Have them type it twice to confirm)
>       * Other useful information that may apply (i.e. Credit Card, Birthday, 
Age, etc.)
>       * Also add number of rides, amount paid, and a 10-digit place holder for potential reservations (i.e. xxxxxxxxxx). Set all metrics to 0


## Scooter Usage
### _Troy_
>* Tracking scooters, reserving, unlocking, transportation of them. Charging and fees
>* You will need to build the database(s) required to track the current location, battery, reservation status and ID number of the scooters at their stations. You can either do this in a SQL database or a .csv --- (partially done)
>* Requirements:
>   * Get username (work with Charlie)
>   * If Ride Now:
>       * Get closest station (eventually we'll work with GPS, for now just manually ask them what station they are at)
>       * Check if station has an available scooter (not reserved, at that station, and battery above certain threshold such as 33%)
>       * Ask where they want to go 
>       * Move the scooter to the new station
>       * Mark down battery percentage (you can come up with your own formula for it)
>   * If Reservation:
>       * Ask the time they want to reserve
>       * Ask where they want to reserve
>       * Verify if there will be a scooter at that location at that time (almost always will be, unless every single scooter is reserved at that time)
>       * Mark for a scooter to be reserved at that station until 10 minutes after their intended pickup time
>       * Notify user that a scooter will be available at that time and they have until x to pick it up (x being 10 minutes after their time)
>   * After all transactions are done, update all three History Databases (Scooter, Station(s) and User). Work with Bailey, Joseph and Peter on this.



