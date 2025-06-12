/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.petplus;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaDAO {

    Consulta inserir(Consulta consulta) throws Exception; 
    
    Consulta buscarPorId(long id) throws Exception; 
    
    List<Consulta> buscarTodos() throws Exception;
    
    List<Consulta> buscarPorPaciente(Paciente paciente) throws Exception;

    List<Consulta> buscarPorProprietarioCPF(long cpfProprietario) throws Exception;
    
    List<Consulta> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) throws Exception;

    void atualizar(Consulta consulta) throws Exception;
    
    void deletar(long id) throws Exception;
}
