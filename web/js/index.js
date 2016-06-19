var KEYCODE = {left: 37, up: 38, right: 39, down: 40, enter: 13, backspace: 8, pause: 19};
var markersMapTramThuyVan = {};
var markerArrayTramThuyVan = [];
var infoWindowsTramThuyVan = {};
var contentMapTramThuyVan = {};
var globalInfoWindow = null;
var map;
var isDrawingCalcDistance = false;
var isEditingTramThuyVan = false;
var isEditingTuyenCap = false;
var drawingManager = null;
var markerSize = 32;
var currentTramThuyVanIcon = {
    url: contextPath + '/img/tramquantracthuyvanselect.ico?' + staticVersion,
    scaledSize: new google.maps.Size(markerSize, markerSize)
}
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
document.onready = function () {
    initSize();
    initMap();
    initTramThuyVan();

}
function initTramThuyVan() {
    clearAllTramThuyVanMarker();
    for (var i = 0; i < tramThuyVanList.length; i++) {
        var tramThuyVan = tramThuyVanList[i];
        var key = tramThuyVan.id;
        tramThuyVan.tentram1 = replaceUnicode(tramThuyVan.tentram).toLowerCase();
        var latLng = new google.maps.LatLng(tramThuyVan.x,
                tramThuyVan.y);
        var image = contextPath + '/img/tramquantracthuyvan.ico?' + staticVersion;
        var icon = {
            url: image,
            scaledSize: new google.maps.Size(markerSize, markerSize)
        };
        tramThuyVan.icon = icon;
        var marker = new google.maps.Marker({
            position: latLng,
            icon: image,
            title: tramThuyVan.ten});
        marker.setMap(map);
        marker.tramThuyVan = tramThuyVan;
        tramThuyVan.marker = marker;
        markersMapTramThuyVan[key] = (marker);
        markerArrayTramThuyVan.push(marker);

        var content = $("#tramthuyvaninfowindow").html();
        content = content.replace(/{contextPath}/g, contextPath);
        content = content.replace(/{id}/g, tramThuyVan.id);
        content = content.replace(/{tentram}/g, tramThuyVan.tentram);
        content = content.replace(/{vitri}/g, tramThuyVan.vitri);
        content = content.replace(/{hinh}/g, tramThuyVan.hinh);
        content = content.replace(/{x}/g, tramThuyVan.x);
        content = content.replace(/{y}/g, tramThuyVan.y);


        contentMapTramThuyVan[key] = content;
        var today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth() + 1; //January is 0!
        var yyyy = today.getFullYear();
        google.maps.event.addListener(markersMapTramThuyVan[key], 'click', (function (marker, content, key) {
            return function () {
                if (globalInfoWindow) {
                    globalInfoWindow.close();
                }
                if (isEditingTramThuyVan) {
                    showPopupUpdateTramThuyVan(marker.tramThuyVan);
                    return;
                }

                globalInfoWindow = new google.maps.InfoWindow({
                    content: content
                });
                google.maps.event.addListener(globalInfoWindow, 'domready', function () {
                    $("#nam"+key).val(yyyy);
                    $("#thang"+key).val(mm).change();
                });
                marker.setMap(map);
                globalInfoWindow.open(map, marker);

                if (isDrawingCalcDistance) {
                    addMarker(marker.getPosition());
                }
            };
        })(markersMapTramThuyVan[key], contentMapTramThuyVan[key], key));
    }

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
function showMarker(id) {
    for (var i = 0; i < tramThuyVanList.length; i++) {
        var tramThuyVan = tramThuyVanList[i];
        if (tramThuyVan.id == id) {
            tramThuyVan.marker.setIcon(currentTramThuyVanIcon);
        } else {
            tramThuyVan.marker.setIcon(tramThuyVan.icon);
        }
    }

    if (map.getZoom() < detailZoomLevel) {
        map.setZoom(detailZoomLevel);
        setTimeout(function () {
            show(id);
        }, 1000)
    } else {
        show(id);
    }
    function show(id) {
        map.panTo(markersMapTramThuyVan[id].getPosition());
        if (globalInfoWindow) {
            globalInfoWindow.close();
        }
        globalInfoWindow = new google.maps.InfoWindow({
            content: contentMapTramThuyVan[id]
        });
        markersMapTramThuyVan[id].setMap(map);
        globalInfoWindow.open(map, markersMapTramThuyVan[id]);
    }
}
function setPan() {
    disableDrawing();
    map.setOptions({draggableCursor: null});
}

$(function () {
    $("#zoom-in").click(zoomIn);
    $("#zoom-out").click(zoomOut);
    $("#home").click(goHome);
    $("#pan").click(setPan);
    $("#show-left-panel").click(showLeftPanel);
    $("#hide-left-panel").click(hideLeftPanel);

});
var rad = function (x) {
    return x * Math.PI / 180;
};

function resetTramThuyVanMarkerIcon() {
    for (var i = 0; i < tramThuyVanList.length; i++) {
        var tramThuyVan = tramThuyVanList[i];
        tramThuyVan.marker.setIcon(tramThuyVan.icon);
    }
}

function clearAllTramThuyVanMarker() {
    for (var i = 0; i < tramThuyVanList.length; i++) {
        var tramBTS = tramThuyVanList[i];
        if (tramBTS.marker) {
            tramBTS.marker.setMap(null);
        }
    }
    markersMapTramThuyVan = {};
    markerArrayTramThuyVan = [];
}

function updateSoLieuDoThuyVan(id) {
    var nam = $("#nam" + id).val();
    var thang = $("#thang" + id).val();
    if (thang == -1) {
        $("#solieudo" + id).html("");
        return;
    }
    $.ajax({
        type: "post",
        url: contextPath + '/ajax/getsolieudothuyvan',
        data: {
            nam: nam,
            thang: thang,
            id: id,
        },
        dataType: "json"
    })
            .done(function (resp) {
                var tbody = "";
                if (resp.returnCode == 1) {
                    var soLieuDoList = $.parseJSON(resp.data);


                    for (var i = 0; i < soLieuDoList.length; i++) {
                        var row = "<tr><td>{ngayquantrac}</td><td>{giatricaonhat}</td><td>{giatrithapnhat}</td></tr>";
                        var soLieudDo = soLieuDoList[i];
                        row = row.replace(/{ngayquantrac}/, soLieudDo.ddmmyyyy);
                        row = row.replace(/{giatricaonhat}/, soLieudDo.giatricaonhat);
                        row = row.replace(/{giatrithapnhat}/, soLieudDo.giatrithapnhat);
                        tbody = tbody + row;
                    }
                    if(soLieuDoList.length==0){
                        tbody ="<tr><td colspan=\"3\" style=\"text-align: center\">Không có dữ liệu</td></tr>";
                    }
                }
                $("#solieudo" + id).html(tbody);
            })
}

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
$(function () {
})
