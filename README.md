# AirTraffic Graph Database

This repository contains code and data used for developing the AirTraffic Graph Database in Neo4j.

**Repository for:**

* Team Project 2: Cypher
* Spatial Team Project
* Database 2 Course

**Contents**

* [Introduction](#introduction)
* [Contents](#what-is-inside-the-repository)
* [WebApp](#webapp)

## Introduction 

We model flights, airspaces, airports, airline companies and aircrafts in order to analyze the European air traffic.

Most of our data comes from [EUROCONTROL](https://www.eurocontrol.int), which releases flight data for R&D purposes. The data source for flights is Eurocontrol Network Manager flights plans in PRISME Data Warehouse (DWH).

Our database is intended for people working in the aviation sector, e.g. Airline companies or Airport Control Departments.


## What is inside the repository

 - `data` : zip file with data needed to build the database (available inside the Google Drive folder)
 - `scripts/Sampling_Flights`: Python script for creating the sample files about flights and FIRs.
 - `scripts/scrape_fir`: Python script for scraping the FIR information from the related [Wikipedia page](https://en.wikipedia.org/wiki/Flight_information_region) 
 - `scripts/Scrape_airlines`: Python script for scraping the Airline Companies information from the related [Wikipedia page](https://en.wikipedia.org/wiki/List_of_airline_codes) 
 - `scripts/AirTraffic_Graph`: Python script for data ingestion.
 - `scripts/maps`: Python script for query visualization in geographical maps.
 - `AirTraffic-webapp`: implementation of a webapp for result visualization.
 - `dump`: contains a dump file of the database ready to be import in neo4j (available inside the Google Drive folder)

Google drive: https://drive.google.com/drive/folders/12U_8DkDBIcKzFlR04mKsq5eDWKYA57x7?usp=drive_link


## Webapp

Inside the folder `AirTraffic-webapp` you can find the whole webapp project, developed using Java for the backend part and HTML/JS/CSS for the frontend part.
The webapp is splitted in two cores:

 - geographical maps visualization of some queries;
 - simple use-case of the database.

If you want to run the webapp locally, you can find a .war file inside `AirTraffic-webapp/target` that allows you to load everything in a web server (e. g. [Apache Tomcat](https://tomcat.apache.org/download-90.cgi))

After you upload the webapp you will be able to see all about maps, but you can't load the use-case since you do not own the database.
If you want to play also with the use-case you must load the database in [neo4j](https://neo4j.com/download/). To do that please perform the following steps:

 1. download [neo4j](https://neo4j.com/download/) if it is not installed yet
 2. create a new Project in neo4j, no matter the name of the project
 3. pick the dump file inside `dump` folder and load it in the neo4j project
 4. among the option you can find 'Create new DBMS from dump'
 5. **DISCLAIMER**: you MUST set name and password to `AirTraffic_DB`
 6. finally, start the database.

After that you will be able to play also with the use-case page.




