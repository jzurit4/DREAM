package cl.pzdream.backend.service;

import cl.pzdream.backend.model.Producto;
import cl.pzdream.backend.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, Producto datos) {
        return productoRepository.findById(id).map(producto -> {
            producto.setNombre(datos.getNombre());
            producto.setPrecio(datos.getPrecio());
            producto.setPrecioOriginal(datos.getPrecioOriginal());
            producto.setStock(datos.getStock());
            producto.setImagen(datos.getImagen());
            producto.setDescripcion(datos.getDescripcion());
            producto.setCategoria(datos.getCategoria());
            producto.setOferta(datos.getOferta());
            return productoRepository.save(producto);
        }).orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }
}