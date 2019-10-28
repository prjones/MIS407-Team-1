import csv

with open ('Users2.csv') as users:
    reader = csv.reader(users)
    
    usernameList = []

    for row in reader:
        username = row[0]
        usernameList.append(username)

    usernameList.sort()
    new_list = sorted(set(usernameList))
    dup_list =[]
 
 
    for i in range(len(new_list)):
        if (usernameList.count(new_list[i]) > 1 ):
            dup_list.append(new_list[i])
        
    print(dup_list) 