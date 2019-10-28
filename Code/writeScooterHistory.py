import csv
import sys
from datetime import datetime
from datetime import timedelta


undockTime = datetime.now()

'''

Lines 16 - 22 will all be automated into the main code when integrated

'''
scooterID = input('Enter Scooter ID: ')
username = input('Enter username: ')
isReserved = 'no'
startLocation = ''
endLocation = input('End Location: ')
distance = 0.0
time = 0.0
startCharge = 0
endCharge = 0
cost = 0.0




with open ('Scooters.csv') as scooters:
    reader = csv.reader(scooters)
    for row in reader:
        if row[0] == scooterID:
            startLocation = row[2]
            startCharge = row[1]
            endCharge = int(startCharge) - 12          #Change once Troy updates formula for battery usage
with open ('Distances.csv') as distances:
    distanceReader = csv.reader(distances)
    for row in distanceReader:
        if row[0] == startLocation and row[1] == endLocation:
            distance = row[2]
            time = row[3]

                                    #cost calculation
cost = 1 + (.15 * float(time))

split = float(time) % 1
minutes = float(time) - float(split)
seconds = float(split) * 60
print(seconds)
print(minutes)

redockTime = undockTime + timedelta(minutes=minutes, seconds=seconds)
     
'''
            Here we will update the Scooters database with the new batter charges and locations. For now we will assume 
            all scooters are being charged equally and are being put in the correct location
'''
with open('scooterHistory.csv', 'a') as histWrite:
    historyWriter = csv.writer(histWrite)
    historyWriter.writerow([str(scooterID), str(undockTime), str(redockTime), str(username), int(startCharge), int(endCharge), str(startLocation), str(endLocation), float(distance), float(time), float(cost), str(isReserved)])
