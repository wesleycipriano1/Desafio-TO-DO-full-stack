document.getElementById('cadastro-form').addEventListener('submit', function(event) {
    event.preventDefault();
    var nome = document.querySelector('#cadastro-form input[name="nome"]').value;
    var email = document.querySelector('#cadastro-form input[name="email"]').value;
    var senha = document.querySelector('#cadastro-form input[name="senha"]').value;
    var regex = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=./?]).*$/;
    if (!regex.test(senha)) {
        var msg = document.getElementById('error-message');
        msg.textContent = 'A senha deve conter pelo menos 1 número e 1 letra.';
        setTimeout(function(){ msg.textContent = ''; }, 5000);
        return;
    }

    fetch('/usuarios/cadastrar', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ nome: nome, email: email, senha: senha }),
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro no cadastro');
        }
        return response.json();
    })
    .then(data => {
        console.log(data);
        document.getElementById('error-message').textContent = ''; 
        alert('Cadastro realizado com sucesso!');
    })
    .catch((error) => {
        console.error('Error:', error);
        document.getElementById('error-message').textContent = 'Erro no cadastro. Por favor, tente novamente.'; 
        setTimeout(function() {
            document.getElementById('error-message').textContent = ''; 
        }, 5000);
    });
});
