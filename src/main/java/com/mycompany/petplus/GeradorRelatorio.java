/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.mycompany.petplus;

import java.util.List;
/**
 *
 * @author yasmi
 */
public interface GeradorRelatorio<T> {
    void gerar(List<T> dados);
}
