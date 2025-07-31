from flask import Flask, request ,send_from_directory,jsonify,session,url_for,redirect
import random
import socket
import json
import sqlite3
import hashlib
import re
import os
from flask import request
from flask import render_template
import base64
from datetime import date
# import connection


# ip = "http://192.168.43.185:8080"
#veg_list,nonveg_list = create_list()
app = Flask(__name__, static_url_path='' )
conn = sqlite3.connect('database.db')
app.secret_key = "abc"

# session["doc_id"] = ""

# try:
# 	conn.execute('CREATE TABLE if not exists doctor(name TEXT,password TEXT)')
# 	conn.close()
# except :
# 	print("Error")
# 	conn.close()


@app.route('/login', methods = ['POST', 'GET'])
def login():
	if request.method== 'GET':
		return render_template("login.html")
	elif request.method == 'POST':
		email= request.form.get('email')
		password= request.form.get('password')
		print(email,password)

		# result = (hashlib.md5(password.encode())).hexdigest()
		with sqlite3.connect("database.db") as conn:
			cur = conn.cursor()
			cur.execute("SELECT * from doctor where email = ? and password = ?", (email, password))
			rows = cur.fetchone();
			if(rows):
				print(rows)
				print("Login Success")
				session["doc_id"] = rows[0]
				print("session:",session["doc_id"])
				#cur.execute("SELECT * from patient")
				cur.execute("SELECT * from basic_patient_details")
				row = cur.fetchall();
				print(row)
				return render_template('home.html', data=row)
			else:
				print("Unsuccessful login")
				return render_template('login.html')


@app.route('/registration', methods = ['POST', 'GET'])
def registration():
	if request.method== 'GET':
		return render_template("registration.html")
	elif request.method == 'POST':
		# content = request.json
		# print(content)
		# password= content['password']
		# name = content['name']
		name= request.form.get('name')
		email= request.form.get('email')
		password= request.form.get('password')


		#check whether parameters are blank
		if(name=="" or password==""):
			return "One of the parameters is blank."
		else:
			with sqlite3.connect("database.db") as conn:
				cur = conn.cursor()
				cur.execute("SELECT * FROM doctor where email = ?", (email,)) #check whether user is present or not
				rows = cur.fetchall();
				if(rows):
					return "User already exists."
				else:
					cur = conn.cursor()
					cur.execute("SELECT  MAX(doctor_id) FROM doctor")
					row = cur.fetchone();
					doc_id = row[0]
					if doc_id != None:
						doc_id +=1
						print("innnnnn")
						cur.execute("INSERT INTO doctor VALUES (?, ?, ?, ?)",(doc_id,email, name, password))
						conn.commit()
					else:
						doc_id = 1
						cur.execute("INSERT INTO doctor VALUES (?, ?, ?, ?)",(doc_id,email, name, password))
						conn.commit()

					print("Successful")
					return render_template("login.html")#registration success



@app.route('/patient_details', methods = ['POST', 'GET'])
def patient_details():
	# return render_template("patient_details.html")
	if request.method== 'GET':
		return render_template("patient_details.html")
	elif request.method == 'POST':
		patient_id= request.form.get('patient_id')
		patient_name= request.form.get('patient_name')
		phone= request.form.get('phone')
		place= request.form.get('place')
		age= request.form.get('age')
		bloodgroup= request.form.get('bloodgroup')
		weight= request.form.get('weight')
		height= request.form.get('height')
		duedate= request.form.get('duedate')
		exercise= request.form.get('exercise')
		# print("exercise")

		#check whether parameters are blank
		if(patient_id=="" or patient_name=="" or phone=="" or place=="" or age=="" or bloodgroup=="" or weight=="" or height=="" or duedate=="" or exercise==""):
			return "One of the parameters is blank."
		else:
			with sqlite3.connect("database.db") as conn:
				cur = conn.cursor()
				cur.execute("SELECT * FROM basic_patient_details where patient_id = ?", (patient_id,)) #check whether user is present or not
				rows = cur.fetchall();
				if(rows):
					return "User already exists."
				else:
					doc_id=session["doc_id"]
					print("dfsft",doc_id)
					cur = conn.cursor()
					cur.execute("INSERT INTO basic_patient_details VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",( patient_id, doc_id, patient_name, phone, place, age, bloodgroup, weight, height, duedate, exercise))
					conn.commit()
					print("Successful")
					cur.execute("SELECT * FROM basic_patient_details where doctor_id = ?", (doc_id,))
					row = cur.fetchall();
					print(row)
					return render_template('home.html', data=row)
	# return patient_id



