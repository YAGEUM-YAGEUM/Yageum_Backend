package com.yageum.fintech.domain.post.controller.port;

import com.yageum.fintech.domain.post.controller.response.PostResponse;

import java.util.List;

public interface PostReadService {

    List<PostResponse> readAll(Long projectId, Long userId, String category);

    List<PostResponse> readThree(Long projectId, Long userId, String category);

}
