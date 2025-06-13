/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.petplus;

import java.util.List;

public interface VeterinarioDAO {
    Veterinario inserir(Veterinario veterinario) throws Exception; 
    
    Veterinario buscarPorId(long id) throws Exception; 
    
    Veterinario buscarPorCPF(long cpf) throws Exception;
    
    List<Veterinario> buscarTodos() throws Exception;
    
    void atualizar(Veterinario veterinario) throws Exception; 
    
    void deletar(long id) throws Exception; 
}