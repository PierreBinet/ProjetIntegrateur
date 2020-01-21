#!/bin/sh

sudo docker network create --driver bridge --subnet 10.0.0.0/24 net
sudo docker run -itd --network=net --name flow --ip 10.0.0.201  constancegay/projet_int:pred_flow /bin/bash
sudo docker run -itd --network=net --name user --ip 10.0.0.202  constancegay/projet_int:pred_user /bin/bash

