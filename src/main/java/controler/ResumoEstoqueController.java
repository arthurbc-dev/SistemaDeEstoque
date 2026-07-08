package controler;

import com.google.gson.Gson;
import connection.ConnectionFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/resumo")
public class ResumoEstoqueController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String sql = """
                SELECT
                    COALESCE((SELECT SUM(quantidade) FROM historico WHERE tipo = 'entrada'), 0) AS entrada,
                    COALESCE((SELECT SUM(quantidade) FROM historico WHERE tipo = 'saida'), 0) AS saida,
                    COALESCE((SELECT SUM(quantidade) FROM produtos), 0) AS total
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            long entrada = 0;
            long saida = 0;
            long total = 0;

            if (rs.next()) {
                entrada = rs.getLong("entrada");
                saida = rs.getLong("saida");
                total = rs.getLong("total");
            }

            Map<String, Long> resultado = new HashMap<>();
            resultado.put("entrada", entrada);
            resultado.put("saida", saida);
            resultado.put("total", total);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new Gson().toJson(resultado));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
