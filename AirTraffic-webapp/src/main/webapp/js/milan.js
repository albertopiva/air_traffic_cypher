
document.addEventListener('DOMContentLoaded', function(event) {
    // your page initialization code here
    // the DOM will be available here
    loadCountry();

});

function loadCountry(){
    var url = contextPath+"flights/country/LIMC";

    console.log(url);
    genericGETRequest(url, fillCountry);
}

function fillCountry(req){
    if (req.readyState === XMLHttpRequest.DONE) {
        if (req.status === 200) {
            //{"resource-list":[{"Conference":{"AlphanumericCode":"7jWMBB5XE5","Date":"2021-04-26","Title":"webapp","Description":"cciao","OrganizerID":"987654321","ConferenceRoomID":"CF1"}}]}
            var list = JSON.parse(req.responseText)['resource-list'];
            console.log(list);

            var divTop = document.getElementById("select");
            divTop.innerHTML="";
            var divSelect = document.createElement("div");
            divSelect.setAttribute("class","center");
            var span = document.createElement("span");
            span.innerHTML ="Select the destination country:";
            divSelect.append(span);
            var select = document.createElement("select");
            select.setAttribute("name","selectCountry");
            select.setAttribute("id","selectCountry");
            for (var i = 0; i < list.length; i++) {
                var country = list[i]['country'];
                var option = document.createElement("option");
                option.setAttribute("value",country['isoCode']);
                option.innerHTML=country['name'];
                select.add(option);
            }
            divSelect.append(select);
            var button = document.createElement("button");
            button.setAttribute("id","loadTable");
            button.setAttribute('onclick','loadTable();');
            button.innerHTML="LOAD";
            divSelect.append(button);
            divTop.append(divSelect);

            /*var divRight = document.getElementById("pageRight");
            divRight.innerHTML = "";
            divRight.append(loadImage("/AirTraffic-1.00/media/query/Milan_query/country.png"));
*/

        }else if(req.status==400){
            alert("Bad Request!");
            window.location.href = (contextPath+"/html/home.html");
        }
        else {
            console.log(req.responseText);
            alert("problem processing the request");
        }
    }
}


function loadTable(){
    var isocode = document.getElementById("selectCountry").selectedOptions[0].value;
    var url = contextPath+"flights/airports/LIMC/"+isocode;
    console.log(url);
    genericGETRequest(url, fillAirports);
}

function fillAirports(req){
    if (req.readyState === XMLHttpRequest.DONE) {
        if (req.status === 200) {
            //{"resource-list":[{"Conference":{"AlphanumericCode":"7jWMBB5XE5","Date":"2021-04-26","Title":"webapp","Description":"cciao","OrganizerID":"987654321","ConferenceRoomID":"CF1"}}]}
            var list = JSON.parse(req.responseText)['resource-list'];
            console.log(list);
            //fill conference

            var divResult = document.getElementById("result");
            divResult.innerHTML="";
            document.getElementById("example").innerHTML = "";
            var table = document.createElement("table");
            var thead = document.createElement("thead");

            var th = document.createElement("th");
            th.innerHTML="Airport Name";
            thead.append(th);

            th = document.createElement("th");
            th.innerHTML="City Name";
            thead.append(th);
            th = document.createElement("th");
            th.setAttribute("class","expand")
            thead.append(th);

            table.append(thead);

            var tbody = document.createElement("tbody");
            for(var i=0; i < list.length;i++){
                var airport = list[i]['airport'];
                var tr = document.createElement("tr");
                var td = document.createElement("td");
                td.innerHTML=airport.name;
                tr.append(td);
                var td = document.createElement("td");
                td.innerHTML=airport.city;
                tr.append(td);
                var td = document.createElement("td");
                td.setAttribute("class","expand")
                var img = document.createElement("img");
                img.setAttribute("src","../media/plus.png");
                img.setAttribute("style","cursor:pointer");
                img.setAttribute("onclick","loadFir(\""+airport.icao+"\")");
                td.append(img);
                tr.append(td);

                tbody.append(tr);
            }
            table.append(tbody);
            divResult.append(table);

            /*var divRight = document.getElementById("pageRight");
            divRight.innerHTML = "";
            divRight.append(loadImage("/AirTraffic-1.00/media/query/Milan_query/country.png"));
            divRight.append(loadImage("/AirTraffic-1.00/media/query/Milan_query/airport_city.png"));
*/
        }else if(req.status==400){
            alert("Bad Request!");
            window.location.href = (contextPath+"/html/home.html");
        }
        else {
            console.log(req.responseText);
            alert("problem processing the request");
        }
    }
}


function loadFir(airport_icao){
    var url = contextPath+"flights/fir/LIMC/"+airport_icao;
    console.log(url);
    genericGETRequest(url, fillFir);
}

