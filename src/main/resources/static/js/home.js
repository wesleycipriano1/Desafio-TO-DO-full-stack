window.onload = function() {
    let token = localStorage.getItem('token');
    console.log(token);

    if (!token) {
        window.location.href = '/login';
        return;
    }

    

    fetch('/home', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': token
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro ao acessar a página home');
        }
        return response.text();
    })
    .then(data => {
        
    })
    .catch((error) => {
        console.error('Error:', error);
        alert('Erro ao acessar a página home. Por favor, tente novamente.');
        setTimeout(function(){ alert(''); }, 4000);
    });
};
