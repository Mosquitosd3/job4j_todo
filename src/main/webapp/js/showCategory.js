function loadCategory() {
    $.ajax({
        cache: false,
        type: 'GET',
        url: '/todo/category.do',
        dataType: 'json'
    }).done(function(data) {
        let categoryName = "";
        let result = "<select class=\"form-control\" id=\"categorySelection\" name=\"category\" multiple>" +
            "<option selected disabled value=\"\">Выберите категорию</option>";
        for (var category in data) {
            result += '<option value=\"' + data[category].id + '\">' + data[category].name + '</option>';
            categoryName += "<input type=\"hidden\" value=\"" + data[category].name + "\" "
                + "id=\"categoryName"+ data[category].id + "\"/>";
        }
        result += "</select>";
        document.getElementById('categorySelection').innerHTML = result;
        document.getElementById('hiddenCategoryName').innerHTML = categoryName;
    }).fail(function(err){
        alert("Error:" + err);
    });
};

$(document).ready(function () {
    loadCategory();
});