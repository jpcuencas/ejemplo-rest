package org.expertojava.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="productos")
@XmlAccessorType(XmlAccessType.FIELD)
public class Productos {
    @XmlElement(name="producto")
    private Producto[] listaProductos;

    public Productos() {
    }

    public Productos(Producto[] listaProductos) {
        this.listaProductos = listaProductos;
    }
}
