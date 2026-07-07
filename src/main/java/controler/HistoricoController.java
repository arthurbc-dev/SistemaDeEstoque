package controler;

import com.google.gson.Gson;
import dao.HistoricoDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.HistoricoModel;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/historico")
public class HistoricoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String produtoIdParam = request.getParameter("produtoId");

        if (produtoIdParam == null || produtoIdParam.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int produtoId = Integer.parseInt(produtoIdParam);
        List<HistoricoModel> historico = new HistoricoDAO().listarPorProduto(produtoId);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(historico));
    }
}
