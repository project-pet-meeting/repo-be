package sideproject.petmeeting.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sideproject.petmeeting.common.Response;
import sideproject.petmeeting.common.ResponseResource;
import sideproject.petmeeting.common.StatusEnum;
import sideproject.petmeeting.post.domain.Post;
import sideproject.petmeeting.post.dto.PostRequestDto;
import sideproject.petmeeting.post.dto.PostResponseDto;
import sideproject.petmeeting.post.service.PostService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api", produces = HAL_JSON_VALUE)
public class PostController {
    private final PostService postService;

    /**
     * 게시글 작성
     * @param postRequestDto : 게시글 작성에 필요한 데이터
     * @param image :
     * @return
     */
    @PostMapping("/post")
    public ResponseEntity createPost(@RequestPart(value = "data") @Valid PostRequestDto postRequestDto,
                                     @RequestPart(value = "image" ,required = false) @Valid MultipartFile image, // @valid 객체 검증 수행
                                     Errors errors, HttpServletResponse httpServletResponse) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        if (errors.hasErrors()) {
            Response response = new Response(StatusEnum.BAD_REQUEST, "다시 시도해 주세요", errors);

            return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
        }

        Post post = postService.createPost(postRequestDto, image);

        PostResponseDto postResponseDto = new PostResponseDto(post);

        ResponseResource responseResource = new ResponseResource(postResponseDto);
        responseResource.add(linkTo(PostController.class).withSelfRel());
        responseResource.add(linkTo(PostController.class).slash(post.getId()).withRel("post-get"));
        responseResource.add(linkTo(PostController.class).slash(post.getId()).withRel("post-edit"));
        responseResource.add(linkTo(PostController.class).slash(post.getId()).withRel("post-delete"));

        Response response = new Response(StatusEnum.CREATED, "게시글 작성 성공", responseResource);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 게시글 전체 조회
     * @return
     */
    @GetMapping("/post")
    public Page<PostResponseDto> getAllPost() {
        Pageable pageRequest = PageRequest.of(0, 15, Sort.by("id").descending());
        return postService.getAllPost(pageRequest);
    }

    /**
     * 게시글 단건 조회
     * @param postId : 조회할 게시글 id
     * @return
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity getPost(@PathVariable Long postId) {
        Post post = postService.getPost(postId);

        PostResponseDto postResponseDto = new PostResponseDto(post);

        ResponseResource responseResource = new ResponseResource(postResponseDto);
        responseResource.add(linkTo(PostController.class).withSelfRel());

        Response response = new Response(StatusEnum.OK, "게시글 조회 성공", responseResource);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 게시글 수정
     * @param postId : 수정할 게시글 id
     * @return
     */
    @PutMapping("/post/{postId}")
    public ResponseEntity updatePost(@PathVariable Long postId,
                                     @RequestPart(value = "data") @Valid PostRequestDto postRequestDto,
                                     @RequestPart(value = "image" ,required = false) @Valid MultipartFile image) throws IOException {
        Post post = postService.updatePost(postId, postRequestDto, image);

        PostResponseDto postResponseDto = new PostResponseDto(post);

        ResponseResource responseResource = new ResponseResource(postResponseDto);
        responseResource.add(linkTo(PostController.class).withSelfRel());

        Response response = new Response(StatusEnum.OK, "게시글 수정 성공", responseResource);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 게시글 삭제
     * @param postId : 삭제할 게시글 id
     * @return
     */
    @DeleteMapping( "/post/{postId}")
    public ResponseEntity deletePost(@PathVariable Long postId) throws IOException {
        postService.postDelete(postId);
        ResponseResource responseResource = new ResponseResource(null);
        responseResource.add(linkTo(PostController.class).withSelfRel());

        Response response = new Response(StatusEnum.OK, "게시글 삭제 성공", responseResource);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