function fillFir(req){
    if (req.readyState === XMLHttpRequest.DONE) {
        if (req.status === 200) {
            console.log(req.responseText)
            var list = JSON.parse(req.responseText)['resource-list'];
            console.log(list);
            var flight = list[0]['flight']
            var airlines = [];
            var index = 0;
            for(var i = 1; i < list.length;i++){
                if(list[i]['airline'] != null)
                    airlines[index++] = list[i]['airline'];
            }
            var travers = [];
            index = 0;
            for(var i = 1; i < list.length;i++){
                if(list[i]['travers'] != null)
                    travers[index++] = list[i]['travers'];
            }
            console.log(flight);
            console.log(airlines);
            console.log(travers);
            var divExample = document.getElementById("example");
            divExample.innerHTML=""
            var p = document.createElement("p");
            p.setAttribute("class","p-airlines");
            var str = "";
            for(var i = 0; i < airlines.length;i++){
                str+=airlines[i].name;
                if(i< airlines.length-1)
                    str+=", ";
            }
            //Manage the airlines
            if(airlines != null && airlines.length>0)
                p.innerHTML = "The route <b>"+(flight.airportDep)+"-"+(flight.airportArr)+"</b> is covered by these airlines: "+str;
            else
                p.innerHTML = "Data about airlines are not available for this route.";
            divExample.append(p);

            //Manage the example flight
            var p1 = document.createElement("p");
            p1.setAttribute("class","p-flight");
            p1.innerHTML = "Flight example in this route:";
            divExample.append(p1);
            //flight information table
            var fTab = document.createElement("table");
            var fBody = document.createElement("tbody");
            fBody.append(createTrThVertical("Flight ID",flight.id));
            fBody.append(createTrThVertical("Airline Company",flight.airline));
            fBody.append(createTrThVertical("Departure Airport",flight.airportDep));
            fBody.append(createTrThVertical("Arrival Airport",flight.airportArr));
            fBody.append(createTrThVertical("Departure Time",flight.timeDep.replace("T"," ").replace("Z","")));
            fBody.append(createTrThVertical("Arrival Time",flight.timeArr.replace("T"," ").replace("Z","")));
            fTab.append(fBody);
            divExample.append(fTab);

            //airspaces information table
            if(travers!=null || travers.length==0) {
                var p1 = document.createElement("p");
                p1.setAttribute("class","p-flight");
                p1.innerHTML = "Airspaces traversed in this route:";
                divExample.append(p1);
                var aTab = document.createElement("table");
                var aHead = document.createElement("thead");
                aHead.append(createTh("Sequence"));
                aHead.append(createTh("ICAO"));
                aHead.append(createTh("Type"));
                aHead.append(createTh("Country"));
                aHead.append(createTh("Entry Time"));
                aHead.append(createTh("Exit Time"));
                aTab.append(aHead);
                var aBody = document.createElement("tbody");
                for (var i = 0; i < travers.length; i++) {
                    var tr = document.createElement("tr");

                    var td = document.createElement("td");
                    td.innerHTML = travers[i].sequence;
                    tr.append(td);
                    var td = document.createElement("td");
                    td.innerHTML = travers[i].icao;
                    tr.append(td);
                    var td = document.createElement("td");
                    td.innerHTML = travers[i].type;
                    tr.append(td);
                    var td = document.createElement("td");
                    td.innerHTML = travers[i].country;
                    tr.append(td);
                    var td = document.createElement("td");
                    td.innerHTML = travers[i].entryTime.replace("T"," ").replace("Z","");
                    tr.append(td);
                    var td = document.createElement("td");
                    td.innerHTML = travers[i].exitTime.replace("T"," ").replace("Z","");
                    tr.append(td);
                    aBody.append(tr);
                }
                aTab.append(aBody);
                divExample.append(aTab);

                /*var divRight = document.getElementById("pageRight");
                divRight.innerHTML = "";
                divRight.append(loadImage("/AirTraffic-1.00/media/query/Milan_query/country.png"));
                divRight.append(loadImage("/AirTraffic-1.00/media/query/Milan_query/airport_city.png"));
                divRight.append(loadImage("/AirTraffic-1.00/media/query/Milan_query/airlines.png"));
                divRight.append(loadImage("/AirTraffic-1.00/media/query/Milan_query/flight.png"));
                divRight.append(loadImage("/AirTraffic-1.00/media/query/Milan_query/fir_traversed.png"));
*/
            }else{
                var p = document.createElement("p");
                p.innerHTML = "No airspaces for this flight.";
                divExample.append(p);
            }

        }else if(req.status==400){
            alert("Bad Request!");
            window.location.href = (contextPath+"/html/home.html");
        }
        else {
            console.log(req.responseText);
            alert("problem processing the request");
        }
    }
}
function createTrThVertical(header, value){

    var tr = document.createElement("tr");
    var th = document.createElement("th");
    th.setAttribute("scope","row");
    th.innerHTML = header;
    var td = document.createElement("td");
    if(value == null || value == "null")
        td.innerHTML = "Data not available"
    else
        td.innerHTML = value;
    tr.append(th);
    tr.append(td);
    return tr;
}

function createTh(value){
    var th = document.createElement("th");
    th.innerHTML = value;
    return th;
}
function loadImage(src){
    var img = document.createElement("img");
    img.setAttribute("src",src);
    img.setAttribute("class","img-query");
    return img;
}