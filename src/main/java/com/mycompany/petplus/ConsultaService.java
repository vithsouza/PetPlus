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

            String sql = "INSERT INTO consultas (status, diagnostico, tratamento, data_hora, id_paciente) VALUES (?, ?, ?, ?, ?) RETURNING id";
            pstmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, consulta.getStatus());
            pstmt.setString(2, consulta.getDiagnostico());
            pstmt.setString(3, consulta.getTratamento());
            pstmt.setTimestamp(4, Timestamp.valueOf(consulta.getDataHora())); 
            pstmt.setLong(5, consulta.getPaciente().getId()); 
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
    public Consulta buscarPorId(long id) throws Exception {
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Consulta consulta = null;
        try {
            conexao = Conexao.conectar();
            if (conexao == null) {
                throw new Exception("Falha ao conectar ao banco de dados.");
            }

            String sql = "SELECT id, status, diagnostico, tratamento, data_hora, id_paciente FROM consultas WHERE id = ?";
            pstmt = conexao.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                long consultaId = rs.getLong("id");
                String status = rs.getString("status");
                String diagnostico = rs.getString("diagnostico");
                String tratamento = rs.getString("tratamento");
                LocalDateTime dataHora = rs.getTimestamp("data_hora").toLocalDateTime(); 
                long idPaciente = rs.getLong("id_paciente");

  
                Paciente paciente = pacienteDAO.buscarPorId(idPaciente);
                if (paciente == null) {
                    throw new Exception("Paciente associado à consulta ID " + consultaId + " não encontrado.");
                }

                consulta = new Consulta(consultaId, status, diagnostico, tratamento, dataHora, paciente);
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar consulta por ID: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
            Conexao.fecharConexao(conexao);
        }
        return consulta;
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

            String sql = "SELECT id, status, diagnostico, tratamento, data_hora, id_paciente FROM consultas ORDER BY data_hora DESC";
            pstmt = conexao.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                long consultaId = rs.getLong("id");
                String status = rs.getString("status");
                String diagnostico = rs.getString("diagnostico");
                String tratamento = rs.getString("tratamento");
                LocalDateTime dataHora = rs.getTimestamp("data_hora").toLocalDateTime();
                long idPaciente = rs.getLong("id_paciente");


                Paciente paciente = pacienteDAO.buscarPorId(idPaciente);
                if (paciente == null) {
                    System.err.println("Aviso: Paciente com ID " + idPaciente + " associado à consulta ID " + consultaId + " não encontrado.");
                    continue; 
                }

                consultas.add(new Consulta(consultaId, status, diagnostico, tratamento, dataHora, paciente));
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
    
    @Override
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

            String sql = "SELECT id, status, diagnostico, tratamento, data_hora, id_paciente FROM consultas WHERE id_paciente = ? ORDER BY data_hora DESC";
            pstmt = conexao.prepareStatement(sql);
            pstmt.setLong(1, paciente.getId());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                long consultaId = rs.getLong("id");
                String status = rs.getString("status");
                String diagnostico = rs.getString("diagnostico");
                String tratamento = rs.getString("tratamento");
                LocalDateTime dataHora = rs.getTimestamp("data_hora").toLocalDateTime();

                consultas.add(new Consulta(consultaId, status, diagnostico, tratamento, dataHora, paciente));
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
    public List<Consulta> buscarPorProprietarioCPF(long cpfProprietario) throws Exception {
        List<Consulta> consultas = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conexao = Conexao.conectar();
            if (conexao == null) {
                throw new Exception("Falha ao conectar ao banco de dados.");
            }

            Proprietario proprietario = proprietarioDAO.buscarPorCPF(cpfProprietario); 
            if (proprietario == null) {
                throw new Exception("Proprietário com CPF " + cpfProprietario + " não encontrado.");
            }

            List<Paciente> pacientesDoProprietario = pacienteDAO.buscarPorProprietario(proprietario);
            if (pacientesDoProprietario.isEmpty()) {
                return consultas; 
            }


            StringBuilder pacienteIds = new StringBuilder();
            for (int i = 0; i < pacientesDoProprietario.size(); i++) {
                pacienteIds.append(pacientesDoProprietario.get(i).getId());
                if (i < pacientesDoProprietario.size() - 1) {
                    pacienteIds.append(",");
                }
            }


            String sql = "SELECT id, status, diagnostico, tratamento, data_hora, id_paciente FROM consultas WHERE id_paciente IN (" + pacienteIds.toString() + ") ORDER BY data_hora DESC";
            pstmt = conexao.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                long consultaId = rs.getLong("id");
                String status = rs.getString("status");
                String diagnostico = rs.getString("diagnostico");
                String tratamento = rs.getString("tratamento");
                LocalDateTime dataHora = rs.getTimestamp("data_hora").toLocalDateTime();
                long idPaciente = rs.getLong("id_paciente");

              
                Paciente pacienteAssociado = pacientesDoProprietario.stream()
                                                .filter(p -> p.getId() == idPaciente)
                                                .findFirst()
                                                .orElse(null);
                
                if (pacienteAssociado == null) {
                    System.err.println("Aviso: Paciente com ID " + idPaciente + " associado à consulta ID " + consultaId + " não encontrado na lista do proprietário.");
                    continue; 
                }

                consultas.add(new Consulta(consultaId, status, diagnostico, tratamento, dataHora, pacienteAssociado));
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar consultas por CPF do proprietário: " + e.getMessage(), e);
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
    public List<Consulta> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) throws Exception {
        List<Consulta> consultas = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conexao = Conexao.conectar();
            if (conexao == null) {
                throw new Exception("Falha ao conectar ao banco de dados.");
            }

            String sql = "SELECT id, status, diagnostico, tratamento, data_hora, id_paciente FROM consultas WHERE data_hora BETWEEN ? AND ? ORDER BY data_hora";
            pstmt = conexao.prepareStatement(sql);
            pstmt.setTimestamp(1, Timestamp.valueOf(inicio));
            pstmt.setTimestamp(2, Timestamp.valueOf(fim));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                long consultaId = rs.getLong("id");
                String status = rs.getString("status");
                String diagnostico = rs.getString("diagnostico");
                String tratamento = rs.getString("tratamento");
                LocalDateTime dataHora = rs.getTimestamp("data_hora").toLocalDateTime();
                long idPaciente = rs.getLong("id_paciente");

                Paciente paciente = pacienteDAO.buscarPorId(idPaciente);
                if (paciente == null) {
                    System.err.println("Aviso: Paciente com ID " + idPaciente + " associado à consulta ID " + consultaId + " não encontrado.");
                    continue; 
                }

                consultas.add(new Consulta(consultaId, status, diagnostico, tratamento, dataHora, paciente));
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar consultas por período: " + e.getMessage(), e);
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

            String sql = "UPDATE consultas SET status = ?, diagnostico = ?, tratamento = ?, data_hora = ?, id_paciente = ? WHERE id = ?";
            pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, consulta.getStatus());
            pstmt.setString(2, consulta.getDiagnostico());
            pstmt.setString(3, consulta.getTratamento());
            pstmt.setTimestamp(4, Timestamp.valueOf(consulta.getDataHora()));
            pstmt.setLong(5, consulta.getPaciente().getId());
            pstmt.setLong(6, consulta.getId());
            
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
    public void deletar(long id) throws Exception {
        Connection conexao = null;
        PreparedStatement pstmt = null;
        try {
            conexao = Conexao.conectar();
            if (conexao == null) {
                throw new Exception("Falha ao conectar ao banco de dados.");
            }

            String sql = "DELETE FROM consultas WHERE id = ?";
            pstmt = conexao.prepareStatement(sql);
            pstmt.setLong(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new Exception("Consulta com ID " + id + " não encontrada para exclusão.");
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
}