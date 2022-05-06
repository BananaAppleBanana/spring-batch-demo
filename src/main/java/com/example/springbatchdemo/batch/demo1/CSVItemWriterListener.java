package com.example.springbatchdemo.batch.demo1;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;


public class CSVItemWriterListener<T> implements ItemWriteListener<T> {

    @Override
    public void beforeWrite(List<? extends T> list) {

    }

    @Override
    public void afterWrite(List<? extends T> list) {
        System.out.println("saved into csv file");
    }

    @Override
    public void onWriteError(Exception e, List<? extends T> list) {

    }
}
