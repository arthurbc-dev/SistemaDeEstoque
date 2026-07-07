package controler;

import com.google.gson.Gson;
import dao.CadastroProdutoDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CadastroProdutoModel;

import java.io.IOException;

@WebServlet("/api/produtos/codigo")
public class ProdutoCodigoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String codigoBarras = request.getParameter("codigoBarras");

        if (codigoBarras == null || codigoBarras.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        CadastroProdutoDAO dao = new CadastroProdutoDAO();
        CadastroProdutoModel produto = dao.buscarPorCodigoBarras(codigoBarras);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (produto == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{}");
            return;
        }

        String json = new Gson().toJson(produto);
        response.getWriter().write(json);
    }
}