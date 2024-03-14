window.onload = function() {
    var token = localStorage.getItem('token'); 
    console.log("tokem é esse ------->"+token);

    if (!token) {
        alert('Token de acesso não encontrado. Por favor, faça login novamente.');
        return;
    }

    fetch('/tarefa/pendentes', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': token 
        }
    })
    .then(response => response.json())
    .then(tarefas => {
        const tarefasElement = document.getElementById('tarefas');
        tarefas.forEach(tarefa => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${tarefa.descricao}</td>
                <td>${tarefa.prioridade}</td>
                <td>
                    <button onclick="concluirTarefa(${tarefa.id})">Concluir</button>
                    <button onclick="editarTarefa(${tarefa.id})">Editar</button>
                </td>
            `;
            tarefasElement.appendChild(tr);
        });
    });
}

function concluirTarefa(id) {
    var token = localStorage.getItem('token');

    fetch(`/tarefa/concluir/${id}`, {
        method: 'PUT',
        headers: {
            'Authorization': 'Bearer ' + token
        },
    })
    .then(response => response.json())
    .then(data => {
        console.log(data);
        alert('Tarefa concluída com sucesso!');
        location.reload();
    })
    .catch((error) => {
        console.error('Error:', error);
        alert('Erro ao concluir a tarefa. Por favor, tente novamente.');
    });
}

function editarTarefa(id) {
    // Implemente a lógica para editar a tarefa aqui
}
