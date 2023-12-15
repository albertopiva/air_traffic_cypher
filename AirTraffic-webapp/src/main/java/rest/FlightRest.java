/**
 * Manage the REST call about flights
 *
 * @author SpatialTeam
 * @version 1.00
 * @since 1.00
 */
package rest;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import resources.*;
import servlet.Connection;
import utils.ErrorCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

import static org.neo4j.driver.Values.parameters;

public class FlightRest extends RestResource {

    /**
     * The constructor of the REST Resource.
     *
     * @param req the HTTPSServletRequest object.
     * @param res the HTTPSServletResponse object.
     */
    public FlightRest(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }


    public void getCountryOut(String city) throws Exception {
        Connection conn;
        Session session;

        try{
            conn = new Connection( "bolt://localhost:7687", "neo4j", "AirTraffic_DB" );
            session = conn.getSession();
            ArrayList<Record> result = session.writeTransaction(tx ->
            {
                //find the countries reachable from Milan Malpensa Airport
                String query = """
                        MATCH (f:Flight)-[:ARRIVES]->(a:Airport)-[:LOCATED]->(c:City)-[]->(co:Country)
                        MATCH (f)-[dep:DEPARTS]->(d:Airport{icao:$icaoFrom})
                        WHERE f.marketSegment <> \"All-Cargo\" 
                        RETURN DISTINCT co.name AS name ,co.isocode AS code ORDER BY name
                        """;
                Result res = tx.run(query, parameters( "icaoFrom", city ) );
                ArrayList<Record> r = new ArrayList<>();
                while(res.hasNext()){
                    r.add(res.next());
                }
                return r;
            } );
            ArrayList<Country> country = new ArrayList<Country>();
            for(Record r : result){
                country.add(new Country(r.get("code").asString(),r.get("name").asString()));
            }
            session.close();
            conn.close();

            if (country.size() == 0) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writeError(res, ErrorCode.COUNTRY_NOT_FOUND);
            } else {
                ResourceList<Country> lists = new ResourceList<>(country);
                res.setStatus(HttpServletResponse.SC_OK);
                lists.toJSON(res.getOutputStream());
            }
        }finally {
            session = null;
            conn = null;
        }
    }

    public void getAirportToCountry(String cityFrom,String isocode) throws Exception {
        Connection conn;
        Session session;

        try{
            conn = new Connection( "bolt://localhost:7687", "neo4j", "AirTraffic_DB" );
            session = conn.getSession();
            ArrayList<Record> result = session.writeTransaction(tx ->
            {
                //Given a specific country, find which airport and city are reachable from Milan Malpensa Airport
                String query= """
                        MATCH (f:Flight)-[arr:ARRIVES]->(a:Airport)-[:LOCATED]->(c:City)-[]->(co:Country{isocode:$iso})
                        MATCH (f)-[dep:DEPARTS]->(d:Airport{icao:$icaoFrom})
                        WHERE f.marketSegment <> \"All-Cargo\" 
                        RETURN DISTINCT a.icao AS icao, a.name AS name, c.name AS city
                        ORDER BY city,name
                        """;
                Result res = tx.run(query, parameters("icaoFrom", cityFrom ,"iso", isocode) );
                ArrayList<Record> r = new ArrayList<>();
                while(res.hasNext()){
                    r.add(res.next());
                }
                return r;
            } );
            ArrayList<Airport> airports = new ArrayList<>();
            for(Record r : result){
                Airport c = new Airport(r.get("icao").asString(),r.get("name").asString(),r.get("city").asString());
                //System.out.println(c.toString());
                airports.add(c);
            }
            session.close();
            conn.close();

            if (airports.size() == 0) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writeError(res, ErrorCode.COUNTRY_NOT_FOUND);
            } else {
                ResourceList<Airport> lists = new ResourceList<>(airports);
                res.setStatus(HttpServletResponse.SC_OK);
                lists.toJSON(res.getOutputStream());
            }
        }finally {
            session = null;
            conn = null;
        }
    }

    public void getFirToAirport(String cityFrom,String airportIcao) throws Exception {
        Connection conn;
        Session session;

        try{
            conn = new Connection( "bolt://localhost:7687", "neo4j", "AirTraffic_DB" );
            session = conn.getSession();
            //get the airlines
            ArrayList<Record> result = session.writeTransaction(tx ->
            {
                //Given a specific destination airport, find which airlines performs flights
                //from Milan Malpensa to this airport
                String query = """
                        MATCH (d:Airport{icao:$icaoFrom})<-[:DEPARTS]-(f:Flight)-[:ARRIVES]->(a:Airport{icao:$icao})
                        MATCH (f)-[:PERFORMEDBY]->(air:Airline) 
                        RETURN DISTINCT air.icao AS icao , air.name AS name
                        """;
                Result res = tx.run(query, parameters("icaoFrom", cityFrom ,"icao", airportIcao) );
                ArrayList<Record> r = new ArrayList<>();
                while(res.hasNext()){
                    r.add(res.next());
                }
                return r;
            } );
            ArrayList<Airline> airlines = new ArrayList<>();
            for(Record r : result){
                Airline c = new Airline(r.get("icao").asString(),r.get("name").asString());
                //System.out.println(c.toString());
                airlines.add(c);
            }


            //get the firs

            result = session.writeTransaction(tx ->
            {
                //Given the destination airport, choose one flight from Milan Malpensa to this airport
                //and find which airspaces travers during the flight.
                String query = """
                        MATCH (d:Airport{icao:$icaoFrom})<-[:DEPARTS]-(f:Flight)-[arr:ARRIVES]->(a:Airport{icao:$icao}) 
                        WITH f LIMIT 1 
                        MATCH (f)-[t:TRAVERS]->(fir:Airspace) 
                        OPTIONAL MATCH (fir)-[:BELONGSTO]->(cou:Country)
                        RETURN f.id AS id, fir.icao AS icao, fir.type AS type, cou.name AS name, 
                        t.entryTime AS entry,t.exitTime AS exit, t.sequence AS seq ORDER BY t.sequence""";
                Result res = tx.run(query, parameters("icaoFrom", cityFrom ,"icao", airportIcao) );
                ArrayList<Record> r = new ArrayList<>();
                while(res.hasNext()){
                    r.add(res.next());
                }
                return r;
            } );
            String flightID = null;
            ArrayList<Travers> firs = new ArrayList<>();
            for(Record r : result){
                Travers c = new Travers(r.get("icao").asString(),r.get("type").asString(),r.get("name").asString(),r.get("id").asString(),r.get("seq").asInt(),r.get("entry").asZonedDateTime(),r.get("exit").asZonedDateTime());
                flightID = r.get("id").asString();
                firs.add(c);
            }

            String finalFlightID = flightID;
            Flight flight = session.writeTransaction(tx ->
            {
                //Given the code of the flight, find all the important informations
                String query = """
                        MATCH (d:Airport) <-[dep:DEPARTS]-(f:Flight{id:$id})-[arr:ARRIVES]->(a:Airport) 
                        OPTIONAL MATCH (f)-[:PERFORMEDBY]->(air:Airline)
                        RETURN DISTINCT f.id AS id ,air.name AS airline,a.name AS airArr,d.name AS airDep,
                        arr.actualArrival AS timeArr,dep.actualOffBlock AS timeDep
                        """;
                Result res = tx.run(query, parameters("id", finalFlightID) );
                Record r = res.single();
                return new Flight(r.get("id").asString(),r.get("airline").asString(),r.get("airDep").asString(),r.get("airArr").asString(),r.get("timeDep").asZonedDateTime(),r.get("timeArr").asZonedDateTime());
            } );
            session.close();
            conn.close();
            ArrayList<Resource> all = new ArrayList<>();
            all.add(flight);
            for(Airline a: airlines){
                all.add(a);
            }
            for(Travers a: firs){
                all.add(a);
            }
            if (firs.size() == 0) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writeError(res, ErrorCode.COUNTRY_NOT_FOUND);
            } else {
                ResourceList<Resource> lists = new ResourceList<>(all);
                res.setStatus(HttpServletResponse.SC_OK);
                lists.toJSON(res.getOutputStream());
            }
        }finally {
            session = null;
            conn = null;
        }
    }

}