@app.route('/blood_report_details/<int:id>', methods = ['POST', 'GET'])
def blood_report_details(id):
	if request.method == "GET":
		return render_template("blood_report_details.html",data=id)

	elif request.method == 'POST':
		print("dee")
		protein= request.form.get('protein')
		cholesterol= request.form.get('cholesterol')
		vitamin_a= request.form.get('vitamin_a')
		vitamin_b= request.form.get('vitamin_b')
		vitamin_c= request.form.get('vitamin_c')
		vitamin_d= request.form.get('vitamin_d')
		folate= request.form.get('folate')
		calcium= request.form.get('calcium')
		iron= request.form.get('iron')
		sodium= request.form.get('sodium')
		print(protein)
		#check whether parameters are blank
		if(protein=="" or cholesterol=="" or vitamin_a=="" or vitamin_b=="" or vitamin_c=="" or vitamin_d=="" or folate=="" or calcium=="" or iron=="" or sodium==""):
			return "One of the parameters is blank."
		else:
			with sqlite3.connect("database.db") as conn:
				cur = conn.cursor()
				doc_id=session["doc_id"]
				print("dfsft",doc_id)
				cur = conn.cursor()
				cur.execute("SELECT * FROM patient_blood_report_details where patient_id = ?", (id,)) #check whether user is present or not
				rows = cur.fetchone();
				if(rows):
					cur.execute("UPDATE patient_blood_report_details SET protein= ?, cholesterol= ?, vitamin_a= ?, vitamin_b= ?, vitamin_c= ?, vitamin_d= ?, folate= ?, calcium= ?, iron= ?, sodium= ? WHERE patient_id = ? AND doctor_id = ?",(protein, cholesterol, vitamin_a, vitamin_b, vitamin_c, vitamin_d, folate, calcium, iron, sodium, id, doc_id))
					conn.commit()
					print("dee")
				else:
					cur.execute("INSERT INTO patient_blood_report_details VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",( id, doc_id, protein, cholesterol, vitamin_a, vitamin_b, vitamin_c, vitamin_d, folate, calcium, iron, sodium))
					conn.commit()
				print("Successful")
				cur.execute("SELECT * FROM patient_blood_report_details where patient_id = ?", (id,))
				row = cur.fetchone();
				return render_template("show_report.html",data=row)

@app.route('/view_patient/<int:id>', methods = ['POST', 'GET'])
def view_patient(id):
	if request.method== 'GET':
		with sqlite3.connect("database.db") as conn:
			cur = conn.cursor()
			cur.execute("SELECT * FROM basic_patient_details where patient_id = ?", (id,)) #check whether user is present or not
			rows = cur.fetchone();
			print(rows)
			if rows:
				return render_template("view_patient.html",data=rows)
			else:
				return "0"

@app.route('/view_doctor/<int:id>', methods = ['POST', 'GET'])
def view_doctor(id):
	if request.method== 'GET':
		with sqlite3.connect("database.db") as conn:
			cur = conn.cursor()
			cur.execute("SELECT * FROM doctor where doctor_id = ?", (id,)) #check whether user is present or not
			rows = cur.fetchone();
			print(rows)
			if rows:
				return render_template("navbar.html",data=rows)
			else:
				return "0"

@app.route('/getpatientdata/<int:patient_id>', methods = ['POST', 'GET'])
def getpatientdata(patient_id):
	print(patient_id)
	if request.method== 'GET':
		print("deepika")
		with sqlite3.connect("database.db") as conn:
			cur = conn.cursor()
			cur.execute("SELECT * FROM basic_patient_details where patient_id = ?", (patient_id,)) #check whether user is present or not
			rows = cur.fetchone();
			print(rows)
			if rows:
				return jsonify(rows)
			else:
				return "0"

@app.route('/allergies/<int:id>', methods = ['POST', 'GET'])
def allergies(id):
	if request.method == "GET":
		return render_template("allergies.html",data=id)
	elif request.method == 'POST':
		print("dee")
		allergy1= request.form.get('allergy1')
		allergy2= request.form.get('allergy2')
		allergy3= request.form.get('allergy3')
		allergy4= request.form.get('allergy4')
		allergy5= request.form.get('allergy5')

		with sqlite3.connect("database.db") as conn:
			cur = conn.cursor()
			doc_id=session["doc_id"]
			print("dfsft",doc_id)
			cur = conn.cursor()
			cur.execute("SELECT * FROM patient_allergies where patient_id = ?", (id,)) #check whether user is present or not
			rows = cur.fetchone();
			if(rows):
				cur.execute("UPDATE patient_allergies SET allergy1= ?, allergy2= ?, allergy3= ?, allergy4= ?, allergy5= ? WHERE patient_id = ? AND doctor_id = ?",(allergy1, allergy2, allergy3, allergy4, allergy5, id, doc_id))
				conn.commit()
				print("dee")
			else:
				cur = conn.cursor()
				cur.execute("INSERT INTO patient_allergies VALUES (?, ?, ?, ?, ?, ?, ?)",( id, doc_id, allergy1, allergy2, allergy3, allergy4, allergy5))
				conn.commit()
			print("Successful")
			show(id)
			# os.system("python connection.py %s" %(id))

			cur.execute("SELECT * FROM patient_blood_report_details where patient_id = ?", (id,))
			row = cur.fetchone();
			return render_template("show_report.html",data=row)

def show(id):
	print(id)
	cmd="python connection.py %s" %(id)
	print(cmd)
	os.system(cmd)

@app.route('/logout')
def logout():
	if 'doc_id' in session:
		print(session["doc_id"], " logged out")
		session.pop('doc_id',None)
		return render_template('login.html')


if __name__ == '__main__':
	app.run(debug=True)
