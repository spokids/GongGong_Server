package com.example.gonggong_server.scrap.application.service;

import com.example.gonggong_server.auth.exception.AuthException;
import com.example.gonggong_server.global.status.ErrorStatus;
import com.example.gonggong_server.program.domain.entity.Program;
import com.example.gonggong_server.program.domain.repository.ProgramRepository;
import com.example.gonggong_server.program.exception.ProgramException;
import com.example.gonggong_server.scrap.application.response.ScrapListResponseDTO;
import com.example.gonggong_server.scrap.domain.entity.Scrap;
import com.example.gonggong_server.scrap.domain.repository.ScrapRepository;
import com.example.gonggong_server.scrap.exception.ScrapException;
import com.example.gonggong_server.user.domain.entity.User;
import com.example.gonggong_server.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final UserRepository userRepository;
    private final ScrapRepository scrapRepository;
    private final ProgramRepository programRepository;

    @Transactional
    public void scrapProgram(String userInputId, Long programId) {
        User user = findUserById(userInputId);

        // 이미 스크랩한 프로그램인지 확인
        validateAlreadyScrapped(programId, user);

        Scrap scrap = Scrap.builder()
                .userId(user.getUserId())
                .programId(programId)
                .build();

        scrapRepository.save(scrap);
    }

    private void validateAlreadyScrapped(Long programId, User user) {
        if(scrapRepository.existsByUserIdAndProgramId(user.getUserId(), programId)) {
            throw new ScrapException(ErrorStatus.ALREADY_SCRAPPED);
        }
    }

    private User findUserById(String userInputId) {
        return userRepository.findByUserInputId(userInputId)
                .orElseThrow(() -> new AuthException(ErrorStatus.USER_NOT_FOUND));
    }

    @Transactional
    public void cancelScrapProgram(String userInputId, Long programId) {
        User user = findUserById(userInputId);

        int deletedCount = scrapRepository.deleteByUserIdAndProgramId(user.getUserId(), programId);

        if (deletedCount == 0) {
            throw new ScrapException(ErrorStatus.NOT_SCRAPPED);
        }
    }
    public ScrapListResponseDTO getScrapList(String userInputId, int pageSize, int pageIndex) {
        User user = findUserById(userInputId);

        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<Scrap> scraps = scrapRepository.findScraps(user.getUserId(), pageable);

        List<ScrapListResponseDTO.ScrapProgramDTO> scrapPrograms = convertScrapsToDTOs(scraps.getContent());

        return buildScrapListResponse(user, scrapPrograms, scraps);
    }

    private List<ScrapListResponseDTO.ScrapProgramDTO> convertScrapsToDTOs(List<Scrap> scraps) {
        return scraps.stream()
                .map(scrap -> {
                    Program program = programRepository.findByProgramId(scrap.getProgramId())
                            .orElseThrow(() -> new ProgramException(ErrorStatus.PROGRAM_NOT_EXIST));

                    return ScrapListResponseDTO.ScrapProgramDTO.of(
                            program.getProgramId(),
                            program.getType(),
                            program.getProgramName(),
                            program.getFacultyName(),
                            program.getStartAge(),
                            program.getEndAge(),
                            program.getProgramStartDate(),
                            program.getProgramEndDate()
                    );
                })
                .toList();
    }

    private ScrapListResponseDTO buildScrapListResponse(User user, List<ScrapListResponseDTO.ScrapProgramDTO> scrapPrograms, Page<Scrap> scraps) {
        return ScrapListResponseDTO.of(
                user.getNickName(),
                user.getUserInputId(),
                scrapPrograms,
                scraps.getTotalPages(),
                scraps.getNumber() + 1 
        );
    }
}
