package cinema.controller;

import cinema.modal.request.AccountRequest;
import cinema.modal.request.LoginRequest;
import cinema.modal.response.DTO.AccountDTO;
import cinema.service.Global.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("")
@CrossOrigin
public class GlobalController {
    @Autowired
    private GlobalService globalService;

    @PostMapping("/login/email")
    public ResponseEntity<?> loginByEmail(@RequestBody LoginRequest request){
        try {
            return ResponseEntity.ok(globalService.loginByEmail(request));
        }catch (Exception e){
            return ResponseEntity.status(401).body(Map.of("message", "Thông tin đăng nhập không hợp lệ"));
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AccountRequest request){
        try{
            return new ResponseEntity<>(new AccountDTO(globalService.register(request)), HttpStatus.OK);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of("message", "Error when register"));
        }
    }

    @PostMapping("/uploadImg")
    public ResponseEntity<?> uploadImg(MultipartFile file){
        try{
            return ResponseEntity.ok(globalService.upload(file));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error when upload image"));
        }
    }
}
