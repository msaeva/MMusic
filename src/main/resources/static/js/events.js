(() => {
    const csrfHeaderName = document.getElementById('csrf').getAttribute('name')
    const csrfHeaderValue = document.getElementById('csrf').getAttribute('value');

    const buttons = document.getElementsByClassName('delete-song-btn');

    Array.from(buttons).forEach((button) => {
        button.addEventListener('click', () => {
            const songUuid = button.dataset['songUuid'];
            console.log(button.songUuid);

            fetch(`http://localhost:8080/song/${songUuid}/delete`, {
                method: 'DELETE',
                headers :{
                    'Content-Type': 'application/json',
                    [csrfHeaderName]: csrfHeaderValue
                }
            })
                .then(() => {
                    location.reload();
                })
                .catch(error => {
                    console.error(error);
                });
        })
    });

})();
