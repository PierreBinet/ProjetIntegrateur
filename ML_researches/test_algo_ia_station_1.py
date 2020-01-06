# -*- coding: utf-8 -*-
"""
Created on Wed Nov 27 14:58:34 2019

@author: Lucile

state : training ok.
"""

# imports
from sklearn.cluster import DBSCAN
from sklearn import svm
from sklearn import metrics
from datetime import datetime
from matplotlib import pyplot as plt
from matplotlib import dates as md
import pandas as pd
import numpy as np

# extraction from csv
df = pd.read_csv('../RExtractor/output/JC-201901-citibike-tripdata.csv', sep=',', header=0)

# df.shape  # dimensions du tableau de données 577703x15

""" ancienne méthode d'extraction lorsque la BDD n'avait pas de colonnes séparées pour année/mois/jour/heure/min
df['starttime'] = pd.to_datetime(df['starttime'])  # type datetime
df['stoptime']=pd.to_datetime(df['stoptime'])  # type datetime



df.at[0, 'stoptime'].year #affiche la donnée 'stoptime' de la donnée d'indice 0


# creates columns for year, month, day ...
# START
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
"""


# Convertit la colonne usertype en subscribing avec 1=oui et 0=non
#ça serait cool de le faire dans le prétraitement
def convert_usertype_to_boolean(data_array):
    for row in range(0, len(data_array)):
        if data_array.at[row, 'usertype'] == "Subscriber":
            data_array.at[row, 'usertype'] = 1
        else:
            data_array.at[row, 'usertype'] = 0
    return data_array


# Retourne les datasets où le départ ou l'arrivée est à une station donnée
def find_by_start_station_id(dataset, id_start_station):
    dataset_station_s = dataset[dataset.start_station_id == id_start_station]
    return dataset_station_s

#def add_weekday(dataset):
#    dataset[1]


# delete incomplete data
df = df.dropna()
df = convert_usertype_to_boolean(df)
df_station = find_by_start_station_id(df, 3183)
# print(df_station)
# print(df_station[['start_year', 'start_month', 'start_day', 'start_hour', 'start_minute', 'start_second']])


# dataset %70 train 30%test
msk = np.random.rand(len(df_station)) < 0.7
train_fold = df_station[msk][['start_year', 'start_month', 'start_day', 'start_hour']]
train_y = df_station[msk][['usertype']]
test = df_station[~msk][['start_year', 'start_month', 'start_day', 'start_hour']]


# train.shape
# test.shape

# test algo
clf = svm.SVR()
clf.fit(train_fold, train_y.values.ravel())
res = clf.predict(test)

# print('\007')  # bell sound pour savoir que c'est fini !!


#dates = np.arange(0,24,0.5)
#xfmt = md.DateFormatter('%H:%M:%S')
plt.rcParams['figure.figsize'] = [10, 5]
plt.xticks(rotation= 90, )
plt.plot(train_y)
plt.plot(res, color='red')
plt.show()
