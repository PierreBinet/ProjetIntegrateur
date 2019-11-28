library(ggplot)
library(plyr)
library(dplyr)
install.packages("rjson", repos="http://cran.rstudio.com/")
install.packages("jsonlite", dependencies=TRUE, repos="http://cran.rstudio.com/")
library(rjson)
library(jsonlite)


ridershipAndMembership = read.csv("./ridershipdata/2019Q1.csv", sep=",")
#tripHistory = read.csv("./tripdata/201901-citibike-tripdata.csv", sep=",")
JCtripHistory = read.csv("./tripdata/JC-201901-citibike-tripdata.csv", sep=",")


#old code that was supposed to fetch station information using what was in the original dataset
#unused since we found the API returning station informations
#sideTableMaker = function(x) {
#  return (head(data.frame(name = x$start.station.name,latitude = x$start.station.latitude,longitude = x$start.station.longitude),1))
#}
#  
#StationTable = ddply(JCtripHistory, .(start.station.id), sideTableMaker)


drop_vec <- c("start.station.name","start.station.latitude","start.station.longitude","end.station.name","end.station.latitude","end.station.longitude")
JCtripHistory <- JCtripHistory [, ! names(JCtripHistory) %in% drop_vec, drop = TRUE]


#if needed, code to export in JSON
#JCtripJson <- toJSON(unname(split(JCtripHistory, 1:nrow(JCtripHistory))))
#write_json(JCtripJson, path = "./tripdataJson/JC-201901-citibike-tripdata.json")
