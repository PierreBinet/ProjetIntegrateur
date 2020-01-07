
#########################
#Fichier de preparation des donnees a du clustering dans le but de prédire le profil utilisateur.
#author : Lucile
#date : 03/12/2019
#########################


library(tidyverse)

travel_data <- read.csv("data/201306-citibike-tripdata.csv", encoding = "UTF-8")

#nettoyage donnees travel
travel_data <- subset(travel_data, select=c(tripduration,
                                            starttime, 
                                            stoptime,
                                            start.station.latitude, 
                                            start.station.longitude, 
                                            end.station.latitude, 
                                            end.station.longitude,
                                            usertype,
                                            birth.year,
                                            gender))

# transformer date en année, mois, jour semaine (1=lundi, 2=mardi...) et tranche horaire.
travel_data$Start_date <- format(as.POSIXct(travel_data$starttime,format="%Y-%m-%d %H:%M:%S"),"%Y:%m:%d")
travel_data$Stop_date <- format(as.POSIXct(travel_data$stoptime,format="%Y-%m-%d %H:%M:%S"),"%Y:%m:%d")
#le timeslot correspond à l'heure tronquée (00 : entre 0h et 1h, 01 : entre 1h et 2h, etc)
travel_data$Start_time_slot <- format(as.POSIXct(travel_data$starttime,format="%Y-%m-%d %H:%M:%S"),"%H")
travel_data$Stop_time_slot <- format(as.POSIXct(travel_data$stoptime,format="%Y-%m-%d %H:%M:%S"),"%H")

travel_data$start_date_year <- format(as.Date(travel_data$Start_date, format="%Y:%m:%d"),"%Y")
travel_data$start_date_month <- format(as.Date(travel_data$Start_date, format="%Y:%m:%d"),"%m")
travel_data$start_weekday_lit <- weekdays(as.Date(travel_data$Start_date, format="%Y:%m:%d"))

travel_data$stop_date_year <- format(as.Date(travel_data$Start_date, format="%Y:%m:%d"),"%Y")
travel_data$stop_date_month <- format(as.Date(travel_data$Start_date, format="%Y:%m:%d"),"%m")
travel_data$stop_weekday_lit <- weekdays(as.Date(travel_data$Start_date, format="%Y:%m:%d"))

  
travel_data <- travel_data %>% mutate(start_weekday = case_when(start_weekday_lit == 'lundi' ~ 1,
                                                               start_weekday_lit == 'mardi' ~ 2,
                                                               start_weekday_lit == "mercredi" ~ 3,
                                                               start_weekday_lit == "jeudi" ~ 4,
                                                               start_weekday_lit == "vendredi" ~ 5,
                                                               start_weekday_lit == "samedi" ~ 6,
                                                               start_weekday_lit == "dimanche" ~ 7))

travel_data <- travel_data %>% mutate(stop_weekday = case_when(stop_weekday_lit == 'lundi' ~ 1,
                                                                stop_weekday_lit == 'mardi' ~ 2,
                                                                stop_weekday_lit == "mercredi" ~ 3,
                                                                stop_weekday_lit == "jeudi" ~ 4,
                                                                stop_weekday_lit == "vendredi" ~ 5,
                                                                stop_weekday_lit == "samedi" ~ 6,
                                                                stop_weekday_lit == "dimanche" ~ 7))

# ajouter un booléen is_weekend (si jour_semaine = 6 ou 7) ?
travel_data <- travel_data %>% mutate(is_weekend = if_else(start_weekday >=6, 1, 0))

# comment lier latitude et longitude ? (idea : clustering --> convert latitude + longitude in a unique geographical zone id)

# sélection des colonnes

dataset <- subset(travel_data, select=-c(tripduration,
                                         starttime,
                                         stoptime,
                                         start_weekday_lit,
                                         stop_weekday_lit))

# traitement des valeurs vides
dataset <- na.omit(dataset)
dataset <- dataset %>% filter(birth.year != "NULL")

#ET POURQUOI PAS ? transformer l'année de naissance en tranche d'âge ? 

#enregistrement du dataset .csv des entrees-sorties des velos en fct de la station et de l'heure
write.csv(dataset, "data/profile_user_dataset.csv", row.names = FALSE)

