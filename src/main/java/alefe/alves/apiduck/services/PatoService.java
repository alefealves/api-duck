package alefe.alves.apiduck.services;

import alefe.alves.apiduck.dtos.PatoDTO;
import alefe.alves.apiduck.dtos.PatoUpdateDTO;
import alefe.alves.apiduck.dtos.ResponsePato;
import alefe.alves.apiduck.enums.TipoPato;
import alefe.alves.apiduck.exceptions.PatoNotFoundException;
import alefe.alves.apiduck.interfaces.PatoInterface;
import alefe.alves.apiduck.models.pato.Pato;
import alefe.alves.apiduck.repositories.PatoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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
            PatoDTO patoDTO = modelMapper.map(pato, PatoDTO.class);
            patoDTO.setMae_id(pato.getMae().getId());
            return patoDTO;
        } else {
            throw new PatoNotFoundException("Pato de id "+id+" não foi encontrado.");
        }
    }

    @Override
    public List<ResponsePato> getAllPatos() throws Exception {
        try {

            List<ResponsePato> responsePatos = new ArrayList<>();
            Sort sort = Sort.by(Sort.Direction.ASC, "id");
            List<Pato> patoList = this.repository.findAll(sort);

            for(Pato patoId : patoList){
                Optional<Pato> OptPato = this.repository.findPatoById(patoId.getId());
                if(OptPato.isPresent()) {
                    Pato pato = OptPato.get();
                    ResponsePato responsePato = createResponse(pato);
                    responsePatos.add(responsePato);
                }
            }

            return responsePatos;
        } catch (Exception e) {
            throw new Exception("Ocorreu um erro ao buscar os patos.");
        }
    }

    @Override
    public PatoDTO createPato(PatoDTO dto) throws Exception {
        if (dto.getTipo() == TipoPato.FILHO) {
            // Verifica se foi informado mae_id
            if (dto.getMae_id() != null) {
                // Busca o pato mãe pelo ID
                Pato mae = repository.findPatoById(dto.getMae_id())
                        .orElseThrow(() -> new Exception("ID da pata_mae inválido"));
                // O valor do pato que foi vinculado como mae fica em R$50
                mae.setValor(BigDecimal.valueOf(50.00));
                dto.setMae(mae);
                dto.setTipo(TipoPato.FILHO);
            } else {
                throw new Exception("Informe o id da Pata Mae");
            }
        }

        Pato newPato = new Pato(dto);
        this.repository.save(newPato);
        PatoDTO patoDTO = modelMapper.map(newPato, PatoDTO.class);
        patoDTO.setMae_id(dto.getMae_id());
        return patoDTO;
    }

    @Override
    public PatoUpdateDTO updatePato(PatoUpdateDTO dto, Long id) throws Exception {
        return null;
    }

    @Override
    public void deletePato(Long id) throws Exception {

    }

    public ResponsePato createResponse(Pato pato) throws Exception {
        ResponsePato responsePato = new ResponsePato();

        responsePato.setId(pato.getId());
        responsePato.setTipoPato(pato.getTipo());
        responsePato.setValor(pato.getValor());
        responsePato.setStatus(pato.getStatus());
        if (pato.getMae() != null)
            responsePato.setMae_id(pato.getMae().getId());
        /*if (pato.getVenda() != null)
            responsePato.setVenda_id(pato.getVenda().getId());*/

        return responsePato;
    }
}
