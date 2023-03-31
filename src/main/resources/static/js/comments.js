let songUuid = document.getElementById("songUuid").getAttribute("value");
let commentSection = document.getElementById("commentSection");
let authUser = document.getElementById("authUser").value

function addHtmlComment(body) {
    for (let comment of body) {
        let commentHtml = `<div id="comment-container-${comment.uuid}" class="second py-2 px-2 mt-2">\n`
        commentHtml += '<div>'
        commentHtml += '<h4>' + comment.username + '</h4>'
        commentHtml += '<p>' + comment.text + '</p>'
        commentHtml += '<span>' + comment.createdDate + '</span>'
        commentHtml += '</div>\n'
        if (comment.username === authUser) {
            commentHtml += `<button id="comment-delete-btn-${comment.uuid}" class="btn btn-danger" onclick="deleteComment('${comment.uuid}')">Delete</button>`
        }
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
});

function deleteComment(commentUuid) {
    fetch(`http://localhost:8080/song/${songUuid}/comments/${commentUuid}/delete`, {
        method: 'DELETE',
        headers: {
            [csrfHeaderName]: csrfHeaderValue
        }
    }).then(res => {
        console.log(res)
        document.getElementById("comment-container-" + commentUuid).remove()
    })
}