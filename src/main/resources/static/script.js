// URL base de los servicios REST publicados por Spring Boot.
const API = "/api";

/**
 * Ejecuta una solicitud HTTP contra la API y normaliza su respuesta.
 * Si el servidor responde con un estado de error, genera una excepción
 * para que cada formulario pueda mostrar un mensaje comprensible.
 */
async function solicitud(ruta, opciones = {}) {
    const respuesta = await fetch(`${API}${ruta}`, {
        headers: { "Content-Type": "application/json" },
        ...opciones
    });
    const tipo = respuesta.headers.get("content-type") || "";
    const datos = tipo.includes("application/json") ? await respuesta.json() : await respuesta.text();
    if (!respuesta.ok) {
        const detalle = datos && datos.message ? datos.message : (typeof datos === "string" ? datos : "No fue posible completar la operación.");
        throw new Error(detalle);
    }
    return datos;
}

// Convierte los valores recibidos del servidor en texto seguro antes de
// insertarlos en las tarjetas HTML, evitando interpretar etiquetas ingresadas.
function textoSeguro(valor) {
    return String(valor ?? "").replace(/[&<>"']/g, c => ({
        "&":"&amp;", "<":"&lt;", ">":"&gt;", '"':"&quot;", "'":"&#39;"
    }[c]));
}

// Presenta los valores monetarios usando el formato colombiano (COP).
function moneda(valor) {
    return new Intl.NumberFormat("es-CO", { style: "currency", currency: "COP", maximumFractionDigits: 0 }).format(Number(valor || 0));
}

function mostrarError(elemento, error) {
    if (elemento) elemento.textContent = error.message || "Ocurrió un error.";
}

// -----------------------------------------------------------------------------
// MÓDULO DE CLIENTES
// Permite consultar, registrar, editar y eliminar clientes mediante la API REST.
// -----------------------------------------------------------------------------
const formCliente = document.getElementById("formCliente");
if (formCliente) {
    const mensaje = document.getElementById("mensaje");
    const lista = document.getElementById("listaClientes");
    const cancelar = document.getElementById("cancelarEdicionCliente");
    let clientes = [];

    // Consulta la lista actualizada y construye una tarjeta por cada cliente.
    async function cargarClientes() {
        try {
            clientes = await solicitud("/clientes");
            lista.innerHTML = clientes.map(c => `
              <article class="tarjeta"><h3>${textoSeguro(c.nombre)}</h3>
                <p>Correo: ${textoSeguro(c.correo)}</p><p>Teléfono: ${textoSeguro(c.telefono)}</p>
                <p>Dirección: ${textoSeguro(c.direccion)}</p><p>ID: ${c.id_cliente}</p>
                <div class="acciones"><button type="button" data-editar-cliente="${c.id_cliente}">Editar</button><button class="eliminar" type="button" data-eliminar-cliente="${c.id_cliente}">Eliminar</button></div>
              </article>`).join("") || "<p>No hay clientes registrados.</p>";
        } catch (e) { lista.textContent = "No se pudieron cargar los clientes: " + e.message; }
    }
    // Restablece el formulario y devuelve la pantalla al modo de registro.
    function limpiarCliente() {
        formCliente.reset(); document.getElementById("idCliente").value = "";
        document.getElementById("tituloFormularioCliente").textContent = "Registrar cliente";
        document.getElementById("botonCliente").textContent = "Registrar cliente";
        cancelar.hidden = true;
    }
    // El mismo formulario se utiliza para crear (POST) o actualizar (PUT).
    formCliente.addEventListener("submit", async e => {
        e.preventDefault(); mensaje.textContent = "";
        const id = document.getElementById("idCliente").value;
        const datos = {
            nombre: document.getElementById("nombre").value.trim(),
            correo: document.getElementById("correo").value.trim(),
            telefono: document.getElementById("telefono").value.trim(),
            direccion: document.getElementById("direccion").value.trim()
        };
        try {
            await solicitud("/clientes" + (id ? `/${id}` : ""), { method: id ? "PUT" : "POST", body: JSON.stringify(datos) });
            mensaje.textContent = id ? "Cliente actualizado correctamente." : "Cliente registrado correctamente.";
            limpiarCliente(); await cargarClientes();
        } catch (err) { mostrarError(mensaje, err); }
    });
    cancelar.addEventListener("click", limpiarCliente);
    lista.addEventListener("click", async e => {
        const edit = e.target.closest("[data-editar-cliente]");
        const del = e.target.closest("[data-eliminar-cliente]");
        if (edit) {
            const c = clientes.find(x => x.id_cliente === Number(edit.dataset.editarCliente));
            if (!c) return;
            document.getElementById("idCliente").value = c.id_cliente;
            for (const k of ["nombre","correo","telefono","direccion"]) document.getElementById(k).value = c[k] || "";
            document.getElementById("tituloFormularioCliente").textContent = "Actualizar cliente";
            document.getElementById("botonCliente").textContent = "Guardar cambios";
            cancelar.hidden = false; formCliente.scrollIntoView({behavior:"smooth"});
        } else if (del && confirm("¿Seguro que deseas eliminar este cliente?")) {
            try { await solicitud(`/clientes/${del.dataset.eliminarCliente}`, {method:"DELETE"}); mensaje.textContent = "Cliente eliminado."; await cargarClientes(); }
            catch (err) { mostrarError(mensaje, err); }
        }
    });
    cargarClientes();
}

