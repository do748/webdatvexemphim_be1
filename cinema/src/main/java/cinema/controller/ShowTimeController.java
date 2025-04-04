package cinema.controller;

import cinema.modal.entity.ShowTime;
import cinema.modal.request.ShowTimeRequest;
import cinema.modal.response.DTO.SeatDTO;
import cinema.modal.response.DTO.ShowTimeDTO;
import cinema.service.ShowTime.ShowTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/showTime")
public class ShowTimeController {
    @Autowired
    private ShowTimeService showTimeService;

    @GetMapping("/findId/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        ShowTime showTime = showTimeService.findByID(id);
        if (showTime == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "text/plain; charset=UTF-8") // ✅ Đảm bảo UTF-8
                    .body("Error: Suất chiếu không tồn tại");
        }
        return ResponseEntity.ok(new ShowTimeDTO(showTime));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseEntity<?> create(@RequestBody ShowTimeRequest showTimeRequest){
        try{
            return new ResponseEntity<>(new ShowTimeDTO(showTimeService.createShowTime(showTimeRequest)), HttpStatus.CREATED);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error "+e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody ShowTimeRequest request){
        try{
            return ResponseEntity.ok(new ShowTimeDTO(showTimeService.updateShowTime(id, request)));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/findMovieAndShowDate/{movieId}")
    public ResponseEntity<?> findMovie(@PathVariable int movieId, @RequestParam String date){
        try {
            List<LocalTime> localTimes = showTimeService.findByMovie(movieId, date);
            List<String> timeDTO = localTimes.stream()
                    .map(time -> time.format(DateTimeFormatter.ofPattern("HH:mm")))
                    .collect(Collectors.toList());
            if (timeDTO.isEmpty()){
                return ResponseEntity.ok("ShowTime is null value");
            }
            return ResponseEntity.ok(timeDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("findSeatRoomByMovieAndStartTime/{movieId}/{startTime}")
    public ResponseEntity<?> findSeatRoomByMovieAndStartTime(@PathVariable int movieId, @PathVariable String startTime){
        try{
            return ResponseEntity.ok(showTimeService.findSeatRoomByMovieAndStartTime(movieId, startTime).stream()
                    .map(SeatDTO::new)
                    .collect(Collectors.toList()));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: "+e.getMessage());
        }
    }
//    @GetMapping("findStartTimeByMovieId/{movieId}")
//    public ResponseEntity<?> findStartTimeByMovieId(@PathVariable int movieId){
//        try{
//            return showTimeService.fin
//        }
//    }
}
