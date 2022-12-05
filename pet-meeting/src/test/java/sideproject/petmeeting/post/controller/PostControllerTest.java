package sideproject.petmeeting.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sideproject.petmeeting.member.domain.Member;
import sideproject.petmeeting.post.Repository.PostRepository;
import sideproject.petmeeting.post.TestMemberDetailsService;
import sideproject.petmeeting.post.domain.Post;
import sideproject.petmeeting.post.dto.PostRequestDto;
import sideproject.petmeeting.post.service.PostService;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static sideproject.petmeeting.post.domain.Category.RECOMMAND;

@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@SpringBootTest
@AutoConfigureMockMvc // MockMvc 자동 설정
@ActiveProfiles(profiles = "credential") // 스프링 프로필 명시
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostService postService;


    @Autowired
    private PostRepository postRepository;

// @Before
// public void setup() {
//     mockMvc = MockMvcBuilders
//             .webAppContextSetup(context)
//             .apply(springSecurity())
//             .build();
//     Member member = Member.builder()
//             .memberEntityId(EntityId.of(Member.class, 1L))
//             .nickname(TestMemberDetailsService.USERNAME)
//             .email(TestMemberDetailsService.USERNAME)
//             .password("test")
//             .role(Role.User)
//             .provider(null)
//             .provider(null)
//             .build();
//     memberRepository.save(member);
//
// }




    @Test
    @DisplayName("정상적인 값이 들어 왔을 때 허용")
    @WithMockUser(username = "nickname")
    public void createPost() throws Exception {

        // Given
        // PostRequestDto
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .category(RECOMMAND)
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // 이미지 업로드, MockMultipartFile 을  MultipartFile 인터페이스를 상속받아 mock 구현
        String fileName = "jjang";
        String contentType = "png";
        String filePath = "src/test/resources/testImage/" + fileName + "." + contentType;
        FileInputStream fileInputStream = new FileInputStream(filePath);

        // Mock 파일 생성
        MockMultipartFile image = new MockMultipartFile(
                "image",
                fileName + "." + contentType,
                contentType,
                fileInputStream);

        String postRequestDtoJson = objectMapper.writeValueAsString(postRequestDto);
        MockMultipartFile data = new MockMultipartFile(
                "data",
                "data",
                "application/json",
                postRequestDtoJson.getBytes(StandardCharsets.UTF_8));

        // When & Then
        mockMvc.perform(
                        multipart("/api/post")
                                .file(data)
                                .file(image)
//                                .contentType("multipart/mixed")
//                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated());

        System.out.println(data);
        System.out.println("[originalFileName=" + image.getOriginalFilename() + "]");

    }






//    @Test
//    @DisplayName("DB에 값 저장")
//    public void insertPost() throws Exception {
//        // Given
//        PostRequestDto postRequestDto = PostRequestDto.builder()
//                .category(RECOMMAND)
//                .title("제목입니다.")
//                .content("내용입니다.")
//                .build();
//
//        String json = objectMapper.writeValueAsString(postRequestDto);
//
//        // When
//        // perform() - 요청을 전송하는 역할, 결과로 ResultActions 객체를 받고, 이 객체에서 리턴값을 검증하고 확인할 수 있는 andExpect()메소드를 제공함.
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/meeting")
//                    .contentType(APPLICATION_JSON)
//                    .content(json))
//                .andExpect(MockMvcResultMatchers.status().isOk()) // 상태코드: isOk()는 200
//                .andExpect()
//                .andDo(print());
//
//        // Then
//        assertEquals(1L, postRepository.count());
//        Post post = postRepository.findAll().get(0);
//        assertEquals(RECOMMAND, post.getCategory());
//        assertEquals("제목입니다.", post.getTitle());
//        assertEquals("내용입니다.", post.getContent());
//
//    }

//    @Test
//    @DisplayName("GET : 전체 게시글 조회")
//    void getPosts() throws Exception {
//        mockMvc.perform(get("/api/post"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("api/post"));
//    }
//
//    @Test
//    @DisplayName("GET : 검색 게시글 조회")
//    @WithMockCustomMember
//    void getSearchPosts() throws Exception {
//        mockMvc.perform(get("/api/search"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("api/post"));
//    }

//    @Test
//    public void helloDto가_리턴됨() throws Exception {
//        String name = "hello";
//        int amount = 1000;
//
//        mvc.perform(
//                        get("/dto")
//                                // param -> api테스트할때 사용될 요청 파라미터를 설정한다. (단 값은 string만 허용되므로 숫자나 날짜는 문자열로 변공후 사용)
//                                .param("name", name)
//                                .param("amount", String.valueOf(amount)))
//                .andExpect(status().isOk())
//                //jsonPath -> JSON응답값을 필드별로 검증할 수 있는 메소드
//                // $를 기준으로 필드명을 명시한다.
//                .andExpect(jsonPath("$.name", is(name)))
//                .andExpect(jsonPath("$.amount", is(amount)));
//    }

//    @Test
//    public void home이_리턴됨() throws Exception {
//        String home = "home";
//
//        // perform() - 요청을 전송하는 역할, 결과로 ResultActions객체를 받고, 이 객체에서 리턴값을 검증하고 확인할 수 있는 andExcpect()메소드를 제공함.
//        mvc.perform(get("/"))
//                //상태코드 -> isOk()는 상태코드 200
//                .andExpect(status().isOk())
//                // 응답본문의 내용을 검증함 -> Controller에서 'home'을 리턴하기 때문에 이값이 맞는지 검증한다.
//                .andExpect(content().string(home));
//    }





}