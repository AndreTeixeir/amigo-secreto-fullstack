const listaAmigos = [];

function adicionarAmigo() {
    const input = document.getElementById("amigo");
    const nome = input.value.trim();

    if (nome === "") {
        alert("Por favor, insira um nome válido.");
        return;
    }

    if (listaAmigos.includes(nome)) {
        alert("Esse nome já foi adicionado!");
        return;
    }

    listaAmigos.push(nome);
    atualizarLista();
    input.value = "";
}

function atualizarLista() {
    const lista = document.getElementById("listaAmigos");
    lista.innerHTML = "";

    listaAmigos.forEach((nome, index) => {
        const item = document.createElement("li");
        item.textContent = nome;

        const botaoRemover = document.createElement("button");
        botaoRemover.textContent = "❌";
        botaoRemover.onclick = () => removerAmigo(index);
        botaoRemover.style.marginLeft = "10px";

        item.appendChild(botaoRemover);
        lista.appendChild(item);
    });
}

function removerAmigo(index) {
    listaAmigos.splice(index, 1);
    atualizarLista();
}

function sortearAmigo() {
    if (listaAmigos.length === 0) {
        alert("Adicione pelo menos um amigo antes de sortear.");
        return;
    }

    const indiceSorteado = Math.floor(Math.random() * listaAmigos.length);
    const nomeSorteado = listaAmigos[indiceSorteado];

    const resultado = document.getElementById("resultado");
    resultado.innerHTML = "";
    const item = document.createElement("li");
    item.innerHTML = `🎉 O amigo secreto é: <strong>${nomeSorteado}</strong> 🎉`;
    item.style.opacity = "0";
    resultado.appendChild(item);

    setTimeout(() => {
        item.style.transition = "opacity 1s";
        item.style.opacity = "1";
    }, 100);
}

