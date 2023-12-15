/**
 * Manage the Connection with neo4j
 *
 * @author SpatialTeam
 * @version 1.00
 * @since 1.00
 */package servlet;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Record;
import resources.Airline;
import resources.Flight;
import resources.Travers;

import java.util.ArrayList;

import static org.neo4j.driver.Values.parameters;

public class Connection implements AutoCloseable
{
    private final Driver driver;

    public Connection( String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    public Session getSession(){
        return driver.session();
    }
    @Override
    public void close() throws Exception
    {
        driver.close();
    }

    public void printGreeting( final String message )
    {
        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( tx ->
            {
                Result result = tx.run( "CREATE (a:Greeting) " +
                                "SET a.message = $message " +
                                "RETURN a.message + ', from node ' + id(a)",
                        parameters( "message", message ) );
                return result.single().get( 0 ).asString();
            } );
            System.out.println( greeting );
        }
    }
    public void printFlight( final String flightID )
    {
        try ( Session session = driver.session() )
        {
            Flight flight = session.writeTransaction(tx ->
            {
                Result res = tx.run("MATCH (f:Flight{id:$id})-[arr:ARRIVES]->(a:Airport) " +
                                "MATCH (air:Airline)<-[]-(f)-[dep:DEPARTS]->(d:Airport) " +
                                "return DISTINCT f.id AS id ,air.name AS airline,a.name AS airArr,d.name AS airDep,arr.actualArrival AS timeArr,dep.actualOffBlock AS timeDep",
                        parameters("id", flightID) );
                Record r = res.single();
                return new Flight(r.get("id").asString(),r.get("airline").asString(),r.get("airDep").asString(),r.get("airArr").asString(),r.get("timeDep").asZonedDateTime(),r.get("timeArr").asZonedDateTime());
            } );
            System.out.println(flight.toString());
        }
    }
    public  void printFir(final String airportIcao, final String cityFrom){
        try ( Session session = driver.session() ) {
            ArrayList<Record> result = session.writeTransaction(tx ->
            {
                Result res = tx.run("MATCH (f:Flight)-[arr:ARRIVES]->(a:Airport{icao:$icao}) " +
                                "MATCH (f)-[dep:DEPARTS]->(d:Airport)-[:LOCATED]->(m:City{name:$cityFrom}) " +
                                "WITH f LIMIT 1 " +
                                "MATCH (f)-[t:TRAVERS]->(fir:Airspace) " +
                                "RETURN f.id AS id, fir.icao AS icao, fir.type AS type, fir.name AS name, " +
                                "t.entryTime AS entry,t.exitTime AS exit, t.sequence AS seq ORDER BY t.sequence",
                        parameters("cityFrom", cityFrom, "icao", airportIcao));
                ArrayList<Record> r = new ArrayList<>();
                while (res.hasNext()) {
                    r.add(res.next());
                }
                return r;
            });
            ArrayList<Travers> firs = new ArrayList<>();
            for(Record r : result){
                Travers c = new Travers(r.get("id").asString(),r.get("icao").asString(),r.get("type").asString(),r.get("name").asString(),r.get("seq").asInt(),r.get("entry").asZonedDateTime(),r.get("exit").asZonedDateTime());
                //flightID = r.get("id").asString();
                firs.add(c);
            }
            System.out.println(firs.get(0).getEntryTime().toString());
            System.out.println(firs.get(0).getEntryTime());
        }
    }

    public void printAirline(String cityFrom,String airportIcao) throws Exception {
        Connection conn;
        Session session;

        try {
            conn = new Connection("bolt://localhost:7687", "neo4j", "AirTraffic_DB");
            session = conn.getSession();
            //get the airlines
            ArrayList<Record> result = session.writeTransaction(tx ->
            {
                Result res = tx.run("MATCH (f:Flight)-[arr:ARRIVES]->(a:Airport{icao:$icao}) " +
                                "MATCH (air:Airline)<-[]-(f)-[dep:DEPARTS]->(d:Airport)-[:LOCATED]->(m:City{name:$cityFrom}) "+
                                "RETURN DISTINCT air.icao AS icao , air.name AS name",
                        parameters("cityFrom", cityFrom, "icao", airportIcao));
                ArrayList<Record> r = new ArrayList<>();
                while (res.hasNext()) {
                    r.add(res.next());
                }
                return r;
            });
            ArrayList<Airline> airlines = new ArrayList<>();
            for (Record r : result) {
                Airline c = new Airline(r.get("icao").asString(), r.get("name").asString());
                //System.out.println(c.toString());
                airlines.add(c);
            }
            String str = "";
            for(Airline a : airlines)
                str+=a+",";
            System.out.println(str);
        }finally {
            session = null;
            conn = null;
        }
    }
    public static void main( String... args ) throws Exception
    {
        try ( Connection greeter = new Connection( "bolt://localhost:7687", "neo4j", "AirTraffic_DB" ) )
        {
            greeter.printFlight("227847023");
            //greeter.printFir("EDDK","Milan");
            greeter.printAirline("EDDL","Milan");
        }
    }
}