async function validarLogin() {
    try {
        const res = await fetch("../api/perfil");
        const dado = await res.json();

        if (!dado.perfil || dado.perfil.toLowerCase() !== "admin") {
            const btnCadastro = document.querySelector(".btn-menu");
            if (btnCadastro) btnCadastro.classList.add("is-hidden");
        }
    } catch (e) {
        console.error("Erro ao verificar o perfil", e);
    }
}

validarLogin();
