document.getElementById('create-post-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const title = document.getElementById('title').value;
    const content = document.getElementById('content').value;

    if (username && title && content) {
        const postHtml = `
            <div class="post">
                <h3>${title}</h3>
                <p>${content}</p>
                <small>Posted by: ${username}</small>
            </div>
        `;

        document.getElementById('posts-container').innerHTML += postHtml;
        document.getElementById('create-post-form').reset();
    } else {
        alert("Please fill out all fields.");
    }
});
