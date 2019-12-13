from flask import Flask, render_template, request, redirect
import markdown as md
import csv, os
import sqlite3
from datetime import datetime
from datetime import timedelta
import psycopg2
import uuid
import matplotlib.pyplot as plt
import pandas as pd
from pandas import DataFrame, Series 


app = Flask(__name__)
conn = psycopg2.connect(host="ec2-174-129-214-193.compute-1.amazonaws.com",database="d245l8lq8fvt3k", user="jptsqrcgcolpzw", password="68ba5ad64cadf6d0b7f387da474436dfbd3dca326fd914db29d2b35a59690ef9", port="5432")
c = conn.cursor()

c.execute("INSERT INTO userhistory (username, totaldistance, totalcost, totaltime, tripnum) VALUES ('{}', 0, 0, 0, 0)".format('PaBreme'))

conn.commit()
conn.close()