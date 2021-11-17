function changeStatus(data) {
    let description = $('#userId' + data.value).val();
    let question = "Вы уверены что задание выполнено? \n" + description;
    let result = confirm(question);
    let arr = {id: data.value, description: description};
    if (result) {
        $.ajax({
            cache: false,
            type: 'POST',
            url: '/todo/index.do',
            data: JSON.stringify(arr),
            contentType: 'application/json'
        }).done(function() {
            showAll();
        }).fail(function(err){
            alert("Error:" + err);
        });
    }
}