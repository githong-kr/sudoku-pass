function clearValue(object) {
    object.value = "";
}

function maxLengthCheck(object) {
    if (object.value.length > object.maxLength){
        object.value = object.value.slice(0, object.maxLength);
    }
}

function clearGrid() {
    let currentGrid = document.forms["x9"];
    currentGrid.reset();

    let inputFormElements = currentGrid.getElementsByClassName("space");
    for(let index = 0 ; index < inputFormElements.length ; index++) {
        inputFormElements[index].style.cssText = '';
    }
}

function solve() {
    let inputFormElements = document.forms["x9"].getElementsByClassName("space");
    let inputValue = [];

    for(let index = 0 ; index < inputFormElements.length ; index++) {
        let value = inputFormElements[index].value;
        if(value === "") {
            inputValue.push("0");
        } else {
            inputValue.push(value);
        }
    }

    $.ajax({
        type: 'POST',
        url: 'https://sudoku.sizeof.shop/solve',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(inputValue)
    }).done(function(data) {
        for(let index = 0 ; index < inputFormElements.length ; index++) {
            if(inputFormElements[index].value !== "") continue;
            inputFormElements[index].value = data[index];
            inputFormElements[index].style.cssText = 'color:blue';
        }
    })
}