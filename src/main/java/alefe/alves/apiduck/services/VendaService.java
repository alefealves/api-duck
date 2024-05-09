package alefe.alves.apiduck.services;

import alefe.alves.apiduck.dtos.ClienteDTO;
import alefe.alves.apiduck.dtos.ResponsePato;
import alefe.alves.apiduck.dtos.ResponseVenda;
import alefe.alves.apiduck.dtos.VendaDTO;
import alefe.alves.apiduck.enums.StatusPato;
import alefe.alves.apiduck.enums.TipoCliente;
import alefe.alves.apiduck.exceptions.ClienteNotFoundException;
import alefe.alves.apiduck.exceptions.PatoNotFoundException;
import alefe.alves.apiduck.exceptions.VendaNotFoundException;
import alefe.alves.apiduck.interfaces.VendaInterface;
import alefe.alves.apiduck.models.cliente.Cliente;
import alefe.alves.apiduck.models.pato.Pato;
import alefe.alves.apiduck.models.venda.LongPato;
import alefe.alves.apiduck.models.venda.Venda;
import alefe.alves.apiduck.repositories.ClienteRepository;
import alefe.alves.apiduck.repositories.PatoRepository;
import alefe.alves.apiduck.repositories.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        BigDecimal soma = BigDecimal.ZERO;
        List<LongPato> patosIds;
        List<Pato> patos = new ArrayList<>();
        Optional<Cliente> OptionalCliente = clienteRepository.findClienteById(dto.getClienteId());

        if(OptionalCliente.isPresent()) {

            Cliente cliente = OptionalCliente.get();
            if (!cliente.getAtivo()){
                throw new Exception("O cliente "+cliente.getNome()+" está inativo.");
            }

            dto.setCliente(cliente);
            dto.setTipo_cliente(cliente.getTipo());

            patosIds = dto.getPatosIds();
            for (LongPato patosList : patosIds) {
                Optional<Pato> OptionalPato = patoRepository.findPatoById(patosList.id());
                if(OptionalPato.isPresent()) {
                    Pato pato = OptionalPato.get();
                    if (pato.getStatus() == StatusPato.DISPONIVEL){
                        soma = soma.add(pato.getValor());
                        patos.add(pato);
                    } else {
                        throw new Exception("Pato do id "+pato.getId()+" já esta vendido.");
                    }
                } else {
                    throw new PatoNotFoundException("Pato de id "+patosList.id()+" não foi encontrado.");
                }
            }

            dto.setPatos(patos);
            dto.setValor(soma);

            if (dto.getTipo_cliente() == TipoCliente.COM_DESCONTO)
                dto.setValor(dto.getValor().subtract(dto.getValor().multiply(BigDecimal.valueOf(0.2))));

            Venda newVenda = new Venda(dto);
            saveVenda(newVenda);

            for (LongPato patosList : patosIds) {
                Optional<Pato> OptionalPato = patoRepository.findPatoById(patosList.id());
                if(OptionalPato.isPresent()) {
                    Pato pato = OptionalPato.get();
                    pato.setVenda(newVenda);
                    pato.setStatus(StatusPato.VENDIDO);
                } else {
                    throw new PatoNotFoundException("Pato de id "+patosList.id()+" não foi encontrado.");
                }
            }

            return createResponse(newVenda);
        } else {
            throw new ClienteNotFoundException("Cliente de id "+dto.getClienteId()+" não foi encontrado.");
        }
    }

    @Override
    public ResponseVenda updateVenda(VendaDTO dto, Long id) throws Exception {
        BigDecimal soma = BigDecimal.ZERO;
        List<LongPato> patosIds;
        List<Pato> patos = new ArrayList<>();

        Optional<Venda> optionalVenda = this.repository.findById(id);
        if(optionalVenda.isPresent()){
            Venda venda = optionalVenda.get();

            if (dto.getClienteId() != null) {
                Optional<Cliente> OptionalCliente = clienteRepository.findClienteById(dto.getClienteId());
                if(OptionalCliente.isPresent()) {
                    Cliente cliente = OptionalCliente.get();
                    if (!cliente.getAtivo()){
                        throw new Exception("O cliente "+cliente.getNome()+" está inativo.");
                    }

                    venda.setCliente(cliente);
                    venda.setTipo_cliente(cliente.getTipo());

                    if (cliente.getTipo() == TipoCliente.COM_DESCONTO)
                        venda.setValor(venda.getValor().subtract(venda.getValor().multiply(BigDecimal.valueOf(0.2))));
                } else {
                    throw new ClienteNotFoundException("Cliente de id "+dto.getClienteId()+" não foi encontrado.");
                }
            }

            if (dto.getPatosIds() != null) {

                patosIds = dto.getPatosIds();
                for (LongPato idPato : patosIds) {
                    Optional<Pato> OptionalPato = patoRepository.findPatoById(idPato.id());
                    if(OptionalPato.isPresent()) {
                        Pato pato = OptionalPato.get();

                        if (pato.getStatus() == StatusPato.DISPONIVEL) {
                            soma = soma.add(pato.getValor());
                            patos.add(pato);
                        } else {
                            throw new Exception("Pato do id "+pato.getId()+" já está vendido.");
                        }
                    } else {
                        throw new PatoNotFoundException("Pato de id "+idPato.id()+" não foi encontrado.");
                    }
                }
                // desvincula os patos que estavam vinculados a venda e muda status para DISPONIVEL novamente
                List<Pato> patosOld = venda.getPatos();
                for (Pato patoOld: patosOld){
                    patoOld.setVenda(null);
                    patoOld.setStatus(StatusPato.DISPONIVEL);
                }

                venda.setPatos(patos);
                venda.setValor(soma);

                if (venda.getTipo_cliente() == TipoCliente.COM_DESCONTO)
                    venda.setValor(venda.getValor().subtract(venda.getValor().multiply(BigDecimal.valueOf(0.2))));

                // vincula e novos patos para a venda e muda status para VENDIDO
                for (LongPato idPato : patosIds) {
                    Optional<Pato> OptionalPato = patoRepository.findPatoById(idPato.id());
                    if(OptionalPato.isPresent()) {
                        Pato pato = OptionalPato.get();
                        pato.setVenda(venda);
                        pato.setStatus(StatusPato.VENDIDO);
                    } else {
                        throw new PatoNotFoundException("Pato de id "+idPato.id()+" não foi encontrado.");
                    }
                }
            }

            return createResponse(venda);
        } else {
            throw new VendaNotFoundException("Venda de id "+id+" não foi encontrada.");
        }
    }

    @Override
    public void deleteVenda(Long id) throws Exception {
        Optional<Venda> optionalVenda = this.repository.findById(id);
        if(optionalVenda.isPresent()){
            Venda venda = optionalVenda.get();
            venda.setAtivo(false);
        } else {
            throw new VendaNotFoundException("Venda de id "+id+" não foi encontrada.");
        }
    }

    @Override
    public void saveVenda(Venda venda) throws Exception{
        try {
            this.repository.save(venda);
        }catch (Exception e){
            throw new Exception("Erro ao salvar a Venda.");
        }
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
