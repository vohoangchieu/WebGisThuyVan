var markersMapTramThuyVan = {};
var markerArrayTramThuyVan = [];
var infoWindowsTramThuyVan = {};
var contentMapTramThuyVan = {};

var currentTramThuyVanIcon = {
    url: contextPath + '/img/tramquantracthuyvanselect.ico?' + staticVersion,
    scaledSize: new google.maps.Size(markerSize, markerSize)
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
                    $("#nam" + key).val(yyyy);
                    $("#thang" + key).val(mm).change();
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

function showTramThuyVanMarker(id) {
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
                    if (soLieuDoList.length == 0) {
                        tbody = "<tr><td colspan=\"3\" style=\"text-align: center\">Không có dữ liệu</td></tr>";
                    }
                }
                $("#solieudo" + id).html(tbody);
            })
}