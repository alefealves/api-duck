package alefe.alves.apiduck.controllers;

import alefe.alves.apiduck.dtos.ResponseVenda;
import alefe.alves.apiduck.dtos.VendaDTO;
import alefe.alves.apiduck.services.VendaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venda")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseVenda> getVenda(@PathVariable Long id) throws Exception {
        ResponseVenda venda = this.vendaService.findVendaById(id);
        return new ResponseEntity<>(venda, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ResponseVenda>> getAllVendas() throws Exception{
        List<ResponseVenda> vendas = this.vendaService.getAllVendas();
        return new ResponseEntity<>(vendas, HttpStatus.OK);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<ResponseVenda> createCliente(@RequestBody @Valid VendaDTO dto) throws Exception {
        ResponseVenda newVenda = vendaService.createVenda(dto);
        return new ResponseEntity<>(newVenda, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ResponseVenda> updateVenda(@RequestBody @Valid VendaDTO dto, @PathVariable Long id) throws Exception {
        ResponseVenda venda = this.vendaService.updateVenda(dto, id);
        return new ResponseEntity<>(venda, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteVenda(@PathVariable Long id) throws Exception{
        this.vendaService.deleteVenda(id);
        return ResponseEntity.ok().build();
    }
}
