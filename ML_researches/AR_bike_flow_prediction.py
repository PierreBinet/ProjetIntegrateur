from datetime import datetime, timedelta
from matplotlib import pyplot as plt
from matplotlib import dates as md
from pandas.plotting import register_matplotlib_converters
from io import StringIO
from sklearn import metrics
from sklearn.metrics import mean_squared_error
from statsmodels.tsa.ar_model import AR
import io
import sys
import pandas as pd
import numpy as np


# Conversion d'un string en datetime
def convertStringToDatetime(hour, minute, second):
    date_time_str = str(hour) + ':' + str(minute) + ':' + str(round(second, 3))
    date_time_obj = datetime.strptime(date_time_str, '%H:%M:%S.%f')
    date_time_obj = pd.to_datetime(date_time_obj)
    return date_time_obj


# Réalise le range d'intevalle pour un delta donné
def datetime_range(start, end, delta):
    current = start
    while current < end:
        yield current
        current += delta


# Retourne
# La liste des dates où un vélo part d'une station
# La liste des dates où un vélo arrive à une station
# La liste des temps de trajets
def getDateData(dataset, mode="timestamp"):
    dataset = dataset.reset_index(drop=True)
    s_day = dataset['start_day']
    s_month = dataset['start_month']
    s_year = dataset['start_year']
    s_hour = dataset['start_hour']
    s_minute = dataset['start_minute']
    s_second = dataset['start_second']

    e_day = dataset['end_day']
    e_month = dataset['end_month']
    e_year = dataset['end_year']
    e_hour = dataset['end_hour']
    e_minute = dataset['end_minute']
    e_second = dataset['end_second']

    data_s = []
    data_e = []
    data_diff = []
    for i in range(len(dataset)):
        start_date = convertStringToDatetime(s_hour[i], s_minute[i], s_second[i])
        end_date = convertStringToDatetime(e_hour[i], e_minute[i], e_second[i])
        delta = end_date - start_date
        if mode == "timestamp":
            data_s.append(start_date.timestamp())
            data_e.append(end_date.timestamp())
            data_diff.append(delta.total_seconds())
        else:
            data_s.append(start_date)
            data_e.append(end_date)
            data_diff.append(delta)
    return data_s, data_e, data_diff


def bike_per_hour(data):
    bike_per_hours_list = []
    for hours in range(0, 24):
        hour_first_half = 0
        hour_second_half = 0
        count_first_half = 0
        count_second_half = 0
        for date in data:
            if hours == date[0].hour:
                if date[0].minute <= 30:
                    count_first_half += 1
                    hour_first_half += date[1]
                else:
                    count_second_half += 1
                    hour_second_half += date[1]
        if count_first_half == 0:
            bike_per_hours_list.append(0)
        else:
            bike_per_hours_list.append(hour_first_half)
        if count_second_half == 0:
            bike_per_hours_list.append(0)
        else:
            bike_per_hours_list.append(hour_second_half)

    return bike_per_hours_list


def predict(coef, history):
    yhat = coef[0]
    for i in range(1, len(coef)):
        yhat += coef[i] * history[-i]
    return yhat


def main():
    if len(sys.argv) != 4:
        print("use: day | start_station | csv trips | csv stations")
        exit(-1)

    '''    day = 1
    station_num = 3183
    trips_one_month = pd.read_csv('../RExtractor/output/JC-201904-citibike-tripdata.csv')
    stations = pd.read_csv('../RExtractor/output/stationTable.csv')'''

    day = int(sys.argv[1])
    station_num = int(sys.argv[2])
    trips_one_month = pd.read_csv(sys.argv[3])
   # stations = sys.argv[4]

    # Liste d'intervalles de 30 min sur une journée
    dts30 = [dt.strftime('%H:%M:%S.%f') for dt in
             datetime_range(datetime(2019, 1, 1, 0, 0), datetime(2019, 1, 1, 23, 59),
                            timedelta(minutes=30))]

    # On se concentre sur les trajet qui commencent ou finissent à une station donnée
    dataset_station_s = trips_one_month[trips_one_month.start_station_id == station_num]

    dataset_station_e = trips_one_month[trips_one_month.end_station_id == station_num]

    # On extrait tout les mardis du mois (one day)
    dataset_station_s_day = dataset_station_s[trips_one_month.start_day % 7 == day]
    dataset_station_s_day.reset_index(drop=True)
    dataset_station_e_day = dataset_station_e[trips_one_month.start_day % 7 == day]
    dataset_station_e_day.reset_index(drop=True)
    # et les lundis (second day)
    dataset_station_s_day_train = dataset_station_s[trips_one_month.start_day % 7 != day]
    dataset_station_s_day_train.reset_index(drop=True)
    dataset_station_e_day_train = dataset_station_e[trips_one_month.start_day % 7 != day]
    dataset_station_e_day_train.reset_index(drop=True)

    # en utilisant la fonction getDateData,
    # on extrait toutes les entrées et sorties de vélo dans la station pour un jour de la semaine donné:

    data_s, _, _ = getDateData(dataset_station_s_day, "notimestamp")
    _, data_e, _ = getDateData(dataset_station_e_day, "notimestamp")

    data_s_train, _, _ = getDateData(dataset_station_s_day_train, "notimestamp")
    _, data_e_train, _ = getDateData(dataset_station_e_day_train, "notimestamp")

    # Les sorties de vélos (lorsque la course démarre dans la station) sont comptées négativement (-1 vélo)
    l1 = [-1] * len(data_s)
    # Les fin de courses sont comptées positivement (+1 vélo dans la station)
    l2 = [1] * len(data_e)

    l3 = [-1] * len(data_s_train)
    l4 = [1] * len(data_e_train)

    # On associe les listes de 1/-1
    data_s = list(zip(data_s, l1))
    data_e = list(zip(data_e, l2))
    # et on les additionne
    d = data_s + data_e
    d.sort(key=lambda tup: tup[0])

    data_s_train = list(zip(data_s_train, l3))
    data_e_train = list(zip(data_e_train, l4))
    d_train = data_s_train + data_e_train
    d_train.sort(key=lambda tup: tup[0])

    data_diff = bike_per_hour(d)
    data_diff_train = bike_per_hour(d_train)

    # on train avec les mardis, on teste avec les lundis
    train, test = data_diff_train, data_diff
    # train autoregression
    model = AR(train)
    model_fit = model.fit(maxlag=6, disp=False)
    window = model_fit.k_ar
    coef = model_fit.params
    # walk forward over time steps in test
    history = [train[i] for i in range(len(train))]
    predictions = list()
    for t in range(len(test)):
        yhat = predict(coef, history)
        obs = test[t]
        predictions.append(yhat)
        history.append(obs)
    error = mean_squared_error(test, predictions)
    # print('Test MSE: %.3f' % error)

    dates = np.arange(0, 24, 0.5)
    xfmt = md.DateFormatter('%H:%M:%S')
    plt.rcParams['figure.figsize'] = [10, 5]
    plt.xticks(rotation=90, )
    plt.plot(dts30, test)
    plt.plot(dts30, predictions, color='red')
    # plt.show()

    # serialize the image into bytearray png
    buf = io.BytesIO()
    plt.savefig(buf, format='png')
    # convert the bytearray into a hexa string
    hexstring = ''.join(format(x, '02x') for x in buf.getvalue())

    # return the string to the micro service
    print(hexstring)


if __name__ == "__main__":
    main()