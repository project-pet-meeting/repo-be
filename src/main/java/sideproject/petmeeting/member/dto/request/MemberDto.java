package sideproject.petmeeting.member.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Getter
public class MemberDto {
    @NotEmpty(message = "빈 값일 수 없습니다.")
    private String nickname;
    @NotEmpty
    private String password;
    @NotEmpty
    private String email;
}
