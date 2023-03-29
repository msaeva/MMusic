const csrfHeaderName = document.getElementById('csrf').getAttribute('name')
const csrfHeaderValue = document.getElementById('csrf').getAttribute('value');

document.getElementById('add-favourite-btn')?.addEventListener('click', (event) => {
        console.log('clicked');
        const songUuid = event.target.dataset['songUuid'];

        fetch(`http://localhost:8080/song/${songUuid}/addToFavourite`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeaderName]: csrfHeaderValue
            },
            body: {}
        })
        .then(() => { location.reload(); })
        .catch(error => { console.error(error); });
});

document.getElementById('remove-favourite-btn')?.addEventListener('click', (event) => {
    const songUuid = event.target.dataset['songUuid'];
    console.log('clicked remove');

    fetch(`http://localhost:8080/song/${songUuid}/removeFromFavourite`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeaderName]: csrfHeaderValue
        },
    })
    .then(() => { location.reload(); })
    .catch(error => { console.error(error); });
});

document.getElementById('like-song-btn')?.addEventListener('click', (event) => {
    const songUuid = event.target.dataset['songUuid'];
    console.log('clicked like');

    fetch(`http://localhost:8080/song/${songUuid}/like`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeaderName]: csrfHeaderValue
        },
        body: {}
    })
        .then(() => { location.reload(); })
        .catch(error => { console.error(error); });
});

document.getElementById('unlike-song-btn')?.addEventListener('click', (event) => {
    const songUuid = event.target.dataset['songUuid'];
    console.log('clicked unlike');

    fetch(`http://localhost:8080/song/${songUuid}/unlike`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeaderName]: csrfHeaderValue
        },
    })
        .then(() => { location.reload(); })
        .catch(error => { console.error(error); });
});


