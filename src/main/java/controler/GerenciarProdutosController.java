package controler;

import com.google.gson.Gson;
import dao.GerenciarProdutosDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.GerenciarProdutoModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/gerenciar")
public class GerenciarProdutosController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        GerenciarProdutosDAO dao = new GerenciarProdutosDAO();
        List<GerenciarProdutoModel> lista = dao.listarTodos();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(lista));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String linha;
            while ((linha = reader.readLine()) != null) sb.append(linha);
        }

        GerenciarProdutoModel produto = new Gson().fromJson(sb.toString(), GerenciarProdutoModel.class);
        GerenciarProdutosDAO dao = new GerenciarProdutosDAO();

        boolean ok = dao.atualizar(produto);
        response.setStatus(ok ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int id = Integer.parseInt(idParam);
        GerenciarProdutosDAO dao = new GerenciarProdutosDAO();
        boolean ok = dao.excluir(id);
        response.setStatus(ok ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}