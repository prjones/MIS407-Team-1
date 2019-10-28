import csv
import sys
from datetime import datetime
from datetime import timedelta

searchID = input('Enter the scooter ID you want to analyze: ')
scooterID = ''
startTime = ''
endTime = ''
startCharge = 0
endCharge = 0
startLocation = ''
endLocation = ''
distance = 0.0
cost = 0.0
isReserved = ''


with open ('scooterHistory.csv') as scooterHistory:
    historyReader = csv.reader(scooterHistory)

    for row in historyReader:

        if row[0] == searchID:
            scooterID = str(row[0]) #string
            startTime = str(row[1]) #string (for now)
            endTime = str(row[2])   #string (for now)
            username = str(row[3]) #string
            startCharge = float(row[4]) #float
            endCharge = float(row[5]) #float
            startLocation = str(row[6]) #string
            endLocation = str(row[7]) #string
            distance = float(row[8]) #float
            cost = float(row[9]) #float
            isReserved = str(row[10]) #string

print('{:15s} {:35s} {:35s} {:12s} {:>4,.2f} {:>4,.2f} {:25s} {:25s} {:>5,.2f} {:>5,.2f} {:5s}'.format(scooterID, startTime, endTime, username, startCharge, endCharge, startLocation, endLocation, distance, cost, isReserved))
        
        