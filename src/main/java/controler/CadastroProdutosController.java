/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controler;

import dao.CadastroProdutoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.CadastroProdutoModel;

/**
 *
 * @author 232.999257
 */
@WebServlet("/cadastroProdutos")
public class    CadastroProdutosController extends HttpServlet{

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

        if (dao.salvar(produto)) {
            response.sendRedirect(request.getContextPath() + "/pages/dashboard.html");
        } else {
            response.sendRedirect(request.getContextPath() + "/pages/cadastroProdutos.html");
        }
    }
}
