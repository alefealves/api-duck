package alefe.alves.apiduck.controllers;

import alefe.alves.apiduck.dtos.PatoDTO;
import alefe.alves.apiduck.dtos.PatoUpdateDTO;
import alefe.alves.apiduck.dtos.ResponsePato;
import alefe.alves.apiduck.services.PatoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pato")
public class PatoController {

    @Autowired
    private PatoService patoService;

    @GetMapping("/{id}")
    public ResponseEntity<PatoDTO> getPato(@PathVariable Long id) throws Exception {
        PatoDTO pato = this.patoService.findPatoById(id);
        return new ResponseEntity<>(pato, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ResponsePato>> getAllPatos() throws Exception{
        List<ResponsePato> patos = this.patoService.getAllPatos();
        return new ResponseEntity<>(patos, HttpStatus.OK);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<PatoDTO> createPato(@RequestBody @Valid PatoDTO dto) throws Exception {
        PatoDTO pato = patoService.createPato(dto);
        return new ResponseEntity<>(pato, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity updatePato(@RequestBody PatoUpdateDTO dto, @PathVariable Long id) throws Exception{
        PatoUpdateDTO pato = this.patoService.updatePato(dto, id);
        return new ResponseEntity<>(pato, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletePato(@PathVariable Long id) throws Exception{
        this.patoService.deletePato(id);
        return ResponseEntity.ok().build();
    }

}
