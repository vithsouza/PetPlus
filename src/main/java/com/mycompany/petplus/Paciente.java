/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.petplus;

public class Paciente {
    
    private long id; 
    private String nome;
    private int idade;
    private String raca;
    private int peso;
    private Proprietario proprietario; 

    public Paciente(String nome, int idade, String raca, int peso, Proprietario proprietario) {
        this.nome = nome;
        this.idade = idade;
        this.raca = raca;
        this.peso = peso;
        this.proprietario = proprietario;
    }

    public Paciente(long id, String nome, int idade, String raca, int peso, Proprietario proprietario) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.raca = raca;
        this.peso = peso;
        this.proprietario = proprietario;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public Proprietario getProprietario() {
        return proprietario;
    }

    public void setProprietario(Proprietario proprietario) {
        this.proprietario = proprietario;
    }
}