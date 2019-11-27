library(ggplot)
library(plyr)
library(dplyr)

ridershipAndMembership = read.csv("./ridershipdata/2019Q1.csv", sep=",")
#tripHistory = read.csv("./tripdata/201901-citibike-tripdata.csv", sep=",")
JCtripHistory = read.csv("./tripdata/JC-201901-citibike-tripdata.csv", sep=",")


sideTableMaker = function(x) {
  return (head(data.frame(name = x$start.station.name,latitude = x$start.station.latitude,longitude = x$start.station.longitude),1))
}
  
StationTable = ddply(JCtripHistory, .(start.station.id), sideTableMaker)

drop_vec <- c("start.station.name","start.station.latitude","start.station.longitude","end.station.name","end.station.latitude","end.station.longitude")
JCtripHistory <- JCtripHistory [, ! names(JCtripHistory) %in% drop_vec, drop = TRUE]
