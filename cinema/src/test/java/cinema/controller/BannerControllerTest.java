package cinema.controller;

import cinema.modal.entity.Banner;
import cinema.modal.entity.Movie;
import cinema.modal.entity.constant.StatusBanner;
import cinema.modal.request.BannerRequest;
import cinema.modal.response.DTO.BannerDTO;
import cinema.service.Banner.BannerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BannerControllerTest {

    @Mock
    private BannerService bannerService;

    @InjectMocks
    private BannerController bannerController;

    private Banner banner;
    private Movie movie;

    @BeforeEach
    void setUp() {
        movie = new Movie();
        movie.setId(1);
        movie.setName("Movie Name");

        banner = new Banner();
        banner.setId(1);
        banner.setTitle("Test Banner");
        banner.setDescription("Test Description");
        banner.setImageUrl("http://example.com/image.jpg");
        banner.setMovie(movie);
        banner.setStatus(StatusBanner.ACTIVE);
    }

    // Test cho phương thức lấy tất cả banner
    @Test
    void testFindBanner() {
        // Khi gọi findBanners từ service thì trả về danh sách chứa banner giả
        when(bannerService.findBanners()).thenReturn(Arrays.asList(banner));

        // Gọi controller
        ResponseEntity<?> response = bannerController.findBanner();

        // Kiểm tra mã phản hồi là 200 OK
        assertEquals(200, response.getStatusCodeValue());

        // Body không được null
        assertNotNull(response.getBody());

        // Ép kiểu và kiểm tra nội dung trả về
        List<BannerDTO> banners = (List<BannerDTO>) response.getBody();
        assertEquals(1, banners.size());
        assertEquals("Test Banner", banners.get(0).getBanner_title());
    }

    // Test cho phương thức lấy banner đang active
    @Test
    void testFindActiveBanner() {
        // Giả lập dữ liệu trả về từ service
        when(bannerService.findBanners()).thenReturn(Arrays.asList(banner));

        // Gọi controller
        ResponseEntity<?> response = bannerController.findActiveBanner();

        // Kiểm tra mã phản hồi
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        // Kiểm tra dữ liệu trả về
        List<BannerDTO> banners = (List<BannerDTO>) response.getBody();
        assertEquals(1, banners.size());
        assertEquals("Test Banner", banners.get(0).getBanner_title());
    }

    // Test cho phương thức tìm banner theo ID
    @Test
    void testFindBannerById() {
        // Khi gọi findBannerById với id = 1 thì trả về banner
        when(bannerService.findBannerById(1)).thenReturn(banner);

        // Gọi controller
        ResponseEntity<?> response = bannerController.findBannerId(1);

        // Kiểm tra phản hồi
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        // Kiểm tra nội dung banner trả về
        BannerDTO bannerDTO = (BannerDTO) response.getBody();
        assertEquals("Test Banner", bannerDTO.getBanner_title());
    }

    // Test tạo mới banner
    @Test
    void testCreateBanner() {
        // Tạo yêu cầu tạo banner mới
        BannerRequest request = new BannerRequest();
        request.setTitle("New Banner");
        request.setDescription("New Description");
        request.setImageUrl("http://example.com/newimage.jpg");
        request.setMovieId(1);

        // Giả lập khi gọi createBanner thì trả về banner đã setup sẵn
        when(bannerService.createBanner(any(BannerRequest.class))).thenReturn(banner);

        // Gọi controller
        ResponseEntity<?> response = bannerController.createBanner(request);

        // Kiểm tra phản hồi
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        // Kiểm tra nội dung trả về
        BannerDTO bannerDTO = (BannerDTO) response.getBody();
        assertEquals("Test Banner", bannerDTO.getBanner_title());
    }

    // Test cập nhật banner
    @Test
    void testUpdateBanner() {
        // Tạo yêu cầu cập nhật banner
        BannerRequest request = new BannerRequest();
        request.setTitle("Updated Banner");
        request.setDescription("Updated Description");
        request.setImageUrl("http://example.com/updatedimage.jpg");
        request.setMovieId(1);

        // Giả lập khi gọi updateBanner thì trả về banner đã setup
        when(bannerService.updateBanner(eq(1), any(BannerRequest.class))).thenReturn(banner);

        // Gọi controller
        ResponseEntity<?> response = bannerController.updateBanner(1, request);

        // Kiểm tra phản hồi
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        // Kiểm tra nội dung trả về
        BannerDTO bannerDTO = (BannerDTO) response.getBody();
        assertEquals("Test Banner", bannerDTO.getBanner_title());
    }
}
