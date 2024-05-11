package alefe.alves.apiduck.interfaces;

import alefe.alves.apiduck.dtos.PatoDTO;
import alefe.alves.apiduck.dtos.PatoUpdateDTO;
import alefe.alves.apiduck.dtos.ResponsePato;
import alefe.alves.apiduck.models.pato.Pato;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface PatoInterface {
    public PatoDTO findPatoById(Long id) throws Exception;
    public List<ResponsePato> getAllPatos() throws Exception;
    public PatoDTO createPato(PatoDTO dto) throws Exception;
    public PatoUpdateDTO updatePato(PatoUpdateDTO dto, Long id) throws Exception;
    public void deletePato (Long id) throws Exception;
    public void savePato (Pato pato) throws Exception;
    public void exportToPdf(HttpServletResponse response) throws Exception;
}
