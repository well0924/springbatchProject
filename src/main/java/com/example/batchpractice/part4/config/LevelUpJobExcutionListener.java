package com.example.batchpractice.part4.config;

import com.example.batchpractice.part4.domain.BatchMember;
import com.example.batchpractice.part4.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.time.LocalDate;
import java.util.Collection;

@Log4j2
public class LevelUpJobExcutionListener implements JobExecutionListener {
    private final UserRepository userRepository;

    public LevelUpJobExcutionListener(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        Collection<BatchMember> users = userRepository.findAllByUpdatedDate(LocalDate.now());

        long time = jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime();

        log.info("회원등급 업데이트 배치 프로그램");
        log.info("-------------------------------");
        log.info("총 데이터 처리 {}건, 처리 시간 {}millis", users.size(), time);
    }
}
