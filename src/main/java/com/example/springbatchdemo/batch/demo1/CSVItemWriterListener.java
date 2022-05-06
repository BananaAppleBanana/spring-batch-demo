package com.example.springbatchdemo.batch.demo1;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;

import java.util.List;

public class CSVItemWriterListener<T> implements ItemWriteListener<T> {


    @Override
    public void beforeWrite(List<? extends T> list) {

    }

    @Override
    public void afterWrite(List<? extends T> list) {
        System.out.println("after write");
    }

    @Override
    public void onWriteError(Exception e, List<? extends T> list) {

    }
}
