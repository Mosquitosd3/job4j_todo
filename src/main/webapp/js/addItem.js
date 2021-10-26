function addNewItem() {
    let description = $('#formForDefinition1').val();
    let id = 0;
    let arr = {id: id, description: description};
    if (validate(description)) {
        $.ajax({
            cache: false,
            type: 'POST',
            url: '/todo/index',
            data: JSON.stringify(arr),
            contentType: 'application/json'
        }).done(function() {
            showAll();
        }).fail(function(err){
            alert("Error:" + err);
        });
    }
}