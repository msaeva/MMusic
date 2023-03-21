const csrfHeaderName = document.getElementById('csrf').getAttribute('name')
const csrfHeaderValue = document.getElementById('csrf').getAttribute('value');

let songUuid = document.getElementById("songUuid").getAttribute("value");
let commentSection = document.getElementById("commentSection");

function addHtmlComment(body) {
    for (comment of body) {
        let commentHtml = '<div class="second py-2 px-2 mt-2">\n'
        commentHtml += '<div>'
        commentHtml += '<h4>' + comment.authorName + '</h4>'
        commentHtml += '<p>' + comment.text + '</p>'
        commentHtml += '<span>' + comment.createdDate + '</span>'
        commentHtml += '</div>\n'
        commentHtml += '</div>'

        commentSection.innerHTML += commentHtml
    }
}

fetch(`http://localhost:8080/song/${songUuid}/comments`)
    .then((response) => response.json())
    .then((body) => {
            addHtmlComment(body);
        }
);

let commentForm = document.getElementById("commentForm")
commentForm.addEventListener("submit", (event) => {
    event.preventDefault()

    let message = document.getElementById("message");

    fetch(`http://localhost:8080/song/${songUuid}/comments`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            [csrfHeaderName]: csrfHeaderValue
        },
        body: JSON.stringify({
            text: message.value
        })
    })
        .then((res) => res.json())
        .then((data) => {
                message.value = "";
                addHtmlComment([data])
            }
        )
})