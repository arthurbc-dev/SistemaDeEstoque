package dao;

import connection.ConnectionFactory;
import model.HistoricoModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoricoDAO {

    public void registrar(Connection conn, int produtoId, String nomeProduto, long quantidade, String tipo, String usuario)
            throws SQLException {

        String sql = "INSERT INTO historico (produto_id, nome_produto, quantidade, tipo, usuario) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, produtoId);
            stmt.setString(2, nomeProduto);
            stmt.setLong(3, quantidade);
            stmt.setString(4, tipo);
            stmt.setString(5, usuario);
            stmt.executeUpdate();
        }
    }
    public boolean registrar(int produtoId, String nomeProduto, long quantidade, String tipo, String usuario) {
        try (Connection conn = ConnectionFactory.getConnection()) {
            registrar(conn, produtoId, nomeProduto, quantidade, tipo, usuario);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<HistoricoModel> listarPorProduto(int produtoId) {
        List<HistoricoModel> lista = new ArrayList<>();

        String sql = "SELECT id, produto_id, nome_produto, quantidade, tipo, usuario, data_hora " +
                "FROM historico WHERE produto_id = ? ORDER BY data_hora DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, produtoId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    HistoricoModel h = new HistoricoModel();
                    h.setId(rs.getInt("id"));
                    h.setProdutoId(rs.getInt("produto_id"));
                    h.setNomeProduto(rs.getString("nome_produto"));
                    h.setQuantidade(rs.getLong("quantidade"));
                    h.setTipo(rs.getString("tipo"));
                    h.setUsuario(rs.getString("usuario"));
                    h.setDataHora(rs.getString("data_hora"));
                    lista.add(h);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
