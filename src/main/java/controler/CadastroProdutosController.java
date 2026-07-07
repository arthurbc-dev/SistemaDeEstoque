/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controler;

import dao.CadastroProdutoDAO;
import dao.HistoricoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import jakarta.servlet.http.HttpSession;
import model.CadastroProdutoModel;

/**
 *
 * @author 232.999257
 */
@WebServlet("/cadastroProdutos")
public class CadastroProdutosController extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CadastroProdutoModel produto = new CadastroProdutoModel();
        produto.setCodigoBarras(request.getParameter("codigoBarras"));
        produto.setNomeProduto(request.getParameter("nomeProduto"));
        produto.setFabricante(request.getParameter("fabricante"));
        produto.setMarca(request.getParameter("marca"));
        produto.setDataFabricacao(request.getParameter("dataFabricacao"));
        produto.setDataVencimento(request.getParameter("dataVencimento"));
        produto.setQuantidade(Long.parseLong(request.getParameter("quantidade")));
        produto.setValor(request.getParameter("valor"));
        produto.setTotal(request.getParameter("total"));
        produto.setStatus(request.getParameter("status"));

        String prateleira = request.getParameter("prateleira");
        produto.setPrateleira(prateleira != null ? prateleira : "");

        String qtdMinimaParam = request.getParameter("qtdMinima");
        produto.setQtdMinima(qtdMinimaParam != null && !qtdMinimaParam.isBlank()
                ? Integer.parseInt(qtdMinimaParam)
                : 0);

        CadastroProdutoDAO dao = new CadastroProdutoDAO();
        HistoricoDAO historicoDAO = new HistoricoDAO();

        HttpSession session = request.getSession(false);
        String usuario = "Sistema";

        if (session != null && session.getAttribute("usuario") != null) {
            usuario = session.getAttribute("usuario").toString();
        }

        CadastroProdutoModel produtoExistente = dao.buscarPorCodigoBarras(produto.getCodigoBarras());

        boolean sucesso = false;

        if (produtoExistente != null) {
            sucesso = dao.atualizarQuantidadeProduto(
                    produtoExistente.getId(),
                    produto.getQuantidade(),
                    produto.getStatus()
            );

            if (sucesso) {
                historicoDAO.registrar(
                        produtoExistente.getId(),
                        produtoExistente.getNomeProduto(),
                        produto.getQuantidade(),
                        produto.getStatus(),
                        usuario
                );
            }

        } else {
            if ("saida".equalsIgnoreCase(produto.getStatus())) {
                sucesso = false;
            } else {
                sucesso = dao.salvar(produto);

                if (sucesso) {
                    CadastroProdutoModel produtoSalvo = dao.buscarPorCodigoBarras(produto.getCodigoBarras());

                    if (produtoSalvo != null) {
                        historicoDAO.registrar(
                                produtoSalvo.getId(),
                                produtoSalvo.getNomeProduto(),
                                produto.getQuantidade(),
                                produto.getStatus(),
                                usuario
                        );
                    }
                }
            }
        }

        if (sucesso) {
            response.sendRedirect(request.getContextPath() + "/pages/dashboard.html");
        } else {
            response.sendRedirect(request.getContextPath() + "/pages/cadastroProdutos.html");
        }
    }
}

