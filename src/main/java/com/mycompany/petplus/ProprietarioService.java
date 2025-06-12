/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.petplus;

import Conexao.Conexao; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 
import java.util.ArrayList;
import java.util.List;

public class ProprietarioService implements ProprietarioDAO {

    @Override
    public Proprietario inserir(Proprietario proprietario) throws Exception {
        Connection conexao = null;
        PreparedStatement pstmtPessoa = null;
        PreparedStatement pstmtProprietario = null;
        ResultSet rs = null;

        try {
            conexao = Conexao.conectar();
            if (conexao == null) {
                throw new Exception("Falha ao conectar ao banco de dados.");
            }
            conexao.setAutoCommit(false); 

  
            String sqlPessoa = "INSERT INTO pessoas (cpf, nome) VALUES (?, ?) RETURNING id"; 
            pstmtPessoa = conexao.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS);
            pstmtPessoa.setLong(1, proprietario.getCpf());
            pstmtPessoa.setString(2, proprietario.getNome());
            pstmtPessoa.executeUpdate();

            rs = pstmtPessoa.getGeneratedKeys();
            if (rs.next()) {
                long pessoaId = rs.getLong(1);
                proprietario.setId(pessoaId); 

                String sqlProprietario = "INSERT INTO proprietarios (id, telefone) VALUES (?, ?)";
                pstmtProprietario = conexao.prepareStatement(sqlProprietario);
                pstmtProprietario.setLong(1, proprietario.getId()); 
                pstmtProprietario.setLong(2, proprietario.getTelefone());
                pstmtProprietario.executeUpdate();

                conexao.commit();
                return proprietario;
            } else {
                conexao.rollback(); 
                throw new Exception("Não foi possível obter o ID gerado para a pessoa.");
            }

        } catch (SQLException e) {
            if (conexao != null) {
                conexao.rollback(); 
            }
            if (e.getSQLState().equals("23505")) { // Código de erro para violação de UNIQUE (ex: CPF duplicado)
                throw new Exception("Erro: CPF já cadastrado.", e);
            }
            throw new Exception("Erro ao inserir proprietário no banco de dados: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmtPessoa != null) pstmtPessoa.close();
                if (pstmtProprietario != null) pstmtProprietario.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos do PreparedStatement: " + e.getMessage());
            }
            try {
                if (conexao != null) conexao.setAutoCommit(true); 
            } catch (SQLException e) {
                System.err.println("Erro ao restaurar autocommit: " + e.getMessage());
            }
            Conexao.fecharConexao(conexao);
        }
    }

   @Override
    public Proprietario buscarPorId(long id) throws Exception {
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Proprietario proprietario = null;
        try {
            conexao = Conexao.conectar();
            if (conexao == null) {
                throw new Exception("Falha ao conectar ao banco de dados.");
            }
            String sql = "SELECT p.id, p.cpf, p.nome, pr.telefone " +
                         "FROM pessoas p JOIN proprietarios pr ON p.id = pr.id " +
                         "WHERE p.id = ?";
            pstmt = conexao.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                long pessoaId = rs.getLong("id");
                long cpf = rs.getLong("cpf");
                String nome = rs.getString("nome");
                long telefone = rs.getLong("telefone");
                proprietario = new Proprietario(pessoaId, telefone, cpf, nome);
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar proprietário por ID: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
            Conexao.fecharConexao(conexao);
        }
        return proprietario;
    }

   @Override
    public List<Proprietario> buscarTodos() throws Exception {
        List<Proprietario> proprietarios = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conexao = Conexao.conectar();
            if (conexao == null) {
                throw new Exception("Falha ao conectar ao banco de dados.");
            }
            String sql = "SELECT p.id, p.cpf, p.nome, pr.telefone " +
                         "FROM pessoas p JOIN proprietarios pr ON p.id = pr.id " +
                         "ORDER BY p.nome";
            pstmt = conexao.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                long pessoaId = rs.getLong("id");
                long cpf = rs.getLong("cpf");
                String nome = rs.getString("nome");
                long telefone = rs.getLong("telefone");
                proprietarios.add(new Proprietario(pessoaId, telefone, cpf, nome));
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar todos os proprietários: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
            Conexao.fecharConexao(conexao);
        }
        return proprietarios;
    }

   @Override
    public void atualizar(Proprietario proprietario) throws Exception {
        Connection conexao = null;
        PreparedStatement pstmtPessoa = null;
        PreparedStatement pstmtProprietario = null;
        try {
            conexao = Conexao.conectar();
            if (conexao == null) {
                throw new Exception("Falha ao conectar ao banco de dados.");
            }
            conexao.setAutoCommit(false); 

          
            String sqlPessoa = "UPDATE pessoas SET cpf = ?, nome = ? WHERE id = ?";
            pstmtPessoa = conexao.prepareStatement(sqlPessoa);
            pstmtPessoa.setLong(1, proprietario.getCpf());
            pstmtPessoa.setString(2, proprietario.getNome());
            pstmtPessoa.setLong(3, proprietario.getId());
            pstmtPessoa.executeUpdate();

      
            String sqlProprietario = "UPDATE proprietarios SET telefone = ? WHERE id = ?";
            pstmtProprietario = conexao.prepareStatement(sqlProprietario);
            pstmtProprietario.setLong(1, proprietario.getTelefone());
            pstmtProprietario.setLong(2, proprietario.getId());
            pstmtProprietario.executeUpdate();

            conexao.commit(); 

        } catch (SQLException e) {
            if (conexao != null) {
                conexao.rollback(); 
            }
            if (e.getSQLState().equals("23505")) { // Código de erro para violação de UNIQUE (ex: CPF duplicado)
                throw new Exception("Erro: CPF já cadastrado.", e);
            }
            throw new Exception("Erro ao atualizar proprietário no banco de dados: " + e.getMessage(), e);
        } finally {
            try {
                if (pstmtPessoa != null) pstmtPessoa.close();
                if (pstmtProprietario != null) pstmtProprietario.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage());
            }
            try {
                if (conexao != null) conexao.setAutoCommit(true); 
            } catch (SQLException e) {
                System.err.println("Erro ao restaurar autocommit: " + e.getMessage());
            }
            Conexao.fecharConexao(conexao);
        }
    }

    @Override
    public void deletar(long id) throws Exception {
        Connection conexao = null;
        PreparedStatement pstmtPessoa = null;
        try {
            conexao = Conexao.conectar();
            if (conexao == null) {
                throw new Exception("Falha ao conectar ao banco de dados.");
            }
            conexao.setAutoCommit(false); 

            String sqlPessoa = "DELETE FROM pessoas WHERE id = ?";
            pstmtPessoa = conexao.prepareStatement(sqlPessoa);
            pstmtPessoa.setLong(1, id);
            int rowsAffected = pstmtPessoa.executeUpdate();

            if (rowsAffected == 0) {
                conexao.rollback();
                throw new Exception("Proprietário com ID " + id + " não encontrado para exclusão.");
            }

            conexao.commit(); 

        } catch (SQLException e) {
            if (conexao != null) {
                conexao.rollback(); 
            }
            throw new Exception("Erro ao deletar proprietário do banco de dados: " + e.getMessage(), e);
        } finally {
            try {
                if (pstmtPessoa != null) pstmtPessoa.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage());
            }
            try {
                if (conexao != null) conexao.setAutoCommit(true); 
            } catch (SQLException e) {
                System.err.println("Erro ao restaurar autocommit: " + e.getMessage());
            }
            Conexao.fecharConexao(conexao);
        }
    }
    
    @Override
    public Proprietario buscarPorCPF(long cpf) throws Exception {
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Proprietario proprietario = null;
        try {
            conexao = Conexao.conectar();
            if (conexao == null) {
                throw new Exception("Falha ao conectar ao banco de dados.");
            }
            String sql = "SELECT p.id, p.cpf, p.nome, pr.telefone " +
                         "FROM pessoas p JOIN proprietarios pr ON p.id = pr.id " +
                         "WHERE p.cpf = ?"; // Busca pelo CPF na tabela pessoas
            pstmt = conexao.prepareStatement(sql);
            pstmt.setLong(1, cpf);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                long pessoaId = rs.getLong("id");
                long cpfEncontrado = rs.getLong("cpf");
                String nome = rs.getString("nome");
                long telefone = rs.getLong("telefone");
                proprietario = new Proprietario(pessoaId, telefone, cpfEncontrado, nome);
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar proprietário por CPF: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
            Conexao.fecharConexao(conexao);
        }
        return proprietario;
    }
}