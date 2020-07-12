package org.hehh.security.blocklist;

import org.hehh.security.IpRule;
import lombok.Data;

/**
 * @author: HeHui
 * @date: 2020-06-17 11:20
 * @description: 黑名单IP规则
 */
@Data
public class BlockIpRule extends IpRule {

    /**
     *  过期时间
     */
    private long expirationTime;

}
