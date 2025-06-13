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
import java.sql.Timestamp; 
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsultaService implements ConsultaDAO {


    private final PacienteDAO pacienteDAO;
    private final ProprietarioDAO proprietarioDAO; 

    public ConsultaService() {
        this.pacienteDAO = new PacienteService(); 
        this.proprietarioDAO = new ProprietarioService(); 
    }

    @Override
    public Consulta inserir(Consulta consulta) throws Exception {
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conexao = Conexao.conectar();
            if (conexao == null) {
                throw new Exception("Falha ao conectar ao banco de dados.");
            }


            if (consulta.getPaciente() == null || consulta.getPaciente().getId() == 0) {
                throw new Exception("Paciente da consulta não possui um ID válido. Cadastre o paciente primeiro.");
            }

            String sql = "INSERT INTO consultas (data_hora, paciente_id) VALUES (?, ?) RETURNING id";
            pstmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setTimestamp(1, Timestamp.valueOf(consulta.getDataHora())); 
            pstmt.setLong(2, consulta.getPaciente().getId()); 
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                long consultaId = rs.getLong(1);
                consulta.setId(consultaId); 
            } else {
                throw new Exception("Não foi possível obter o ID gerado para a consulta.");
            }
            return consulta;
        } catch (SQLException e) {
            throw new Exception("Erro ao inserir consulta no banco de dados: " + e.getMessage(), e);
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
    public List<Consulta> buscarTodos() throws Exception {
        List<Consulta> consultas = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conexao = Conexao.conectar();
            if (conexao == null) {
                throw new Exception("Falha ao conectar ao banco de dados.");
            }

            String sql = "SELECT id, data_hora, paciente_id FROM consultas ORDER BY data_hora DESC";
            pstmt = conexao.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                long consultaId = rs.getLong("id");
                LocalDateTime dataHora = rs.getTimestamp("data_hora").toLocalDateTime();
                long idPaciente = rs.getLong("paciente_id");


                Paciente paciente = pacienteDAO.buscarPorId(idPaciente);
                if (paciente == null) {
                    System.err.println("Aviso: Paciente com ID " + idPaciente + " associado à consulta ID " + consultaId + " não encontrado.");
                    continue; 
                }

                consultas.add(new Consulta(consultaId, dataHora, paciente));
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar todas as consultas: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
            Conexao.fecharConexao(conexao);
        }
        return consultas;
    }
    
   
    public List<Consulta> buscarPorPaciente(Paciente paciente) throws Exception {
        List<Consulta> consultas = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conexao = Conexao.conectar();
            if (conexao == null) {
                throw new Exception("Falha ao conectar ao banco de dados.");
            }
            if (paciente == null || paciente.getId() == 0) {
                throw new Exception("ID do paciente inválido para busca de consultas.");
            }

            String sql = "SELECT id, data_hora, paciente_id FROM consultas WHERE paciente_id = ? ORDER BY data_hora DESC";
            pstmt = conexao.prepareStatement(sql);
            pstmt.setLong(1, paciente.getId());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                long consultaId = rs.getLong("id");
                LocalDateTime dataHora = rs.getTimestamp("data_hora").toLocalDateTime();

                consultas.add(new Consulta(consultaId,dataHora, paciente));
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar consultas por paciente: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
            Conexao.fecharConexao(conexao);
        }
        return consultas;
    }

    

    @Override
    public void atualizar(Consulta consulta) throws Exception {
        Connection conexao = null;
        PreparedStatement pstmt = null;
        try {
            conexao = Conexao.conectar();
            if (conexao == null) {
                throw new Exception("Falha ao conectar ao banco de dados.");
            }

            if (consulta.getId() == 0) {
                throw new Exception("ID da consulta inválido para atualização.");
            }
            if (consulta.getPaciente() == null || consulta.getPaciente().getId() == 0) {
                throw new Exception("Paciente da consulta não possui um ID válido para atualização.");
            }

            String sql = "UPDATE consultas SET data_hora = ?, paciente_id = ? WHERE id = ?";
            pstmt = conexao.prepareStatement(sql);
            pstmt.setTimestamp(1, Timestamp.valueOf(consulta.getDataHora()));
            pstmt.setLong(2, consulta.getPaciente().getId());
            pstmt.setLong(3, consulta.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new Exception("Consulta com ID " + consulta.getId() + " não encontrada para atualização.");
            }

        } catch (SQLException e) {
            throw new Exception("Erro ao atualizar consulta no banco de dados: " + e.getMessage(), e);
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
    public void deletar(String nomePaciente) throws Exception {
    Connection conexao = null;
    PreparedStatement pstmt = null;
    try {
        conexao = Conexao.conectar();
        if (conexao == null) {
            throw new Exception("Falha ao conectar ao banco de dados.");
        }

        String sql = "DELETE FROM consultas " +
                     "USING pacientes " +
                     "WHERE consultas.paciente_id = pacientes.id " +
                     "AND pacientes.nome = ?";

        pstmt = conexao.prepareStatement(sql);
        pstmt.setString(1, nomePaciente);
        
        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected == 0) {
            throw new Exception("Consulta com nome do paciente '" + nomePaciente + "' não encontrada para exclusão.");
        }

    } catch (SQLException e) {
        throw new Exception("Erro ao deletar consulta do banco de dados: " + e.getMessage(), e);
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
public List<Consulta> buscarPorCPFProprietario(String cpf) throws Exception {
    List<Consulta> consultas = new ArrayList<>();
    String sql = "SELECT c.id, c.data_hora, p.id AS paciente_id, pes.nome " +
                 "FROM consultas c " +
                 "JOIN pacientes p ON c.paciente_id = p.id " +
                 "JOIN proprietarios pr ON p.id_proprietario = pr.id " +
                 "JOIN pessoas pes ON pr.id = pes.id " +
                 "WHERE pes.cpf = ? " +
                 "ORDER BY c.data_hora DESC";
    
    try (Connection conexao = Conexao.conectar();
         PreparedStatement pstmt = conexao.prepareStatement(sql)) {
        
        long cpfNum = Long.parseLong(cpf);
        pstmt.setLong(1, cpfNum);

        
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                long id = rs.getLong("id");
                Timestamp timestamp = rs.getTimestamp("data_hora");
                LocalDateTime dataHora = timestamp.toLocalDateTime();
                long pacienteId = rs.getLong("paciente_id");
                String nomePaciente = rs.getString("nome");
                
                Paciente paciente = pacienteDAO.buscarPorId(pacienteId);
                paciente.setNome(nomePaciente);  
                
                Consulta consulta = new Consulta(id, dataHora, paciente);
                consultas.add(consulta);
            }
        }
    } catch (SQLException e) {
        throw new Exception("Erro ao buscar consultas pelo CPF: " + e.getMessage(), e);
    }
    return consultas;
}


}