package sideproject.petmeeting.member.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class LoginRequestDto {
    @NotNull
    private String email;
    @NotNull
    private String password;

    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
