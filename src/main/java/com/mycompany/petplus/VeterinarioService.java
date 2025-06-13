package com.mycompany.petplus;

import Conexao.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VeterinarioService implements VeterinarioDAO {

    @Override
    public Veterinario inserir(Veterinario veterinario) throws Exception {
        Connection conexao = null;
        PreparedStatement pstmtPessoa = null;
        PreparedStatement pstmtVeterinario = null;
        ResultSet rs = null;

        try {
            conexao = Conexao.conectar();
            if (conexao == null) throw new Exception("Falha ao conectar ao banco de dados.");
            conexao.setAutoCommit(false);

            String sqlPessoa = "INSERT INTO pessoas (cpf, nome) VALUES (?, ?) RETURNING id";
            pstmtPessoa = conexao.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS);
            pstmtPessoa.setLong(1, veterinario.getCpf());
            pstmtPessoa.setString(2, veterinario.getNome());
            pstmtPessoa.executeUpdate();

            rs = pstmtPessoa.getGeneratedKeys();
            if (rs.next()) {
                long pessoaId = rs.getLong(1);
                veterinario.setId(pessoaId);

                String sqlVeterinario = "INSERT INTO veterinarios (id, crmv) VALUES (?, ?)";
                pstmtVeterinario = conexao.prepareStatement(sqlVeterinario);
                pstmtVeterinario.setLong(1, veterinario.getId());
                pstmtVeterinario.setInt(2, veterinario.getCRMV());
                pstmtVeterinario.executeUpdate();

                conexao.commit();
                return veterinario;
            } else {
                conexao.rollback();
                throw new Exception("Não foi possível obter o ID gerado para a pessoa.");
            }

        } catch (SQLException e) {
            if (conexao != null) conexao.rollback();
            if ("23505".equals(e.getSQLState())) {
                throw new Exception("Erro: CPF já cadastrado.", e);
            }
            throw new Exception("Erro ao inserir veterinário no banco de dados: " + e.getMessage(), e);
        } finally {
            if (rs != null) rs.close();
            if (pstmtPessoa != null) pstmtPessoa.close();
            if (pstmtVeterinario != null) pstmtVeterinario.close();
            if (conexao != null) {
                conexao.setAutoCommit(true);
                Conexao.fecharConexao(conexao);
            }
        }
    }

    @Override
    public Veterinario buscarPorId(long id) throws Exception {
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Veterinario veterinario = null;

        try {
            conexao = Conexao.conectar();
            if (conexao == null) throw new Exception("Falha ao conectar ao banco de dados.");
            String sql = "SELECT p.id, p.cpf, p.nome, v.crmv " +
                         "FROM pessoas p JOIN veterinarios v ON p.id = v.id " +
                         "WHERE p.id = ?";
            pstmt = conexao.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                long cpf = rs.getLong("cpf");
                String nome = rs.getString("nome");
                int crmv = rs.getInt("crmv");
                veterinario = new Veterinario(crmv, cpf, nome);
                veterinario.setId(id);
            }

        } catch (SQLException e) {
            throw new Exception("Erro ao buscar veterinário por ID: " + e.getMessage(), e);
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            Conexao.fecharConexao(conexao);
        }
        return veterinario;
    }

    @Override
    public List<Veterinario> buscarTodos() throws Exception {
        List<Veterinario> veterinarios = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = Conexao.conectar();
            if (conexao == null) throw new Exception("Falha ao conectar ao banco de dados.");
            String sql = "SELECT p.id, p.cpf, p.nome, v.crmv " +
                         "FROM pessoas p JOIN veterinarios v ON p.id = v.id " +
                         "ORDER BY p.nome";
            pstmt = conexao.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                long id = rs.getLong("id");
                long cpf = rs.getLong("cpf");
                String nome = rs.getString("nome");
                int crmv = rs.getInt("crmv");
                Veterinario veterinario = new Veterinario(crmv, cpf, nome);
                veterinario.setId(id);
                veterinarios.add(veterinario);
            }

        } catch (SQLException e) {
            throw new Exception("Erro ao buscar todos os veterinários: " + e.getMessage(), e);
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            Conexao.fecharConexao(conexao);
        }

        return veterinarios;
    }

    @Override
    public void atualizar(Veterinario veterinario) throws Exception {
        Connection conexao = null;
        PreparedStatement pstmtPessoa = null;
        PreparedStatement pstmtVeterinario = null;

        try {
            conexao = Conexao.conectar();
            if (conexao == null) throw new Exception("Falha ao conectar ao banco de dados.");
            conexao.setAutoCommit(false);

            String sqlPessoa = "UPDATE pessoas SET cpf = ?, nome = ? WHERE id = ?";
            pstmtPessoa = conexao.prepareStatement(sqlPessoa);
            pstmtPessoa.setLong(1, veterinario.getCpf());
            pstmtPessoa.setString(2, veterinario.getNome());
            pstmtPessoa.setLong(3, veterinario.getId());
            pstmtPessoa.executeUpdate();

            String sqlVeterinario = "UPDATE veterinarios SET crmv = ? WHERE id = ?";
            pstmtVeterinario = conexao.prepareStatement(sqlVeterinario);
            pstmtVeterinario.setInt(1, veterinario.getCRMV());
            pstmtVeterinario.setLong(2, veterinario.getId());
            pstmtVeterinario.executeUpdate();

            conexao.commit();

        } catch (SQLException e) {
            if (conexao != null) conexao.rollback();
            if ("23505".equals(e.getSQLState())) {
                throw new Exception("Erro: CPF já cadastrado.", e);
            }
            throw new Exception("Erro ao atualizar veterinário: " + e.getMessage(), e);
        } finally {
            if (pstmtPessoa != null) pstmtPessoa.close();
            if (pstmtVeterinario != null) pstmtVeterinario.close();
            if (conexao != null) {
                conexao.setAutoCommit(true);
                Conexao.fecharConexao(conexao);
            }
        }
    }

    @Override
    public void deletar(long id) throws Exception {
        Connection conexao = null;
        PreparedStatement pstmt = null;

        try {
            conexao = Conexao.conectar();
            if (conexao == null) throw new Exception("Falha ao conectar ao banco de dados.");
            conexao.setAutoCommit(false);

            String sql = "DELETE FROM pessoas WHERE id = ?";
            pstmt = conexao.prepareStatement(sql);
            pstmt.setLong(1, id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                conexao.rollback();
                throw new Exception("Veterinário com ID " + id + " não encontrado para exclusão.");
            }

            conexao.commit();

        } catch (SQLException e) {
            if (conexao != null) conexao.rollback();
            throw new Exception("Erro ao deletar veterinário: " + e.getMessage(), e);
        } finally {
            if (pstmt != null) pstmt.close();
            if (conexao != null) {
                conexao.setAutoCommit(true);
                Conexao.fecharConexao(conexao);
            }
        }
    }

    @Override
    public Veterinario buscarPorCPF(long cpf) throws Exception {
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Veterinario veterinario = null;

        try {
            conexao = Conexao.conectar();
            if (conexao == null) throw new Exception("Falha ao conectar ao banco de dados.");

            String sql = "SELECT p.id, p.cpf, p.nome, v.crmv " +
                         "FROM pessoas p JOIN veterinarios v ON p.id = v.id " +
                         "WHERE p.cpf = ?";
            pstmt = conexao.prepareStatement(sql);
            pstmt.setLong(1, cpf);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                long id = rs.getLong("id");
                String nome = rs.getString("nome");
                int crmv = rs.getInt("crmv");
                veterinario = new Veterinario(crmv, cpf, nome);
                veterinario.setId(id);
            }

        } catch (SQLException e) {
            throw new Exception("Erro ao buscar veterinário por CPF: " + e.getMessage(), e);
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            Conexao.fecharConexao(conexao);
        }

        return veterinario;
    }
}
