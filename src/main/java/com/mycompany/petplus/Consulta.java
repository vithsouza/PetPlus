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
    private String status; 
    private String diagnostico;
    private String tratamento;
    private LocalDateTime dataHora; 
    private Paciente paciente; 

    public Consulta(String status, String diagnostico, String tratamento, LocalDateTime dataHora, Paciente paciente) {
        this.status = status;
        this.diagnostico = diagnostico;
        this.tratamento = tratamento;
        this.dataHora = dataHora;
        this.paciente = paciente;
    }

  
    public Consulta(long id, String status, String diagnostico, String tratamento, LocalDateTime dataHora, Paciente paciente) {
        this.id = id;
        this.status = status;
        this.diagnostico = diagnostico;
        this.tratamento = tratamento;
        this.dataHora = dataHora;
        this.paciente = paciente;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamento() {
        return tratamento;
    }

    public void setTratamento(String tratamento) {
        this.tratamento = tratamento;
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
