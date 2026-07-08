let todosProdutos = [];
let idParaExcluir = null;

async function carregarProdutos() {
    try {
        const response = await fetch("/api/gerenciar");
        if (!response.ok) { console.error("Erro ao carregar produtos:", response.status); return; }

        todosProdutos = await response.json();
        renderizarTabela(todosProdutos);
        renderizarAlertas(todosProdutos);
    } catch (erro) {
        console.error("Erro:", erro);
    }
}

function produtoPrecisaReposicao(produto) {
    return produto.qtdMinima > 0 && produto.quantidadeTotalCodigo <= produto.qtdMinima;
}

function renderizarTabela(produtos) {
    const corpo = document.getElementById("corpoTabela");

    if (produtos.length === 0) {
        corpo.innerHTML = `<tr><td colspan="10" class="msg-vazia">Nenhum produto encontrado.</td></tr>`;
        return;
    }

    corpo.innerHTML = produtos.map(p => {
        const precisaRepor = produtoPrecisaReposicao(p);
        const badgeReposicao = precisaRepor
            ? `<span class="badge-reposicao badge-repor">⚠ Repor</span>`
            : `<span class="badge-reposicao badge-ok">✓ OK</span>`;

        const badgeStatus = p.status === "entrada"
            ? `<span class="badge-status-entrada">Entrada</span>`
            : `<span class="badge-status-saida">Saída</span>`;

        const prateleira = p.prateleira
            ? `<span class="prateleira-badge">${p.prateleira}</span>`
            : `<span class="texto-ausente">—</span>`;

        const qtdMinima = p.qtdMinima > 0 ? p.qtdMinima : `<span class="texto-ausente">—</span>`;

        return `<tr>
            <td>${p.codigoBarras ?? "—"}</td>
            <td>${p.nomeProduto ?? "—"}</td>
            <td>${p.fabricante ?? "—"}</td>
            <td>${prateleira}</td>
            <td>${p.quantidade ?? 0}</td>
            <td>${qtdMinima}</td>
            <td>${p.dataVencimento ?? "—"}</td>
            <td>${p.valor ?? "—"}</td>
            <td>${p.total ?? "—"}</td>
            <td>${badgeStatus}</td>
            <td>${badgeReposicao}</td>
            <td>
                <button class="btn-editar" data-produto-id="${p.id}">Editar</button>
                <button class="btn-excluir" data-produto-id="${p.id}" data-produto-nome="${p.nomeProduto ?? ""}">Excluir</button>
            </td>
        </tr>`;
    }).join("");
}

function renderizarAlertas(produtos) {
    const secao = document.getElementById("alertas-reposicao");
    const lista = document.getElementById("listaAlertas");

    const precisamRepor = produtos.filter(p => produtoPrecisaReposicao(p));

    if (precisamRepor.length === 0) {
        secao.classList.add("is-hidden");
        return;
    }

    secao.classList.remove("is-hidden");
    lista.innerHTML = precisamRepor.map(p => `
        <div class="card-alerta">
            <div class="card-alerta-nome">${p.nomeProduto}</div>
            <div class="card-alerta-info">Estoque: ${p.quantidadeTotalCodigo} | Mínimo: ${p.qtdMinima}</div>
            <div class="card-alerta-prateleira">${p.prateleira ? "📍 " + p.prateleira : "Sem localização definida"}</div>
        </div>
    `).join("");
}

function filtrar() {
    const busca = document.getElementById("filtroBusca").value.toLowerCase();
    const status = document.getElementById("filtroStatus").value;
    const reposicao = document.getElementById("filtroReposicao").value;

    const filtrados = todosProdutos.filter(p => {
        const matchBusca = !busca
            || (p.nomeProduto ?? "").toLowerCase().includes(busca)
            || (p.codigoBarras ?? "").toLowerCase().includes(busca);

        const matchStatus = !status || p.status === status;

        const precisaRepor = produtoPrecisaReposicao(p);
        const matchReposicao = !reposicao
            || (reposicao === "sim" && precisaRepor)
            || (reposicao === "nao" && !precisaRepor);

        return matchBusca && matchStatus && matchReposicao;
    });

    renderizarTabela(filtrados);
}


