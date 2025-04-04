package cinema.controller;

import cinema.modal.entity.MoreService;
import cinema.modal.request.MoreServiceRequest;
import cinema.modal.response.DTO.MoreServiceDTO;
import cinema.service.MoreService.MoreServiceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/moreService")
public class MoreServiceController {
    @Autowired
    private MoreServiceService moreServiceService;

    @GetMapping("/find")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseEntity<?> findServices() {
        try{
            List<MoreService> moreServices = moreServiceService.findServices();
            List<MoreServiceDTO> moreServiceDTOS = moreServices.stream()
                    .map(MoreServiceDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(moreServiceDTOS);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/findId/{id}")
    public ResponseEntity<?> findServiceById(@PathVariable int id) {
        try{
            return ResponseEntity.ok(new MoreServiceDTO(moreServiceService.findById(id)));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<?> createService(@RequestBody MoreServiceRequest request) {
        try{
            return new ResponseEntity<>(new MoreServiceDTO(moreServiceService.createService(request)), HttpStatus.CREATED);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("update/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseEntity<?> updateService(@PathVariable int id ,@RequestBody MoreServiceRequest request) {
        try{
            return new ResponseEntity<>(new MoreServiceDTO(moreServiceService.updateService(id, request)), HttpStatus.ACCEPTED);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/changeStatus/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseEntity<?> changeStatus(@PathVariable int id, @RequestParam String status) {
        try{
            return new ResponseEntity<>(new MoreServiceDTO(moreServiceService.changeStatus(id, status)), HttpStatus.ACCEPTED);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/findActive")
    public ResponseEntity<?> serviceActive() {
        try{
            List<MoreServiceDTO> serviceDTOS = moreServiceService.findStatusActive().stream()
                    .map(MoreServiceDTO::new)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(serviceDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error : " + e.getMessage());
        }
    }
}
