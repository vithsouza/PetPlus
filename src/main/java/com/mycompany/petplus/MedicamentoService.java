/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.petplus;

import Conexao.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoService implements MedicamentoDAO {

    @Override
    public void inserir(Medicamento medicamento) throws Exception {
        Connection conexao = null;
        PreparedStatement pstmt = null;
        try {
            conexao = Conexao.conectar(); 
            if (conexao != null) {
                String sql = "INSERT INTO medicamentos (nome, quantidade, categoria) VALUES (?, ?, ?)";
                pstmt = conexao.prepareStatement(sql);
                pstmt.setString(1, medicamento.getNome());
                pstmt.setInt(2, medicamento.getQuantidade());
                pstmt.setString(3, medicamento.getCategoria());
                
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao inserir medicamento no banco de dados: " + e.getMessage(), e);
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage());
            }
            Conexao.fecharConexao(conexao);
        }
    }

    @Override
    public List<Medicamento> buscarTodos() throws Exception {
        List<Medicamento> medicamentos = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conexao = Conexao.conectar(); 
            if (conexao != null) {
                String sql = "SELECT nome, quantidade, categoria FROM medicamentos ORDER BY nome";
                pstmt = conexao.prepareStatement(sql);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    String nome = rs.getString("nome");
                    int quantidade = rs.getInt("quantidade");
                    String categoria = rs.getString("categoria");
                    medicamentos.add(new Medicamento(nome, quantidade, categoria));
                }
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar medicamentos no banco de dados: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
            Conexao.fecharConexao(conexao); 
        }
        return medicamentos;
    }
}