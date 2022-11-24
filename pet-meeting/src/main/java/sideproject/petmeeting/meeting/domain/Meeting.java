package sideproject.petmeeting.meeting.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String address;

    @NotEmpty
    private String coordinateX;

    @NotEmpty
    private String coodinateY;

    @NotEmpty
    private String placeName;

    @NotEmpty
    private String time;

    @NotEmpty
    private Integer recruitNum;

    @NotEmpty
    private String species;


}
