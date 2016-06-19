var tuyencapPathList = [],globalInfoWindowTuyenCap;
function initTuyenCap() {
    clearTuyenCap();
    for (var i = 0; i < tuyencapList.length; i++) {
        var tuyencap = tuyencapList[i];
        var tuyencapCoordinates = [
            {lat: tuyencap.X1, lng: tuyencap.Y1},
            {lat: tuyencap.X2, lng: tuyencap.Y2},
        ];
        var tuyencapPath = new google.maps.Polyline({
            path: tuyencapCoordinates,
            geodesic: true,
            strokeColor: '#FF0000',
            strokeOpacity: 0.9,
            strokeWeight: 6
        });
        tuyencapPathList.push(tuyencapPath);
        tuyencapPath.setMap(map);
        google.maps.event.addListener(tuyencapPath, "click", (function (tuyencap) {
            return function () {
                if (isEditingTuyenCap == false) {
                    return;
                }
                showPopupUpdateTuyenCap(tuyencap);
            };

        })(tuyencap));
        google.maps.event.addListener(tuyencapPath, "mouseover", (function (tuyencap, tuyencapPath) {
            return function () {
                if (globalInfoWindowTuyenCap) {
                    globalInfoWindowTuyenCap.close();
                }
                globalInfoWindowTuyenCap = new google.maps.InfoWindow({
                    content: tuyencap.Ten
                });
                globalInfoWindowTuyenCap.setPosition(new google.maps.LatLng((tuyencap.X1 + tuyencap.X2) / 2, (tuyencap.Y1 + tuyencap.Y2) / 2)); 
                globalInfoWindowTuyenCap.open(map, tuyencapPath);
            };

        })(tuyencap, tuyencapPath));
        
        google.maps.event.addListener(tuyencapPath, "mouseout", (function (tuyencap, tuyencapPath) {
            return function () {
                if (globalInfoWindowTuyenCap) {
                    globalInfoWindowTuyenCap.close();
                }
            };

        })(tuyencap, tuyencapPath));

    }
}

function clearTuyenCap() {
    for (var i = 0; i < tuyencapPathList.length; i++) {
        var tuyencapPath = tuyencapPathList[i];
        tuyencapPath.setMap(null);
    }
    tuyencapPathList = [];
}

function hidePopupAddTuyenCap() {
    $("#popup-add-tuyencap").hide();
}
function showPopupAddTuyenCap() {
    $("#popup-tuyencap-title").text("Thêm tuyến cáp");
    $("#btn-add-tuyencap").show();
    $("#btn-update-tuyencap").hide();
    $("#btn-delete-tuyencap").hide();
    $("#popup-add-tuyencap").show();
    $("#Ten").val("");
    $("#X1").val("");
    $("#Y1").val("");
    $("#X2").val("");
    $("#Y2").val("");
    $("#Ten").focus();
}
function showPopupUpdateTuyenCap(tuyencap) {
    $("#popup-tuyencap-title").text("Cập nhật tuyến cáp");
    $("#Id").val(tuyencap.Id);
    $("#Ten").val(tuyencap.Ten);
    $("#X1").val(tuyencap.X1);
    $("#Y1").val(tuyencap.Y1);
    $("#X2").val(tuyencap.X2);
    $("#Y2").val(tuyencap.Y2);
    $("#btn-add-tuyencap").hide();
    $("#btn-update-tuyencap").show();
    $("#btn-delete-tuyencap").show();
    $("#popup-add-tuyencap").show();
    $("#Ten").focus();
}

function  handlePopupSubmitAddTuyenCap() {
    $.ajax({
        type: "post",
        url: contextPath + '/ajax/add-tuyencap',
        data: {
            Id: $("#Id").val(),
            Ten: $("#Ten").val(),
            X1: $("#X1").val(),
            Y1: $("#Y1").val(),
            X2: $("#X2").val(),
            Y2: $("#Y2").val(),
        },
        dataType: "json"
    })
            .done(function (resp) {
                if (resp.returnCode == 1) {
                    hidePopupAddTuyenCap();
                    showPopupMessage("success", resp.returnMessage);
                    var tuyencap = $.parseJSON(resp.data);
                    tuyencapList.push(tuyencap);
                    initTuyenCap();
                } else {
                    showErrorMessageTuyenCap(resp.returnMessage);
                }
            })
}

