package yumyum.demo.src.user.service;

import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yumyum.demo.config.BaseException;
import yumyum.demo.config.BaseResponseStatus;
import yumyum.demo.config.Status;
import yumyum.demo.src.community.dto.CommunityMainDto;
import yumyum.demo.src.restaurant.entity.ReviewEntity;
import yumyum.demo.src.restaurant.entity.ReviewImgEntity;
import yumyum.demo.src.restaurant.repository.ReviewRepository;
import yumyum.demo.src.user.dto.*;
import yumyum.demo.src.user.entity.UserEntity;
import yumyum.demo.src.user.repository.UserJdbcTempRepository;
import yumyum.demo.src.user.repository.UserRepository;

import java.util.List;

import static yumyum.demo.config.BaseResponseStatus.*;
import static yumyum.demo.utils.ConvertUtil.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final UserJdbcTempRepository userJdbcTempRepository;

    public GetUserProfileDto getUserProfile(Long userId) throws BaseException {
        UserEntity foundUserEntity = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        return new GetUserProfileDto(
                foundUserEntity.getId(),
                foundUserEntity.getEmail(),
                foundUserEntity.getProfileImgUrl(),
                foundUserEntity.getNickName(),
                foundUserEntity.getAge(),
                foundUserEntity.getGender());
    }

    public void updateUserProfile(Long userId, UpdateUserProfileDto userProfileDto) throws BaseException {
        UserEntity foundUserEntity = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        //이 전 닉네임과 다른 닉네임으로 바꿀때는 중복 검사 진행
        if (!foundUserEntity.getNickName().equals(userProfileDto.getNickName())) {
            checkNickName(userProfileDto.getNickName());
        }
        //이 전 닉네임으로 그대로 바꿀때는 중복 검사하지 않음
        foundUserEntity.updateUserProfile(
                userProfileDto.getProfileImgUrl(),
                userProfileDto.getNickName(),
                userProfileDto.getAge(),
                userProfileDto.getGender());
        userRepository.save(foundUserEntity);
    }

    public void checkNickName(String nickName) throws BaseException {
        if(userRepository.findUserEntityByNickNameAndStatus(nickName, Status.ACTIVE).isPresent()) {
            throw new BaseException(DUPLICATED_NICKNAME);
        }
    }

    /*public List<CommunityMainDto> getPost(Long userId) throws BaseException {
        try{
            return userJdbcTempRepository.getPost(userId);
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }*/

    public List<MyReviewDto> getMyReview(Long userId) throws BaseException{
        UserEntity foundUserEntity = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_ACTIVATED_USER));

        List<ReviewEntity> reviewEntityList = reviewRepository.findAllByUserAndStatus(foundUserEntity, Status.ACTIVE);

        List<MyReviewDto> result = new ArrayList<>();

        for (ReviewEntity reviewEntity : reviewEntityList) {
            List<String> imageUrlList = new ArrayList<>();

            if (!reviewEntity.getReviewImgEntities().isEmpty()) {
                for (ReviewImgEntity reviewImgEntity : reviewEntity.getReviewImgEntities()) {
                    imageUrlList.add(reviewImgEntity.getImgUrl());
                }
            }

            result.add(new MyReviewDto(
                    reviewEntity.getId(),
                    reviewEntity.getContents(),
                    imageUrlList,
                    reviewEntity.getRatingStar(),
                    reviewEntity.getRestaurant().getId(),
                    foundUserEntity.getId(),
                    reviewEntity.getRestaurant().getRestaurantName(),
                    convertCreatedAt(reviewEntity.getCreatedAt())));
        }

        return result;
    }

    public List<MyHeartRestaurantDto> getMyHeartRestaurant(Long userId) throws BaseException{
        try{
            return userJdbcTempRepository.getMyHeartRestaurant(userId);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
