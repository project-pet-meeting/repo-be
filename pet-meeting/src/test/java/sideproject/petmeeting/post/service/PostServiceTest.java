package sideproject.petmeeting.post.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import sideproject.petmeeting.post.domain.Post;
import sideproject.petmeeting.post.dto.PostRequestDto;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static sideproject.petmeeting.post.domain.Category.RECOMMAND;


class PostServiceTest {

    @Test
    @DisplayName("정상 케이스")
    void createPost()  throws Exception{

        // Given
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .category(RECOMMAND)
                .title("저녁 7시에 같이 강아지 산책해요!")
                .content("공지천에서 강아지 산책하실 분 있으신가용")
                .build();


        // When
        Post post = Post.builder()
                .category(postRequestDto.getCategory())
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .build();


        // Then
        assertNull(post.getId());
        assertEquals(post.getCategory(), postRequestDto.getCategory());


    }

    @Nested
    @DisplayName("게시글 생성 테스트")
    class CreatePost {



//        @Nested
//        @DisplayName("실패 케이스")
//        class FailCreatePost {
//            @Nested
//            @DisplayName("카테고리")
//            class categoryTest {
//                @Test
//                @DisplayName("null")
//                void categoryFail() {
//                    // given
//                    PostRequestDto postRequestDto = PostRequestDto.builder()
//                            .category(null)
//                            .title("저녁 7시에 같이 강아지 산책해요!")
//                            .content("공지천에서 강아지 산책하실 분 있으신가용")
//                            .build();
//
//                    Post post = Post.builder()
//                            .category(postRequestDto.getCategory())
//                            .title(postRequestDto.getTitle())
//                            .content(postRequestDto.getContent())
//                            .build();
//
//
//                    // when
////                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
////                        new Post(postRequestDto);
////                    });
//                    assertStrength(null, handleMethodArgumentNotValidException)
//
//
//                    // then
//                    assertEquals("카테고리를 선택하지 않았습니다.", exception.getMessage());
//                }
            }

}