function prolongOrder(id) {
    $.ajax({
        url: 'controller?cmd=prolongOrder&orderId=' + id,
        context: document.body
    }).done(function() {
        location.reload();
    });
}
