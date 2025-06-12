/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.petplus;

import java.util.List;

public interface ProprietarioDAO {
    Proprietario inserir(Proprietario proprietario) throws Exception; 
    
    Proprietario buscarPorId(long id) throws Exception; 
    
    Proprietario buscarPorCPF(long cpf) throws Exception;
    
    List<Proprietario> buscarTodos() throws Exception;
    
    void atualizar(Proprietario proprietario) throws Exception; 
    
    void deletar(long id) throws Exception; 
}