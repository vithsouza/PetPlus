/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.petplus;

import java.util.List;

public interface PacienteDAO {

    Paciente inserir(Paciente paciente) throws Exception; 
    
    Paciente buscarPorId(long id) throws Exception; 
    
    List<Paciente> buscarTodos() throws Exception;
    
    List<Paciente> buscarPorProprietario(Proprietario proprietario) throws Exception;
    
    void atualizar(Paciente paciente) throws Exception;
    
    void deletar(long id) throws Exception;
    
}
