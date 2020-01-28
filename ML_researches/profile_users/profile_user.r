
#########################
#Fichier de preparation des donnees a du clustering dans le but de prédire le profil utilisateur.
#date : 03/12/2019
#status : fonction get_borough_index trop lourde, améliorer ou bien inclure dans le preprocess. 
#########################


library(ggplot2)
library(sf)
library(plyr)
library(dplyr)
library(tidyverse)
library(maps)
library(tibble)
library(purrr)


travel_data <- read.csv("data/201306-citibike-tripdata.csv", encoding = "UTF-8")
borough_data <- read.csv("data/nyc_zones.csv", encoding = "UTF-8")



# traitement des valeurs vides
travel_data <- na.omit(travel_data)
travel_data <- travel_data %>% filter(birth.year != "NULL" & end.station.latitude != "NULL" & end.station.longitude !="NULL")

#conversion des coordonnées des stations d'arrivée 
as.numeric.factor <- function(x) {as.numeric(levels(x))[x]}
travel_data$end.station.longitude <- mapply(as.numeric.factor, travel_data$end.station.longitude)
travel_data$end.station.latitude <- mapply(as.numeric.factor, travel_data$end.station.latitude)

#ajout du quartier à travel data
borough_data$the_geom <- st_as_sfc(borough_data$the_geom)

#pt1 = st_point(c(travel_data$start.station.longitude[1], travel_data$start.station.latitude[1]))

get_borough_index <- function (long, lat){
   index=NULL
   pt1 = st_point(c(long,lat))
   test = st_intersects(borough_data$the_geom,pt1)
   for(i in 1:nrow(borough_data)) {
     if (!(is.na(test[i]==0))){
       index = i
     }
     else {i = i+1 }
   }
   return(index)
}

travel_data$borough_start <- mapply(get_borough_index, travel_data$start.station.longitude, travel_data$start.station.latitude)
travel_data$borough_end <- mapply(get_borough_index, travel_data$end.station.longitude, travel_data$end.station.latitude)

#plot(st_geometry(borough_data$the_geom))

#nettoyage donnees travel
travel_data <- subset(travel_data, select=c(tripduration,
                                            starttime, 
                                            stoptime,
                                            borough_start,
                                            borough_end,
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

# sélection des colonnes

travel_data <- subset(travel_data, select=-c(tripduration,
                                         starttime,
                                         stoptime,
                                         Start_date,
                                         Stop_date,
                                         start_weekday_lit,
                                         stop_weekday_lit))





#replace birth year into decade
get_decade <- function(year){
   decade = year - year %% 10
   decade
}

travel_data$birth_year_decade <- as.numeric(as.character(travel_data$birth.year)) - as.numeric(as.character(travel_data$birth.year)) %% 10 
travel_data <- subset(travel_data, select=-c(birth.year))
#enregistrement du dataset .csv des entrees-sorties des velos en fct de la station et de l'heure
write.csv(travel_data, "data/clustering_profiling_dataset.csv", row.names = FALSE)


