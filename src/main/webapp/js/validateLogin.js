function validate() {
    const answer = "Please fill field(s) ";
    let result = answer;
    if ($('#emailField').val() === '') {
        result += "-Name ";
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