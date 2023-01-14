package com.example.batchpractice.part3;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Configuration
public class ItemReaderConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    public ItemReaderConfiguration(StepBuilderFactory stepBuilderFactory, JobBuilderFactory jobBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job itemReaderJob() {
        return this.jobBuilderFactory
                .get("itemReader job")
                .incrementer(new RunIdIncrementer())
                .start(this.step()).build();
    }

    @Bean
    public Step step() {
        return this.stepBuilderFactory.get("customReaderStep")
                .<Person,Person>chunk(10)
                .reader(new CustomItemReader<>(getItems()))
                //.processor()
                .writer(itemWriter())
                .build();
    }

    private ItemWriter<Person> itemWriter(){
        return items -> log.info(items.stream()
                .map(Person::getName)
                .collect(Collectors.joining(",")));
    }
    private List<Person> getItems(){
        List<Person> items = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            items.add(new Person(i+1,"test name"+i,"test age","test address"));
        }

        return items;
    }
}
