# -*- coding: utf-8 -*-


"""
Created on Wed Nov 27 14:58:34 2019

@author: Lucile & Pierre

state : does not compile
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


""" ancienne méthode d'extraction lorsque la BDD n'avait pas de colonnes séparées pour année/mois/jour/heure/min
df['starttime'] = pd.to_datetime(df['starttime'])  # type datetime
df['stoptime']=pd.to_datetime(df['stoptime'])  # type datetime



df.at[0, 'stoptime'].year #affiche la donnée 'stoptime' de la donnée d'indice 0
"""





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


# Retourne les datasets où le départ ou l'arrivée est à une station donnée
def find_by_start_station_id(dataset, id_start_station):
    dataset_station_s = dataset[dataset.start_station_id == id_start_station]
    return dataset_station_s


def extract_one_day(dfs, day):
    df_one_day = dfs[dfs.start_wday == day]

    return df_one_day


# Conversion d'un string en datetime
def convert_string_to_datetime(hour, minute, second):
    date_time_str = str(hour)+':'+str(minute)+':'+str(round(second,3))
    date_time_obj = datetime.strptime(date_time_str, '%H:%M:%S.%f')
    date_time_obj = pd.to_datetime(date_time_obj)
    return date_time_obj


# récupère les dates du dataset après extraction station/jour et récupère les dates, les convertit en timestamp
def getDateData(dataset, mode="timestamp"):
    dataset = dataset.reset_index(drop=True)
    s_day = dataset['start_day']
    s_month = dataset['start_month']
    s_year = dataset['start_year']
    s_hour = dataset['start_hour']
    s_minute = dataset['start_minute']
    s_second = dataset['start_second']

    data_s = []
    for i in range(len(dataset)):
        start_date = convert_string_to_datetime(s_hour[i], s_minute[i], s_second[i])
        if mode == "timestamp":
            data_s.append(start_date.timestamp())
        else:
            data_s.append(start_date)
    return data_s


# autoregression
# Fonction de prédiction
def predict(coef, history):
    y = coef[0]
    for i in range(1, len(coef)):
        y += coef[i] * history[-i]
    return y


# usertype prétraité en R: subscriber=0, customer=1
# "data" arg is a list of tuples (date, value)
def avg_user_per_halfhour(data)
    avg_data_per_halfhour_list = []
    for hours in range(0, 24):
        first_halfhour = 0
        second_halfhour = 0
        for line in data:
            if hours == line[0].hour:
                if line[0].minute <= 30:
                    first_halfhour += line[1]  # on ajoute +1 ou -1
                else:
                    second_halfhour += line[1]  # on ajoute +1 ou -1
            avg_data_per_halfhour_list.append(first_halfhour)
            avg_data_per_halfhour_list.append(second_halfhour)
    return avg_data_per_halfhour_list


def main()
	# extraction from csv
	df = pd.read_csv('../RExtractor/output/201901-citibike-tripdata.csv', sep=',', header=0)


	# id 3255 (8 Ave & W 31 St in NY)
	df_station = find_by_start_station_id(df, 3255)
	# print(df_station)
	# print(df_station[['start_year', 'start_month', 'start_day', 'start_hour', 'start_minute', 'start_second']])


	# delete incomplete data
	df = df.dropna()


	# train on mondays, test on tuesdays
	df_wday1 = extract_one_day(df_station, 1)
	df_wday2 = extract_one_day(df_station, 2)


	sorted_dates1 = getDateData(df_wday1, "notimestamp")
	sorted_dates2 = getDateData(df_wday2, "notimestamp")

	
	tuples1 = list(zip(sorted_dates1,dfwday1[['usertype']]))
	tuples2 = list(zip(sorted_dates2,dfwday1[['usertype']]))


	averaged_data_per_30min1 = avg_user_per_halfhour(tuples1)
	averaged_data_per_30min2 = avg_user_per_halfhour(tuples2)


	train, test = averaged_data_per_30min2, averaged_data_per_30min1


	# Train autoregression
	model = AR(train)
	model_fit = model.fit()
	window = model_fit.k_ar
	coef = model_fit.params
	# Walk forward over time steps in test
	history = train[len(train)-window:]
	history = [history[i] for i in range(len(history))]
	predictions = list()
	for t in range(len(test)):
	    length = len(history)
	    lag = [history[i] for i in range(length-window, length)]
	    yhat = coef[0]
	    for d in range(window):
		yhat += coef[d+1] * lag[window-d-1]
	    obs = test[t]
	    predictions.append(yhat)
	    history.append(obs)
	    print('predicted=%f, expected=%f' % (yhat, obs))
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
