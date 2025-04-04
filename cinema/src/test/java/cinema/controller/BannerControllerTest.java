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

    @Test
    void testFindBanner() {
        when(bannerService.findBanners()).thenReturn(Arrays.asList(banner));

        ResponseEntity<?> response = bannerController.findBanner();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        List<BannerDTO> banners = (List<BannerDTO>) response.getBody();
        assertEquals(1, banners.size());
        assertEquals("Test Banner", banners.get(0).getBanner_title());
    }

    @Test
    void testFindActiveBanner() {
        when(bannerService.findBanners()).thenReturn(Arrays.asList(banner));

        ResponseEntity<?> response = bannerController.findActiveBanner();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        List<BannerDTO> banners = (List<BannerDTO>) response.getBody();
        assertEquals(1, banners.size());
        assertEquals("Test Banner", banners.get(0).getBanner_title());
    }

    @Test
    void testFindBannerById() {
        when(bannerService.findBannerById(1)).thenReturn(banner);

        ResponseEntity<?> response = bannerController.findBannerId(1);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        BannerDTO bannerDTO = (BannerDTO) response.getBody();
        assertEquals("Test Banner", bannerDTO.getBanner_title());
    }

    @Test
    void testCreateBanner() {
        BannerRequest request = new BannerRequest();
        request.setTitle("New Banner");
        request.setDescription("New Description");
        request.setImageUrl("http://example.com/newimage.jpg");
        request.setMovieId(1);

        when(bannerService.createBanner(any(BannerRequest.class))).thenReturn(banner);

        ResponseEntity<?> response = bannerController.createBanner(request);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        BannerDTO bannerDTO = (BannerDTO) response.getBody();
        assertEquals("Test Banner", bannerDTO.getBanner_title());
    }

    @Test
    void testUpdateBanner() {
        BannerRequest request = new BannerRequest();
        request.setTitle("Updated Banner");
        request.setDescription("Updated Description");
        request.setImageUrl("http://example.com/updatedimage.jpg");
        request.setMovieId(1);

        when(bannerService.updateBanner(eq(1), any(BannerRequest.class))).thenReturn(banner);

        ResponseEntity<?> response = bannerController.updateBanner(1, request);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        BannerDTO bannerDTO = (BannerDTO) response.getBody();
        assertEquals("Test Banner", bannerDTO.getBanner_title());
    }
}
