package sideproject.petmeeting.meeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sideproject.petmeeting.common.S3Uploader;
import sideproject.petmeeting.common.exception.BusinessException;
import sideproject.petmeeting.common.exception.ErrorCode;
import sideproject.petmeeting.meeting.domain.Meeting;
import sideproject.petmeeting.meeting.dto.MeetingRequestDto;
import sideproject.petmeeting.meeting.dto.MeetingResponseDto;
import sideproject.petmeeting.meeting.repository.MeetingRepository;
import sideproject.petmeeting.member.domain.Member;

import java.io.IOException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final S3Uploader s3Uploader;

    /**
     * 모임 생성
     * @param postRequestDto : 모임 작성에 필요한 데이터
     */
    @Transactional
    public Meeting createMeeting(MeetingRequestDto postRequestDto, MultipartFile image, Member member) throws IOException {
        String imageUrl = s3Uploader.upload(image, "/meeting/image");

        Meeting meeting = Meeting.builder()
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .imageUrl(imageUrl)
                .address(postRequestDto.getAddress())
                .coordinateX(postRequestDto.getCoordinateX())
                .coordinateY(postRequestDto.getCoordinateY())
                .placeName(postRequestDto.getPlaceName())
                .time(postRequestDto.getTime())
                .recruitNum(postRequestDto.getRecruitNum())
                .species(postRequestDto.getSpecies())
                .member(member)
                .build();

        return meetingRepository.save(meeting);

    }


    /**
     * 모임 전체 조회
     * @param pageable :
     * @return :
     */
    @Transactional(readOnly = true)
    public Page<Meeting> getAllMeeting(int page) {
        PageRequest pageRequest = PageRequest.of(page, 15, Sort.by("modifiedAt").descending());

         return meetingRepository.findAll(pageRequest);

    }

    /**
     * 모임 단건 조회
     * @param postId : 조회할 모임 id
     * @return : 조회한 모임
     */
    @Transactional
    public Meeting getMeeting(Long postId) {
        Meeting meeting = meetingRepository.findById(postId).orElseThrow(
                () -> new BusinessException("존재하지 않는 모임 id 입니다.", ErrorCode.MEETING_NOT_EXIST)
        );
        return meeting;
    }

    /**
     * 모임 수정
     * @param meetingId : 수정할 모임 id
     * @param meetingRequestDto : 수정할 데이터
     * @param image : 수정할 이미지 파일
     * @return :
     * @throws IOException :
     */
    @Transactional
    public Meeting updateMeeting(Long meetingId, MeetingRequestDto meetingRequestDto, MultipartFile image, Member member) throws IOException {
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(
                () -> new BusinessException("존재하지 않는 모임 id 입니다.", ErrorCode.MEETING_NOT_EXIST)
        );

        if (!meeting.getMember().getId().equals(member.getId())) {
            throw new BusinessException("수정 권한이 없습니다.", ErrorCode.HANDLE_ACCESS_DENIED);
        }

        String imageUrl = meeting.getImageUrl();

        // 이미지 존재 시 삭제 후 업로드
        if (imageUrl != null) {
            s3Uploader.deleteImage(imageUrl);
        }

        imageUrl = s3Uploader.upload(image, "/post/image");
        meeting.update(meetingRequestDto, imageUrl);

        return meeting;
    }



    /**
     * 모임 삭제
     * @param meetingId : 삭제할 모임 id
     * @throws IOException : 삭제할 모임의 image 파일명 인코딩 예외 처리, UnsupportedEncodingException
     */
    @Transactional
    public void meetingDelete(Long meetingId, Member member) throws IOException {
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(
                () -> new BusinessException("존재하지 않는 모임 id 입니다.", ErrorCode.MEETING_NOT_EXIST)
        );

        if (!meeting.getMember().getId().equals(member.getId())) {
            throw new BusinessException("삭제 권한이 없습니다.", ErrorCode.HANDLE_ACCESS_DENIED);
        }

        String imageUrl = meeting.getImageUrl();

        if (imageUrl != null) {
            s3Uploader.deleteImage(imageUrl);
        }

        meetingRepository.deleteById(meetingId);

    }
}
