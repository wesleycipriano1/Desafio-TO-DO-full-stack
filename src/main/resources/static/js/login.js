
        document.getElementById('login-button').addEventListener('click', function(event) {
            var email = document.querySelector('#login-form input[name="email"]').value;
            var senha = document.querySelector('#login-form input[name="senha"]').value;
        
            fetch('/usuarios/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email: email, senha: senha }),
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Erro no login');
                }
                return response.text();
            })
            .then(token => {
                localStorage.setItem('token', token);
                
                setTimeout(function(){  }, 4000);
                  
                    window.location.href = '/tarefas/pendentes';
                   
            })
            
            .catch((error) => {
                console.error('Error:', error);
                alert('Erro no login. Por favor, tente novamente.');
                setTimeout(function(){ alert(''); }, 4000);
            });
        });