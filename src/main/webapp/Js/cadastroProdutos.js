window.onload = function () {
    const form = document.getElementById("FormCadastro");

    const inputCodigo = document.querySelector('input[name="codigoBarras"]');
    const inputNome = document.querySelector('input[name="nomeProduto"]');
    const inputFabricante = document.querySelector('input[name="fabricante"]');
    const inputMarca = document.querySelector('input[name="marca"]');
    const inputDataFabricacao = document.querySelector('input[name="dataFabricacao"]');
    const inputDataVencimento = document.querySelector('input[name="dataVencimento"]');
    const inputQtd = document.querySelector('input[name="quantidade"]');
    const inputValor = document.querySelector('input[name="valor"]');
    const inputTotal = document.querySelector('input[name="total"]');
    const inputPrateleira = document.querySelector('input[name="prateleira"]');
    const inputQtdMinima = document.querySelector('input[name="qtdMinima"]');

    const camposProduto = [
        inputNome,
        inputFabricante,
        inputMarca,
        inputValor,
        inputPrateleira,
        inputQtdMinima
    ];
    let codigoProdutoCarregado = "";

    function calcularTotal() {
        const valor = parseFloat(inputValor.value) || 0;
        const quantidade = parseFloat(inputQtd.value) || 0;
        inputTotal.value = (valor * quantidade).toFixed(2);
    }

    function bloquearCamposProduto(bloquear) {
        camposProduto.forEach(campo => {
            if (campo) {
                campo.readOnly = bloquear;
            }
        });
    }

    function preencherProduto(produto) {
        inputNome.value = produto.nomeProduto ?? "";
        inputFabricante.value = produto.fabricante ?? "";
        inputMarca.value = produto.marca ?? "";
        inputValor.value = produto.valor ?? "";
        inputPrateleira.value = produto.prateleira ?? "";
        inputQtdMinima.value = produto.qtdMinima ?? "";

        inputDataFabricacao.value = produto.dataFabricacao ?? "";
        inputDataVencimento.value = produto.dataVencimento ?? "";
        inputQtd.value = "";
        inputTotal.value = "";
    }

    function limparProdutoMantendoCodigo() {
        const codigo = inputCodigo.value;
        form.reset();
        inputCodigo.value = codigo;
        bloquearCamposProduto(false);
        codigoProdutoCarregado = "";
    }

    async function buscarProdutoPorCodigo() {
        const codigo = inputCodigo.value.trim();

        if (!codigo) {
            if (codigoProdutoCarregado) {
                form.reset();
                bloquearCamposProduto(false);
                codigoProdutoCarregado = "";
            }
            return;
        }

        try {
            const response = await fetch(`/api/produtos/codigo?codigoBarras=${codigo}`);

            if (response.status === 404) {
                if (codigoProdutoCarregado) {
                    limparProdutoMantendoCodigo();
                } else {
                    bloquearCamposProduto(false);
                }
                return;
            }

            if (!response.ok) {
                return;
            }

            const produto = await response.json();

            preencherProduto(produto);
            bloquearCamposProduto(true);
            codigoProdutoCarregado = codigo;

            inputDataFabricacao.readOnly = false;
            inputDataVencimento.readOnly = false;
            inputQtd.readOnly = false;

            inputDataFabricacao.focus();
        } catch (erro) {
            console.error("Erro ao buscar produto:", erro);
        }
    }
    const mensagemErro = document.getElementById("mensagemErroProduto");
    const params = new URLSearchParams(window.location.search);
    const erro = params.get("erro");

    if (mensagemErro && erro === "estoque") {
        mensagemErro.textContent = "Não há estoque suficiente para saída.";
    } else if (mensagemErro && erro === "cadastro") {
        mensagemErro.textContent = "Não foi possível cadastrar o produto.";
    }
    inputTotal.readOnly = true; 

    inputCodigo.addEventListener("blur", buscarProdutoPorCodigo);
    inputValor.addEventListener("input", calcularTotal);
    inputQtd.addEventListener("input", calcularTotal);
};
