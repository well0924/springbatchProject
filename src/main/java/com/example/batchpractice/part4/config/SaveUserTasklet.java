package com.example.batchpractice.part4.config;

import com.example.batchpractice.part4.domain.BatchMember;
import com.example.batchpractice.part4.repository.UserRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SaveUserTasklet implements Tasklet {

    private final UserRepository userRepository;

    public SaveUserTasklet(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<BatchMember> users = createUsers();
        Collections.shuffle(users);
        userRepository.saveAll(users);

        return RepeatStatus.FINISHED;
    }

    private List<BatchMember> createUsers() {
        List<BatchMember>users = new ArrayList<>();

        for(int i=0;i<100;i++){
            users.add(BatchMember
                    .builder()
                            .totalAmount(1000)
                            .username("test username"+i)
                    .build());
        }

        for(int i=100;i<200;i++){
            users.add(BatchMember
                    .builder()
                    .totalAmount(200000)
                    .username("test username"+i)
                    .build());
        }

        for(int i=200;i<300;i++){
            users.add(BatchMember
                    .builder()
                    .totalAmount(300000)
                    .username("test username"+i)
                    .build());
        }

        for(int i=300;i<400;i++){
            users.add(BatchMember
                    .builder()
                    .totalAmount(500000)
                    .username("test username"+i)
                    .build());
        }
        return users;
    }
}
