package com.ssafy.backend.domain.training.service;

import com.ssafy.backend.domain.training.dto.*;
import com.ssafy.backend.domain.training.entity.Training;
import com.ssafy.backend.domain.training.entity.UserTraining;
import com.ssafy.backend.domain.training.repository.TrainingRepository;
import com.ssafy.backend.domain.training.repository.UserTrainingRepository;
import com.ssafy.backend.domain.user.entity.User;
import com.ssafy.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserTrainingServiceImpl implements UserTrainingService {

    private final UserTrainingRepository userTrainingRepository;
    private final UserRepository userRepository;
    private final TrainingRepository trainingRepository;

    // 운동 조회(특정 날짜&회원)
    @Override
    @Transactional
    public UserTrainingDataResponseDto getTrainings(Long userId, int year, int month, int day) {

        LocalDate date = LocalDate.of(year, month, day);
        List<UserTraining> userTrainings = userTrainingRepository.findAllWithUserIdAndDate(userId, date);

        log.info("운동 관리 데이터 받아왔나? {}", userTrainings);

        UserTrainingDataResponseDto responseDto = UserTrainingDataResponseDto
                .builder()
                .date(date)
                .build();

        TreeMap<Integer, UserTrainingResponseDto> userTrainingResponseDtoTreeMap = new TreeMap<>();

        userTrainings.forEach(u -> {

            int index = u.getSequence();

            if (userTrainingResponseDtoTreeMap.get(index) == null) {
                UserTrainingResponseDto userTrainingResponseDto = UserTrainingResponseDto.toDto(u);
                userTrainingResponseDtoTreeMap.put(index, userTrainingResponseDto);
            }

            SetResponseDto setResponseDto = SetResponseDto.toDto(u);

            userTrainingResponseDtoTreeMap.get(index).getSets().add(setResponseDto);

        });

        log.info("TreeMap {}", userTrainingResponseDtoTreeMap);

        Set<Integer> keySet = userTrainingResponseDtoTreeMap.keySet();

        for (Integer key : keySet) {
            responseDto.getUserTrainingList().add(userTrainingResponseDtoTreeMap.get(key));
        }

        userTrainingRepository.flush();

        return responseDto;
    }

    // 운동 추가
    @Override
    @Transactional
    public void saveTrainings(Long userId, int year, int month, int day, List<Long> trainings) {

        log.info("요청 잘 들어오나?");
        List<UserTraining> userTrainingList = new ArrayList<>();
        User user = userRepository.getReferenceById(userId);
        LocalDate date = LocalDate.of(year, month, day);

        List<UserTraining> list = userTrainingRepository.findAllWithUserIdAndDate(userId, date);
        int startIndex = !list.isEmpty() ? 1 + list.get(list.size() - 1).getSequence() : 0;

        for (int i = 0; i < trainings.size(); i++) {
            Training training = trainingRepository.getReferenceById(trainings.get(i));

            UserTraining userTraining = UserTraining
                    .builder()
                    .user(user)
                    .training(training)
                    .date(date)
                    .sequence(startIndex + i)
                    .build();

            userTrainingList.add(userTraining);
        }

        userTrainingRepository.saveAllAndFlush(userTrainingList);
    }

    // 운동 완료 여부 수정(세트별로)
    @Override
    @Transactional
    public void toggle(Long userTrainingId) {
        UserTraining userTraining = userTrainingRepository.findById(userTrainingId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userTrainingId 입니다."));

        userTraining.updateIsFinished();

        userTrainingRepository.flush();
    }

    // kg, count 데이터 수정
    @Override
    @Transactional
    public void updateSet(SetUpdateRequestDto requestDto) {
        userTrainingRepository.updateWithUserTrainingIdAndKgAndCount(requestDto.getUserTrainingId(), requestDto.getKg(), requestDto.getCount());
    }

    // 세트 추가
    @Override
    @Transactional
    public void addSet(SetCreateRequestDto requestDto) {

        log.info("잘 들어왔나? -{}", requestDto);

        Long userId = requestDto.getUserId();
        Long trainingId = requestDto.getTrainingId();
        LocalDate date = requestDto.getDate();

        User user = userRepository.getReferenceById(userId);
        Training training = trainingRepository.getReferenceById(trainingId);

        UserTraining lastUserTraining = userTrainingRepository.findLastOneWithUserIdAndTrainingIdAndDate(userId, trainingId, date);

        UserTraining userTraining = UserTraining
                .builder()
                .user(user)
                .training(training)
                .date(date)
                .sequence(lastUserTraining.getSequence())
                .sets(lastUserTraining.getSets() + 1)
                .build();

        userTrainingRepository.saveAndFlush(userTraining);
    }

    // 세트 삭제
    @Override
    @Transactional
    public void removeSet(SetDeleteRequestDto requestDto) {

        Long userId = requestDto.getUserId();
        Long trainingId = requestDto.getTrainingId();
        LocalDate date = requestDto.getDate();

        UserTraining lastUserTraining = userTrainingRepository.findLastOneWithUserIdAndTrainingIdAndDate(userId, trainingId, date);
        userTrainingRepository.delete(lastUserTraining);
        userTrainingRepository.flush();
    }

    // 운동 삭제
    @Override
    @Transactional
    public void deleteUserTraining(UserTrainingDeleteRequestDto requestDto) {

        Long userId = requestDto.getUserId();
        Long trainingId = requestDto.getTrainingId();
        LocalDate date = requestDto.getDate();

        // 삭제할 운동의 sequence 획득
        UserTraining userTraining = userTrainingRepository.findLastOneWithUserIdAndTrainingIdAndDate(userId, trainingId, date);

        int sequence = userTraining.getSequence();

        // 운동 삭제
        userTrainingRepository.deleteWithUserIdAndTrainingIdAndDate(userId, trainingId, date);

        // 삭제한 운동 다음에 있는 운동들 sequence 1씩 감소
        userTrainingRepository.updateWithUserIdAndDateAndSequence(userId, date, sequence);

    }

    @Override
    @Transactional
    public List<UserTrainingDataResponseDto> getAllTraining(Long userId) {

        List<UserTraining> userTrainings = userTrainingRepository.findAllWithUserId(userId);
        List<UserTrainingDataResponseDto> responseDtoList = new ArrayList<>();
        TreeMap<LocalDate, UserTrainingDataResponseDto> dataResponseDtoTreeMap = new TreeMap<>();
        TreeMap<LocalDate, TreeMap<Integer, UserTrainingResponseDto>> treeMap = new TreeMap<>();

        userTrainings.forEach(u -> {
            LocalDate date = u.getDate();
            if (dataResponseDtoTreeMap.get(date) == null) {
                UserTrainingDataResponseDto responseDto = UserTrainingDataResponseDto
                        .builder()
                        .date(date)
                        .build();
                dataResponseDtoTreeMap.put(date, responseDto);
            }

            treeMap.putIfAbsent(date, new TreeMap<>());

            int index = u.getSequence();

            if (treeMap.get(date).get(index) == null) {
                UserTrainingResponseDto userTrainingResponseDto = UserTrainingResponseDto.toDto(u);
                treeMap.get(date).put(index, userTrainingResponseDto);
            }

            SetResponseDto setResponseDto = SetResponseDto.toDto(u);
            treeMap.get(date).get(index).getSets().add(setResponseDto);
        });

        Set<LocalDate> keySet = treeMap.keySet();

        for (LocalDate key : keySet) {
            Set<Integer> ks = treeMap.get(key).keySet();

            for (Integer k : ks) {
                dataResponseDtoTreeMap.get(key).getUserTrainingList().add(treeMap.get(key).get(k));
            }

            responseDtoList.add(dataResponseDtoTreeMap.get(key));
        }

        userTrainingRepository.flush();

        return responseDtoList;
    }

}
