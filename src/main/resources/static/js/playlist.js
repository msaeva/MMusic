const csrfHeaderName = document.getElementById('csrf').getAttribute('name');
const csrfHeaderValue = document.getElementById('csrf').getAttribute('value');

const buttons = document.getElementsByClassName('remove-song-playlist-btn');
Array.from(buttons).forEach((button) => {
    button.addEventListener('click', () => {
        const songUuid = button.dataset['songUuid'];
        const playlistUuid = button.dataset['playlistUuid'];

        fetch(`http://localhost:8080/playlist/${playlistUuid}/song/${songUuid}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                [csrfHeaderName]: csrfHeaderValue
            },
        }).then(() => {
            location.reload();
        }).catch(() => {
            console.log('error');
        })
    });
});

const form = document.getElementById('add-song-playlist-form');
form.addEventListener('submit', (event) => {
    event.preventDefault();

    const data = new FormData(event.target);

    const songUuid = data.get('selected-song');
    const playlistUuid = data.get('playlist-uuid');

    fetch(`http://localhost:8080/playlist/${playlistUuid}/add/${songUuid}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            [csrfHeaderName]: csrfHeaderValue
        },
        body: {}
    }).then(() => {
        location.reload();
    }).catch(() => {
        console.log('error');
    });
})

