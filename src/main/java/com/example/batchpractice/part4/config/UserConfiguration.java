package com.example.batchpractice.part4.config;

import com.example.batchpractice.part4.domain.BatchMember;
import com.example.batchpractice.part4.repository.UserRepository;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
public class UserConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final UserRepository userRepository;
    private final EntityManagerFactory entityManagerFactory;

    public UserConfiguration(StepBuilderFactory stepBuilderFactory, JobBuilderFactory jobBuilderFactory,
                             UserRepository userRepository,EntityManagerFactory entityManagerFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.jobBuilderFactory = jobBuilderFactory;
        this.userRepository = userRepository;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    public Job userJob() throws Exception {
        return this.jobBuilderFactory
                .get("user job")
                .incrementer(new RunIdIncrementer())
                .start(this.saveUserStep())
                .next(this.userLevelUpStep())
                .listener(new LevelUpJobExcutionListener(userRepository))
                .build();
    }
    @Bean
    public Step saveUserStep() {
        return this.stepBuilderFactory.get("saveUserStep")
                .tasklet(new SaveUserTasklet(userRepository))
                .build();
    }

    @Bean
    public Step userLevelUpStep()throws Exception{
        return this.stepBuilderFactory
                .get("userLevelUpStep")
                .<BatchMember, BatchMember>chunk(100)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }
    private ItemWriter<?super BatchMember>itemWriter(){
        return users -> users.forEach(x->{
            x.levelUp();
            userRepository.save(x);
        });
    }
    private ItemProcessor<? super BatchMember,? extends BatchMember>itemProcessor(){
        return user->{
            if(user.availableLevelUp()){
                return user;
            }
          return null;
        };
    }
    private ItemReader<?extends BatchMember>itemReader()throws Exception{
        JpaPagingItemReader<BatchMember>itemReader = new JpaPagingItemReaderBuilder<BatchMember>()
                .queryString("select u from BatchMember u")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(100)
                .name("userItemReader")
                .build();

        itemReader.afterPropertiesSet();

        return itemReader;
    }


}
