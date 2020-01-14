# -*- coding: utf-8 -*-


"""
Created on Wed Nov 27 14:58:34 2019

@author: Lucile & Pierre

state : does not compile
"""


# imports
from sklearn.cluster import DBSCAN
from datetime import datetime, timedelta
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
import io
import sys


# Retourne les datasets où le départ ou l'arrivée est à une station donnée
def find_by_start_station_id(dataset, id_start_station):
    dataset_station_s = dataset[dataset.start_station_id == id_start_station]
    return dataset_station_s


def extract_one_day(dfs, day, train):
    """ method one : arbitrary train/test couples depending on the day (putting weekdays together
    if train:
        df_one_day = dfs[dfs.start_wday == day]
    else:
        if day == 0:
            df_one_day = dfs[dfs.start_wday == day+1]
        elif day == 5:
            df_one_day = dfs[dfs.start_wday == 4]
        elif day == 6:
            df_one_day = dfs[dfs.start_wday == 0]
        else:  # day in range(1,4)
            df_one_day = dfs[dfs.start_wday == 6]
    """

    # method 2
    if train:
        df_one_day = dfs[dfs.start_wday != day]
    else:
        df_one_day = dfs[dfs.start_wday == day]

    return df_one_day

# Réalise le range d'intevalle pour un delta donné
def datetime_range(start, end, delta):
    current = start
    while current < end:
        yield current
        current += delta

def column_extraction(dataset, pred_type):
    lst = []
    if pred_type == 0:  # usertype prediction
        lst = dataset['usertype'].tolist()
    elif pred_type == 1:  # gender prediction
        for line in dataset['gender']:
            if line == 1:  # male
                lst.append(0)
            elif line == 2:  # female
                lst.append(1)
            else:  # unknown
                lst.append(0.5)
    elif pred_type == 2:  # birth year
        lst = dataset['birth.year'].tolist()
    return lst


# Conversion d'un string en datetime
def convert_string_to_datetime(hour, minute, second):
    date_time_str = str(hour)+':'+str(minute)+':'+str(round(second, 3))
    date_time_obj = datetime.strptime(date_time_str, '%H:%M:%S.%f')
    date_time_obj = pd.to_datetime(date_time_obj)
    return date_time_obj


# récupère les dates du dataset après extraction station/jour et récupère les dates, les convertit en timestamp
def get_date_data(dataset, mode="timestamp"):
    dataset = dataset.reset_index(drop=True)
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
def avg_user_per_halfhour(data, pred_type):
    avg_data_per_halfhour_list = []
    for hours in range(0, 23):
        first_halfhour = 0
        second_halfhour = 0
        number_of_data_fh = 0
        number_of_data_sh = 0
        for line in data:
            if hours == line[0].hour:
                if line[0].minute < 30:
                    first_halfhour += line[1]
                    number_of_data_fh += 1
                else:
                    second_halfhour += line[1]
                    number_of_data_sh += 1

        # possibility of having no clients at all during an half hour
        # (often during nighttime)
        if number_of_data_fh != 0:
            first_halfhour = first_halfhour/number_of_data_fh
        elif not avg_data_per_halfhour_list:  # no client and first half hour of the day
            if pred_type == 0 or pred_type == 1:  # if we consider gender or usertype, we put a default "0"
                first_halfhour = 0
            else:  # if we consider birthyear we put a default 1990
                first_halfhour = 1990
        else:
            first_halfhour = avg_data_per_halfhour_list[-1]

        avg_data_per_halfhour_list.append(first_halfhour)

        # same thing for the second half hour
        if number_of_data_sh != 0:
            second_halfhour = second_halfhour/number_of_data_sh
        else:
            second_halfhour = avg_data_per_halfhour_list[-1]

        avg_data_per_halfhour_list.append(second_halfhour)

    return avg_data_per_halfhour_list


