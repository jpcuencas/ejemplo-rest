package org.expertojava;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="cliente")
@XmlAccessorType(XmlAccessType.FIELD)
public class Cliente {
    @XmlElement
    private int id;

    @XmlElement
    private String nombre;

    @XmlElement
    private String apellidos;

    private String direccion;
    private String codPostal;

    @XmlElement
    private String ciudad;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nom) {
        this.nombre = nom;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String dir) {
        this.direccion = dir;
    }

    public String getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(String cp) {
        this.codPostal = cp;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", direccion='" + direccion + '\'' +
                ", codPostal='" + codPostal + '\'' +
                ", ciudad='" + ciudad + '\'' +
                '}';
    }
}