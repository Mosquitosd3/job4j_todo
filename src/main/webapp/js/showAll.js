function showAll() {
    let statusBox = $('#checkbox1').is(":checked");
    $.ajax({
        cache: false,
        type: 'GET',
        url: '/todo/index',
        dataType: 'json'
    }).done(function (data) {
        let result = "<tbody id=\"bodyTableId\">";
        let row = 1;
        for (let i = 0; i < data.length; i++) {
            let item = data[i];
            if (!statusBox && item.done) {
                continue;
            }
            result += "<tr>";
            result += "<input type=\"hidden\" value=\"" + item.description + "\" id=\"userId"+ item.id + "\"/>";
            result += "<th>" + row + "</th>";
            result += "<th>" + item.description + "</th>";
            let date = new Date(item.created);
            const dtFormat = new Intl.DateTimeFormat('ru', {
                hour: "numeric",
                minute: "numeric",
                year: "numeric",
                month: "long",
                day: "numeric"
            });
            result += "<th>" + dtFormat.format(date) + "</th>";
            let done = item.done ? "Выполнено"
                : "<input type=\"checkbox\" value=\"" + item.id + "\" onclick=\"changeStatus(this)\">";
            result += "<th>" + done + "</th></tr>";
            row++;
        }
        result += "</tbody>";
        document.getElementById('bodyTableId').innerHTML = result;
    }).fail(function (err) {
        console.log(err);
    });
};