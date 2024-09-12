let stompClient = null;
const WEB_SOCKET_URL = "/websocket";

function setConnected(connected) {
    $("#connected").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
}

function connect() {
    const socket = new SockJS(WEB_SOCKET_URL);

    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);

        stompClient.subscribe("/topic/monitor", (monitor) => {
            displayMonitor(JSON.parse(monitor).content);
        })
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

$(function () {
    $("#navigateToMonitor").click((e) => {
        window.location.href = "/monitor";
    });

    $("#connect").click(function () {
        connect()
    });

    $("#disconnect").click(function () {
        disconnect()
    });
})

const displayMonitor = (monitor) => {
    console.log("Displaying monitor");
    console.log(monitor);

    $("#averageQueryTime").append("<tr><td>" + monitor.averageQueryTime + "</td></tr>");

    monitor.activeBarrels.forEach((barrel) => {
        $("#activeBarrels").append("<tr><td>" + barrel + "</td></tr>");
    });

    monitor.topTenSearches.forEach((search) => {
        $("#topTenSearches").append("<tr><td>" + search + "</td></tr>");
    });
}

document.addEventListener('DOMContentLoaded', () => connect());