package org.expertojava.services;

import org.expertojava.domain.Producto;
import org.expertojava.domain.Productos;

import javax.ejb.Singleton;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@Singleton
public class ProductosSesionBean {
    private Map<Integer, Producto> productoDB =
            new ConcurrentHashMap<Integer, Producto>();
    private AtomicInteger idContador = new AtomicInteger();

    public ProductosSesionBean() {
        productoDB = new ConcurrentHashMap<Integer, Producto>();
    }

    public void addProducto(Producto p) {
        p.setId(idContador.addAndGet(1));
        productoDB.put(p.getId(), p);
        //return Response.created(URI.create("/clientes/"
        //        + p.getId())).build();
    }


    public List<Producto> getProductos() {
        List<Producto> lista = new ArrayList<Producto>(productoDB.values());
        /*List<Producto> lista = new ArrayList<Producto>();
        lista.addAll(productoDB.values()); */
        return lista;
    }

        public void deleteProducto(String name) {
            Producto p = findProductoByName(name);
            if (p != null) {
                productoDB.remove(p.getId());
            }
        }

        private Producto findProductoByName(String name) {
            List<Producto> lista = new ArrayList<Producto>(productoDB.values());
            for (Producto p : lista) {
                if (name.equals(p.getNombre()))
                    return p;
            }
            return null;
        }

}
