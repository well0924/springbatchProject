package com.example.batchpractice.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfig(JobBuilderFactory jobBuilderFactory,StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job HelloJob(){
        return jobBuilderFactory.get("Hello Batch!!")
                .incrementer(new RunIdIncrementer())
                .start(this.HelloStep())
                .build();
    }

    @Bean
    public Step HelloStep(){
        return stepBuilderFactory.get("helloStep").tasklet((contribution, chunkContext) -> {
          log.info("hello spirng batch");
          return  RepeatStatus.FINISHED;
        }).build();
    }
}