function  handlePopupSubmitUpdateTuyenCap() {
    $.ajax({
        type: "post",
        url: contextPath + '/ajax/update-tuyencap',
        data: {
            Id: $("#Id").val(),
            Ten: $("#Ten").val(),
            X1: $("#X1").val(),
            Y1: $("#Y1").val(),
            X2: $("#X2").val(),
            Y2: $("#Y2").val(),
        },
        dataType: "json"
    })
            .done(function (resp) {
                if (resp.returnCode == 1) {
                    hidePopupAddTuyenCap();
                    showPopupMessage("success", resp.returnMessage);
                    var tuyencap = $.parseJSON(resp.data);
                    for (var i = 0; i < tuyencapList.length; i++) {
                        if (tuyencapList[i].Id == tuyencap.Id) {
                            tuyencapList[i] = tuyencap;
                            break;
                        }
                    }
                    initTuyenCap();
                } else {
                    showErrorMessageTuyenCap(resp.returnMessage);
                }
            })


}
function  handlePopupSubmitDeleteTuyenCap() {
    $.ajax({
        type: "post",
        url: contextPath + '/ajax/delete-tuyencap',
        data: {
            Id: $("#Id").val(),
        },
        dataType: "json"
    })
            .done(function (resp) {
                if (resp.returnCode == 1) {
                    showPopupMessage("success", resp.returnMessage);
                    var Id = $("#Id").val();
                    for (var i = 0; i < tuyencapList.length; i++) {
                        var tuyencap = tuyencapList[i];
                        var key = tuyencap.Id;
                        if (key == Id) {
                            tuyencapList.splice(i, 1);
                            break;
                        }
                    }
                    initTuyenCap();

                } else {
                    showPopupMessage("danger", resp.returnMessage);
                }
            })
}
function showPopupConfirmDeleteTuyenCap() {
//    showPopupConfirm("Xác nhận xóa tuyến cáp",
//            "Bạn có muốn xóa tuyến cáp '" + $("#Ten").val() + "'",
//            "Hủy",
//            "Đồng ý",
//            handlePopupSubmitDeleteTuyenCap);
//    $("#popup-confirm").show();
    handlePopupSubmitDeleteTuyenCap();
}


function showErrorMessageTuyenCap(message) {
    $("#message-tuyencap").addClass("alert-danger").removeClass("alert-success");
    $("#message-tuyencap").text(message).show();
}
function handleEditTuyenCapButtonClick() {
    if ($("#edit-tuyencap span").hasClass("toolactive")) {
        isEditingTuyenCap = false;
        $("#edit-tuyencap span").removeClass("toolactive");
        map.setOptions({draggableCursor: null});
    } else {
        disableDrawing();
        map.setOptions({draggableCursor: 'crosshair'});
        isEditingTuyenCap = true;
        $("#edit-tuyencap span").addClass("toolactive");
    }
}
$(function () {
    setTimeout(function () {
        initTuyenCap();
        google.maps.event.addListener(map, "click", function () {
            if (isEditingTuyenCap == false) {
                return;
            }
            showPopupAddTuyenCap();
        });
    }, 1000);

    $("#edit-tuyencap").click(handleEditTuyenCapButtonClick);
    $(".tuyencap").click(function () {
        var isCheck = $(this).is(":checked");
        for (var i = 0; i < tuyencapPathList.length; i++) {
            var tuyencapPath = tuyencapPathList[i];
            if (isCheck) {
                tuyencapPath.setMap(map);
            } else {
                tuyencapPath.setMap(null);
            }
        }
    })

})




