function printMap() {
//    $('#googleMap').css({width: "980px"});
//    google.maps.event.trigger(map, 'resize');
    
    var body = $('body'),
            mapContainer = $('#googleMap'),
            mapContainerParent = mapContainer.parent(),
            printContainer = $('<div>');

    body.prepend(printContainer);
    printContainer
            .addClass('print-container')
            .css('position', 'relative')
            .height(mapContainer.height())
            .append(mapContainer);

    var content = body.children()
            .not('script')
            .not(printContainer)
            .detach();

    window.print();
//    $('#googleMap').css({width: "100%"});
//    google.maps.event.trigger(map, 'resize');
    body.prepend(content);
    mapContainerParent.prepend(mapContainer);
    printContainer.remove();
}
$(function () {
    setTimeout(function(){
        $("#print").click(printMap);
    },500)
    
})