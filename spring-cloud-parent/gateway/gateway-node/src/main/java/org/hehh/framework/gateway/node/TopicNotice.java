package org.hehh.framework.gateway.node;


/**
 * @author: HeHui
 * @date: 2020-06-23 09:49
 * @description: topic通知数据
 */
public class TopicNotice implements java.io.Serializable {


    private static final long serialVersionUID = -8942657038385248914L;

    /**
     * 主题名
     */
    private String topic;


    /**
     * 机器序列ID
     */
    private String sequenceId;


    public String getTopic() {
        return topic;
    }


    public String getSequenceId() {
        return sequenceId;
    }
}
