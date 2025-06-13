/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.petplus;

import java.time.LocalDateTime;

/**
 *
 * @author yasmi
 */
public class Consulta {
    private long id;

    private LocalDateTime dataHora; 
    private Paciente paciente; 

    public Consulta( LocalDateTime dataHora, Paciente paciente) {
        this.dataHora = dataHora;
        this.paciente = paciente;
    }

  
    public Consulta(long id, LocalDateTime dataHora, Paciente paciente) {
        this.id = id;
        this.dataHora = dataHora;
        this.paciente = paciente;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}
