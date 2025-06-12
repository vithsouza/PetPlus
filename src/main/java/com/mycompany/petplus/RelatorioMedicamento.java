/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.petplus;

import java.util.List;
import javax.swing.SwingUtilities;

public class RelatorioMedicamento implements GeradorRelatorio<Medicamento> {

    @Override
    public void gerar(List<Medicamento> dados) {
        SwingUtilities.invokeLater(() -> {
            // Usa o padrão singleton para obter a instância do formulário de relatório
            FormRelatorioMedicamento formRelatorio = FormRelatorioMedicamento.geraFormRelatorioMedicamento();
            formRelatorio.preencherTabela(dados); // Preenche a tabela com os dados
            formRelatorio.setVisible(true); // Torna o formulário visível
        });
    }
}