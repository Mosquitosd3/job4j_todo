function validate(description) {
    let answer = "Задача не может быть пустой";
    if (description === '') {
    alert(answer);
    return false;
    }
    return true;
}
