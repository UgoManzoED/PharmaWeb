package it.unisa.pharmaweb.control.admin;

import it.unisa.pharmaweb.model.bean.CategoriaBean;
import it.unisa.pharmaweb.model.bean.ProductBean;
import it.unisa.pharmaweb.model.dao.CategoriaDAO;
import it.unisa.pharmaweb.model.dao.ProductDAO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/admin/prodotti")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB se supera scrive su disco temporaneo
    maxFileSize = 1024 * 1024 * 10,      // 10MB dimensione massima del file
    maxRequestSize = 1024 * 1024 * 50    // 50MB dimensione massima della richiesta totale
)
public class AdminProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Cartella dove salvare le immagini
    private static final String UPLOAD_DIR = "img";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDAO productDAO = new ProductDAO();
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        
        String action = request.getParameter("action");

        // Caso MODIFICA Carichiamo il prodotto singolo per popolare il form
        if ("edit".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            ProductBean p = productDAO.getProductById(id);
            request.setAttribute("productToEdit", p);
        }

        // Carichiamo sempre categorie e lista prodotti
        request.setAttribute("products", productDAO.getProductsPaginated(0, 100));
        request.setAttribute("categories", categoriaDAO.doRetrieveAll());

        request.getRequestDispatcher("/WEB-INF/views/admin/prodotti.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        ProductDAO productDAO = new ProductDAO();

        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            productDAO.deleteProduct(id);
            response.sendRedirect(request.getContextPath() + "/admin/prodotti");
            return;
        } 
        
        if ("save".equals(action) || "update".equals(action)) {
            try {
                String nome = request.getParameter("nome");
                String descrizione = request.getParameter("descrizione");
                double prezzo = Double.parseDouble(request.getParameter("prezzo"));
                int sconto = Integer.parseInt(request.getParameter("sconto"));
                int quantita = Integer.parseInt(request.getParameter("quantita"));
                int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
                
                // --- GESTIONE UPLOAD IMMAGINE ---
                Part filePart = request.getPart("immagine"); // Recupera il file dal form
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); 
                
                String imagePath = "";
                
                if (fileName != null && !fileName.isEmpty()) {
                    // Costruisce il percorso assoluto sul server
                    String applicationPath = request.getServletContext().getRealPath("");
                    String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
                    
                    // Crea la cartella se non esiste
                    File uploadDir = new File(uploadFilePath);
                    if (!uploadDir.exists()) uploadDir.mkdir();
                    
                    // Salva il file
                    String filePath = uploadFilePath + File.separator + fileName;
                    filePart.write(filePath);
                    
                    // Percorso relativo da salvare nel DB
                    imagePath = UPLOAD_DIR + "/" + fileName;
                } else {
                    // Se stiamo aggiornando e non carichiamo una nuova foto, manteniamo quella vecchia
                    imagePath = request.getParameter("oldImmagine");
                }

                // --- POPOLAMENTO BEAN ---
                ProductBean product = new ProductBean();
                product.setNomeProdotto(nome);
                product.setDescrizione(descrizione);
                product.setPrezzoDiListino(prezzo);
                product.setScontoPercentuale(sconto);
                product.setQuantitaDisponibile(quantita);
                product.setIdCategoria(idCategoria);
                product.setUrlImmagine(imagePath);

                if ("save".equals(action)) {
                    productDAO.saveProduct(product);
                } else {
                    int id = Integer.parseInt(request.getParameter("id"));
                    product.setIdProdotto(id);
                    productDAO.updateProduct(product);
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Gestione errore
            }
            response.sendRedirect(request.getContextPath() + "/admin/prodotti");
        }
    }
}