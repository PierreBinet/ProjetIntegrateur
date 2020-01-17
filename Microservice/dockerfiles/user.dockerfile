FROM python:latest

WORKDIR /

ADD . /

# install build utilities
RUN apt-get update && \
	apt-get install -y net-tools gcc make apt-transport-https ca-certificates build-essential \
	&& cat ML_researches/AR_user_profile_prediction.py > /home/AR_user_profile_prediction.py

# check our python environment
RUN python3 --version
RUN pip3 --version

# Installing python dependencies
RUN pip3 install --no-cache-dir matplotlib pandas sklearn statsmodels flask


# Running Python Application
CMD ["python3", "/home/AR_user_profile_prediction.py"]
