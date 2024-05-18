let stompClient = null;

function setConnected(connected) {
    $("#connected").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
}

function connect() {
    const socket = new SockJS('/websocket');

    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);

        stompClient.subscribe("/topic/monitor", (monitor) => {
            displayMonitor(JSON.parse(monitor.topTenSearches));
        })

        stompClient.subscribe('/topic/', function (message) {
            showMessage(JSON.parse(message.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function showMessage(message) {
    $("#webmessage").append("<tr><td>" + message.message + "</td></tr>");
}

const displayMonitor = (monitor) => {
    $("#monitor").html("");

    monitor.forEach((m) => {
        $("#monitor").append("<tr><td>" + m.search + "</td><td>" + m.count + "</td></tr>");
    });
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

function requestMonitor() {
    stompClient.send("/topic/monitor", {});
}

document.addEventListener('DOMContentLoaded', () => connect());