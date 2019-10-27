Steps for downloading:

Download:
1) Download all .java and .txt files. Download Extra files if they are not already on your computer.
2) Don't worry about the other folders yet

Eclipse:

1) Create new project in Eclipse 
2) Drag .java files into the src folder of the new project
3) Drag .txt files to the new project folder (should automatically be placed in the folder, NOT in src)
4) Right-click the JRE System Library folder in the Project Explorer tab
5) Select Build Path --> Configure Build Path
6) Click Add External JARs --> select Derby (in Extra files in case you don't have it)
7) Apply and Close
8) Open UserInterface class
9) Select Run Configurations
10) In the arguments tab, paste database.properties
11) Apply and Run
12) Run the UserInterface Class at least two times. The first two times it should say "DROP TABLE Scooters" or "DROP TABLE Distances" and then some other words. Ignore this, and just run it again until the first line printed in the console is "Welcome! Are you:"