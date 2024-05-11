package alefe.alves.apiduck.services;

import alefe.alves.apiduck.dtos.PatoDTO;
import alefe.alves.apiduck.dtos.PatoUpdateDTO;
import alefe.alves.apiduck.dtos.RelatorioPato;
import alefe.alves.apiduck.dtos.ResponsePato;
import alefe.alves.apiduck.enums.StatusPato;
import alefe.alves.apiduck.enums.TipoPato;
import alefe.alves.apiduck.exceptions.PatoNotFoundException;
import alefe.alves.apiduck.interfaces.PatoInterface;
import alefe.alves.apiduck.models.cliente.Cliente;
import alefe.alves.apiduck.models.pato.Pato;
import alefe.alves.apiduck.models.venda.Venda;
import alefe.alves.apiduck.repositories.PatoRepository;
import alefe.alves.apiduck.repositories.VendaRepository;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

@Service
public class PatoService implements PatoInterface {

    @Autowired
    private PatoRepository repository;

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public PatoDTO findPatoById(Long id) throws Exception {
        Optional<Pato> optPato = this.repository.findPatoById(id);
        if(optPato.isPresent()){
            Pato pato = optPato.get();
            PatoDTO patoDTO = modelMapper.map(pato, PatoDTO.class);
            if (pato.getMae() != null)
                patoDTO.setMae_id(pato.getMae().getId());
            if (pato.getVenda() != null)
                patoDTO.setVenda_id(pato.getVenda().getId());
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
            if (dto.getMae_id() != null) {
                Pato mae = repository.findPatoById(dto.getMae_id())
                        .orElseThrow(() -> new Exception("ID da pata_mae inválido"));

                mae.setTipo(TipoPato.MAE);
                mae.setValor(BigDecimal.valueOf(50.00));
                dto.setMae(mae);
                dto.setTipo(TipoPato.FILHO);
            } else {
                throw new Exception("Informe o id da Pata Mae");
            }
        }

        Pato newPato = new Pato(dto);
        savePato(newPato);
        PatoDTO patoDTO = modelMapper.map(newPato, PatoDTO.class);
        if (newPato.getMae() != null)
            patoDTO.setMae_id(dto.getMae_id());
        if (newPato.getVenda() != null)
            patoDTO.setVenda_id(newPato.getVenda().getId());

        return patoDTO;
    }

    @Override
    public PatoUpdateDTO updatePato(PatoUpdateDTO dto, Long id) throws Exception {
        Optional<Pato> optPato = this.repository.findById(id);
        if(optPato.isPresent()){
            Pato pato = optPato.get();
            if(dto.getNome() != null) {
                pato.setNome(dto.getNome());
            }

            if (dto.getTipo() != null){
                if ((dto.getTipo() != TipoPato.MAE) && (dto.getTipo() != TipoPato.FILHO)) {
                    throw new Exception("Tipo informado inválido, tipo aceito MAE, FILHO");
                }
                if (dto.getTipo() == TipoPato.MAE) {
                    pato.setValor(BigDecimal.valueOf(50.00));
                }
                pato.setTipo(dto.getTipo());
            }

            if (dto.getMae_id() != null) {
                Pato mae = repository.findPatoById(dto.getMae_id())
                        .orElseThrow(() -> new Exception("ID da pata_mae inválido"));
                pato.setMae(mae);
            }

            if (dto.getValor() != null)
                pato.setValor(dto.getValor());

            if (dto.getStatus() != null) {
                if ((dto.getStatus() != StatusPato.DISPONIVEL) && (dto.getStatus() != StatusPato.VENDIDO)) {
                    throw new Exception("Status informado inválido, status aceito DISPONIVEL, VENDIDO");
                }
                pato.setStatus(dto.getStatus());
            }

            PatoUpdateDTO patoDTO = modelMapper.map(pato, PatoUpdateDTO.class);
            if (pato.getMae() != null)
                patoDTO.setMae_id(pato.getMae().getId());
            if (pato.getVenda() != null)
                patoDTO.setVenda_id(pato.getVenda().getId());

            return patoDTO;
        } else {
            throw new PatoNotFoundException("Pato de id "+id+" não foi encontrado.");
        }
    }

    @Override
    public void deletePato(Long id) throws Exception {
        Optional<Pato> optPato = this.repository.findById(id);
        if(optPato.isPresent()){
            this.repository.deleteById(id);
        } else {
            throw new PatoNotFoundException("Pato de id "+id+" não foi encontrado.");
        }
    }

