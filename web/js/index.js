var KEYCODE = {left: 37, up: 38, right: 39, down: 40, enter: 13, backspace: 8, pause: 19};
var globalInfoWindow = null;
var map;
var isDrawingCalcDistance = false;
var isEditingTramThuyVan = false;
var isEditingTuyenCap = false;
var drawingManager = null;
var markerSize = 32;
function initMap() {
    var location = new google.maps.LatLng(homeLat, homeLng);
    var mapOptions = {
        zoom: parseInt(homeZoomLevel),
        center: location,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    map = new google.maps.Map(document.getElementById("googleMap"), mapOptions);
}


function initSize() {
    var contentTop = $("#row-banner").outerHeight() + $("#row-nav").outerHeight() + 8;
    $("#show-left-panel").css({top: contentTop - 3});
    var contentHeight = $(window).height() - $("#row-banner").outerHeight() - $("#row-nav").outerHeight() - 8;
    $("#row-content").height(contentHeight);
    $("#googleMap").height(contentHeight);

}

function zoomIn() {
    disableDrawing();
    map.setOptions({draggableCursor: null});
    var zoom = map.getZoom();
    if (zoom < 21) {
        map.setZoom(zoom + 1);
    }
}
function zoomOut() {
    disableDrawing();

    map.setOptions({draggableCursor: null});
    var zoom = map.getZoom();
    if (zoom > 0) {
        map.setZoom(zoom - 1);
    }
}
function goHome() {
    disableDrawing();
    map.setOptions({draggableCursor: null});
    var location = new google.maps.LatLng(homeLat, homeLng);
    map.setCenter(location);
    map.setZoom(homeZoomLevel);
}
function setPan() {
    disableDrawing();
    map.setOptions({draggableCursor: null});
}

$(function () {
    initSize();
    initMap();
    $("#zoom-in").click(zoomIn);
    $("#zoom-out").click(zoomOut);
    $("#home").click(goHome);
    $("#pan").click(setPan);
    $("#show-left-panel").click(showLeftPanel);
    $("#hide-left-panel").click(hideLeftPanel);
    $.getScript(contextPath + "/js/tramthuyvan.js")
            .done(function (script, textStatus) {
                initTramThuyVan();
            });

});
function hideLeftPanel() {
    $("#left-panel").hide();
    $("#right-panel").attr("class", "col-md-12 col-sm-12 col-lg-12 col-xs-12");
    $("#show-left-panel").show();
    google.maps.event.trigger(map, 'resize');
    map.setZoom(map.getZoom());
}
function showLeftPanel() {
    $("#left-panel").show(400);
    $("#right-panel").attr("class", "col-md-9 col-sm-9 col-lg-9 col-xs-9");
    $("#show-left-panel").hide();
    google.maps.event.trigger(map, 'resize');
    map.setZoom(map.getZoom());
}
function disableDrawing() {
    disableDrawingCalcDistance();
    disableZoomInSelect();
    disableZoomOutSelect();
    disableEdit();
    disableEditTuyenCap();
}

