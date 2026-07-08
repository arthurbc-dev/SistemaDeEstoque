document.addEventListener("DOMContentLoaded", function () {
    const params = new URLSearchParams(window.location.search);
    const erro = params.get("erro");
    const mensagem = document.getElementById("mensagemErroCadastro");

    if (!mensagem) {
        return;
    }

    if (erro === "usuario") {
        mensagem.textContent = "Este usuario ja esta sendo utilizado.";
    } else if (erro === "true") {
        mensagem.textContent = "Nao foi possivel realizar o cadastro. Verifique os dados.";
    }
});
