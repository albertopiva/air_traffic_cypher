/**
 * LoadHF file, that load Header and Footer.
 *
 * @author AirTraffic-1.00
 * @version 1.0
 */

/**
 * The context path of the webapp
 * @type {string}
 * The header path of the webapp
 * @type {string}
 * The footer path of the webapp
 * @type {string}
 */
var contextPath = "http://localhost:8080/AirTraffic-1.00/"
const header = "html/commonPart/header.html"
const footer = "html/commonPart/footer.html"

document.addEventListener('DOMContentLoaded', function() {
    loadTemplate()
})

function loadTemplate(){
    const headerUrl = new URL(contextPath+header);
    const footerUrl = new URL(contextPath+footer);

    templateGETRequest(headerUrl, loadHeader);
    templateGETRequest(footerUrl, loadFooter);
}


/**



/**
 * A template GET XMLHTTPRequest.
 *
 * @param url the url of the request.
 * @param callback the function to invoke when the servlet answer.
 * @returns {boolean} false if the request did not created.
 */
function templateGETRequest(url, callback){
    var httpRequest = new XMLHttpRequest();

    if (!httpRequest) {
        alert('Cannot create an XMLHTTP instance');
        return false;
    }
    httpRequest.onreadystatechange = function (){
        if(httpRequest.readyState == XMLHttpRequest.DONE){
            if(httpRequest.status == 200){
                callback(httpRequest.responseText)
            }else {
                console.log(httpRequest.responseText);
                alert("problem processing the request");
            }
        }
    }

    httpRequest.open('GET', url);
    httpRequest.send();
}

/**
 * Load header function
 * @param data
 */
function loadHeader(data){
    document.getElementsByTagName("header")[0].innerHTML = data;
}


/**
 * Load footer function
 * @param data
 */
function loadFooter(data){
    document.getElementsByTagName("footer")[0].innerHTML = data;
}