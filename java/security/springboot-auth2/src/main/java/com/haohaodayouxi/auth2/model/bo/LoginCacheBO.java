package com.haohaodayouxi.auth2.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录缓存对象
 *
 * @author TONE
 * @date 2024/8/29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginCacheBO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2595187542942637434L;
    /**
     * token
     */
    private String token;

    // 用户信息。。。

}
