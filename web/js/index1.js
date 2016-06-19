var drawingManagerZoom, rectangle, markersCalcDistance = [], polyCalcDistance, startNewLineCalcDistance;
function enableZoomOutSelect() {
    $("#zoom-out-select span").addClass("toolactive");
    if (!drawingManagerZoom)
        drawingManagerZoom = new google.maps.drawing.DrawingManager();
    zin = 2;  //out

    drawingManagerZoom.setOptions({
        drawingMode: google.maps.drawing.OverlayType.RECTANGLE,
        drawingControl: false,
        drawingControlOptions: {
            position: google.maps.ControlPosition.TOP_CENTER,
            drawingModes: [google.maps.drawing.OverlayType.RECTANGLE]
        },
        rectangleOptions: {
            strokeWeight: 1,
            clickable: true,
            editable: false,
            zIndex: 1
        }
    });
    // Loading the drawing Tool in the Map.
    drawingManagerZoom.setMap(map);
    google.maps.event.addListener(drawingManagerZoom, 'overlaycomplete', function (event) {
        if (event.type == google.maps.drawing.OverlayType.RECTANGLE) {
            if (rectangle != null)
                rectangle.setMap(null);
            rectangle = event.overlay;
            var bounds = rectangle.getBounds();
            // bounds.extend(5);
            var increasePercentage = 1.5;  //10%

            var pointNorthEast = bounds.getNorthEast();
            var pointSouthWest = bounds.getSouthWest();
            var latAdjustment = (pointNorthEast.lat() - pointSouthWest.lat()) * (increasePercentage - 1);
            var lngAdjustment = (pointNorthEast.lng() - pointSouthWest.lng()) * (increasePercentage - 1);
            var newPointNorthEast = new google.maps.LatLng(pointNorthEast.lat() + latAdjustment, pointNorthEast.lng() + lngAdjustment);
            var newPointSouthWest = new google.maps.LatLng(pointSouthWest.lat() - latAdjustment, pointSouthWest.lng() - lngAdjustment);

            bounds = new google.maps.LatLngBounds();
            bounds.extend(newPointNorthEast);
            bounds.extend(newPointSouthWest);

            var bm = map.getBounds();
            var pNE = bm.getNorthEast();
            var pSW = bm.getSouthWest();


            var d = ((pNE.lat() - pSW.lat())) / ((pointNorthEast.lat() - pointSouthWest.lat()));
            map.panTo(bounds.getCenter());

            var zoom = map.getZoom();
            // alert(zoom);
            if (d >= 0 && d < 1)
                map.setZoom(zoom - 4);
            if ((d >= 1) && (d < 3))
                map.setZoom(zoom - 1);

            if ((d >= 3) && (d < 6))
                map.setZoom(zoom - 2);
            if (d > 6)
                map.setZoom(zoom - 3);

            rectangle.setMap(null);
        }
    });

    google.maps.event.addListener(drawingManagerZoom, "drawingmode_changed", function () {
        if ((drawingManagerZoom.getDrawingMode() == google.maps.drawing.OverlayType.RECTANGLE) &&
                (rectangle != null))
            rectangle.setMap(null);
    });

    // when the user clicks somewhere else on the map.
    google.maps.event.addListener(map, 'click', function () {
        if (rectangle != null)
            rectangle.setMap(null);
    });


}