function abrirModalEdicao(id) {
    const p = todosProdutos.find(x => x.id === id);
    if (!p) return;

    document.getElementById("editId").value = p.id;
    document.getElementById("editCodigo").value = p.codigoBarras ?? "";
    document.getElementById("editNome").value = p.nomeProduto ?? "";
    document.getElementById("editFabricante").value = p.fabricante ?? "";
    document.getElementById("editMarca").value = p.marca ?? "";
    document.getElementById("editPrateleira").value = p.prateleira ?? "";
    document.getElementById("editQtdMinima").value = p.qtdMinima ?? "";
    document.getElementById("editQuantidade").value = p.quantidade ?? "";
    document.getElementById("editValor").value = p.valor ?? "";
    document.getElementById("editDataFabricacao").value = p.dataFabricacao ?? "";
    document.getElementById("editDataVencimento").value = p.dataVencimento ?? "";
    document.getElementById("editStatus").value = p.status ?? "entrada";

    const qtd = parseFloat(p.quantidade) || 0;
    const val = parseFloat(p.valor) || 0;
    document.getElementById("editTotal").value = p.total ?? (qtd * val).toFixed(2);

    document.getElementById("modalOverlay").classList.remove("is-hidden");
}

function fecharModalEdicao() {
    document.getElementById("modalOverlay").classList.add("is-hidden");
}

function calcularTotal() {
    const quantidade = parseFloat(document.getElementById("editQuantidade").value) || 0;
    const valor = parseFloat(document.getElementById("editValor").value) || 0;
    document.getElementById("editTotal").value = (quantidade * valor).toFixed(2);
}

async function salvarEdicao() {
    const id = document.getElementById("editId").value;

    const body = {
        id: parseInt(id),
        codigoBarras: document.getElementById("editCodigo").value,
        nomeProduto: document.getElementById("editNome").value,
        fabricante: document.getElementById("editFabricante").value,
        marca: document.getElementById("editMarca").value,
        prateleira: document.getElementById("editPrateleira").value,
        qtdMinima: parseInt(document.getElementById("editQtdMinima").value) || 0,
        quantidade: parseInt(document.getElementById("editQuantidade").value) || 0,
        valor: document.getElementById("editValor").value,
        total: document.getElementById("editTotal").value,
        dataFabricacao: document.getElementById("editDataFabricacao").value,
        dataVencimento: document.getElementById("editDataVencimento").value,
        status: document.getElementById("editStatus").value
    };

    try {
        const response = await fetch("/api/gerenciar", {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });

        if (response.ok) {
            fecharModalEdicao();
            carregarProdutos();
        } else {
            alert("Erro ao salvar. Tente novamente.");
        }
    } catch (erro) {
        console.error("Erro ao salvar edição:", erro);
    }
}

function abrirModalExclusao(id, nome) {
    idParaExcluir = id;
    document.getElementById("nomeProdutoExclusao").textContent = nome;
    document.getElementById("modalExclusaoOverlay").classList.remove("is-hidden");
}

function fecharModalExclusao() {
    idParaExcluir = null;
    document.getElementById("modalExclusaoOverlay").classList.add("is-hidden");
}

async function confirmarExclusao() {
    if (!idParaExcluir) return;

    try {
        const response = await fetch(`/api/gerenciar?id=${idParaExcluir}`, {
            method: "DELETE"
        });

        if (response.ok) {
            fecharModalExclusao();
            carregarProdutos();
        } else {
            alert("Erro ao excluir. Tente novamente.");
        }
    } catch (erro) {
        console.error("Erro ao excluir:", erro);
    }
}


document.getElementById("editQuantidade").addEventListener("input", calcularTotal);
document.getElementById("editValor").addEventListener("input", calcularTotal);
document.getElementById("btnPesquisar").addEventListener("click", filtrar);
document.getElementById("filtroBusca").addEventListener("keyup", e => { if (e.key === "Enter") filtrar(); });
document.getElementById("btnFecharModal").addEventListener("click", fecharModalEdicao);
document.getElementById("btnCancelarModal").addEventListener("click", fecharModalEdicao);
document.getElementById("btnSalvarEdicao").addEventListener("click", salvarEdicao);
document.getElementById("btnFecharExclusao").addEventListener("click", fecharModalExclusao);
document.getElementById("btnCancelarExclusao").addEventListener("click", fecharModalExclusao);
document.getElementById("btnConfirmarExclusao").addEventListener("click", confirmarExclusao);

document.getElementById("corpoTabela").addEventListener("click", e => {
    const btnEditar = e.target.closest(".btn-editar");
    if (btnEditar) {
        abrirModalEdicao(parseInt(btnEditar.dataset.produtoId));
        return;
    }

    const btnExcluir = e.target.closest(".btn-excluir");
    if (btnExcluir) {
        abrirModalExclusao(parseInt(btnExcluir.dataset.produtoId), btnExcluir.dataset.produtoNome);
    }
});

document.getElementById("modalOverlay").addEventListener("click", e => {
    if (e.target === document.getElementById("modalOverlay")) fecharModalEdicao();
});
document.getElementById("modalExclusaoOverlay").addEventListener("click", e => {
    if (e.target === document.getElementById("modalExclusaoOverlay")) fecharModalExclusao();
});

window.onload = carregarProdutos;
