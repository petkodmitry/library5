function orderToReadingRoom(id) {
    $.ajax({
        url: 'controller?cmd=orderToReadingRoom&bookId='+id,
        context: document.body
    }).done(function() {
        location.reload();
    });
}
