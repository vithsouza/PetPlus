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

public class PacienteService implements PacienteDAO {

 
    private final ProprietarioDAO proprietarioDAO;

    public PacienteService() {
        this.proprietarioDAO = new ProprietarioService();  
    }

    @Override
    public Paciente inserir(Paciente paciente) throws Exception {
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conexao = Conexao.conectar();
            if (conexao == null) {
                throw new Exception("Falha ao conectar ao banco de dados.");
            }
     
            if (paciente.getProprietario() == null || paciente.getProprietario().getId() == 0) {
                throw new Exception("Proprietário do paciente não possui um ID válido. Cadastre o proprietário primeiro.");
            }

            String sql = "INSERT INTO pacientes (nome, idade, raca, peso, id_proprietario) VALUES (?, ?, ?, ?, ?) RETURNING id";
            pstmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, paciente.getNome());
            pstmt.setInt(2, paciente.getIdade());
            pstmt.setString(3, paciente.getRaca());
            pstmt.setInt(4, paciente.getPeso());
            pstmt.setLong(5, paciente.getProprietario().getId()); 
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                long pacienteId = rs.getLong(1);
                paciente.setId(pacienteId); 
            } else {
                throw new Exception("Não foi possível obter o ID gerado para o paciente.");
            }
            return paciente;
        } catch (SQLException e) {
            throw new Exception("Erro ao inserir paciente no banco de dados: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
            Conexao.fecharConexao(conexao);
        }
    }

    @Override
    public Paciente buscarPorId(long id) throws Exception {
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Paciente paciente = null;
        try {
            conexao = Conexao.conectar();
            if (conexao == null) {
                throw new Exception("Falha ao conectar ao banco de dados.");
            }

            String sql = "SELECT id, nome, idade, raca, peso, id_proprietario FROM pacientes WHERE id = ?";
            pstmt = conexao.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                long pacienteId = rs.getLong("id");
                String nome = rs.getString("nome");
                int idade = rs.getInt("idade");
                String raca = rs.getString("raca");
                int peso = rs.getInt("peso");
                long idProprietario = rs.getLong("id_proprietario");

             
                Proprietario proprietario = proprietarioDAO.buscarPorId(idProprietario);
                if (proprietario == null) {
                    throw new Exception("Proprietário associado ao paciente ID " + pacienteId + " não encontrado.");
                }

                paciente = new Paciente(pacienteId, nome, idade, raca, peso, proprietario);
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar paciente por ID: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
            Conexao.fecharConexao(conexao);
        }
        return paciente;
    }

    @Override
    public List<Paciente> buscarTodos() throws Exception {
        List<Paciente> pacientes = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conexao = Conexao.conectar();
            if (conexao == null) {
                throw new Exception("Falha ao conectar ao banco de dados.");
            }

            String sql = "SELECT id, nome, idade, raca, peso, id_proprietario FROM pacientes ORDER BY nome";
            pstmt = conexao.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                long pacienteId = rs.getLong("id");
                String nome = rs.getString("nome");
                int idade = rs.getInt("idade");
                String raca = rs.getString("raca");
                int peso = rs.getInt("peso");
                long idProprietario = rs.getLong("id_proprietario");

               
                Proprietario proprietario = proprietarioDAO.buscarPorId(idProprietario);
                if (proprietario == null) {
                   
                    System.err.println("Aviso: Proprietário com ID " + idProprietario + " associado ao paciente ID " + pacienteId + " não encontrado.");
                    
                    continue; 
                }

                pacientes.add(new Paciente(pacienteId, nome, idade, raca, peso, proprietario));
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar todos os pacientes: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
            Conexao.fecharConexao(conexao);
        }
        return pacientes;
    }
    
    @Override
    public List<Paciente> buscarPorProprietario(Proprietario proprietario) throws Exception {
        List<Paciente> pacientes = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conexao = Conexao.conectar();
            if (conexao == null) {
                throw new Exception("Falha ao conectar ao banco de dados.");
            }
            if (proprietario == null || proprietario.getId() == 0) {
                throw new Exception("ID do proprietário inválido para busca de pacientes.");
            }

            String sql = "SELECT id, nome, idade, raca, peso, id_proprietario FROM pacientes WHERE id_proprietario = ? ORDER BY nome";
            pstmt = conexao.prepareStatement(sql);
            pstmt.setLong(1, proprietario.getId());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                long pacienteId = rs.getLong("id");
                String nome = rs.getString("nome");
                int idade = rs.getInt("idade");
                String raca = rs.getString("raca");
                int peso = rs.getInt("peso");
                
                pacientes.add(new Paciente(pacienteId, nome, idade, raca, peso, proprietario));
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar pacientes por proprietário: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
            Conexao.fecharConexao(conexao);
        }
        return pacientes;
    }


    @Override
    public void atualizar(Paciente paciente) throws Exception {
        Connection conexao = null;
        PreparedStatement pstmt = null;
        try {
            conexao = Conexao.conectar();
            if (conexao == null) {
                throw new Exception("Falha ao conectar ao banco de dados.");
            }

            
            if (paciente.getId() == 0) {
                throw new Exception("ID do paciente inválido para atualização.");
            }
            if (paciente.getProprietario() == null || paciente.getProprietario().getId() == 0) {
                throw new Exception("Proprietário do paciente não possui um ID válido para atualização.");
            }

            String sql = "UPDATE pacientes SET nome = ?, idade = ?, raca = ?, peso = ?, id_proprietario = ? WHERE id = ?";
            pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, paciente.getNome());
            pstmt.setInt(2, paciente.getIdade());
            pstmt.setString(3, paciente.getRaca());
            pstmt.setInt(4, paciente.getPeso());
            pstmt.setLong(5, paciente.getProprietario().getId());
            pstmt.setLong(6, paciente.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new Exception("Paciente com ID " + paciente.getId() + " não encontrado para atualização.");
            }

        } catch (SQLException e) {
            throw new Exception("Erro ao atualizar paciente no banco de dados: " + e.getMessage(), e);
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
public void deletarPorNomePaciente(String nomePaciente) throws Exception {
    Connection conexao = null;
    PreparedStatement pstmt = null;
    try {
        conexao = Conexao.conectar();
        if (conexao == null) {
            throw new Exception("Falha ao conectar ao banco de dados.");
        }

        String sql = "DELETE FROM pacientes WHERE nome = ?";
        pstmt = conexao.prepareStatement(sql);
        pstmt.setString(1, nomePaciente);

        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected == 0) {
            throw new Exception("Nenhum paciente encontrado com o nome: " + nomePaciente);
        }

    } catch (SQLException e) {
        throw new Exception("Erro ao deletar paciente: " + e.getMessage(), e);
    } finally {
        try {
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage());
        }
        Conexao.fecharConexao(conexao);
    }
}

}