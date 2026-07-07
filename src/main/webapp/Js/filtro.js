async function filtroEstoque() {
    try {
        const nome = document.getElementById("pesquisarNome").value;
        const tipo = document.getElementById("tipoMovimentacao").value;
        const data = document.getElementById("filtroData").value;

        const params = new URLSearchParams();
        if (nome) params.append("nome", nome);
        if (tipo) params.append("tipo", tipo);
        if (data) params.append("data", data);

        const response = await fetch("/api/estoque?" + params.toString());
        const dados = await response.json();

        const tabela = document.getElementById("corpoTabela");
        tabela.innerHTML = "";

        if (dados.length === 0) {
            tabela.innerHTML = "<tr><td colspan='10'>Nenhum produto encontrado.</td></tr>";
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
                <td>${parseFloat(item.valor || 0).toFixed(2)}</td>
                <td>${parseFloat(item.total || 0).toFixed(2)}</td>
                <td>
                    <span>${item.status ?? ""}</span>
                    <button type="button" class="btn-historico" data-produto-id="${item.id}">
                        Historico
                    </button>
                </td>
            </tr>`;
        });
    } catch (erro) {
        console.error("Erro ao filtrar:", erro);
    }
}

document.getElementById("btnPesquisar").addEventListener("click", filtroEstoque);
