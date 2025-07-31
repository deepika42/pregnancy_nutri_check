from flask import Flask,request,send_from_directory,jsonify,session,url_for,redirect
import random
import socket
import json
import sqlite3
import hashlib
import re


ip = "http://192.168.1.108:8080"
# description = "Lorem ipsum dolor sit amet, proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
server = Flask(__name__, static_url_path='' )

# userdata table
conn = sqlite3.connect('users.db')
try:
	conn.execute('CREATE TABLE if not exists userdata ( name TEXT,phone INTEGER(11),password TEXT,street TEXT,city TEXT,pincode INTEGER(7))')
	conn.close()
except:
	print("error")
	conn.close()



#Route for LOGIN
@server.route('/login', methods = ['POST', 'GET'])
def login():
	if request.method == 'POST':
		content = request.json
		phone = content['phone']
		password = content['password']
		print(phone)
		print(password)
		result = (hashlib.md5(password.encode())).hexdigest()
		print(result)
		with sqlite3.connect("users.db") as conn:
			cur = conn.cursor()
			cur.execute("SELECT * from userdata where phone = ?", (phone,))
			rows = cur.fetchall()
			print(rows)
			if(rows):
				return "1"
			else:
				return "0"


#Route for REGISTER

@server.route('/register', methods = ['POST', 'GET'])
def register():
	if request.method == 'POST':
		content = request.json
		name = content['name']
		phone= content['phone']
		password= content['password']
		street = content['street']
		city = content['city']
		pincode = content['pincode']
		pincode = int(pincode)

		#check whether parameters are blank
		if(name=="" or phone=="" or password=="" or street=="" or city=="" or pincode==""):
			return "One of the parameters is blank."
		else:
			if not re.compile("(0/91)?[7-9][0-9]{9}").match(phone): #check format of phone
				return "Invalid phone number"
			else:
				result = (hashlib.md5(password.encode())).hexdigest()
				with sqlite3.connect("users.db") as conn:
					cur = conn.cursor()
					cur.execute("SELECT * FROM userdata where phone = ?", (phone,)) #check whether user is present or not
					rows = cur.fetchall();
					if(rows):
						return "User already exists."
					else:
						cur = conn.cursor()
						cur.execute("INSERT INTO userdata VALUES (?, ?, ?, ?, ?, ?)",(phone, result, name,street,city, pincode)) #insert
						conn.commit()
						return "1" #registration success


#Run server on local IP and port 8080
if __name__ == '__main__':
   server.run('0.0.0.0',8080,True)
