
package com.baozistore.controller;

import com.baozistore.model.Pedido;
import com.baozistore.repository.PedidoRepository;
import com.baozistore.repository.ClienteRepository;
import com.baozistore.repository.ProdutoRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository repository;
    private final ClienteRepository clientes;
    private final ProdutoRepository produtos;

    public PedidoController(
            PedidoRepository repository,
            ClienteRepository clientes,
            ProdutoRepository produtos) {
        this.repository = repository;
        this.clientes = clientes;
        this.produtos = produtos;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Pedido pedido) {

        if (pedido.getClienteId() == null ||
                !clientes.existsById(pedido.getClienteId())) {
            return ResponseEntity.badRequest()
                    .body("Cliente inexistente.");
        }

        if (pedido.getProdutoId() == null ||
                !produtos.existsById(pedido.getProdutoId())) {
            return ResponseEntity.badRequest()
                    .body("Produto inexistente.");
        }

        if (pedido.getQuantidade() == null ||
                pedido.getQuantidade() <= 0) {
            return ResponseEntity.badRequest()
                    .body("Quantidade deve ser maior que zero.");
        }

        pedido.setId(null);
        return ResponseEntity.ok(repository.save(pedido));
    }

    @GetMapping
    public List<Pedido> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

