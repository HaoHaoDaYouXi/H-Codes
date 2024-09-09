package com.haohaodayouxi.auth.model.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * DemoId
 *
 * @author TONE
 * @date 2024/8/24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "demo_id")
public class DemoId implements Serializable {
    @Serial
    private static final long serialVersionUID = -9011319580092719841L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
}
