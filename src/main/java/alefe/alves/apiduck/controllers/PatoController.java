package alefe.alves.apiduck.controllers;

import alefe.alves.apiduck.dtos.PatoDTO;
import alefe.alves.apiduck.services.PatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
