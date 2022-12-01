package sideproject.petmeeting.post.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import sideproject.petmeeting.common.Timestamped;
import sideproject.petmeeting.member.domain.Member;
import sideproject.petmeeting.post.dto.PostRequestDto;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    @NotEmpty
    @Size(max = 2000)
    private String imageUrl;

    @ColumnDefault("0")
    private Integer numHeart;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Member member;
//
//    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
//    private List<ImageFile> photo = new ArrayList<>();


    /**
     * 게시글 수정
     * @param postRequestDto
     * @param imageUrl
     */
    public void update(PostRequestDto postRequestDto, String imageUrl) {
        this.category = postRequestDto.getCategory();
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.imageUrl =imageUrl;
    }

    /**
     * 좋아요 합계 저장
     * @param numHeart
     */
    public void addNumHeart(Integer numHeart) {
        this.numHeart = numHeart;
    }



}
