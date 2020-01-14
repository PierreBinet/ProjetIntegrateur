import unittest
import datetime
import pandas
from pandas import Timestamp
from matplotlib import pyplot as plt
from matplotlib import dates as md
from pandas.plotting import register_matplotlib_converters
from io import StringIO
from sklearn import metrics
from sklearn.metrics import mean_squared_error
from statsmodels.tsa.ar_model import AR
import io
import sys
import numpy as np
from AR_bike_flow_prediction import *

# time stamp example
# 2 bikes leave between 0h-0h30
# 3 bikes come in between 1h30-2
# 2 bike leave and one comes between 4h30-5
# 1 bike comes in between 6-6:30
d = [(Timestamp('1900-01-01 00:07:14.899000'), -1),
     (Timestamp('1900-01-01 00:08:06.551000'), -1),
     (Timestamp('1900-01-01 01:38:26.543000'), 1),
     (Timestamp('1900-01-01 01:38:26.611000'), 1),
     (Timestamp('1900-01-01 01:38:29.956000'), 1),
     (Timestamp('1900-01-01 04:41:42.820000'), -1),
     (Timestamp('1900-01-01 04:45:38.804000'), 1),
     (Timestamp('1900-01-01 04:47:24.672000'), 1),
     (Timestamp('1900-01-01 06:18:27.066000'), 1)]


class TestBikeFlow(unittest.TestCase):

    # tests if what the function returns is a datetime
    def test_is_Datetime(self):
        self.assertEqual(type(convertStringToDatetime(10, 33, 54.543)), pandas._libs.tslibs.timestamps.Timestamp)

    # tests if the date is correct
    def test_is_correctDate(self):
        self.assertEqual(str(convertStringToDatetime(10, 33, 54.543)), "1900-01-01 10:33:54.543000")

    # tests bike flow
    def test_bike_flow(self):
        self.assertEqual(bike_per_hour(d)[0], -2)
        self.assertEqual(bike_per_hour(d)[3], 3)


if __name__ == '__main__':
    unittest.main()