    public ResponsePato createResponse(Pato pato) throws Exception {
        ResponsePato responsePato = new ResponsePato();

        responsePato.setId(pato.getId());
        responsePato.setNome(pato.getNome());
        responsePato.setTipo(pato.getTipo());
        responsePato.setValor(pato.getValor());
        responsePato.setStatus(pato.getStatus());
        if (pato.getMae() != null)
            responsePato.setMae_id(pato.getMae().getId());
        if (pato.getVenda() != null)
            responsePato.setVenda_id(pato.getVenda().getId());

        return responsePato;
    }

    @Override
    public void savePato(Pato pato) throws Exception{
        try {
            this.repository.save(pato);
        }catch (Exception e){
            throw new Exception("Erro ao salvar o Pato.");
        }
    }

    @Override
    public void exportToPdf(HttpServletResponse response) throws Exception{
        //try {
            List<RelatorioPato> relatorioPatos = new ArrayList<>();
            relatorioPatos = createDadosRelatorio();
            //exportToPdfService.exportToPDF(response, relatorioPatos);

            File file = ResourceUtils.getFile("classpath:relatorio.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(relatorioPatos);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", "Simplifying Tech");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());

        /*} catch (Exception e) {
            throw new Exception("Ocorreu um erro ao exportar o relatório em pdf.");
        }*/
    }

    public List<RelatorioPato> createDadosRelatorio(){
        List<RelatorioPato> relatorioPatos = new ArrayList<>();
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<Pato> patos = this.repository.findAll(sort);

        for (Pato pato : patos){
            //verifica se pato tem filhos
            List<RelatorioPato> patosFilhos = new ArrayList<>();
            Optional<List<Pato>> optFilhos = this.repository.findPatoFilhosByMae(pato);
            if (optFilhos.isPresent()) {
                List<Pato> filhos = optFilhos.get();
                for (Pato filho : filhos) {
                    RelatorioPato patoReportFilho = new RelatorioPato();
                    patoReportFilho.setId(filho.getId());
                    patoReportFilho.setNome(filho.getTipo().toString());
                    patoReportFilho.setStatus(filho.getStatus().toString());

                    Optional<List<Pato>> optFilhos2 = this.repository.findPatoFilhosByMae(filho);
                    if (optFilhos2.isPresent()) {
                        List<RelatorioPato> patosFilhos2 = new ArrayList<>();
                        List<Pato> filhos2 = optFilhos2.get();
                        for (Pato filho2 : filhos2) {
                            RelatorioPato patoReportFilho2 = new RelatorioPato();
                            patoReportFilho2.setId(filho2.getId());
                            patoReportFilho2.setNome(filho2.getTipo().toString());
                            patoReportFilho2.setStatus(filho2.getStatus().toString());

                            if (filho2.getVenda() != null) {
                                Optional<Venda> OptVenda = vendaRepository.findVendaById(filho2.getVenda().getId());
                                if (OptVenda.isPresent()){
                                    Venda venda = OptVenda.get();
                                    patoReportFilho2.setCliente(venda.getCliente().getNome());
                                    patoReportFilho2.setTipo_cliente(venda.getTipo_cliente().toString());
                                    patoReportFilho2.setValor("R$ " + venda.getValor());
                                }
                            }
                            patosFilhos2.add(patoReportFilho2);
                        }
                        patoReportFilho.setFilhos(patosFilhos2);
                    }

                    if (filho.getVenda() != null) {
                        Optional<Venda> OptVenda = vendaRepository.findVendaById(filho.getVenda().getId());
                        if (OptVenda.isPresent()){
                            Venda venda = OptVenda.get();
                            patoReportFilho.setCliente(venda.getCliente().getNome());
                            patoReportFilho.setTipo_cliente(venda.getTipo_cliente().toString());
                            patoReportFilho.setValor("R$ " + venda.getValor());
                        }
                    }

                    patosFilhos.add(patoReportFilho);
                }
            }
            RelatorioPato patoReport = new RelatorioPato();
            patoReport.setFilhos(patosFilhos);
            patoReport.setId(pato.getId());
            patoReport.setNome(pato.getTipo().toString());
            patoReport.setStatus(pato.getStatus().toString());

            if (pato.getVenda() != null) {
                Optional<Venda> OptVenda = vendaRepository.findVendaById(pato.getVenda().getId());
                if (OptVenda.isPresent()){
                    Venda venda = OptVenda.get();
                    patoReport.setCliente(venda.getCliente().getNome());
                    patoReport.setTipo_cliente(venda.getTipo_cliente().toString());
                    patoReport.setValor("R$ " + venda.getValor());
                }
            }
            relatorioPatos.add(patoReport);
        }
        return relatorioPatos;
    }
}