// -----------------------------------------------------------------------------
// MÓDULO DE PRODUCTOS
// Administra el catálogo y el inventario: consulta, registro, edición y borrado.
// -----------------------------------------------------------------------------
const formProducto = document.getElementById("formProducto");
if (formProducto) {
    const mensaje = document.getElementById("mensajeProducto"), lista = document.getElementById("listaProductos");
    const cancelar = document.getElementById("cancelarEdicionProducto");
    let productos = [];
    // Obtiene los productos y actualiza las tarjetas del inventario.
    async function cargarProductos() {
        try {
            productos = await solicitud("/productos");
            lista.innerHTML = productos.map(p => `<article class="tarjeta"><h3>${textoSeguro(p.modelo)}</h3>
              <p>Marca: ${textoSeguro(p.marca)}</p><p>Almacenamiento: ${textoSeguro(p.almacenamiento)}</p><p>Color: ${textoSeguro(p.color)}</p>
              <p>Precio: ${moneda(p.precio)}</p><p>Stock: ${p.stock}</p><p>ID: ${p.id}</p>
              <div class="acciones"><button type="button" data-editar-producto="${p.id}">Editar</button><button class="eliminar" type="button" data-eliminar-producto="${p.id}">Eliminar</button></div></article>`).join("") || "<p>No hay productos registrados.</p>";
        } catch(e) { lista.textContent = "No se pudieron cargar los productos: " + e.message; }
    }
    function limpiar() {
        formProducto.reset(); document.getElementById("idProducto").value = "";
        document.getElementById("tituloFormularioProducto").textContent = "Registrar producto";
        document.getElementById("botonProducto").textContent = "Registrar producto"; cancelar.hidden = true;
    }
    formProducto.addEventListener("submit", async e => {
        e.preventDefault(); mensaje.textContent = "";
        const id = document.getElementById("idProducto").value;
        const datos = Object.fromEntries(["modelo","marca","almacenamiento","color","precio","stock"].map(k => [k, document.getElementById(k).value.trim()]));
        try {
            await solicitud("/productos" + (id ? `/${id}` : ""), {method:id ? "PUT":"POST", body:JSON.stringify(datos)});
            mensaje.textContent = id ? "Producto actualizado correctamente." : "Producto registrado correctamente.";
            limpiar(); await cargarProductos();
        } catch(err) { mostrarError(mensaje, err); }
    });
    cancelar.addEventListener("click", limpiar);
    lista.addEventListener("click", async e => {
        const edit=e.target.closest("[data-editar-producto]"), del=e.target.closest("[data-eliminar-producto]");
        if(edit) {
            const p=productos.find(x=>x.id===Number(edit.dataset.editarProducto)); if(!p)return;
            document.getElementById("idProducto").value=p.id;
            for(const k of ["modelo","marca","almacenamiento","color","precio","stock"]) document.getElementById(k).value=p[k] ?? "";
            document.getElementById("tituloFormularioProducto").textContent="Actualizar producto";
            document.getElementById("botonProducto").textContent="Guardar cambios"; cancelar.hidden=false; formProducto.scrollIntoView({behavior:"smooth"});
        } else if(del && confirm("¿Seguro que deseas eliminar este producto?")) {
            try { await solicitud(`/productos/${del.dataset.eliminarProducto}`,{method:"DELETE"}); mensaje.textContent="Producto eliminado."; await cargarProductos(); }
            catch(err) { mostrarError(mensaje,err); }
        }
    });
    cargarProductos();
}