def main():

    if len(sys.argv) != 5:
        print("autoregression algorithm should be called with 4 arguments")
        print("use: day | start_station | csv trips(one month) | prediction type (0 for usertype)")
        sys.exit(-1)

    argday = int(sys.argv[1])
    argstation_num = int(sys.argv[2])
    argtrips_one_month = sys.argv[3]
    # argstations = sys.argv[4]
    argpredtype = int(sys.argv[4])
    """

    # tests
    # lundi = 1
    argday = 2
    # id 3255 (8 Ave & W 31 St in NY)
    # id 3183 (JC)
    argstation_num = int(3183)
    argtrips_one_month = '../RExtractor/output/JC-201811-citibike-tripdata.csv'
    argpredtype = 0
    # argstations = sys.argv[4]
    """


    # extraction from csv
    df = pd.read_csv(argtrips_one_month, sep=',', header=0)

    df_station = find_by_start_station_id(df, argstation_num)
    # print(df_station)
    # print(df_station[['start_year', 'start_month', 'start_day', 'start_hour', 'start_minute', 'start_second']])

    # delete incomplete data
    df = df.dropna()

    # train on mondays, test on tuesdays
    df_wday1 = extract_one_day(df_station, argday, train=1)
    df_wday2 = extract_one_day(df_station, argday, train=0)

    # print("df_wday1['usertype'] : "+str(df_wday1['usertype']))
    # print(type(df_wday1))

    list_df_wday1 = column_extraction(df_wday1, argpredtype)
    list_df_wday2 = column_extraction(df_wday2, argpredtype)

    # print("\n list_df_wday1 : "+str(list_df_wday1))
    # print(type(list_df_wday1))

    sorted_dates1 = get_date_data(df_wday1, "notimestamp")
    sorted_dates2 = get_date_data(df_wday2, "notimestamp")

    # print("\n sorted_dates1 : "+str(sorted_dates1))
    # print(type(sorted_dates1))

    tuples1 = list(zip(sorted_dates1, list_df_wday1))
    tuples2 = list(zip(sorted_dates2, list_df_wday2))

    # print("\n tuples1 : "+str(tuples1))
    # print(type(tuples1))

    averaged_data_per_30min1 = avg_user_per_halfhour(tuples1, argpredtype)
    averaged_data_per_30min2 = avg_user_per_halfhour(tuples2, argpredtype)

    #print("\n averaged_data_per_30min1 (train): ")
    #print(averaged_data_per_30min1)
    #print("len : "+str(len(averaged_data_per_30min1)))
    #print("\n averaged_data_per_30min2 (test): ")
    #print(averaged_data_per_30min2)
    #print("len : "+str(len(averaged_data_per_30min2)))

    train, test = averaged_data_per_30min1, averaged_data_per_30min2

    # Train autoregression
    model = AR(train)
    model_fit = model.fit()
    window = model_fit.k_ar
    # print("\n window : "+str(window))

    coef = model_fit.params
    # print("\n coef : "+str(coef))

    # Walk forward over time steps in test
    history = train[len(train)-window:]
    # print("\n history : "+str(history))

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
        # print('predicted=%f, expected=%f' % (yhat, obs))

        # error = metrics.mean_squared_error(test, predictions)
        # print('Test MSE: %.3f' % error)

    # Liste d'intervalles de 30 min sur une journée

    # plot
    dates = np.arange(0, 24, 0.5)
    xfmt = md.DateFormatter('%H:%M:%S')
    plt.rcParams['figure.figsize'] = [10, 5]
    plt.xticks(rotation=90, )
    plt.plot(test)
    plt.plot(predictions, color='red')
    #plt.show()

    # serialize the image into bytearray png
    buf = io.BytesIO()
    plt.savefig(buf, format='png')
    # convert the bytearray into a hexa string
    hexstring = ''.join(format(x, '02x') for x in buf.getvalue())

    # return the string to the micro service
    print(hexstring)

if __name__ == "__main__":
    main()
