package org.expertojava.services;

import org.expertojava.domain.Cliente;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@Path("/clientes")
public class ClienteResource {

    private static Map<Integer, Cliente> clienteDB = new ConcurrentHashMap<Integer, Cliente>();
    private static AtomicInteger idContador = new AtomicInteger();

    @GET
    @Path("/otro")
    @Produces("text/plain")
    public String hola() {
        return "hola mundo";
    }

    @POST
    @Consumes("application/xml")
    public Response crearCliente(Cliente cli) {
        //el parámetro cli se instancia con los datos del cliente del body del mensaje HTTP
        cli.setId(idContador.incrementAndGet());
        clienteDB.put(cli.getId(), cli);
        System.out.println("Cliente creado " + cli.getId());
        //Devuelve el valor de la cabecera Location
        //La URI creada se añade a la raíz del contexto, es decir, http://localhost:8080/rest
        return Response.created(URI.create("/clientes/" + cli.getId())).build();
    }

    @GET
    @Path("{id}")
    @Produces("application/xml")
    public Cliente recuperarClienteId(@PathParam("id") int id) {
        final Cliente cli = clienteDB.get(id);
        if (cli == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return new Cliente(cli.getId(), cli.getNombre(), cli.getApellidos(),
                           cli.getDireccion(),  cli.getCodPostal(), cli.getCiudad());
    }

    @PUT
    @Path("{id}")
    @Consumes("application/xml")
    public void modificarCliente(@PathParam("id") int id,
                                 Cliente nuevoCli) {

        Cliente actual = clienteDB.get(id);
        if (actual == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);

        actual.setNombre(nuevoCli.getNombre()); actual.setApellidos(nuevoCli.getApellidos());
        actual.setDireccion(nuevoCli.getDireccion());
        actual.setCodPostal(nuevoCli.getCodPostal());
        actual.setCiudad(nuevoCli.getCiudad());
    }

    @DELETE
    @Path("{id}")
    public void borrarCliente(@PathParam("id") int id) {
        Cliente cli = clienteDB.get(id);
        if (cli == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        clienteDB.remove(id);
    }


}