// -----------------------------------------------------------------------------
// MÓDULO DE VENTAS
// Gestiona la cabecera de las ventas. Al editarla, los detalles asociados se
// conservan de acuerdo con el comportamiento implementado en el backend.
// -----------------------------------------------------------------------------
const formVenta = document.getElementById("formVenta");
if (formVenta) {
    const mensaje=document.getElementById("mensajeVenta"), lista=document.getElementById("listaVentas"), selector=document.getElementById("idClienteVenta");
    const cancelar=document.getElementById("cancelarEdicionVenta");
    let ventas=[];
    // Carga los clientes para relacionar cada venta con un cliente existente.
    async function cargarSelectorClientes() {
        const cs=await solicitud("/clientes");
        selector.innerHTML='<option value="">Seleccione un cliente</option>'+cs.map(c=>`<option value="${c.id_cliente}">${textoSeguro(c.nombre)} (ID ${c.id_cliente})</option>`).join("");
    }
    // Consulta el historial y muestra cliente, fecha y total de cada venta.
    async function cargarVentas() {
        try {
            ventas=await solicitud("/ventas");
            lista.innerHTML=ventas.map(v=>`<article class="tarjeta"><h3>Venta #${v.id_venta}</h3><p>Cliente: ${textoSeguro(v.cliente)}</p><p>Fecha: ${textoSeguro(String(v.fecha).slice(0,10))}</p><p>Total: ${moneda(v.total)}</p>
            <div class="acciones"><button type="button" data-editar-venta="${v.id_venta}">Editar</button><button class="eliminar" type="button" data-eliminar-venta="${v.id_venta}">Eliminar</button></div></article>`).join("") || "<p>No hay ventas registradas.</p>";
        } catch(e) { lista.textContent="No se pudieron cargar las ventas: "+e.message; }
    }
    function limpiar() {
        formVenta.reset(); document.getElementById("idVenta").value="";
        document.getElementById("tituloFormularioVenta").textContent="Registrar venta";
        document.getElementById("botonVenta").textContent="Registrar venta"; cancelar.hidden=true;
        document.getElementById("fechaVenta").value=new Date().toISOString().slice(0,10);
    }
    formVenta.addEventListener("submit",async e=>{
        e.preventDefault(); mensaje.textContent="";
        const id=document.getElementById("idVenta").value;
        const datos={id_cliente:selector.value,fecha:document.getElementById("fechaVenta").value,total:document.getElementById("totalVenta").value};
        try { await solicitud("/ventas"+(id?`/${id}`:""),{method:id?"PUT":"POST",body:JSON.stringify(datos)});
            mensaje.textContent=id?"Venta actualizada correctamente.":"Venta registrada correctamente."; limpiar(); await cargarVentas();
        } catch(err) { mostrarError(mensaje,err); }
    });
    cancelar.addEventListener("click",limpiar);
    lista.addEventListener("click",async e=>{
        const edit=e.target.closest("[data-editar-venta]"),del=e.target.closest("[data-eliminar-venta]");
        if(edit){const v=ventas.find(x=>x.id_venta===Number(edit.dataset.editarVenta));if(!v)return;
            document.getElementById("idVenta").value=v.id_venta;selector.value=v.id_cliente;
            document.getElementById("fechaVenta").value=String(v.fecha).slice(0,10);
            document.getElementById("totalVenta").value=v.total;
            document.getElementById("tituloFormularioVenta").textContent="Actualizar venta";
            document.getElementById("botonVenta").textContent="Guardar cambios";cancelar.hidden=false;formVenta.scrollIntoView({behavior:"smooth"});
        }else if(del&&confirm("¿Seguro que deseas eliminar esta venta y sus detalles?")){
            try{await solicitud(`/ventas/${del.dataset.eliminarVenta}`,{method:"DELETE"});mensaje.textContent="Venta eliminada.";await cargarVentas();}
            catch(err){mostrarError(mensaje,err);}
        }
    });
    document.getElementById("fechaVenta").value=new Date().toISOString().slice(0,10);
    cargarSelectorClientes().then(cargarVentas).catch(e=>{selector.innerHTML='<option value="">No se pudieron cargar los clientes</option>';mostrarError(mensaje,e);});
}
