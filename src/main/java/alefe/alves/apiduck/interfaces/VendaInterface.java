package alefe.alves.apiduck.interfaces;

import alefe.alves.apiduck.dtos.ResponseVenda;
import alefe.alves.apiduck.dtos.VendaDTO;

import java.util.List;

public interface VendaInterface {
    public ResponseVenda findVendaById(Long id) throws Exception;
    public List<ResponseVenda> getAllVendas() throws Exception;
    public ResponseVenda createVenda(VendaDTO dto) throws Exception;
    public ResponseVenda updateVenda(VendaDTO dto, Long id) throws Exception;
    public void deleteVenda (Long id) throws Exception;
}
