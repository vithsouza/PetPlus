/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.petplus;

/**
 *
 * @author yasmi
 */
public final class Proprietario extends Pessoa {
    private long telefone;
    
    public Proprietario(long telefone, long cpf, String nome){
        super(cpf, nome);
        this.telefone = telefone;
    }

    public Proprietario(long id, long telefone, long cpf, String nome) {
        super(id, cpf, nome);
        this.telefone = telefone;
    }
    
    public long getTelefone() {
        return telefone;
    }

    public void setTelefone(long telefone) {
        this.telefone = telefone;
    }
    
}
