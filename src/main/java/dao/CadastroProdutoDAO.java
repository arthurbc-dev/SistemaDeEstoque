package dao;

import connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CadastroProdutoModel;

public class CadastroProdutoDAO {

    public boolean salvar(CadastroProdutoModel produto) {
        String sql = "INSERT INTO produtos " +
                "(codigo_barras,nome_produto,fabricante,marca,data_fabricacao,data_vencimento,quantidade,valor,total,status,prateleira,qtd_minima) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, produto.getCodigoBarras());
            stmt.setString(2, produto.getNomeProduto());
            stmt.setString(3, produto.getFabricante());
            stmt.setString(4, produto.getMarca());
            stmt.setDate(5, java.sql.Date.valueOf(produto.getDataFabricacao()));
            stmt.setDate(6, java.sql.Date.valueOf(produto.getDataVencimento()));
            stmt.setLong(7, produto.getQuantidade());
            stmt.setString(8, produto.getValor());
            stmt.setString(9, produto.getTotal());
            stmt.setString(10, produto.getStatus());
            stmt.setString(11, produto.getPrateleira());
            stmt.setInt(12, produto.getQtdMinima());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public CadastroProdutoModel buscarPorCodigoBarras(String codigoBarras) {
        String sql = "SELECT id, codigo_barras, nome_produto, fabricante, marca, " +
                "data_fabricacao, data_vencimento, quantidade, valor, total, status, " +
                "prateleira, qtd_minima FROM produtos WHERE codigo_barras = ? LIMIT 1";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigoBarras);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CadastroProdutoModel p = new CadastroProdutoModel();

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

                    return p;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public CadastroProdutoModel buscarPorCodigoEValidade(String codigoBarras, String dataVencimento) {
        String sql = "SELECT id, codigo_barras, nome_produto, fabricante, marca, " +
                "data_fabricacao, data_vencimento, quantidade, valor, total, status, " +
                "prateleira, qtd_minima FROM produtos " +
                "WHERE codigo_barras = ? AND data_vencimento = ? LIMIT 1";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigoBarras);
            stmt.setString(2, dataVencimento);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CadastroProdutoModel p = new CadastroProdutoModel();

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

                    return p;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public CadastroProdutoModel buscarPorId(int id) {
        String sql = "SELECT id, codigo_barras, nome_produto, fabricante, marca, " +
                "data_fabricacao, data_vencimento, quantidade, valor, total, status, " +
                "prateleira, qtd_minima FROM produtos WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CadastroProdutoModel p = new CadastroProdutoModel();

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

                    return p;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public boolean atualizarQuantidadeProduto(int idProduto, long quantidadeMovimentada, String tipo) {
        CadastroProdutoModel produto = buscarPorId(idProduto);

        if (produto == null) {
            return false;
        }

        long quantidadeAtual = produto.getQuantidade();
        long novaQuantidade;

        if ("entrada".equalsIgnoreCase(tipo)) {
            novaQuantidade = quantidadeAtual + quantidadeMovimentada;
        } else if ("saida".equalsIgnoreCase(tipo)) {
            novaQuantidade = quantidadeAtual - quantidadeMovimentada;

            if (novaQuantidade < 0) {
                return false;
            }
        } else {
            return false;
        }

        String sql = "UPDATE produtos SET quantidade = ?, status = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, novaQuantidade);
            stmt.setString(2, tipo);
            stmt.setInt(3, idProduto);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CadastroProdutoModel> listarComFiltro(String nome, String tipo, String data) {
        List<CadastroProdutoModel> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM produtos WHERE 1=1 ");
        if (nome != null && !nome.isEmpty()) {
            sql.append("AND LOWER(nome_produto) LIKE ? ");
        }
        if (tipo != null && !tipo.isEmpty()) {
            sql.append("AND status = ? ");
        }
        if (data != null && !data.isEmpty()) {
            sql.append("AND data_fabricacao = ? ");
        }

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (nome != null && !nome.isEmpty()) {
                stmt.setString(index++, "%" + nome.toLowerCase() + "%");
            }
            if (tipo != null && !tipo.isEmpty()) {
                stmt.setString(index++, tipo);
            }
            if (data != null && !data.isEmpty()) {
                stmt.setString(index++, data);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CadastroProdutoModel p = new CadastroProdutoModel();
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
                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
