import csv

with open ('Users.csv') as users:
    reader = csv.reader(users)
    '''
    firstName = ''
    lastName = ''
    username = ''
    '''
    userWrite = open('Users2.csv', 'w', newline='')
    userWriter = csv.writer(userWrite)                                                             
    for row in reader:
        firstName = row[1]
        lastName = row[2]
        password = row[3]
        username = firstName[:2] + lastName[:5]

        userWriter.writerow([username, firstName, lastName, password])


