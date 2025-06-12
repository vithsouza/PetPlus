/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.petplus;

/**
 *
 * @author yasmi
 */
public abstract class Pessoa {
    private long id;
    private long cpf;
    private String nome;
   
    public Pessoa(long cpf, String nome) {
        this.cpf = cpf;
        this.nome = nome;
    }
    
    public Pessoa(long id, long cpf, String nome) {
        this.id = id;
        this.cpf = cpf;
        this.nome = nome;
    }

    public long getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setCpf(long cpf) {
        this.cpf = cpf;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
     public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
