package yumyum.demo.src.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yumyum.demo.config.BaseException;
import yumyum.demo.src.community.dto.PostDto;
import yumyum.demo.src.community.repository.CommunityRepository;

@Service
public class CommunityService {
    private final CommunityRepository communityRepository;

    @Autowired
    public CommunityService(CommunityRepository communityRepository){
        this.communityRepository = communityRepository;
    }

    public void createPost(String username, PostDto postDto) throws BaseException {
        communityRepository.createPost(username, postDto.getTopic(), postDto.getContents(), postDto.getImgUrl());
    }

}
