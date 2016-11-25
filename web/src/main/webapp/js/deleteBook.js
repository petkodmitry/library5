function deleteBook(id) {
    //location.href= "/controller?cmd=searchBookAdmin";
    $.ajax({
        url: 'controller?cmd=deleteBook&bookId=' + id,
        context: document.body
    }).done(function() {
        //$("#refresh").load(location.href + " #refresh>*", "");
        //location.resolveURL("/controller?cmd=searchBookAdmin");
        //location.reload($("#refresh"));
        location.reload();
    });
}
