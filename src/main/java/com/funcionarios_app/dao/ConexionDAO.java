/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.funcionarios_app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author NATSU DRAGNEEL
 */
public class ConexionDAO {
    private String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static Connection conectar;
    private static ConexionDAO conexion;
    private static final String URL = "jdbc:mysql://localhost:3306/funcionarios_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
            
    public ConexionDAO() {
        
    }
    
    public Connection conectarDB(){
        try {
            Class.forName(this.DRIVER);
            conectar = DriverManager.getConnection(this.URL, this.USERNAME, this.PASSWORD);
            return conectar;
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR: "+ e);
        }
        return conectar;
    }
    
    public void desconectarDB() throws SQLException {
        try {
            conectar.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR: "+ e);
            conectar.close();
        } finally {
            conectar.close();
        }
    }
    
    public static ConexionDAO getConexionDAO() {
        if(conexion == null) {
            conexion = new ConexionDAO();
        }
        
        return conexion;
    }
}
