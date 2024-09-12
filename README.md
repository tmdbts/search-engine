# Search engine

A search engine developed for Distributed Systems. It is now very distributed yet!

## Build

This project is built using Maven. To build the project, run the following command:
`mvn clean compile`

You will also need the properties file to be able to run the different components of the search engine.
`cp app.properties target/classes/`

> **_NOTE_**: Loser users should use `copy` instead of `cp`.
>
> Windows users are the losers here. Don't use windows.

## Run

This project has 4 different entry points, so you will have to run them separately.

```bash
java -cp "target/classes:~/.m2/repository/org/jsoup/jsoup/1.17.2/jsoup-1.17.2.jar" pt.uc.dei.student.tmdbts.search_engine.gateway.GatewayImpl

java -cp "target/classes:~/.m2/repository/org/jsoup/jsoup/1.17.2/jsoup-1.17.2.jar" pt.uc.dei.student.tmdbts.search_engine.client.ClientImpl

java -cp "target/classes:~/.m2/repository/org/jsoup/jsoup/1.17.2/jsoup-1.17.2.jar" pt.uc.dei.student.tmdbts.search_engine.storage_barrels.Main

java -cp "target/classes:~/.m2/repository/org/jsoup/jsoup/1.17.2/jsoup-1.17.2.jar" pt.uc.dei.student.tmdbts.search_engine.downloader.Main
```