package com.example.gonggong_server.scrap.application.service;

import com.example.gonggong_server.auth.exception.AuthException;
import com.example.gonggong_server.global.status.ErrorStatus;
import com.example.gonggong_server.scrap.domain.entity.Scrap;
import com.example.gonggong_server.scrap.domain.repository.ScrapRepository;
import com.example.gonggong_server.scrap.exception.ScrapException;
import com.example.gonggong_server.user.domain.entity.User;
import com.example.gonggong_server.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final UserRepository userRepository;
    private final ScrapRepository scrapRepository;

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
}
