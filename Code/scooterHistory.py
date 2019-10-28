import csv
import sys
from datetime import datetime
from datetime import timedelta


undockTime = datetime.now()
print(undockTime)
redockTime = undockTime + timedelta(minutes=3)

'''

Lines 16 - 22 will all be automated into the main code when integrated

'''
scooterID = input('Enter Scooter ID: ')
username = input('Enter username: ')
isReserved = input('Is it a reservation? ')
endLocation = input('End Location: ')
distance = input('Distance: ')
startCharge = 0
endCharge = 0
cost = input('Cost: ')



with open ('Scooters.csv') as scooters:
    reader = csv.reader(scooters)
    for row in reader:
        if row[0] == scooterID:
            startLocation = row[2]
            startCharge = row[1]
            endCharge = int(startCharge) - 12                                            #Change once Troy updates formula for battery usage
scooterHistory = open('scooterHistory.csv', 'w', newline='')
historyWriter = csv.writer(scooterHistory)                                                             
historyWriter.writerow([str(scooterID), str(undockTime), str(redockTime), str(username), int(startCharge), int(endCharge), str(startLocation), str(endLocation), float(distance), float(cost), str(isReserved)])
    