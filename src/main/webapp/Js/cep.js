document.addEventListener("DOMContentLoaded", function () {
    const campoCep = document.getElementById("cep");
    const campoCpf = document.getElementById("cpf");
    const campoTelefone = document.getElementById("telefone");

    campoCep.addEventListener("blur", buscarCep);

    campoCep.addEventListener("keypress", function (e) {
        if (e.key === "Enter") {
            e.preventDefault();
            buscarCep();
        }
    });

    campoCep.addEventListener("input", function () {
        let valor = this.value.replace(/\D/g, "");

        if (valor.length > 5) {
            valor = valor.substring(0, 5) + "-" + valor.substring(5, 8);
        }

        this.value = valor;
    });

    if (campoCpf) {
        campoCpf.addEventListener("input", function () {
            let valor = this.value.replace(/\D/g, "").substring(0, 11);

            valor = valor.replace(/(\d{3})(\d)/, "$1.$2");
            valor = valor.replace(/(\d{3})(\d)/, "$1.$2");
            valor = valor.replace(/(\d{3})(\d{1,2})$/, "$1-$2");

            this.value = valor;
        });
    }

    if (campoTelefone) {
        campoTelefone.addEventListener("input", function () {
            let valor = this.value.replace(/\D/g, "").substring(0, 11);

            if (valor.length <= 10) {
                valor = valor.replace(/(\d{2})(\d)/, "($1) $2");
                valor = valor.replace(/(\d{4})(\d)/, "$1-$2");
            } else {
                valor = valor.replace(/(\d{2})(\d)/, "($1) $2");
                valor = valor.replace(/(\d{5})(\d)/, "$1-$2");
            }

            this.value = valor;
        });
    }
});

async function buscarCep() {
    const campoCep = document.getElementById("cep");
    const cep = campoCep.value.replace(/\D/g, "");

    if (cep.length !== 8) {
        if (cep.length > 0) {
            alert("O seu CEP está incorreto");
            limparCampoEndereco();
        }
        return;
    }

    campoCep.style.borderColor = "#aaa";
    preencherCampos({ aguardando: true });

    try {
        const response = await fetch(`https://viacep.com.br/ws/${cep}/json/`);
        const dados = await response.json();

        if (dados.erro) {
            alert("CEP não encontrado");
            limparCampoEndereco();
            campoCep.style.borderColor = "red";
            return;
        }

        preencherCampos(dados);
        campoCep.style.borderColor = "green";

        campoCep.value = cep.replace(/(\d{5})(\d{3})/, "$1-$2");

        document.getElementById("numero").focus();

    } catch (error) {
        alert("Verifique a conexão com a internet");
        limparCampoEndereco();
        campoCep.style.borderColor = "red";
        console.error("Erro na busca do CEP", error);
    }
}

function preencherCampos(dados) {
    if (dados.aguardando) {
        document.getElementById("endereco").value = "Buscando...";
        document.getElementById("cidade").value = "Buscando...";
        document.getElementById("bairro").value = "Buscando...";
        document.getElementById("estado").value = "Buscando...";
        document.getElementById("numero").value = "";
        document.getElementById("complemento").value = "";
        return;
    }

    document.getElementById("endereco").value = dados.logradouro || "";
    document.getElementById("cidade").value = dados.localidade || "";
    document.getElementById("bairro").value = dados.bairro || "";
    document.getElementById("estado").value = dados.uf || "";
    document.getElementById("numero").value = "";
    document.getElementById("complemento").value = dados.complemento || "";
}

function limparCampoEndereco() {
    document.getElementById("endereco").value = "";
    document.getElementById("cidade").value = "";
    document.getElementById("bairro").value = "";
    document.getElementById("estado").value = "";
    document.getElementById("numero").value = "";
    document.getElementById("complemento").value = "";
}
