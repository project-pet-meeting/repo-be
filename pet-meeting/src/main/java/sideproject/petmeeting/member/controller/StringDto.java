package sideproject.petmeeting.member.controller;

import lombok.Getter;

@Getter
public class StringDto {
    private String string;

    public StringDto(String string) {
        this.string = string;
    }
}
