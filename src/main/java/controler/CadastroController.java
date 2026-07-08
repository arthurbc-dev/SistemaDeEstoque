package controler;

import dao.CadastrosUsersDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.CadastroUsuarioModel;

@WebServlet("/cadastro")
public class CadastroController extends HttpServlet {

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        CadastroUsuarioModel user = new CadastroUsuarioModel();


        user.setNome(request.getParameter("namefirst"));
        user.setSobrenome(request.getParameter("sobrenome"));
        user.setMatricula(request.getParameter("matricula"));
        user.setCpf(request.getParameter("cpf"));
        user.setSexo(request.getParameter("sexo"));
        user.setDtaNascimento(request.getParameter("dtaNascimento"));
        user.setEmail(request.getParameter("email"));
        user.setTelefone(request.getParameter("telefone"));
        user.setNomeUsuario(request.getParameter("usuario"));
        user.setSenha(request.getParameter("senha"));
        user.setFuncao(request.getParameter("funcao"));
        user.setCep(request.getParameter("cep"));
        user.setEndereco(request.getParameter("endereco"));
        user.setNumero(request.getParameter("numero"));
        user.setBairro(request.getParameter("bairro"));
        user.setCidade(request.getParameter("cidade"));
        user.setEstado(request.getParameter("estado"));
        user.setComplemento(request.getParameter("complemento"));

        CadastrosUsersDAO dao = new CadastrosUsersDAO();

        if (dao.usuarioExiste(user.getNomeUsuario())) {
            response.sendRedirect(request.getContextPath() + "/pages/cadastro.html?erro=usuario");
            return;
        }

        if (dao.cadastrar(user)) {
            response.sendRedirect(request.getContextPath() + "/index.html?cadastro=sucesso");
        } else {
            response.sendRedirect(request.getContextPath() + "/pages/cadastro.html?erro=true");
        }
    }
}
