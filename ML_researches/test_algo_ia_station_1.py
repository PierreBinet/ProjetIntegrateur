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
from statsmodels.tsa.ar_model import AR
from matplotlib import pyplot as plt
from matplotlib import dates as md
import pandas as pd
import numpy as np

# extraction from csv
df = pd.read_csv('../RExtractor/output/201901-citibike-tripdata.csv', sep=',', header=0)

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


# id 3255 (8 Ave & W 31 St in NY)
df_station = find_by_start_station_id(df, 3255)
# print(df_station)
# print(df_station[['start_year', 'start_month', 'start_day', 'start_hour', 'start_minute', 'start_second']])

"""
def train_test_assignment_sk(dfs, ratio):
    msk = np.random.rand(len(dfs)) < ratio
    trainfold = dfs[msk][['start_wday', 'start_hour']]
    trainy = dfs[msk][['usertype']]
    testfold = dfs[~msk][['start_wday', 'start_hour']]
    testy = dfs[~msk][['usertype']]
    return trainfold, trainy, testfold, testy


# dataset %70 train 30%test
train_fold, train_y, test_fold, test_y = train_test_assignment_sk(df_station, 0.7)

# test algo
# clf = nn.MLPClassifier(hidden_layer_sizes=50) KNN: marche pas
clf = svm.SVR()
clf.fit(train_fold, train_y.values.ravel())
res = clf.predict(test_fold)
error = metrics.mean_squared_error(test_y, res)
precision = metrics.explained_variance_score(test_y, res)
print(res.shape)
print("machine learning successful with precision of "+str(precision)+"\n")
print(str(res)+"\n")
print(test_y.values.ravel())
"""


def extract_one_day(dfs, day):
    df_one_day = dfs[dfs.start_wday == day][['usertype', 'start_wday', 'start_hour']]

    return df_one_day



# autoregression
# Fonction de prédiction
def predict(coef, history):
    y = coef[0]
    for i in range(1, len(coef)):
        y += coef[i] * history[-i]
    return y


# train on mondays, test on tuesdays
train = extract_one_day(df_station, 1)
test = extract_one_day(df_station, 2)

# Train autoregression
model = AR(train)
model_fit = model.fit(maxlag=6, disp=False)
window = model_fit.k_ar
coef = model_fit.params
# Walk forward over time steps in test

history = [train[i] for i in range(len(train))]
res = list()
for t in range(len(test)):
    y = predict(coef, history)
    obs = test[t]
    res.append(y)
    history.append(obs)
error = metrics.mean_squared_error(test, res)
print('Test MSE: %.3f' % error)


""" 
# plot
test_one_day_y = []
for line in range(len(df_station[~msk])):
    if df_station[~msk][['start_wday']][line] == 1:
        test_one_day_y.append(df_station[~msk]['usertype'][line])

dates = np.arange(0, 24, 1)
xfmt = md.DateFormatter('%H:%M:%S')
plt.rcParams['figure.figsize'] = [10, 5]
plt.xticks(rotation=90, )
plt.plot(test_one_day_y)
plt.plot(res, color='red')
plt.show()
"""