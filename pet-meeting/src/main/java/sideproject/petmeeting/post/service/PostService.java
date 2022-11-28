package sideproject.petmeeting.post.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sideproject.petmeeting.common.exception.BusinessException;
import sideproject.petmeeting.common.exception.ErrorCode;
import sideproject.petmeeting.common.S3Uploader;
import sideproject.petmeeting.post.Repository.PostRepository;
import sideproject.petmeeting.post.domain.Post;
import sideproject.petmeeting.post.dto.PostRequestDto;
import sideproject.petmeeting.post.dto.PostResponseDto;

import java.io.IOException;


@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final S3Uploader s3Uploader;

    /**
     * 게시글 작성
     * @param postRequestDto : 게시글 작성에 필요한 값
     */
    @Transactional
    public Post createPost(PostRequestDto postRequestDto, MultipartFile image) throws IOException {
        String imageUrl = s3Uploader.upload(image, "/post/image");

        Post post = Post.builder()
                .category(postRequestDto.getCategory())
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .imageUrl(imageUrl)
                .build();

        return postRepository.save(post);

    }


    /**
     * 게시글 전체 조회
     * @param pageable :
     * @return
     */
    @Transactional(readOnly = true)
    public Page<PostResponseDto> getAllPost(Pageable pageable) {
        return postRepository.findAllByOrderByModifiedAtDesc(pageable);

    }

    /**
     * 게시글 단건 조회
     * @param postId : 조회할 게시글 id
     * @return : 조회할 게시글
     */
    @Transactional
    public Post getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new BusinessException("존재하지 않는 게시글 id 입니다.", ErrorCode.POST_NOT_EXIST)
        );
        return post;
    }

    /**
     * 게시글 수정
     * @param postId : 수정할 게시글 id
     * @param postRequestDto : 수정할 게시글
     * @param image : 수정할 이미지 파일
     * @return
     * @throws IOException
     */
    @Transactional
    public Post updatePost(Long postId, PostRequestDto postRequestDto, MultipartFile image) throws IOException {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new BusinessException("존재하지 않는 게시글 id 입니다.", ErrorCode.POST_NOT_EXIST)
        );

        String imageUrl = post.getImageUrl();

        // 이미지 존재 시 삭제 후 업로드
        if (imageUrl != null) {
            s3Uploader.deleteImage(imageUrl);
        }

        imageUrl = s3Uploader.upload(image, "/post/image");
        post.update(postRequestDto, imageUrl);

        return post;
    }



    /**
     * 게시글 삭제
     * @param postId : 삭제할 게시글 id
     * @throws IOException : 삭제할 게시글의 image 파일명 인코딩 예외 처리, UnsupportedEncodingException
     */
    @Transactional
    public void postDelete(Long postId) throws IOException{
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new BusinessException("존재하지 않는 게시글 id 입니다.", ErrorCode.POST_NOT_EXIST)
        );

        String imageUrl = post.getImageUrl();

        if (imageUrl != null) {
            s3Uploader.deleteImage(imageUrl);
        }

        postRepository.deleteById(postId);

    }

}