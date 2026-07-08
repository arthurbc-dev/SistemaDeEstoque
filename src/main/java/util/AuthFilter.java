package util;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class    AuthFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.substring(contextPath.length());

        if (path.equals("/") || path.equals("/index.html") || path.equals("/login")
                || path.equals("/pages/cadastro.html") || path.equals("/cadastro")
                || path.startsWith("/css/") || path.startsWith("/Js/")) {
            chain.doFilter(request, response);
            return;
        }

        if (session == null || session.getAttribute("usuario") == null) {
            res.sendRedirect(req.getContextPath() + "/index.html");
            return;
        }

        String funcao = session.getAttribute("funcao") != null
                ? session.getAttribute("funcao").toString()
                : "";

        boolean isAdmin = "ADMIN".equalsIgnoreCase(funcao);
        boolean isVisualizador = "VISUALIZADOR".equalsIgnoreCase(funcao);

        boolean acessoVisualizador = path.equalsIgnoreCase("/pages/dashboard.html")
                || path.equalsIgnoreCase("/api/estoque")
                || path.equalsIgnoreCase("/api/resumo")
                || path.equalsIgnoreCase("/api/historico")
                || path.equalsIgnoreCase("/logout")
                || path.startsWith("/css/")
                || path.startsWith("/Js/");

        if (isVisualizador && !acessoVisualizador) {
            res.sendRedirect(req.getContextPath() + "/pages/dashboard.html");
            return;
        }

        boolean rotaAdmin = path.equalsIgnoreCase("/pages/cadastroProdutos.html")
                || path.equalsIgnoreCase("/cadastroProdutos")
                || path.equalsIgnoreCase("/pages/GerenciarProdutos.html")
                || path.equalsIgnoreCase("/api/gerenciar")
                || path.equalsIgnoreCase("/pages/cadastro.html")
                || path.equalsIgnoreCase("/cadastro");

        if (rotaAdmin && !isAdmin) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(request, response);
    }
}
