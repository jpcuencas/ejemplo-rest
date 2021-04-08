package org.expertojava;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBContext;
import java.io.*;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/clientes")
public class ClienteResource {

    private static Map<Integer, Cliente> clienteDB =
            new ConcurrentHashMap<Integer, Cliente>();
    private static AtomicInteger idContador = new AtomicInteger();

    //Dependiendo del valor de la cabecera Accept de la petición HTTP
    //se invocará uno de los métodos anotados con @GET

    @GET
    @Path("{id}")
    @Produces({"application/xml","application/json"})
    public Cliente recuperarClienteIdXML(@PathParam("id") int id) {
        Cliente cliente = clienteDB.get(id);
        if (cliente == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return cliente;
    }

    @GET
    @Path("{id}")
    @Produces("text/plain")
    public String recuperarClienteId(@PathParam("id") int id) {
        Cliente cliente = clienteDB.get(id);
        if (cliente == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return cliente.toString();
    }

    @GET
    @Path("{id}")
    @Produces("application/octet-stream")
    public StreamingOutput recuperarBytesClienteId(@PathParam("id") int id) {
        final Cliente cliente = clienteDB.get(id);
        if (cliente == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return new StreamingOutput() {
            public void write(OutputStream outputStream)
                    throws IOException, WebApplicationException {
                escribirCliente(outputStream, cliente);
            }
        };
    }

    // Leemos los datos del cliente del body del mensaje HTTP
    // La cabecera Content-Type decide cuál de los siguientes métodos se invoca

    // Hay que poner la cabecera Content-type: application/xml o application/json
    // Formato XML:
    //   <cliente><nombre>Antonio</nombre><apellidos>Muñoz Molina</apellidos><ciudad>Nueva York</ciudad></cliente>
    // Formato JSON:
    //   {"nombre":"Antonio","apellidos":"Muñoz Molina","ciudad":"Nueva York"}
    @POST
    @Consumes({"application/xml","application/json"})
    public Response crearCliente(Cliente cliente) {
        cliente.setId(idContador.addAndGet(1));
        clienteDB.put(cliente.getId(), cliente);
        return Response.created(URI.create("/clientes/"
                + cliente.getId())).build();
    }

    // Se llama explícitamente al mapeador XML -> Java
    // Ejemplo:<cliente><nombre>Antonio</nombre><apellidos>Muñoz Molina</apellidos><ciudad>Nueva York</ciudad></cliente>

    @POST
    @Consumes("text/plain")
    public Response crearCliente(String clienteStr) {
        try {
            JAXBContext ctx = JAXBContext.newInstance(Cliente.class);
            Cliente cliente = (Cliente)ctx.createUnmarshaller().
                    unmarshal(new StringReader(clienteStr));
            cliente.setId(idContador.addAndGet(1));
            clienteDB.put(cliente.getId(), cliente);
            System.out.println("TEXT/PLAIN!!!!!");
            return Response.created(URI.create("/clientes/"
                    + cliente.getId())).build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }

    // El formato es: Nombre, Apellidos y Ciudad separados por ;
    // Ejemplo: Antonio;Muñoz Molina;Nueva York

    @POST
    @Consumes("application/octet-stream")
    public Response crearCliente(InputStream is) {
        try {
            Cliente cliente = leerCliente(is);
            cliente.setId(idContador.addAndGet(1));
            clienteDB.put(cliente.getId(), cliente);
            System.out.println("APPLICATION/OCTET-STREAM!!!!");
            System.out.println("Cliente creado " + cliente.getId());
            return Response.created(URI.create("/clientes/"
                    + cliente.getId())).build();
        } catch (IOException e) {
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }


    @PUT
    @Path("{id}")
    @Consumes({"application/xml","application/json"})
    public Response modificarCliente(@PathParam("id") int id,
                                     Cliente nuevo) {
        Cliente actual = clienteDB.get(id);
        if (actual == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        if (nuevo.getNombre().equals("Error"))
            throw new ClienteException();
        actual.setNombre(nuevo.getNombre());
        actual.setApellidos(nuevo.getApellidos());
        actual.setCiudad(nuevo.getCiudad());
        return Response.ok().build();
    }

    // Leemos del InputStrem
    // El formato es: Nombre, Apellidos y Ciudad separados por ;
    // Ejemplo: Antonio;Muñoz Molina;Nueva York
    private Cliente leerCliente(InputStream stream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1000];
        int wasRead = 0;
        do {
            wasRead = stream.read(buffer);
            if (wasRead > 0) {
                baos.write(buffer, 0, wasRead);
            }
        } while (wasRead > -1);
        String datos[] = new String(baos.toByteArray()).split(";");
        Cliente cliente = new Cliente();
        cliente.setNombre(datos[0]);
        cliente.setApellidos(datos[1]);
        cliente.setCiudad(datos[2]);
        return cliente;
    }

    private void escribirCliente(OutputStream out, Cliente cliente) throws IOException {
        String salida = Integer.toString(cliente.getId());
        salida = salida.concat(";");
        salida = salida.concat(cliente.getNombre());
        salida = salida.concat(";");
        salida = salida.concat(cliente.getApellidos());
        salida = salida.concat(";");
        salida = salida.concat(cliente.getCiudad());
        out.write(salida.getBytes());
    }
}
