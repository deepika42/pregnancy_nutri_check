# Reading an excel file using Python
import xlrd
import sys
import itertools
import datetime
from datetime import datetime
from datetime import date
import math
import sqlite3

#database connection
conn = sqlite3.connect('database.db')
c = conn.cursor()

def compare(min,max,value,i):
    if max < value:
        excess.append(sheet.cell_value(i, 0))
        user.append(0)
    elif min > value:
        deficient.append(sheet.cell_value(i, 0))
        if(0.8*min > value):
            user.append(2)
        else:
            user.append(1)
    else:
        user.append('X')

def getList():
    return list(nutriscore.keys())

def get_details():
    date=c.execute("Select duedate from basic_patient_details WHERE patient_id=?",(pid,))
    print(date)
    day=c.fetchone()[0]
    global duedate
    duedate = datetime.strptime(day,'%Y-%m-%d').date()
    c.execute("Select protein,cholesterol,vitamin_a,vitamin_b,vitamin_c,vitamin_d,folate,calcium,iron,sodium from patient_blood_report_details WHERE patient_id=?",(pid,))
    x=c.fetchall()
    global input
    input=x.pop(0)
    c.execute("Select allergy1, allergy2,allergy3,allergy4,allergy5 from patient_allergies WHERE patient_id=?",(pid,))
    y=c.fetchall()
    global allergy
    allergy=y.pop(0)

def diet_entry():
    query=c.execute("SELECT * from diet where patient_id=?",(pid,))
    rows=c.fetchall()
    if(rows):
          query="UPDATE diet SET week=?,food1=?,food2=?,food3=?,food4=?,food5=? WHERE patient_id=?"
          columnvalue=(week,food1,food2,food3,food4,food5,pid,)
          c.execute(query,columnvalue)
    else:
          c.execute("INSERT INTO diet VALUES(?,?,?,?,?,?,?)",(pid,week,food1,food2,food3,food4,food5,))
    conn.commit()

# Give the location of the file
loc = "Book1.xlsx"
loc1 = "Book2.xlsx"


nutrients=["protein","cholestrol","vit A","vit B","vit C","vit D","folate","calcium","Iron","sodium"]

# To open Workbook
wb = xlrd.open_workbook(loc)
sheet = wb.sheet_by_index(0)
wb1 = xlrd.open_workbook(loc1)
sheet1 = wb1.sheet_by_index(0)
# For row 0 and column 0
sheet.cell_value(0, 0)
#deficient=[]
#excess=[]

# pid=1
pid=sys.argv[1]
print(pid)


user=[]
nutriscore={}
recommend=[]
diet=[]

#input from database
get_details()

today=date.today()
diff=(duedate-today).days
months=math.floor((280-diff)/30)
week=math.floor((280-diff)/7)
#print(week)
if months <= 3:
    trimester=1
elif 3<months<=6:
    trimester=2
else:
    trimester=3

s=0

for i in range(1,sheet.nrows):
    value=float(input[s])
    if trimester == 1:
        min=sheet.cell_value(i, 2)
        max=sheet.cell_value(i, 3)
        compare(min,max,value,i)
    elif trimester == 2:
        min=sheet.cell_value(i, 4)
        max=sheet.cell_value(i, 5)
        compare(min,max,value,i)
    else:
        min=sheet.cell_value(i, 6)
        max=sheet.cell_value(i, 7)
        compare(min,max,value,i)
    s=s+1
#print("excess nutrients are ")
#print(excess)
#print("deficient nutrients are ")
#print(deficient)

#print(user)
for i in range(1,sheet1.nrows):
    j=0
    score=0
    for k in range(1,sheet1.ncols):
        if user[j]=='X':
            score=score+0
        elif user[j]==1:
            if sheet1.cell_value(i,k)== '0':
                score=score-1
            else:
                score=score + float((sheet1.cell_value(i,k)))
        elif user[j]==2:
            if sheet1.cell_value(i,k)== '0':
                score=score-2
            elif sheet1.cell_value(i,k)== '1':
                score=score-1
            else:
                score=score + float((sheet1.cell_value(i,k)))
        elif user[j]==0:
            score=score - float((sheet1.cell_value(i,k)))
        j=j+1
    nutriscore[(sheet1.cell_value(i,0))]=score


nutriscore=dict(sorted(nutriscore.items(), key = lambda x : x[1], reverse=True))
#print(nutriscore)
recommend=getList()
#print(recommend)

for n in recommend:
    s=0
    for m in allergy:
        if(n.lower()==m.lower()):
            s=-1
            break
    if(s==0):
            diet.append(n)
    if(len(diet)==5):
        break

food1=diet[0]
food2=diet[1]
food3=diet[2]
food4=diet[3]
food5=diet[4]
diet_entry()
print(diet)
