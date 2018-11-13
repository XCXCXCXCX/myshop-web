package com.xcxcxcxcx.myshop.dividecenter.event;


/**
 * @author XCXCXCXCX
 * @date 2018/11/12
 * @comments
 */
public class CreateTopicEvent extends DivideTypeEvent{

    private Long topicId;

    private Long delayTime;

    private Long duration;


    public CreateTopicEvent(Object source, Long topicId, Long delayTime, Long duration) {
        super(source);
        this.topicId = topicId;
        this.delayTime = delayTime;
        this.duration = duration;
    }

    public Long getTopicId() {
        return topicId;
    }

    public Long getDelayTime() {
        return delayTime;
    }

    public Long getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "CreateTopicEvent{" +
                "topicId=" + topicId +
                ", delayTime=" + delayTime +
                ", duration=" + duration +
                '}';
    }
}
