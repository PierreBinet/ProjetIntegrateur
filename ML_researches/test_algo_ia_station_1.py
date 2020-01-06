# -*- coding: utf-8 -*-
"""
Created on Wed Nov 27 14:58:34 2019

@author: Lucile

state : training ok.
"""

# imports
from sklearn.cluster import DBSCAN
from sklearn import svm
from sklearn import neighbors
from sklearn import neural_network as nn
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
"""

# usertype prétraité en R: subscriber=0, customer=1


# Retourne les datasets où le départ ou l'arrivée est à une station donnée
def find_by_start_station_id(dataset, id_start_station):
    dataset_station_s = dataset[dataset.start_station_id == id_start_station]
    return dataset_station_s


# delete incomplete data
df = df.dropna()
df_station = find_by_start_station_id(df, 3183)
# print(df_station)
# print(df_station[['start_year', 'start_month', 'start_day', 'start_hour', 'start_minute', 'start_second']])


# dataset %70 train 30%test
msk = np.random.rand(len(df_station)) < 0.7
train_fold = df_station[msk][['start_wday', 'start_hour']]
train_y = df_station[msk][['usertype']]
test_fold = df_station[~msk][['start_wday', 'start_hour']]
test_y = df_station[~msk][['usertype']]


# train.shape
# test.shape

# test algo
clf = nn.MLPClassifier(hidden_layer_sizes=50)
clf.fit(train_fold, train_y.values.ravel())
res = clf.predict(test_fold)
pres = metrics.mean_absolute_error(test_y, res) * 100
print("machine learning successful with precision of "+str(pres)+"%")

# print('\007')  # bell sound pour savoir que c'est fini !!


#dates = np.arange(0,24,0.5)
#xfmt = md.DateFormatter('%H:%M:%S')
plt.rcParams['figure.figsize'] = [10, 5]
plt.xticks(rotation= 90, )
plt.plot(train_y)
plt.plot(res, color='red')
plt.show()
