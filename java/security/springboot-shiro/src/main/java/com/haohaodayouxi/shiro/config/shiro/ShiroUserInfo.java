package com.haohaodayouxi.shiro.config.shiro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * Shiro管理的用户对象
 *
 * @author TONE
 * @date 2024/8/26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShiroUserInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = -3131778490613692266L;

    private Long id;
    private String code;
    private String loginName;
    private String pwd;
    private String userName;
    private Set<String> roles;
    private Set<String> permissions;

}
