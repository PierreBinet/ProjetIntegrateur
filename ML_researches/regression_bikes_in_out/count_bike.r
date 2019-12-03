
#########################
#Fichier de préparation des données à une régression sur le nombre de vélos entrants et sortants
#à un instant donné pour une station.
#author : Lucile
#date : 03/12/2019
#########################


library(tidyverse)

travel_data <- read.csv("data/201306-citibike-tripdata.csv", encoding = "UTF-8")

#nettoyage données travel
travel_data <- subset(travel_data, select=-c(start.station.latitude, 
                                             start.station.longitude, 
                                             end.station.latitude, 
                                             end.station.longitude,
                                             bikeid,
                                             usertype,
                                             birth.year,
                                             gender))


#convert datetime to 2 columns date and time
travel_data$Start_time <- format(as.POSIXct(travel_data$starttime,format="%Y-%m-%d %H:%M:%S"),"%H:%M:%S")
travel_data$Stop_time <- format(as.POSIXct(travel_data$stoptime,format="%Y-%m-%d %H:%M:%S"),"%H:%M:%S")

travel_data$Start_date <- format(as.POSIXct(travel_data$starttime,format="%Y-%m-%d %H:%M:%S"),"%Y:%m:%d")
travel_data$Stop_date <- format(as.POSIXct(travel_data$stoptime,format="%Y-%m-%d %H:%M:%S"),"%Y:%m:%d")


#puis suppression des datetime d'origine
travel_data <- subset(travel_data, select=-c(starttime, stoptime))

#création du dataset avec les trajets
write.csv(travel_data, "data/travel_data.csv")


####compte des vélos in et out par station et date+plage horaire
#on veut vélos entrants et sortants par jour par heure (de 0h à 1h puis de 1h à 2h etc)

#time slots
travel_data$start_time_slot <- as.POSIXlt(travel_data$Start_time,'%H:%M',tz='')
travel_data$start_time_slot <- paste(format(travel_data$start_time_slot,'%H:00'),format(travel_data$start_time_slot+3600,'%H:00'),sep='-')

travel_data$stop_time_slot <- as.POSIXlt(travel_data$Stop_time,'%H:%M',tz='')
travel_data$stop_time_slot <- paste(format(travel_data$stop_time_slot,'%H:00'),format(travel_data$stop_time_slot+3600,'%H:00'),sep='-')


#compte vélos entrants par station par time slot
bike_entrances_per_station_per_slot <- travel_data %>% group_by(date = Start_date, station_id=start.station.id, time_slot = start_time_slot) %>% summarise(nb_entrance = n())

#compte vélos sortants par station par time slot
bike_departures_per_station_per_slot <- travel_data %>% group_by(date = Stop_date, station_id =end.station.id, time_slot = stop_time_slot) %>% summarise(nb_departures = n())

#compte des vélos (in et out) par station par date+plage horaire
bike_in_and_out <- merge(bike_entrances_per_station_per_slot, bike_departures_per_station_per_slot, by=c("date", "station_id", "time_slot"), all = TRUE)
bike_in_and_out[is.na(bike_in_and_out)] <- 0

#enregistrement du dataset .csv des entrées-sorties des vélos en fct de la station et de l'heure
write.csv(bike_in_and_out, "data/bike_in_and_out.csv", row.names = FALSE)