package sideproject.petmeeting.meeting.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sideproject.petmeeting.common.Response;
import sideproject.petmeeting.common.ResponseResource;
import sideproject.petmeeting.common.StatusEnum;
import sideproject.petmeeting.meeting.domain.Meeting;
import sideproject.petmeeting.meeting.dto.MeetingRequestDto;
import sideproject.petmeeting.meeting.dto.MeetingResponseDto;
import sideproject.petmeeting.meeting.service.MeetingService;
import sideproject.petmeeting.security.UserDetailsImpl;

import javax.validation.Valid;
import java.io.IOException;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api", produces = HAL_JSON_VALUE)
public class MeetingController {

    private final MeetingService meetingService;

    /**
     * 모임 생성
     * @param meetingRequestDto : 모임 생성에 필요한 데이터
     * @param image : 모임 생성에 필요한 이미지
     * @return :
     */
    @PostMapping("/meeting")
    public ResponseEntity createPost(@RequestPart(value = "data") @Valid MeetingRequestDto meetingRequestDto, // @valid 객체 검증 수행
                                     @RequestPart(value = "image" ,required = false) @Valid MultipartFile image,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails,
                                     Errors errors) throws IOException {

        if (errors.hasErrors()) {
            Response response = new Response(StatusEnum.BAD_REQUEST, "다시 시도해 주세요", errors);

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Meeting meeting = meetingService.createMeeting(meetingRequestDto, image, userDetails.getMember());

        MeetingResponseDto meetingResponseDto = MeetingResponseDto.builder()
                .id(meeting.getId())
                .title(meeting.getTitle())
                .content(meeting.getContent())
                .imageUrl(meeting.getImageUrl())
                .address(meeting.getAddress())
                .coordinateX(meeting.getCoordinateX())
                .coordinateY(meeting.getCoordinateY())
                .placeName(meeting.getPlaceName())
                .time(meeting.getTime())
                .recruitNum(meeting.getRecruitNum())
                .species(meeting.getSpecies())
                .memberId(meeting.getMember().getId())
                .username(meeting.getMember().getNickname())
                .memberImageUrl(meeting.getMember().getImage())
                .build();


        ResponseResource responseResource = new ResponseResource(meetingResponseDto);
        responseResource.add(linkTo(MeetingController.class).withSelfRel());
        responseResource.add(linkTo(MeetingController.class).slash(meeting.getId()).withRel("meeting-get"));
        responseResource.add(linkTo(MeetingController.class).slash(meeting.getId()).withRel("meeting-edit"));
        responseResource.add(linkTo(MeetingController.class).slash(meeting.getId()).withRel("meeting-delete"));

        Response response = new Response(StatusEnum.CREATED, "모임 생성 성공", responseResource);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 모임 전체 조회
     * @return : 모임 전체 조회
     */
    @GetMapping("/meeting")
    public ResponseEntity getAllMeeting(@RequestParam("page") int page) {
        Page<Meeting> meeting = meetingService.getAllMeeting(page);

        ResponseResource responseResource = new ResponseResource(meeting);
        responseResource.add(linkTo(MeetingController.class).withSelfRel());

        Response response = new Response(StatusEnum.OK, "모임 조회 성공", responseResource);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 모임 단건 조회
     * @param meetingId : 조회할 모임 id
     * @return :
     */
    @GetMapping("/meeting/{meetingId}")
    public ResponseEntity getMeeting(@PathVariable Long meetingId) {
        Meeting meeting = meetingService.getMeeting(meetingId);

        MeetingResponseDto meetingResponseDto = MeetingResponseDto.builder()
                .id(meeting.getId())
                .title(meeting.getTitle())
                .content(meeting.getContent())
                .imageUrl(meeting.getImageUrl())
                .address(meeting.getAddress())
                .coordinateX(meeting.getCoordinateX())
                .coordinateY(meeting.getCoordinateY())
                .placeName(meeting.getPlaceName())
                .time(meeting.getTime())
                .recruitNum(meeting.getRecruitNum())
                .species(meeting.getSpecies())
                .build();

        ResponseResource responseResource = new ResponseResource(meetingResponseDto);
        responseResource.add(linkTo(MeetingController.class).withSelfRel());

        Response response = new Response(StatusEnum.OK, "모임 조회 성공", responseResource);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 모임 수정
     * @param meetingId : 수정할 모임 id
     * @return :
     */
    @PutMapping("/meeting/{meetingId}")
    public ResponseEntity updateMeeting(@PathVariable Long meetingId,
                                     @RequestPart(value = "data") @Valid MeetingRequestDto meetingRequestDto,
                                     @RequestPart(value = "image" ,required = false) @Valid MultipartFile image,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        Meeting meeting = meetingService.updateMeeting(meetingId, meetingRequestDto, image, userDetails.getMember());

        MeetingResponseDto meetingResponseDto = MeetingResponseDto.builder()
                .id(meeting.getId())
                .title(meeting.getTitle())
                .content(meeting.getContent())
                .imageUrl(meeting.getImageUrl())
                .address(meeting.getAddress())
                .coordinateX(meeting.getCoordinateX())
                .coordinateY(meeting.getCoordinateY())
                .placeName(meeting.getPlaceName())
                .time(meeting.getTime())
                .recruitNum(meeting.getRecruitNum())
                .species(meeting.getSpecies())
                .build();

        ResponseResource responseResource = new ResponseResource(meetingResponseDto);
        responseResource.add(linkTo(MeetingController.class).withSelfRel());

        Response response = new Response(StatusEnum.OK, "모임 수정 성공", responseResource);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 모임 삭제
     * @param meetingId : 삭제할 모임 id
     * @return : 삭제 완료 응답
     */
    @DeleteMapping("/meeting/{meetingId}")
    public ResponseEntity deleteMeeting(@PathVariable Long meetingId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        meetingService.meetingDelete(meetingId, userDetails.getMember());
        ResponseResource responseResource = new ResponseResource(null);
        responseResource.add(linkTo(MeetingController.class).withSelfRel());

        Response response = new Response(StatusEnum.OK, "모임 삭제 성공", responseResource);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
