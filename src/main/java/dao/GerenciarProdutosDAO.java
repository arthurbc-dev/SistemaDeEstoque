package dao;

import connection.ConnectionFactory;
import model.GerenciarProdutoModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GerenciarProdutosDAO {

    public List<GerenciarProdutoModel> listarTodos() {
        List<GerenciarProdutoModel> lista = new ArrayList<>();
        String sql = "SELECT id, codigo_barras, nome_produto, fabricante, marca, " +
                "data_fabricacao, data_vencimento, quantidade, valor, total, status, " +
                "prateleira, qtd_minima FROM produtos ORDER BY nome_produto";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                GerenciarProdutoModel p = new GerenciarProdutoModel();
                p.setId(rs.getInt("id"));
                p.setCodigoBarras(rs.getString("codigo_barras"));
                p.setNomeProduto(rs.getString("nome_produto"));
                p.setFabricante(rs.getString("fabricante"));
                p.setMarca(rs.getString("marca"));
                p.setDataFabricacao(rs.getString("data_fabricacao"));
                p.setDataVencimento(rs.getString("data_vencimento"));
                p.setQuantidade(rs.getLong("quantidade"));
                p.setValor(rs.getString("valor"));
                p.setTotal(rs.getString("total"));
                p.setStatus(rs.getString("status"));
                p.setPrateleira(rs.getString("prateleira"));
                p.setQtdMinima(rs.getInt("qtd_minima"));
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean atualizar(GerenciarProdutoModel p) {
        String sql = "UPDATE produtos SET codigo_barras=?, nome_produto=?, fabricante=?, marca=?, " +
                "data_fabricacao=?, data_vencimento=?, quantidade=?, valor=?, total=?, status=?, " +
                "prateleira=?, qtd_minima=? WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getCodigoBarras());
            stmt.setString(2, p.getNomeProduto());
            stmt.setString(3, p.getFabricante());
            stmt.setString(4, p.getMarca());
            stmt.setString(5, p.getDataFabricacao());
            stmt.setString(6, p.getDataVencimento());
            stmt.setLong(7, p.getQuantidade());
            stmt.setString(8, p.getValor());
            stmt.setString(9, p.getTotal());
            stmt.setString(10, p.getStatus());
            stmt.setString(11, p.getPrateleira());
            stmt.setInt(12, p.getQtdMinima());
            stmt.setInt(13, p.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean excluir(int id) {
        String sql = "DELETE FROM produtos WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}