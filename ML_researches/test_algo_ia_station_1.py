# -*- coding: utf-8 -*-
"""
Created on Wed Nov 27 14:58:34 2019

@author: Lucile

state : training ok.
"""



from sklearn.cluster import DBSCAN
from sklearn import svm
from sklearn import metrics
from datetime import datetime
import pandas as pd
import numpy as np

#extraction from csv

df=pd.read_csv('../RExtractor/output/JC-201901-citibike-tripdata.csv', sep=',',header=0)



df.shape #dimensions du tableau de données 577703x15
df['starttime']=pd.to_datetime(df['starttime'])#type datetime
df['stoptime']=pd.to_datetime(df['stoptime'])#type datetime

df.at[0, 'stoptime'].year #affiche la donnée 'stoptime' de la donnée d'indice 0


# creates columns for year, month, day ...
#START
for row in range (0, len(df)) :
    df.at[row, 'start_year'] = df.at[row,'starttime'].year
    
for row in range (0, len(df)) :
    df.at[row, 'start_month'] = df.at[row,'starttime'].month

for row in range (0, len(df)) :
    df.at[row, 'start_day'] = df.at[row,'starttime'].day  
    
for row in range (0, len(df)) :
    df.at[row, 'start_hour'] = df.at[row,'starttime'].hour

for row in range (0, len(df)) :
    df.at[row, 'start_minute'] = df.at[row,'starttime'].minute
    
for row in range (0, len(df)) :
    df.at[row, 'start_second'] = df.at[row,'starttime'].second
    

#STOP  

for row in range (0, len(df)) :
    df.at[row, 'stop_year'] = df.at[row,'stoptime'].year
    
for row in range (0, len(df)) :
    df.at[row, 'stop_month'] = df.at[row,'stoptime'].month

for row in range (0, len(df)) :
    df.at[row, 'stop_day'] = df.at[row,'stoptime'].day  
    
for row in range (0, len(df)) :
    df.at[row, 'stop_hour'] = df.at[row,'stoptime'].hour

for row in range (0, len(df)) :
    df.at[row, 'stop_minute'] = df.at[row,'stoptime'].minute
    
for row in range (0, len(df)) :
    df.at[row, 'stop_second'] = df.at[row,'stoptime'].second



#Convertir la colonne usertype en subscribing avec 1=oui et 0=non ?

for row in range (0, len(df)) :
    if df.at[row, 'usertype'] == "Subscriber" :
        df.at[row, 'subscriber'] = 1
    else :
        df.at[row, 'subscriber'] = 0
    

#delete datetime columns et nom de stations (String relous)
df= df.drop(columns=['starttime', 'stoptime', 'start station name', 'end station name', 'usertype'])

#delete incomplete data
df= df.dropna()

#dataset %70 train 30%test

msk = np.random.rand(len(df)) < 0.7
train = df[msk]
test = df[~msk]


train.shape
test.shape

#test algo
clf = svm.SVR()
clf.fit(train)
clf.predict()



print('\007') #bell sound pour savoir que c'est fini !!