/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.funcionarios_app.interfaces;

import com.funcionarios_app.modelos.Funcionario;
import java.util.List;

/**
 *
 * @author NATSU DRAGNEEL
 */
public interface FuncionarioI {
    public void crearFuncionario(Funcionario funcionario);
    public void actualizarFuncionario(Funcionario funcionario);
    public void eliminarFuncionario(Funcionario funcionario);
    public Funcionario buscarFuncionario(String numeroIdentificacion);
    public List<Object[]> listarFuncionarios();
    public List<String> listarTiposIdentificaciones();   
}
