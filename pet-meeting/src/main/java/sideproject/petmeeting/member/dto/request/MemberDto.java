package sideproject.petmeeting.member.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Builder
@Getter
public class MemberDto {
    @NotEmpty
    private String nickname;
    @NotEmpty
    private String password;
    @NotEmpty
    private String email;
    @NotEmpty
    private String image;
}
