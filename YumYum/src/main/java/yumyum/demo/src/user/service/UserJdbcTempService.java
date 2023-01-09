package yumyum.demo.src.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yumyum.demo.config.BaseException;
import yumyum.demo.config.BaseResponseStatus;
import yumyum.demo.src.community.dto.CommunityMainDto;
import yumyum.demo.src.community.dto.PostScreenDto;
import yumyum.demo.src.user.dto.MyHeartRestaurantDto;
import yumyum.demo.src.user.dto.MyReviewDto;
import yumyum.demo.src.user.repository.UserJdbcTempRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserJdbcTempService {
    private final UserJdbcTempRepository userJdbcTempRepository;

    @Autowired
    public UserJdbcTempService(UserJdbcTempRepository userJdbcTempRepository){this.userJdbcTempRepository = userJdbcTempRepository;}


    public List<CommunityMainDto> getPost(String username) throws BaseException {
        Long user_id = userJdbcTempRepository.findUserIdByUsername(username);

        try{
            List<CommunityMainDto> communityMainDtoList = userJdbcTempRepository.getPost(user_id);

            return communityMainDtoList;
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public List<MyReviewDto> getMyReview(String username) throws BaseException{
        Long user_id = userJdbcTempRepository.findUserIdByUsername(username);

        try{
            List<MyReviewDto> myReviewDtoList = userJdbcTempRepository.getMyReview(user_id);

            return myReviewDtoList;
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public List<MyHeartRestaurantDto> getMyHeartRestaurant(String username) throws BaseException{
        Long user_id = userJdbcTempRepository.findUserIdByUsername(username);

        try{
            List<MyHeartRestaurantDto> myHeartRestaurantDtoList = userJdbcTempRepository.getMyHeartRestaurant(user_id);

            return myHeartRestaurantDtoList;
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }
}
