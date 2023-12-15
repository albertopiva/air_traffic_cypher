/**
 * Utils file, that provide some useful method.
 *
 * @author Spatial Team
 * @version 1.0
 * @since 1.0
 */


var contextPath = "http://localhost:8080/AirTraffic-1.00/"

/**
 * A generic GET XMLHTTPRequest.
 *
 * @param url the url of the request.
 * @param callback the function to invoke when the servlet answer.
 * @returns {boolean} false if the request did not created.
 */
function genericGETRequest(url, callback){
    var httpRequest = new XMLHttpRequest();

    if (!httpRequest) {
        alert('Cannot create an XMLHTTP instance');
        return false;
    }
    httpRequest.onreadystatechange = function (){ callback(httpRequest)};

    httpRequest.open('GET', url);
    httpRequest.send();
}