    #install.packages("rjson", repos="http://cran.rstudio.com/")
    #install.packages("jsonlite", dependencies=TRUE, repos="http://cran.rstudio.com/")
    library(plyr)
    library(dplyr)
    library(rjson)
    library(jsonlite)
    
    df = read.csv("./station_data/station_data.csv")
    StationTable = data.frame(id = df$stationBeanList__id, name = df$stationBeanList__stationName, capacity = df$stationBeanList__totalDocks, latitude = df$stationBeanList__latitude, longitude = df$stationBeanList__longitude)
    write.csv(StationTable,'./output/stationTable.csv', row.names = FALSE)
    
    #ridershipAndMembership = read.csv("./ridershipdata/2019Q1.csv", sep=",")
    #tripHistory = read.csv("./tripdata/201901-citibike-tripdata.csv", sep=",")
    
    #list all files in tripdata
    mydir = "./tripdata"
    myfiles = list.files(path=mydir, pattern="*.csv", full.names=TRUE)
    
    for (i in 1:length(myfiles)){
     
      JCtripHistory = read.csv(myfiles[i], sep=",")
      
      start_date<- as.POSIXlt(JCtripHistory$starttime)
      end_date <- as.POSIXlt(JCtripHistory$stoptime)
      JCtripHistory$start_day= start_date$mday
      JCtripHistory$start_wday= as.POSIXlt(as.Date(start_date))$wday
      JCtripHistory$start_month = start_date$mon + 1
      JCtripHistory$start_year = start_date$year + 1900
      JCtripHistory$start_hour = start_date$hour
      JCtripHistory$start_minute = start_date$min
      JCtripHistory$start_second = start_date$sec
      
      JCtripHistory$end_day= end_date$mday
      JCtripHistory$end_wday= as.POSIXlt(as.Date(end_date))$wday
      JCtripHistory$end_month = end_date$mon + 1
      JCtripHistory$end_year = end_date$year + 1900
      JCtripHistory$end_hour = end_date$hour
      JCtripHistory$end_minute = end_date$min
      JCtripHistory$end_second = end_date$sec
      
      JCtripHistory <- JCtripHistory %>% mutate(usertype = ifelse(usertype == "Subscriber",0,1))
      
      drop_vec <- c("start.station.name","start.station.latitude","start.station.longitude","end.station.name","end.station.latitude","end.station.longitude","starttime","stoptime")
      JCtripHistory <- JCtripHistory [, ! names(JCtripHistory) %in% drop_vec, drop = TRUE]
      
      names(JCtripHistory)[names(JCtripHistory) == "start.station.id"] <- "start_station_id"
      names(JCtripHistory)[names(JCtripHistory) == "end.station.id"] <- "end_station_id"
    
      filename = strsplit(myfiles[i],"/")[[1]][3]
      write.csv(JCtripHistory,paste('./output',filename,sep="/"), row.names = FALSE)}
    
    
    #old code that was supposed to fetch station information using what was in the original dataset
    #unused since we found the API returning station informations
    #sideTableMaker = function(x) {
    #  return (head(data.frame(name = x$start.station.name,latitude = x$start.station.latitude,longitude = x$start.station.longitude),1))
    #}
    #StationTable = ddply(JCtripHistory, .(start.station.id), sideTableMaker)
    
    
    #if needed, code to export in JSON
    #JCtripJson <- toJSON(unname(split(JCtripHistory, 1:nrow(JCtripHistory))))
    #write_json(JCtripJson, path = "./tripdataJson/JC-201901-citibike-tripdata.json")
