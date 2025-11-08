package edu.inbugwethrust.premier.suite.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "usuarios")
public class Usuario {


    @Id
    private String nombre_usuario;
    private String contrasenia;


    public Usuario() {
    }


    public Usuario(String nombre_usuario, String contrasenia) {
        this.nombre_usuario = nombre_usuario;
        this.contrasenia = contrasenia;
    }


    public String getNombre_usuario() {
        return nombre_usuario;
    }


    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }


    public String getContrasenia() {
        return contrasenia;
    }


    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
