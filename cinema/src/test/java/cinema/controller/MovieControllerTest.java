package cinema.controller;

import cinema.modal.entity.Movie;
import cinema.modal.entity.constant.Genre;
import cinema.modal.entity.constant.Language;
import cinema.modal.entity.constant.StatusMovie;
import cinema.modal.entity.constant.ViewingAge;
import cinema.modal.request.MovieRequest;
import cinema.modal.response.DTO.MovieDTO;
import cinema.service.Movie.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

    @Mock // Sẽ chỉ thực hiện giả lập, không lưu trực tiếp vào db
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

    private Movie movie;
    private MovieRequest movieRequest;

    // Khởi tạo dữ liệu giả lập cho các kiểm thử.
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        movie = new Movie();
        movie.setId(1);
        movie.setName("Inception");
        movie.setActor("Leonardo DiCaprio");
        movie.setDirector("Christopher Nolan");
        movie.setGenre(Genre.ACTION);
        movie.setDuration(LocalTime.of(2, 30));
        movie.setDescription("A mind-bending thriller");
        movie.setLanguage(Language.ENGLISH);
        movie.setTrailer("https://example.com/trailer");
        movie.setStartDate(LocalDate.of(2010, 7, 16));
        movie.setViewingAge(ViewingAge.PG13);
        movie.setRating(8.8f);
        movie.setStatus(StatusMovie.SHOWING);

        movieRequest = new MovieRequest();
        movieRequest.setName("Interstellar");
        movieRequest.setActor("Matthew McConaughey");
    }

    // Kiểm tra chức năng tìm kiếm danh sách các phim.
    @Test
    void testFindMovies() {
        when(movieService.findMovies()).thenReturn(Arrays.asList(movie));

        ResponseEntity<?> response = movieController.findMovies();

        assertEquals(200, response.getStatusCodeValue()); // Kiểm tra mã trạng thái trả về là OK
        assertNotNull(response.getBody());

        List<MovieDTO> movieDTOS = (List<MovieDTO>) response.getBody();
        assertEquals(1, movieDTOS.size()); // Kiểm tra có đúng 1 bộ phim
        assertEquals("Inception", movieDTOS.get(0).getName()); // Kiểm tra tên phim
    }

    // Kiểm tra chức năng tạo phim mới.
    @Test
    void testCreateMovie() {
        when(movieService.createMovie(any(MovieRequest.class))).thenReturn(movie);

        ResponseEntity<?> response = movieController.createMovie(movieRequest);

        assertEquals(201, response.getStatusCodeValue()); // Kiểm tra mã trạng thái trả về là Created
        assertNotNull(response.getBody());

        MovieDTO createdMovie = (MovieDTO) response.getBody();
        assertEquals("Inception", createdMovie.getName()); // Kiểm tra tên phim sau khi tạo
    }

    // Kiểm tra chức năng cập nhật thông tin phim.
    @Test
    void testUpdateMovie() {
        when(movieService.updateMovie(eq(1), any(MovieRequest.class))).thenReturn(movie);

        ResponseEntity<?> response = movieController.updateMovie(1, movieRequest);

        assertEquals(200, response.getStatusCodeValue()); // Kiểm tra mã trạng thái trả về là OK
        assertNotNull(response.getBody());

        MovieDTO updatedMovie = (MovieDTO) response.getBody();
        assertEquals("Inception", updatedMovie.getName()); // Kiểm tra tên phim sau khi cập nhật
    }

    //  Kiểm tra chức năng thay đổi trạng thái phim (Ví dụ: từ đang chiếu sang sắp chiếu).
    @Test
    void testChangeStatus() {
        when(movieService.changeStatus(eq(1), anyString())).thenReturn(movie);

        ResponseEntity<?> response = movieController.changeStatus(1, "COMING_SOON");

        assertEquals(201, response.getStatusCodeValue()); // Kiểm tra mã trạng thái trả về là Created
        assertNotNull(response.getBody());

        MovieDTO movieWithStatusChanged = (MovieDTO) response.getBody();
        assertEquals("Inception", movieWithStatusChanged.getName()); // Kiểm tra tên phim sau khi thay đổi trạng thái
    }

    // Kiểm tra chức năng tìm kiếm phim theo ID.
    @Test
    void testFindById() {
        when(movieService.findById(eq(1))).thenReturn(movie);

        ResponseEntity<?> response = movieController.findById(1);

        assertEquals(200, response.getStatusCodeValue()); // Kiểm tra mã trạng thái trả về là OK
        assertNotNull(response.getBody());

        MovieDTO foundMovie = (MovieDTO) response.getBody();
        assertEquals("Inception", foundMovie.getName()); // Kiểm tra tên phim sau khi tìm theo ID
    }
}
