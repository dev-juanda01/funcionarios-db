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
public class FuncionarioOperaciones implements FuncionarioI {

    private final String CREAR = "INSERT INTO funcionarios(numero_identificacion, tipo_identificacion, "
            + "nombres, apellidos, estado_civil, sexo, direccion, telefono, fecha_nacimiento) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private final String ACTUALIZAR = "UPDATE funcionarios SET "
            + "tipo_identificacion = ?, nombres = ?, apellidos = ?, estado_civil = ?, "
            + "sexo = ?, direccion = ?, telefono = ? WHERE numero_identificacion = ?;";
    private final String ELIMINAR = "DELETE FROM funcionarios WHERE numero_identificacion = ?;";
    private final String BUSCAR = "SELECT * FROM funcionarios WHERE numero_identificacion = ?;";
    private final String LISTAR = "SELECT * FROM funcionarios;";
    private final String LISTAR_TIPOS_IDENTIFICACIONES = "SELECT nombre FROM tipoidentificaciones";

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
        try {
            Connection conectar = conexion.conectarDB();
            PreparedStatement actualizar = conectar.prepareStatement(this.ACTUALIZAR);

            actualizar.setString(1, funcionario.getTipoIdentificacion());
            actualizar.setString(2, funcionario.getNombres());
            actualizar.setString(3, funcionario.getApellidos());
            actualizar.setString(4, funcionario.getEstadoCivil());
            actualizar.setString(5, Character.toString(funcionario.getSexo()));
            actualizar.setString(6, funcionario.getDireccion());
            actualizar.setString(7, funcionario.getTelefono());
            actualizar.setString(8, funcionario.getNumeroIdentificacion());

            actualizar.executeUpdate();
            conexion.desconectarDB();
            JOptionPane.showMessageDialog(null, "Funcionario actualizado!!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR: " + e);
        }
    }

    @Override
    public void eliminarFuncionario(Funcionario funcionario) {
        try {
            // IDS de tablas
            String idGrupoFamilia;

            // Conectar a base de datos
            Connection conectar = conexion.conectarDB();

            // Buscar el grupofamiliar
            String buscarGrupoFamiliar = "SELECT grupofamilia_id FROM grupofamilias WHERE "
                    + "fk_funcionario = ?";
            PreparedStatement buscarGrupoF = conectar.prepareStatement(buscarGrupoFamiliar);
            buscarGrupoF.setString(1, String.valueOf(funcionario.getId()));
            ResultSet query = buscarGrupoF.executeQuery();

            if (query.next()) {
                idGrupoFamilia = query.getString("grupofamilia_id");

                // Eliminar grupofamiliasxpersonas
                String eliminarGrupoFamiliaXPersonas = "DELETE FROM grupofamiliasxpersonas WHERE "
                        + "fk_grupofamilia = ?";
                PreparedStatement eliminarGrupoFamiliaXPersona = conectar.prepareStatement(eliminarGrupoFamiliaXPersonas);
                eliminarGrupoFamiliaXPersona.setString(1, idGrupoFamilia);
                eliminarGrupoFamiliaXPersona.executeUpdate();

                // Eliminar funcionario de grupofamilia
                String eliminarFuncionarioEnGF = "DELETE FROM grupofamilias WHERE fk_funcionario = ?";
                PreparedStatement eliminarGF = conectar.prepareStatement(eliminarFuncionarioEnGF);
                eliminarGF.setString(1, String.valueOf(funcionario.getId()));
                eliminarGF.executeUpdate();

                // Se elimina el funcionario de la tabla funcionarioxacademico
                String eliminarFuncionarioEnFXA = "DELETE FROM funcionarioxacademico WHERE fk_funcionario = ?";
                PreparedStatement eliminarFXA = conectar.prepareStatement(eliminarFuncionarioEnFXA);
                eliminarFXA.setString(1, String.valueOf(funcionario.getId()));
                eliminarFXA.executeUpdate();
                
                // Se elimina de la tabla funcionarios
                PreparedStatement eliminar = conectar.prepareStatement(this.ELIMINAR);
                eliminar.setString(1, funcionario.getNumeroIdentificacion());
                eliminar.executeUpdate();
            } else {
                // Se elimina de la tabla funcionarios si no hay relación
                PreparedStatement eliminar = conectar.prepareStatement(this.ELIMINAR);
                eliminar.setString(1, funcionario.getNumeroIdentificacion());
                eliminar.executeUpdate();
            }
            
            conexion.desconectarDB();
            JOptionPane.showMessageDialog(null, "Funcionario eliminado!!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR: " + e);
        }
    }

    @Override
    public Funcionario buscarFuncionario(String numeroIdentificacion) {
        Funcionario funcionarioEncontrado = new Funcionario();

        try {
            Connection conectar = conexion.conectarDB();
            PreparedStatement buscar = conectar.prepareStatement(this.BUSCAR);
            buscar.setString(1, numeroIdentificacion);

            ResultSet query = buscar.executeQuery();

            if (query.next()) {
                funcionarioEncontrado.setId(Integer.parseInt(query.getString("funcionario_id")));
                funcionarioEncontrado.setNumeroIdentificacion(query.getString("numero_identificacion"));
                funcionarioEncontrado.setTipoIdentificacion(query.getString("tipo_identificacion"));
                funcionarioEncontrado.setNombres(query.getString("nombres"));
                funcionarioEncontrado.setApellidos(query.getString("apellidos"));
                funcionarioEncontrado.setEstadoCivil(query.getString("estado_civil"));
                funcionarioEncontrado.setSexo(query.getString("sexo").charAt(0));
                funcionarioEncontrado.setDireccion(query.getString("direccion"));
                funcionarioEncontrado.setTelefono(query.getString("telefono"));
                funcionarioEncontrado.setFechaNacimiento(Date.valueOf(query.getString("fecha_nacimiento")));
            }

            conexion.desconectarDB();

            return funcionarioEncontrado;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR: " + e);
        } finally {
            return funcionarioEncontrado;
        }
    }

    @Override
    public List<Object[]> listarFuncionarios() {
        List<Object[]> listadoFuncionarios = new ArrayList<>();
        try {
            Connection connect = conexion.conectarDB();
            PreparedStatement listarFuncionarios = connect.prepareStatement(this.LISTAR);
            ResultSet query = listarFuncionarios.executeQuery();

            while (query.next()) {
                Object[] fila = new Object[10];

                for (int i = 0; i < fila.length; i++) {
                    fila[i] = query.getObject(i + 1);
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

    @Override
    public List<String> listarTiposIdentificaciones() {
        List<String> listadoTiposIdentificaciones = new ArrayList<>();
        try {
            Connection connect = conexion.conectarDB();
            PreparedStatement listarTiposIdentificaciones = connect.prepareStatement(this.LISTAR_TIPOS_IDENTIFICACIONES);
            ResultSet query = listarTiposIdentificaciones.executeQuery();

            while (query.next()) {
                listadoTiposIdentificaciones.add(query.getString("nombre"));
            }

            conexion.desconectarDB();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR: " + e);
        } finally {
            return listadoTiposIdentificaciones;
        }
    }

}
