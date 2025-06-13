/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.petplus;

import java.util.List;

public interface ConsultaDAO {

    Consulta inserir(Consulta consulta) throws Exception; 
    
    List<Consulta> buscarTodos() throws Exception;
    
    void atualizar(Consulta consulta) throws Exception;
    
    void deletar(String nomePaciente) throws Exception;
    
    List<Consulta> buscarPorCPFProprietario(String cpf) throws Exception;
}
