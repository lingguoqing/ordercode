package com.ling.generalsystem.advisor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.model.MessageAggregator;
import reactor.core.publisher.Flux;

@Slf4j
public class MyCustomSimpleLoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    private static final Logger logger = LoggerFactory.getLogger(SimpleLoggerAdvisor.class);

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 值越高，优先级越低
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 非流式的
     *
     * @param advisedRequest
     * @param chain
     * @return
     */
    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {

        logger.debug("BEFORE: {}", advisedRequest);
        AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);

        logger.debug("AFTER: {}", advisedResponse);

        return advisedResponse;
    }

    /**
     * 流式
     *
     * @param advisedRequest
     * @param chain
     * @return
     */
    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {

        logger.debug("stream BEFORE: {}", advisedRequest);

        Flux<AdvisedResponse> advisedResponses = chain.nextAroundStream(advisedRequest);

        return new MessageAggregator().aggregateAdvisedResponse(advisedResponses,
                advisedResponse -> logger.debug("AFTER: {}", advisedResponse));
    }
}