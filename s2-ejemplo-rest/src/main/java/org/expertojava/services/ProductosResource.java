package org.expertojava.services;

import org.expertojava.domain.Producto;
import org.expertojava.domain.Productos;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;


@Path("/productos")
@RequestScoped
public class ProductosResource {

    @EJB
    ProductosSesionBean bean;

    @GET
    @Produces({"application/xml", "application/json"})
    @Path("/otro")
    public Productos getListado() {
        return new Productos(bean.getProductos().toArray(new Producto[0]));
    }


    @GET
    @Produces("text/plain")
    @Path("/{tipo}/{color}/{tamanyo}")
    public String getInfo(@Context UriInfo info) {
        String tipo = info.getPathParameters().getFirst("tipo");
        int numero_pathSegments = info.getPathSegments().size(); // a partir de productos
        PathSegment raiz = info.getPathSegments().get(0);
        String valorRaiz = raiz.getPath(); //productos
        PathSegment color = info.getPathSegments().get(1);
        String valorColor = color.getPath();
        String tono = color.getMatrixParameters().getFirst("tono");

        String resultado = "tipo: "+tipo+" "+" " +
                "\n numero de segmentos: "+ numero_pathSegments +
                "\n valor raiz: "+ valorRaiz+
                "\n segmento 1: " + color.getPath()+
                "\n tono: "+tono;
        return resultado;
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public Producto[] getList() {
        return bean.getProductos().toArray(new Producto[0]);
    }

    @GET
    @Produces({"application/json", "application/xml"})
    @Path("{id}")
    public Producto getProduct(@PathParam("id")int id) {
        if (id < bean.getProductos().size())
            return bean.getProductos().get(id);
        else
            return null;
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    //acepta tipos myme: application/x-www-form-urlencoded
    //Ejemplo de formato de datos www-form-urlencoded (pares nombre=valor separados por &):
    //    nombre=iPod%20Touch&precio=129.58
    public Response addToList(@FormParam("nombre") String nombre,
                          @FormParam("precio") double precio) {
        System.out.println("Creando un nuevo producto: " + nombre + ", con precio: " + String.format( "%.2f", precio ));
        bean.addProducto(new Producto(nombre, precio));
        return Response.created(URI.create("/productos/" + nombre)).build();
    }

    @PUT
    public void putToList(@FormParam("nombre") String nombre,
                          @FormParam("precio") double precio) {
        addToList(nombre, precio);

    }


    @DELETE
    @Path("{nombre}")
    public void deleteFromList(@PathParam("nombre") String nombre) {
        bean.deleteProducto(nombre);
    }

}
