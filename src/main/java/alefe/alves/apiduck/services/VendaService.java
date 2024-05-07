package alefe.alves.apiduck.services;

import alefe.alves.apiduck.dtos.ClienteDTO;
import alefe.alves.apiduck.dtos.ResponsePato;
import alefe.alves.apiduck.dtos.ResponseVenda;
import alefe.alves.apiduck.dtos.VendaDTO;
import alefe.alves.apiduck.exceptions.VendaNotFoundException;
import alefe.alves.apiduck.interfaces.VendaInterface;
import alefe.alves.apiduck.models.pato.Pato;
import alefe.alves.apiduck.models.venda.Venda;
import alefe.alves.apiduck.repositories.ClienteRepository;
import alefe.alves.apiduck.repositories.PatoRepository;
import alefe.alves.apiduck.repositories.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VendaService implements VendaInterface {

    @Autowired
    private VendaRepository repository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PatoRepository patoRepository;

    @Override
    public ResponseVenda findVendaById(Long id) throws Exception {
        Optional<Venda> optVenda = this.repository.findVendaById(id);
        if(optVenda.isPresent()) {
            Venda venda = optVenda.get();
            return createResponse(venda);
        } else {
            throw new VendaNotFoundException("Venda de id "+id+" não foi encontrada.");
        }
    }

    @Override
    public List<ResponseVenda> getAllVendas() throws Exception {
        try {

            List<ResponseVenda> responseVendas = new ArrayList<>();
            Sort sort = Sort.by(Sort.Direction.ASC, "id");
            List<Venda> vendasList = this.repository.findAll(sort);

            for(Venda vendaId : vendasList){
                Optional<Venda> OptVenda = this.repository.findVendaById(vendaId.getId());
                if(OptVenda.isPresent()) {
                    Venda venda = OptVenda.get();
                    ResponseVenda responseVenda = createResponse(venda);
                    responseVendas.add(responseVenda);
                }
            }

            return responseVendas;
        } catch (Exception e) {
            throw new Exception("Ocorreu um erro ao buscar as vendas.");
        }
    }

    @Override
    public ResponseVenda createVenda(VendaDTO dto) throws Exception {
        return null;
    }

    @Override
    public ResponseVenda updateVenda(VendaDTO dto, Long id) throws Exception {
        return null;
    }

    @Override
    public void deleteVenda(Long id) throws Exception {

    }

    public ResponseVenda createResponse(Venda venda) {
        ResponseVenda responseVenda = new ResponseVenda();
        ClienteDTO responseCliente = new ClienteDTO();
        List<ResponsePato> responsePatoList = new ArrayList<>();
        List<Pato> patos = venda.getPatos();

        for (Pato pato : patos){

            Optional<Pato> OptionalPato = patoRepository.findPatoById(pato.getId());
            if(OptionalPato.isPresent()) {
                Pato patoList = OptionalPato.get();
                ResponsePato responsePato = new ResponsePato();

                if (patoList.getMae() != null)
                    responsePato.setMae_id(patoList.getMae().getId());

                responsePato.setId(patoList.getId());
                responsePato.setTipo(patoList.getTipo());
                responsePato.setVenda_id(patoList.getVenda().getId());
                responsePato.setValor(patoList.getValor());
                responsePato.setStatus(patoList.getStatus());

                responsePatoList.add(responsePato);
            }
        }

        responseCliente.setId(venda.getCliente().getId());
        responseCliente.setNome(venda.getCliente().getNome());
        responseCliente.setTipo(venda.getTipo_cliente());
        responseCliente.setAtivo(venda.getCliente().getAtivo());

        responseVenda.setId(venda.getId());
        responseVenda.setCliente(responseCliente);
        responseVenda.setValor(venda.getValor());
        responseVenda.setPatos(responsePatoList);

        return responseVenda;
    }
}
