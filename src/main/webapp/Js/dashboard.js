async function carregarEstoque() {
    try {
        const response = await fetch("/api/estoque");
        if (!response.ok) { console.error("Erro estoque:", response.status); return; }

        const dados = await response.json();
        const tabela = document.getElementById("corpoTabela");
        tabela.innerHTML = "";

        if (dados.length === 0) {
            tabela.innerHTML = "<tr><td colspan='10'>Nenhum produto cadastrado.</td></tr>";
            return;
        }

        dados.forEach(item => {
            tabela.innerHTML += `<tr>
                <td>${item.codigoBarras ?? ""}</td>
                <td>${item.nomeProduto ?? ""}</td>
                <td>${item.fabricante ?? ""}</td>
                <td>${item.marca ?? ""}</td>
                <td>${item.dataFabricacao ?? ""}</td>
                <td>${item.dataVencimento ?? ""}</td>
                <td>${item.quantidade ?? ""}</td>
                <td>${item.valor ?? ""}</td>
                <td>${item.total ?? ""}</td>
                <td>
                    <span>${item.status ?? ""}</span>
                    <button type="button" class="btn-historico" data-produto-id="${item.id}">
                        Historico
                    </button>
                </td>
            </tr>`;
        });
    } catch (erro) {
        console.error("Erro ao carregar os produtos:", erro);
    }
}

async function carregarResumo() {
    try {
        const response = await fetch("/api/resumo");
        if (!response.ok) { console.error("Erro resumo:", response.status); return; }

        const dados = await response.json();
        document.getElementById("cardEntrada").innerHTML = dados.entrada ?? 0;
        document.getElementById("cardSaida").innerHTML   = dados.saida   ?? 0;
        document.getElementById("cardTotal").innerHTML   = dados.total   ?? 0;
    } catch (erro) {
        console.error("Erro ao carregar o resumo:", erro);
    }
}

async function abrirHistorico(produtoId, nomeProduto) {
    const painel = document.getElementById("painelHistorico");
    const titulo = document.getElementById("tituloHistorico");
    const conteudo = document.getElementById("conteudoHistorico");

    titulo.textContent = `Historico - ${nomeProduto}`;
    conteudo.innerHTML = "<p class='msg-historico'>Carregando...</p>";
    painel.classList.add("aberto");

    try {
        const response = await fetch(`/api/historico?produtoId=${produtoId}`);

        if (!response.ok) {
            conteudo.innerHTML = "<p class='msg-historico'>Erro ao carregar historico.</p>";
            return;
        }

        const historico = await response.json();

        if (historico.length === 0) {
            conteudo.innerHTML = "<p class='msg-historico'>Nenhuma movimentacao encontrada.</p>";
            return;
        }

        conteudo.innerHTML = historico.map(item => `
            <div class="item-historico">
                <div class="item-historico-topo">
                    <strong class="${item.tipo === "entrada" ? "tipo-entrada" : "tipo-saida"}">${item.tipo}</strong>
                    <span>${item.dataHora ?? ""}</span>
                </div>
                <p>Quantidade: ${item.quantidade ?? 0}</p>
                <p>Usuario: ${item.usuario ?? ""}</p>
            </div>
        `).join("");

    } catch (erro) {
        console.error("Erro ao carregar historico:", erro);
        conteudo.innerHTML = "<p class='msg-historico'>Erro ao carregar historico.</p>";
    }
}

function fecharHistorico() {
    document.getElementById("painelHistorico").classList.remove("aberto");
}

document.addEventListener("click", function (event) {
    const botao = event.target.closest(".btn-historico");
    if (botao) {
        const linha = botao.closest("tr");
        const nomeProduto = linha ? linha.children[1].textContent : "Produto";
        abrirHistorico(botao.dataset.produtoId, nomeProduto);
        return;
    }

    if (event.target.id === "btnFecharHistorico") {
        fecharHistorico();
    }
});

window.onload = () => {
    carregarEstoque();
    carregarResumo();
};
