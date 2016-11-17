function orderToHome(id) {
    $.ajax({
        url: 'controller?cmd=orderToHome&bookId=' + id,
        context: document.body
    }).done(function() {
        location.reload();
    });
}
