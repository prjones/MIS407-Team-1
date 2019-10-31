import csv
import sys
from datetime import datetime
from datetime import timedelta


undockTime = datetime.now()

'''
Lines 16 - 22 will all be integrated into the main code
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
cont = True
tempData = []


with open ('../CSVFiles/Scooters.csv') as scooters:
    reader = csv.reader(scooters)
    for row in reader:
        tempData.append(row)
        
        if row[0] == scooterID:
            if int(row[1]) > 33:
                startLocation = row[2]
                startCharge = row[1]
                endCharge = int(startCharge) - 12          #Change once Troy updates formula for battery usage
            else: 
                print('That scooter does not have enough charge. Go back')
                cont = False 

#print(tempData)
while cont:
    with open ('../CSVFiles/Distances.csv') as distances:
        distanceReader = csv.reader(distances)
        for row in distanceReader:
            if row[0] == startLocation and row[1] == endLocation:
                distance = row[2]
                time = row[3]

                                        
    cost = 1 + (.15 * float(time))      #cost calculation

    split = float(time) % 1
    minutes = float(time) - float(split)
    seconds = float(split) * 60

    redockTime = undockTime + timedelta(minutes=minutes, seconds=seconds)
   
    for row in tempData:
        if row[0] == scooterID:
            row[1] = endCharge
            row[2] = endLocation

    with open('../CSVFiles/Scooters.csv', 'w') as scooterUpdate:
        updateWriter = csv.writer(scooterUpdate)
        for row in tempData:
            updateWriter.writerow(row)

    with open('../CSVFiles/scooterHistory.csv', 'a') as histWrite:
        historyWriter = csv.writer(histWrite)
        historyWriter.writerow([str(scooterID), str(undockTime), str(redockTime), str(username), int(startCharge), int(endCharge), str(startLocation), str(endLocation), float(distance), float(time), float(cost), str(isReserved)])
    
    cont = False