function enableZoomInSelect() {
    $("#zoom-in-select span").addClass("toolactive");
    zin = 1;  // in
    //Setting options for the Drawing Tool. In our case, enabling Polygon shape.
    if (!drawingManagerZoom)
        drawingManagerZoom = new google.maps.drawing.DrawingManager();

    drawingManagerZoom.setOptions({
        drawingMode: google.maps.drawing.OverlayType.RECTANGLE,
        drawingControl: false,
        drawingControlOptions: {
            position: google.maps.ControlPosition.TOP_CENTER,
            drawingModes: [google.maps.drawing.OverlayType.RECTANGLE]
        },
        rectangleOptions: {
            strokeWeight: 1,
            clickable: true,
            editable: false,
            zIndex: 1
        }
    });
    // Loading the drawing Tool in the Map.
    drawingManagerZoom.setMap(map);
    //alert("dc");
    google.maps.event.addListener(drawingManagerZoom, 'overlaycomplete', function (event) {
        if (event.type == google.maps.drawing.OverlayType.RECTANGLE) {
            if (rectangle != null)
                rectangle.setMap(null);
            rectangle = event.overlay;
            var bounds = rectangle.getBounds();



            // bounds.extend(5);
            var increasePercentage = 0.7;  //10%

            var pointNorthEast = bounds.getNorthEast();
            var pointSouthWest = bounds.getSouthWest();
            var latAdjustment = (pointNorthEast.lat() - pointSouthWest.lat()) * (increasePercentage - 1);
            var lngAdjustment = (pointNorthEast.lng() - pointSouthWest.lng()) * (increasePercentage - 1);
            var newPointNorthEast = new google.maps.LatLng(pointNorthEast.lat() + latAdjustment, pointNorthEast.lng() + lngAdjustment);
            var newPointSouthWest = new google.maps.LatLng(pointSouthWest.lat() - latAdjustment, pointSouthWest.lng() - lngAdjustment);
            bounds = new google.maps.LatLngBounds();
            bounds.extend(newPointNorthEast);
            bounds.extend(newPointSouthWest);
            map.fitBounds(bounds);
            rectangle.setMap(null);
        }
    });

    google.maps.event.addListener(drawingManagerZoom, "drawingmode_changed", function () {
        if ((drawingManagerZoom.getDrawingMode() == google.maps.drawing.OverlayType.RECTANGLE) &&
                (rectangle != null))
            rectangle.setMap(null);
    });

    // when the user clicks somewhere else on the map.
    google.maps.event.addListener(map, 'click', function () {
        if (rectangle != null)
            rectangle.setMap(null);
    });
}
$(function () {
    setTimeout(function () {
        initPoly();
        $("#zoom-in-select").click(zoomInSelectClick);
        $("#zoom-out-select").click(zoomOutSelectClick);
        $("#distance").click(distanceClick);
    }, 250);

})
var updateTimeout = null;
function initPoly() {
    polyCalcDistance = new google.maps.Polyline({
        strokeColor: '#00ffff',
        strokeOpacity: 0.5,
        strokeWeight: 3
    });
    map.addListener('dblclick', function (event) {
//        console.log('dblclick');
        if (updateTimeout) {
            clearTimeout(updateTimeout);
        }

        if (isDrawingCalcDistance) {
            addMarkerCalcDistance(event.latLng);
            startNewLineCalcDistance = true;
        }
    });
    map.addListener('click', function (event) {
        updateTimeout = setTimeout(function () {
            if (isDrawingCalcDistance) {
                addMarkerCalcDistance(event.latLng);
            }
        }, 200);

    });

    $("#distance-div .glyphicon-remove").click(function () {
        $("#distance-div").hide();
        if (polyCalcDistance) {
            polyCalcDistance.setMap(null);
        }
        clearMarkerCalcDistance();
        clearPathCalcDistance();

    })
}
function distanceClick() {
    if (isDrawingCalcDistance) {
        map.setOptions({draggableCursor: null});
        disableDrawingCalcDistance();
    } else {
        disableDrawing();
        isDrawingCalcDistance = true;
        polyCalcDistance.setMap(map);
        $("#distance span").addClass("toolactive");
        $("#distanceResult").text("0");
        map.setOptions({disableDoubleClickZoom: true});
        map.setOptions({draggableCursor: 'crosshair'});
        disableZoomInSelect();
        disableZoomOutSelect();
    }
}
function disableDrawingCalcDistance() {
    if (isDrawingCalcDistance) {
        isDrawingCalcDistance = false;
        map.setOptions({disableDoubleClickZoom: false});
        $("#distance span").removeClass("toolactive");
        $("#distance-div").hide();
        if (polyCalcDistance) {
            polyCalcDistance.setMap(null);
        }
        clearMarkerCalcDistance();
        clearPathCalcDistance();
    }
}
function addMarkerCalcDistance(location) {
    var path = polyCalcDistance.getPath();
    if (startNewLineCalcDistance) {
        startNewLineCalcDistance = false;
        clearMarkerCalcDistance();
        clearPathCalcDistance();
    }
    polyCalcDistance.setMap(map);
    path.push(location);

    var image = contextPath + '/img/mapk.png?' + staticVersion;//'mapk.png';

    var marker = new google.maps.Marker({
        position: location,
        icon: image,
        size: 5,
        map: map
    });

    markersCalcDistance.push(marker);
    var latLngArray = polyCalcDistance.getPath().getArray();
    var distance = 0;

    for (var i = 0; i < latLngArray.length - 1; i++) {
        distance += getDistance(latLngArray[i], latLngArray[i + 1]);
    }
    $("#distanceResult").text(distance.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1 ').replace(".", ",").replace(" ", "."));
    $("#distance-div").show();
}
function clearMarkerCalcDistance() {
    for (var i = 0; i < markersCalcDistance.length; i++) {
        markersCalcDistance[i].setMap(null);
    }
    markersCalcDistance = [];
}
function clearPathCalcDistance() {
    var path = polyCalcDistance.getPath();
    path.clear();
}
function disableZoomInSelect() {
    $("#zoom-in-select span").removeClass("toolactive");
    if (drawingManagerZoom) {
        drawingManagerZoom.setMap(null);
    }
    drawingManagerZoom = null;
}
function disableZoomOutSelect() {
    $("#zoom-out-select span").removeClass("toolactive");
    if (drawingManagerZoom) {
        drawingManagerZoom.setMap(null);
    }
    drawingManagerZoom = null;
}
function disableEdit() {
    if ($("#edit span").hasClass("toolactive")) {
        $("#edit span").removeClass("toolactive");
        isEditingTramThuyVan = false;
        map.setOptions({draggableCursor: null});
    }
}
function disableEditTuyenCap() {
    if ($("#edit-tuyencap span").hasClass("toolactive")) {
        $("#edit-tuyencap span").removeClass("toolactive");
        isEditingTuyenCap = false;
        map.setOptions({draggableCursor: null});
    }
}
function zoomInSelectClick() {
    $("#zoom-out-select span").removeClass("toolactive");
    if ($("#zoom-in-select span").hasClass("toolactive")) {
        disableZoomInSelect();
    } else {
//        disableDrawingCalcDistance();
        disableDrawing();
        enableZoomInSelect();
    }
}
function zoomOutSelectClick() {

    $("#zoom-in-select span").removeClass("toolactive");
    if ($("#zoom-out-select span").hasClass("toolactive")) {
        disableZoomOutSelect();
    } else {
//        disableDrawingCalcDistance();
        disableDrawing();
        enableZoomOutSelect();
    }
}

