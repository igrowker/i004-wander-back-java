package com.igrowker.wander.serviceImplTest;

import com.igrowker.wander.dto.review.RequestReviewDto;
import com.igrowker.wander.dto.review.ResponseReviewDto;
import com.igrowker.wander.entity.ExperienceEntity;
import com.igrowker.wander.entity.ReviewEntity;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.exception.InvalidUserCredentialsException;
import com.igrowker.wander.exception.ResourceNotFoundException;
import com.igrowker.wander.repository.ExperienceRepository;
import com.igrowker.wander.repository.ReviewRepository;
import com.igrowker.wander.serviceimpl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class ReviewServiceImplTest {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ExperienceRepository experienceRepository;

    @Mock
    private User mockUser;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(null); // Optional, depending on your setup
    }

    @Test
    void testGetReviewsByExperience() {
        String experienceId = "exp123";
        List<ReviewEntity> reviews = new ArrayList<>();
        ReviewEntity review = new ReviewEntity();
        review.setId("1");
        review.setUserId("user123");
        review.setRating(5.0);
        review.setComment("Great experience!");
        reviews.add(review);

        Mockito.when(reviewRepository.findByExperienceIdOrderByCreatedAtDesc(experienceId)).thenReturn(reviews);

        List<ResponseReviewDto> response = reviewService.getReviewsByExperience(experienceId);
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("user123", response.get(0).getUserId());
        assertEquals(5.0, response.get(0).getRating());
    }

    @Test
    void testGetReviewsByExperienceWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> reviewService.getReviewsByExperience(null));
        assertThrows(IllegalArgumentException.class, () -> reviewService.getReviewsByExperience(""));
    }

    @Test
    void testAddReview() {
        // Crear DTO para la reseña
        RequestReviewDto reviewDto = new RequestReviewDto();
        reviewDto.setExperienceId("exp123");
        reviewDto.setRating(4.5);
        reviewDto.setComment("Nice experience!");

        // Crear entidad de experiencia
        ExperienceEntity experience = new ExperienceEntity();
        experience.setId("exp123");

        // Crear la reseña guardada
        ReviewEntity savedReview = new ReviewEntity();
        savedReview.setId("1");
        savedReview.setExperienceId("exp123");
        savedReview.setRating(4.5);
        savedReview.setComment("Nice experience!");
        savedReview.setUserId("user123");
        savedReview.setCreatedAt(new Date());

        // Simular un usuario autenticado
        User mockUser = new User();
        mockUser.setId("user123");
        mockUser.setName("John Wilson");
        mockUser.setAvatar("avatar_url");

        Authentication authentication = new UsernamePasswordAuthenticationToken(mockUser, null);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Configurar los mocks para el repositorio
        Mockito.when(experienceRepository.findById(reviewDto.getExperienceId())).thenReturn(Optional.of(experience));
        Mockito.when(reviewRepository.save(any(ReviewEntity.class))).thenReturn(savedReview);

        // Llamar al método bajo prueba
        ReviewEntity result = reviewService.addReview(reviewDto);

        // Verificar el resultado
        assertNotNull(result);
        assertEquals("exp123", result.getExperienceId());
        assertEquals(4.5, result.getRating());
        assertEquals("Nice experience!", result.getComment());
    }

    @Test
    void testAddReviewWhenExperienceNotFound() {
        RequestReviewDto reviewDto = new RequestReviewDto();
        reviewDto.setExperienceId("nonexistent_exp");

        Mockito.when(experienceRepository.findById(reviewDto.getExperienceId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> reviewService.addReview(reviewDto));
    }

    @Test
    void testDeleteReview() {
        String reviewId = "1";
        ReviewEntity review = new ReviewEntity();
        review.setId(reviewId);
        review.setUserId("user123");

        User mockUser = new User();
        mockUser.setId("user123");
        mockUser.setRole("USER");

        // Configuramos el contexto de seguridad
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(mockUser, null, mockUser.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        Mockito.doNothing().when(reviewRepository).delete(review);

        ResponseReviewDto response = reviewService.deleteReview(reviewId);

        assertNotNull(response);
        assertEquals("user123", response.getUserId());
        Mockito.verify(reviewRepository).delete(review);
    }

    @Test
    void testDeleteReviewWhenNotOwner() {
        String reviewId = "1";
        ReviewEntity review = new ReviewEntity();
        review.setId(reviewId);
        review.setUserId("user123");

        User mockUser = new User();
        mockUser.setId("user456");
        mockUser.setRole("USER");

        // Configurar el mock de SecurityContext y Authentication
        SecurityContext mockSecurityContext = Mockito.mock(SecurityContext.class);
        Authentication mockAuthentication = Mockito.mock(Authentication.class);

        Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        Mockito.when(mockAuthentication.getPrincipal()).thenReturn(mockUser);

        SecurityContextHolder.setContext(mockSecurityContext);

        // Configurar el mock del repositorio
        Mockito.when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        // Verificar el comportamiento
        assertThrows(IllegalArgumentException.class, () -> reviewService.deleteReview(reviewId));
    }

    @Test
    void testDeleteReviewWhenProviderRole() {
        String reviewId = "1";
        ReviewEntity review = new ReviewEntity();
        review.setId(reviewId);
        review.setUserId("user123");

        User mockUser = new User();
        mockUser.setId("user123");
        mockUser.setRole("PROVIDER");

        // Mock the SecurityContext and Authentication
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext); // Set the mocked SecurityContext

        Mockito.when(authentication.getPrincipal()).thenReturn(mockUser); // Mock the principal

        // Mock the review repository
        Mockito.when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        assertThrows(InvalidUserCredentialsException.class, () -> reviewService.deleteReview(reviewId));
    }

    @Test
    void testDeleteReviewWhenNotFound() {
        String reviewId = "1";

        Mockito.when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.deleteReview(reviewId));
    }
}
