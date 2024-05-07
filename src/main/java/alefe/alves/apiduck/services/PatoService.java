package alefe.alves.apiduck.services;

import alefe.alves.apiduck.dtos.PatoDTO;
import alefe.alves.apiduck.dtos.PatoUpdateDTO;
import alefe.alves.apiduck.dtos.ResponsePato;
import alefe.alves.apiduck.exceptions.PatoNotFoundException;
import alefe.alves.apiduck.interfaces.PatoInterface;
import alefe.alves.apiduck.models.pato.Pato;
import alefe.alves.apiduck.repositories.PatoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatoService implements PatoInterface {

    @Autowired
    private PatoRepository repository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public PatoDTO findPatoById(Long id) throws Exception {
        Optional<Pato> optPato = this.repository.findPatoById(id);
        if(optPato.isPresent()){
            Pato pato = optPato.get();
            return modelMapper.map(pato, PatoDTO.class);
        } else {
            throw new PatoNotFoundException("Pato de id "+id+" não foi encontrado.");
        }
    }

    @Override
    public List<ResponsePato> getAllPatos() throws Exception {
        return List.of();
    }

    @Override
    public PatoDTO createPato(PatoDTO dto) throws Exception {
        return null;
    }

    @Override
    public PatoUpdateDTO updatePato(PatoUpdateDTO dto, Long id) throws Exception {
        return null;
    }

    @Override
    public void deletePato(Long id) throws Exception {

    }
}
