package yumyum.demo.src.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yumyum.demo.config.BaseException;
import yumyum.demo.src.community.dto.PostDto;
import yumyum.demo.src.community.repository.CommunityRepository;

import static yumyum.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static yumyum.demo.config.BaseResponseStatus.FAILED_TO_UPDATE_POST;

@Service
public class CommunityService {
    private final CommunityRepository communityRepository;

    @Autowired
    public CommunityService(CommunityRepository communityRepository){
        this.communityRepository = communityRepository;
    }

    public void createPost(String username, PostDto postDto) throws BaseException {
       try {
           communityRepository.createPost(username, postDto.getTopic(), postDto.getContents(), postDto.getImgUrl());
       } catch (Exception exception){
           throw new BaseException(DATABASE_ERROR);
       }
    }
    public Long updatePost (String username, Long post_id, PostDto postDto) throws BaseException{

        // 수정하는 게시물의 username이 내 계정과 같은지 검사하기
        if(username != communityRepository.findUsernameByPostId(post_id)){
            throw new BaseException(FAILED_TO_UPDATE_POST);
        }
        try {
            return communityRepository.updatePost(username, post_id, postDto.getTopic(), postDto.getContents(), postDto.getImgUrl());
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
