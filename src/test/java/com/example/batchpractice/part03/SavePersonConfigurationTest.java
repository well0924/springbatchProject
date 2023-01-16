package com.example.batchpractice.part03;

import com.example.batchpractice.part3.PersonRepository;
import com.example.batchpractice.part3.SavePersonConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {SavePersonConfiguration.class, TestConfiguration.class})
public class SavePersonConfigurationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void test_allow_duplicate()throws Exception{
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("allow_duplicate","false")
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        Assertions.assertThat(jobExecution
                .getStepExecutions()
                .stream()
                .mapToInt(StepExecution::getWriteCount)
                .sum())
                .isEqualTo(3);
    }
}
