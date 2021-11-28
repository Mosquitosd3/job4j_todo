function addNewItem() {
    let description = $('#formForDefinition1').val();
    let categories = [];
    let id = $('#categorySelection').val();
    for (const idEl of id) {
        let name = $('#categoryName' + idEl).val();
        categories.push({id: idEl, name: name});
    }
    let arr = {description: description, categories: categories};
    if (validate(description, categories)) {
        $.ajax({
            cache: false,
            type: 'POST',
            url: '/todo/index.do',
            data: JSON.stringify(arr),
            contentType: 'application/json'
        }).done(function() {
        }).fail(function(err){
            alert("Error:" + err);
        });
    }
};


function validate(description, categories) {
    const answer = "Пожалуйста ";
    let result = answer;
    if (description === '') {
        result += "-заполните описание задачи ";
    }
    if (categories.length === 0) {
        result += "-выберите категорию ";
    }
    if (answer !== result) {
        alert(result);
        return false;
    }
    return true;
};