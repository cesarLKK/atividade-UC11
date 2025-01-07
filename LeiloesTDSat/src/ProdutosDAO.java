/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Adm
 */

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.util.ArrayList;


public class ProdutosDAO {
    
    Connection conn;
    PreparedStatement prep;
    ResultSet resultset;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();
    
    public void cadastrarProduto(ProdutosDTO produto) {
        conectaDAO conectaDAO = new conectaDAO();
        Connection conn = conectaDAO.connectDB();

        String sql = "INSERT INTO produtos (nome, valor, status) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setInt(2, produto.getValor());
            stmt.setString(3, produto.getStatus());

            stmt.executeUpdate();
            System.out.println("Produto cadastrado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar produto: " + e.getMessage());
        }
    }
    
    public ArrayList<ProdutosDTO> listarProdutos(){
    
        // Definindo a consulta SQL
        String sql = "SELECT * FROM produtos";
    
        try (Connection conn = new conectaDAO().connectDB();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
        
            // Preenchendo a lista com os produtos retornados
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String nome = rs.getString("nome");
                Integer valor = rs.getInt("valor");
                String status = rs.getString("status");

                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(id);
                produto.setNome(nome);
                produto.setValor(valor);
                produto.setStatus(status);

                listagem.add(produto);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }
        
        return listagem;
    }
    
    public boolean venderProduto(int produtoId) {
        
        conectaDAO conectaDAO = new conectaDAO();
        
        try (Connection conn = conectaDAO.connectDB()) {
            String sql = "UPDATE produtos SET status = 'Vendido' WHERE id = ?";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, produtoId);
                return stmt.executeUpdate() > 0;
            }
               
        } catch (SQLException e) {
            
            e.printStackTrace();
            return false;
        
        }
        
    }
    
    public ArrayList<ProdutosDTO> listarProdutosVendidos() {
        
        ArrayList<ProdutosDTO> produtosVendidos = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE status = 'vendido'";
        
        try (Connection conn = new conectaDAO().connectDB(); // Obtendo a conexão diretamente
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                
                ProdutosDTO produto = new ProdutosDTO(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getInt("valor"),
                    rs.getString("status")
                );
               
                produtosVendidos.add(produto);
            
            }
            
        } catch (SQLException e) {
            
            e.printStackTrace();
        
        }
        
        return produtosVendidos;

    }
    
    public boolean atualizarStatus(int produtoId) {
    String sql = "UPDATE produtos SET status = 'Vendido' WHERE id = ?";
    
    try (Connection conn = new conectaDAO().connectDB(); // Obtendo a conexão diretamente
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, produtoId);
        
        int rowsUpdated = stmt.executeUpdate();
        
        return rowsUpdated > 0; // Se o número de linhas afetadas for maior que 0, a atualização foi bem-sucedida
        
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    
        
}

