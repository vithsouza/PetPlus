/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.petplus;

/**
 *
 * @author yasmi
 */
public final class Veterinario extends Pessoa {
   
    private int CRMV;

    public Veterinario(int CRMV, long cpf, String nome) {
        super(cpf, nome);
        this.CRMV = CRMV;
    }

    public int getCRMV() {
        return CRMV;
    }

    public void setCRMV(int CRMV) {
        this.CRMV = CRMV;
    }
    
    
}
