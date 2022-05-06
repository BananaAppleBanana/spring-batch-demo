package com.example.springbatchdemo.batch.demo1;


import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.core.io.Resource;

import java.util.List;

public class CSVItemWriter<T> implements ItemWriter<T> {
    private Resource resource;

    private StepExecution stepExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void write(List<? extends T> list) throws Exception {
        System.out.println(list + ": " + Thread.currentThread() + "----" + stepExecution);
//        System.out.println(stepExecution);
    }
}
