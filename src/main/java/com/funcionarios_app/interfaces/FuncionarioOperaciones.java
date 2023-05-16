/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.funcionarios_app.interfaces;

import com.funcionarios_app.dao.ConexionDAO;
import com.funcionarios_app.modelos.Funcionario;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author NATSU DRAGNEEL
 */
public class FuncionarioOperaciones implements FuncionarioI{
    private String CREAR = "INSERT INTO funcionarios(numero_identificacion,tipo_identificacion, "
            + "nombres, apellidos, estado_civil, sexo, direccion, telefono, fecha_nacimiento) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private String ACTUALIZAR = "UPDATE funcionarios SET "
            + "tipo_identificacion = ?, nombres = ?, apellidos = ?, estado_civil = ?, "
            + "sexo = ?, direccion = ?, telefono = ? WHERE numero_identificacion = ?;";
    private String ELIMINAR = "DELETE FROM funcionarios WHERE numero_identificacion = ?;";
    private String BUSCAR = "SELECT * FROM funcionarios WHERE numero_identificacion = ?;";
    private String LISTAR = "SELECT * FROM funcionarios;";

    ConexionDAO conexion = ConexionDAO.getConexionDAO();
    
    @Override
    public void crearFuncionario(Funcionario funcionario) {
        try {
            Connection conectar = conexion.conectarDB();
            PreparedStatement crear = conectar.prepareStatement(this.CREAR);
            
            crear.setString(1, funcionario.getNumeroIdentificacion());
            crear.setString(2, funcionario.getTipoIdentificacion());
            crear.setString(3, funcionario.getNombres());
            crear.setString(4, funcionario.getApellidos());
            crear.setString(5, funcionario.getEstadoCivil());
            crear.setString(6, Character.toString(funcionario.getSexo()));
            crear.setString(7, funcionario.getDireccion());
            crear.setString(8, funcionario.getTelefono());
            crear.setDate(9, (Date) funcionario.getFechaNacimiento());
            
            crear.executeUpdate();
            JOptionPane.showMessageDialog(null, "Funcionario creado!!!");
            conexion.desconectarDB();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR: " + e);
        }
    }

    @Override
    public void actualizarFuncionario(Funcionario funcionario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminarFuncionario(Funcionario funcionario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void buscarFuncionario(Funcionario funcionario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Object[]> listarFuncionarios() {
        List<Object[]> listadoFuncionarios = new ArrayList<>();
        try {
            Connection connect = conexion.conectarDB();
            PreparedStatement listarFuncionarios = connect.prepareStatement(this.LISTAR);
            ResultSet query = listarFuncionarios.executeQuery();
            
            while(query.next()) {
                Object[] fila = new Object[10];
                
                for (int i = 0; i < fila.length; i++) {
                    fila[i] = query.getObject(i+1);
                }
                listadoFuncionarios.add(fila);
            }
            conexion.desconectarDB();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR: " + e);
        } finally {
            return listadoFuncionarios;
        }
    }
    
}
