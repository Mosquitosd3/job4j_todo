function validate() {
    const answer = "Please fill field(s) ";
    let result = answer;
    if ($('#nameField').val() === '') {
        result += "-Name ";
    }
    if ($('#emailField').val() === '') {
        result += "-Email ";
    }
    if ($('#passwordField').val() === '') {
        result += "-Password";
    }
    if (answer !== result) {
        alert(result);
        return false;
    }
    return true;
}